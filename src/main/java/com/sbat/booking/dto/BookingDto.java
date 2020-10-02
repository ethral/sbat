package com.sbat.booking.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {


	private Long bookingSkey;

	private String bookingToken;

	private String bookingType;

	private String fullName;

	private String email;

	private Long phoneNo;

	private Timestamp bookingSlot;

}
