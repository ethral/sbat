package com.sbat.booking.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name= "booking", schema= "sbat")
@Data
public class Booking {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="booking_skey")
	private Long bookingSkey;
	
	
	@Column(name="booking_token")
	private String bookingToken;
	
	@Column(name="booking_type")
	private String bookingType;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone_no")
	private Long phoneNo;
	
	@Column(name="booking_slot")
	private Timestamp bookingSlot;

}
