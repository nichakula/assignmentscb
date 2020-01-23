package com.assignment.api.core.jdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public interface NamedJdbcTemplate extends NamedParameterJdbcOperations {

  // public String decorateRowNumSQL(String sql, long pageIndex, long pageSize);

  public Long getSequence(String seqName) throws Exception;

  /**
   * 
   * @param sql
   * @param pageNumber : current page
   * @param pageSize   : row per page
   * 
   * @return
   */

  public String toPaginationSQL(String sql, long pageNumber, long pageSize);

  public long getStartRow(long pageIndex, long pageSize);

  public Long countTotal(String sql, Map<String, ?> params);

  public Map<String, Object> callProcedured(String schemaName, String packageName, String procName, String[] names, SqlParameter[] types, MapSqlParameterSource params);

  public Map<String, Object> callProcedured(CallableStatementCreator callable, List<SqlParameter> params);

  public <T> T query2Object(String sql, Map<String, ?> params, RowMapper<T> rowMapper) throws DataAccessException, Exception;

  public Connection getConnection() throws Exception;
}
