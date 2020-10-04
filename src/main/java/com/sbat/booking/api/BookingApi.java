package com.sbat.booking.api;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbat.booking.model.Booking;
import com.sbat.booking.service.BookingService;

@CrossOrigin
@RestController
public class BookingApi {
	
	@Autowired
	private BookingService bookingSvc;
	
	
//	@GetMapping("/slot")
//	public List<String> slot() {
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.YEAR, 2020);
//		c.set(Calendar.MONTH, 8);
//		c.set(Calendar.DAY_OF_MONTH,28);
//		c.set(Calendar.HOUR_OF_DAY,00);
//		c.set(Calendar.MINUTE,00);
//		c.set(Calendar.SECOND,00);
//		c.set(Calendar.MILLISECOND,0);
//	
//		
//		long timeStamp = c.getTimeInMillis();
//		
//		Timestamp ts = new Timestamp(timeStamp);
//		
//		System.out.println("input timestamp for slot: " + ts);
//		return bookingSvc.getSlot(ts);
//	}
	
	// mapping to get the available slots based on selected date and booking type
	
	@RequestMapping(value="/get-slots" , method=RequestMethod.GET)
	public @ResponseBody List<String> getSlots(@RequestParam("selectedDate") String selectedDate, @RequestParam("bookingType") String bookingType) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.s");
		
		Timestamp ts = new Timestamp(((java.util.Date)df.parse(selectedDate)).getTime());
		
		System.out.println("Data from URL: " + ts);
		
		return bookingSvc.getSlot(ts,bookingType);
		
		
	}
	
	// will generate a token for booking upon submission of the booking data
	
	@PostMapping("/submitBooking")
	public String submitBooking(@RequestBody Booking booking) {
		
		
		
//		Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(booking.getBookingSlot());
//	    calendar.add(Calendar.HOUR, 4);
//	    Timestamp ts = new Timestamp(calendar.getTimeInMillis());
	    
	    booking.setBookingSlot(booking.getBookingSlot());
		
		String token = bookingSvc.getToken(booking.getFullName(), booking.getBookingSlot());
		
		booking.setBookingToken(token);
		
		Booking savedBooking = new Booking();
		
		savedBooking = bookingSvc.submitBooking(booking);
		
		String savedToken = savedBooking.getBookingToken();
		
		return savedToken ;
	}
	

}
