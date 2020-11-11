package com.sbat.booking.dto;

import lombok.Data;

@Data
public class MailRequest {
	
	private String name;
	private String to;
	private String bcc;
	private String from;
	private String subject;

}