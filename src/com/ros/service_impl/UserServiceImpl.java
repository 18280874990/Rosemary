package com.ros.service_impl;

import com.ros.dao.UserDao;
import com.ros.dao_impl.UserDaoImpl;
import com.ros.service.UserService;

public class UserServiceImpl implements UserService{

	  UserDao ud=new UserDaoImpl();//实例化用户查询Dao对象
	
	  /*
	   * 用户注册方法
	   * 
	   */
	@Override
	public boolean register(String userName) {
		// TODO Auto-generated method stub
		
		return ud.register(userName);
	}

}