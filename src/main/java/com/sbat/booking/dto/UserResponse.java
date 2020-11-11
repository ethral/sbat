package com.sbat.booking.dto;

import lombok.Data;

@Data
public class UserResponse {
	
	private String userName;
	private String roles;
	private int is_active;
	private int is_authenticated;
	

}
