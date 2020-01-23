package com.assignment.api.core.dao;

import com.assignment.api.core.model.User;

public interface IAssignmentDao {
	public int addUser(User user) throws Exception ;
	public int addOrder(String userid,String orderid);
	public int deleteUser(String userid);
	public User getUser();
	public int login(User user) throws Exception;
}
