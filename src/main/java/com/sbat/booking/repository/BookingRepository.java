package com.sbat.booking.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sbat.booking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query(value = "select * from sbat.booking where booking_slot >= ?1 and booking_slot< ?2 and booking_type = ?3", nativeQuery = true)
	//@Query(value = "select * from sbat.booking where booking_slot >= '2020-09-28 12:01:01.0' and booking_slot < '2020-09-29 12:01:01.0'",nativeQuery = true)
	List<Booking> findByBookingSlot(Timestamp bookingslot_start, Timestamp bookingslot_end, String booking_type);

}

