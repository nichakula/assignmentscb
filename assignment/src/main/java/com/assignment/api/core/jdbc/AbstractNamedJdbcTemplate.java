package com.assignment.api.core.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

public abstract class AbstractNamedJdbcTemplate extends org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate {

  // @Autowired
  // protected DataSource dataSource;

  public AbstractNamedJdbcTemplate(DataSource dataSource) {
    super(dataSource);
  }

  public Connection getConnection() throws Exception {

    return super.getJdbcTemplate().getDataSource().getConnection();
  }
}
