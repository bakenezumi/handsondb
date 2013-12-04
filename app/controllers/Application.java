package controllers;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import models.AnyMapper;
import models.DbColumn;
import models.HandsontableColumn;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.JDBCMetaDataUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

    	@Inject
    	private DataSource _datasource;

    	@Inject
    	private AnyMapper _anymappler;

    	@SuppressWarnings("unused")
    	private static class InitDto {
    		public List<String> tnames;
    	}

    	@SuppressWarnings("unused")
    	private static class ListDto {
    		public List<HandsontableColumn> columns;
    		public List<Map<String, Object>> data;
    	}

    	public Result init() throws Exception {
    		InitDto dto = new InitDto();
    		dto.tnames = JDBCMetaDataUtil.getTables(_datasource);

    		return ok(Json.stringify(Json.toJson(dto)));
    	}

    	public Result getTable(String tname) throws Exception {
    		ListDto dto = new ListDto();
    		List<DbColumn> dbColumns = JDBCMetaDataUtil.getColumns(_datasource, tname);
    		dto.columns = JDBCMetaDataUtil.convertColumunForHandsontable(dbColumns);
    		dto.data = _anymappler.findAll(tname, dbColumns);
    		return ok(Json.toJson(dto));
    	}

    	@SuppressWarnings("unused")
    	private static class ResultDto {
    		public boolean result;
    		public String error;
    	}

    	@Transactional(executorType = ExecutorType.BATCH)
    	//	@Transactional()
    	public Result update() throws Exception {
    		ResultDto dto = new ResultDto();
    		String tname = request().body().asFormUrlEncoded().get("tname")[0];
    		String jsondata = request().body().asFormUrlEncoded().get("data")[0];
    		JsonNode node = Json.parse(jsondata);
    		@SuppressWarnings("unchecked")
    		List<Map<String, Object>> data = Json.fromJson(node, List.class);
    		_anymappler.removeAll(tname);

    		for (Map<String, Object> row : data) {
    			if (checkNull(row)) {
    				continue;
    			}
    			_anymappler.add(tname, row);
    		}
    		dto.result = true;
    		return ok(Json.stringify(Json.toJson(dto)));
    	}

    	private static boolean checkNull(Map<String, Object> data) {
    		for (String key : data.keySet()) {
    			if (data.get(key) != null) {
    				return false;
    			}
    		}
    		return true;

       }

}
