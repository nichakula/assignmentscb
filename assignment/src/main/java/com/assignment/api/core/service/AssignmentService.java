package com.assignment.api.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.assignment.api.core.dao.IAssignmentDao;
import com.assignment.api.core.model.User;

@Service
public class AssignmentService implements IAssignmentService{

	@Autowired
	IAssignmentDao ifollowUpDao;

	@Override
	public int addUser(User user) throws Exception {
		return ifollowUpDao.addUser(user);
	}

	@Override
	public int login(User user) throws Exception {
		return ifollowUpDao.login(user);
	}

	@Override
	public User getUser() {
		return ifollowUpDao.getUser();
	}

	@Override
	public int addOrder(String userid, String orderid) {
		return ifollowUpDao.addOrder(userid, orderid);
	}

	@Override
	public int deleteUser(String userid) {
		return ifollowUpDao.deleteUser(userid);
	}

}
