package com.assignment.api.core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.assignment.api.core.util.StringUtil;

@Component
public class NamedJdbcTemplateImpl extends AbstractNamedJdbcTemplate implements NamedJdbcTemplate {

  private static final String PRE_COUNT_SQL = "select count(1) as total_row from (";
  private static final String SUF_COUNT_SQL = ") c ";

  public NamedJdbcTemplateImpl(DataSource ds) {
    super(ds);
  }

  @Override
  public String toPaginationSQL(String sql, long pageIndex, long pageSize) {
    long startRow = getStartRow(pageIndex, pageSize);
    long endRow = (pageIndex) * pageSize;

    StringBuilder bf = new StringBuilder();
    bf.append(" SELECT T3.* FROM (");
    bf.append(" SELECT T2.*,  rownum row_No FROM (");
    bf.append(" SELECT T1.* FROM (");
    bf.append(sql);
    bf.append(" ) T1 ) T2 ) T3 WHERE T3.row_No BETWEEN ").append(startRow).append(" and ").append(endRow);

    return bf.toString();
  }

  @Override
  public long getStartRow(long pageIndex, long pageSize) {
    return ((pageIndex - 1) * pageSize) + 1;
  }

  @Override
  public Long countTotal(String sql, Map<String, ?> params) {
    StringBuilder sb = new StringBuilder();
    sb.append(PRE_COUNT_SQL);
    sb.append(sql);
    sb.append(SUF_COUNT_SQL);

    return this.queryForObject(sb.toString(), params, Long.class);
  }

  @Override
  public Map<String, Object> callProcedured(String schemaName, String packageName, String procName, String[] names, SqlParameter[] types, MapSqlParameterSource params) {
    SimpleJdbcCall call = new SimpleJdbcCall(getJdbcTemplate());
    call.withSchemaName(schemaName);
    if (!StringUtils.isEmpty(packageName)) {
      call.withCatalogName(schemaName);
    }
    call.withProcedureName(procName).withoutProcedureColumnMetaDataAccess();
    call.useInParameterNames(names);
    call.declareParameters(types);

    return call.execute(params);
  }

  @Override
  public Map<String, Object> callProcedured(CallableStatementCreator callable, List<SqlParameter> params) {

    return getJdbcTemplate().call(callable, params);
  }

  @Override
  public Long getSequence(String seqName) throws Exception {
    Map<String, Object> param = new HashMap<String, Object>();
    StringBuilder bf = new StringBuilder();

    seqName = seqName.toUpperCase().replace(".NEXTVAL", "");

    bf.append("select ").append(seqName).append(".nextval as next_val from dual ");
    List<Long> nextVal = query(bf.toString(), param, new RowMapper<Long>() {
      @Override
      public Long mapRow(ResultSet rs, int i) throws SQLException {
        return rs.getLong("next_val");
      }
    });

    return !StringUtil.isEmpty(nextVal) ? nextVal.get(0) : null;
  }

  @Override
  public <T> T query2Object(String sql, Map<String, ?> params, RowMapper<T> rowMapper) throws DataAccessException, Exception {
    List<T> lists = query(sql, params, rowMapper);
    if (lists == null || lists.size() <= 0) {
      return null;
    }

    return lists.get(0);
  }

}
