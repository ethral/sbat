package com.sbat.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbat.booking.model.User;
import com.sbat.booking.model.UserDeleteResponse;
import com.sbat.booking.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	public User createUser(User user) {
		
		return userRepo.save(user);
		
	}
	
	public List<User> getUsers(){
		
		return userRepo.findAll();
	}
	
	public UserDeleteResponse deleteUser(int id) {
		
		UserDeleteResponse usrDelResp = new UserDeleteResponse();
		
		
		if(userRepo.findById(id) != null) {
		
		 userRepo.deleteById(id);
		 
		 usrDelResp.set_deleted(true);
		 
		 return usrDelResp;
		}
		
		else {
			
			usrDelResp.set_deleted(false);
			
			return usrDelResp;
			
		}
		
	}

}
