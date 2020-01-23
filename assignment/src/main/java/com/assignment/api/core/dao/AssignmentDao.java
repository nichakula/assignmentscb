package com.assignment.api.core.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.assignment.api.core.model.User;
import com.assignment.api.core.util.AESCrypt;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AssignmentDao implements IAssignmentDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int addUser(User user) throws Exception {
		
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" INSERT INTO PUBLIC.USER (username,password,name,surname,date_of_birth) ");
		sql.append(" VALUES (?,?,?,?,to_date(?,'DD/MM/YYYY')) ");
		
		params.add(user.getUsername());
		params.add(AESCrypt.encrypt(user.getPassword()));
		if(!StringUtils.isEmpty(user.getName())) {
			params.add(user.getName());
		}else {
			params.add("name");
		}
		if(!StringUtils.isEmpty(user.getSurname())) {
			params.add(user.getSurname());
		}else {
			params.add("surname");
		}
		params.add(user.getDate_of_birth());

		return jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Override
	public int login(User user) throws Exception {
		
		List<Object> params = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" UPDATE PUBLIC.USER SET last_login = now() WHERE username = ? AND password = ? ");
		
		params.add(user.getUsername());
		params.add(AESCrypt.encrypt(user.getPassword()));
		
		return jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Override
	public User getUser() {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT username FROM public.user WHERE age(now(), last_login) < '30 minutes' order by last_login desc ");
		
		List<Map<String,Object>> res = jdbcTemplate.queryForList(sql.toString());
		String username = "";
		if(!CollectionUtils.isEmpty(res)) {
			username = String.valueOf(res.get(0).get("username"));
			sql = new StringBuilder();
			
			sql.append(" SELECT username,name,surname,to_char( date_of_birth, 'DD/MM/YYYY') as date_of_birth FROM PUBLIC.USER WHERE USERNAME = ? ");
			
			List<User> userList = jdbcTemplate.query(sql.toString(), new Object[] { username },new BeanPropertyRowMapper(User.class));
			sql = new StringBuilder();
			sql.append(" SELECT order_id FROM PUBLIC.ORDER WHERE username = ? ");
			List<Map<String,Object>> orderListMap = jdbcTemplate.queryForList(sql.toString(), new Object[] { username });
			List<String> orderList = new ArrayList<>();
			for(Map<String,Object> map: orderListMap) {
				orderList.add(String.valueOf(map.get("order_id")));
			}
			if(!CollectionUtils.isEmpty(userList)) {
				userList.get(0).setBooks(orderList);
				return userList.get(0);
			}
		}
		return null;
	}

	@Override
	public int addOrder(String userid, String orderid) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO PUBLIC.ORDER (username,order_id,order_date_time) ");
		sql.append(" VALUES (?,?,now())");
		
		params.add(userid);
		params.add(orderid);

		return jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Override
	public int deleteUser(String userid) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" DELETE FROM PUBLIC.USER WHERE USERNAME = ? ");
		
		params.add(userid);
		
		jdbcTemplate.update(sql.toString(), params.toArray());

		sql = new StringBuilder();
		
		sql.append(" DELETE FROM PUBLIC.ORDER WHERE USERNAME = ? ");
		
		return jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
}
