package models;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import utils.mybatis.MybatisMapper;

@MybatisMapper
public interface AnyMapper {

	@SelectProvider(type=AnyProvider.class, method="makeFindAllSql")
    List<Map<String, Object>> findAll(@Param("tname") String tname, @Param("columns") List<DbColumn> columns);

	@DeleteProvider(type=AnyProvider.class, method="makeRemoveAllSql")
    int removeAll(@Param("tname") String tname);

	@InsertProvider(type=AnyProvider.class, method="makeAddSql")
    int add(@Param("tname") String tname, @Param("data") Map<String, Object> data);

	public class AnyProvider {
		public String makeFindAllSql(Map<String,Object> params) {
	        String tname = (String) params.get("tname");
	        @SuppressWarnings("unchecked")
	        List<DbColumn> columns = (List<DbColumn>) params.get("columns");
	        SQL sql = new SQL();
	        for(DbColumn column : columns) {
	        	sql.SELECT(column.column_name + " as \"" + column.column_name + "\"" );
	        }

	        sql.FROM(tname);
	        return sql.toString();
	   }

		public String makeRemoveAllSql(Map<String,Object> params) {
	        String tname = (String) params.get("tname");
	        String sql = new SQL().DELETE_FROM(tname).toString();
	        return sql;
	   }

		public String makeAddSql(Map<String,Object> params) {
	        String tname = (String) params.get("tname");
	        @SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) params.get("data");
	        SQL sql = new SQL().INSERT_INTO(tname);
	        for(String key : data.keySet()) {
	        	if(data.get(key) == null){
	        		continue;
	        	}
	        	if(data.get(key).getClass() == String.class) {
	        		sql.VALUES(key, "'" + data.get(key).toString() + "'");
	        	} else {
	        		sql.VALUES(key, data.get(key).toString());
	        	}
	        }
//	        System.out.println(sql);
	        return sql.toString();
	   }


	}






}