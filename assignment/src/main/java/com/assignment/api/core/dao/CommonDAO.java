package com.assignment.api.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class CommonDAO implements ICommonDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Integer getSeqeunceByName(String seqName) throws Exception {

		String name = seqName + ".NEXTVAL ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(name).append(" FROM DUAL ");
		@SuppressWarnings("rawtypes")
		List<Integer> result = jdbcTemplate.query(sql.toString(), new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println(rs.getInt(1));
				return rs.getInt(1);
			}

		});
		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
