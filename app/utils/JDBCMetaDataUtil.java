package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;

import models.DbColumn;
import models.HandsontableColumn;

import org.apache.commons.dbutils.handlers.BeanListHandler;


public class JDBCMetaDataUtil {

	private static List<String> _tnames = null; //CopyOnWriteArrayList<String>
	private static Map<String, List<DbColumn>> _columnsMap = null; //ConcurrentHashMap<String, List<DbColumn>>

	/**
	 * @return スキーマ内全テーブルのテーブル名のリスト
	 * @throws SQLException
	 */
	public static List<String> getTables(DataSource datasource) throws SQLException {
		if(_tnames != null) {
			return _tnames;
		}
		_tnames = new CopyOnWriteArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
			String[] types = { "TABLE" };
//			rs = dmd.getTables(conn.getCatalog(), conn.getSchema(), "%", types);
			rs = dmd.getTables(conn.getCatalog(), null, "%", types);

			while (rs.next()) {
				String tname = rs.getString("TABLE_NAME");
				_tnames.add(tname);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
			finally {
			if(conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return _tnames;
	}

	/**
	 * @return テーブルの項目リスト
	 * @throws SQLException
	 */
	public static List<DbColumn> getColumns(DataSource datasource, String tname) throws SQLException {
		if(_columnsMap == null) {
			_columnsMap = new ConcurrentHashMap<String, List<DbColumn>>();
		}
		if(_columnsMap.containsKey(tname)) {
			return _columnsMap.get(tname);
		}
		Connection conn = null;
		ResultSet rs = null;
		List<DbColumn> list = new ArrayList<>();
		try {
			conn = datasource.getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
//			rs = dmd.getColumns(conn.getCatalog(), conn.getSchema(), tname, "%");
			rs = dmd.getColumns(conn.getCatalog(), null, tname, "%");
			BeanListHandler<DbColumn> blh = new BeanListHandler<>(DbColumn.class);
			list = blh.handle(rs);

		} finally {
			if(conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		_columnsMap.put(tname, list);
		return list;
	}

	public static List<HandsontableColumn> convertColumunForHandsontable(List<DbColumn> src) {
		List<HandsontableColumn> dist = new ArrayList<>();
		for(DbColumn column : src) {
			HandsontableColumn distcol = new HandsontableColumn();
			distcol.data = column.column_name;
			String type_name = column.type_name.toUpperCase();
			if("DECIMAL".equals(type_name) || "INTEGER".equals(type_name)) {
				distcol.type ="numeric";
			} else if ("BIT".equals(type_name) || "BOOLEAN".equals(type_name)) {
				distcol.type ="checkbox";
			}
			distcol.maxlength = column.column_size;
			dist.add(distcol);
		}

		return dist;
	}


	public static void clearCash() {
		_tnames = null;
		_columnsMap = null;

	}

}
