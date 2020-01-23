package com.assignment.api.core.service;

import com.assignment.api.core.model.User;

public interface IAssignmentService {
	public int addUser(User user)  throws Exception ;
	public User getUser();
	public int login(User user)  throws Exception;
	public int addOrder(String userid,String orderid);
	public int deleteUser(String userid);
}
