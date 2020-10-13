package com.sbat.booking.api;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbat.booking.dto.MailRequest;
import com.sbat.booking.dto.MailResponse;
import com.sbat.booking.model.Booking;
import com.sbat.booking.service.BookingService;
import com.sbat.booking.service.MailService;

@CrossOrigin
@RestController
public class BookingApi {
	
	Logger logger = LoggerFactory.getLogger(BookingApi.class);
	
	@Autowired
	private BookingService bookingSvc;
	
	@Autowired
	private MailService mailSvc;
	
	
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
	
	
	@RequestMapping(value="/get-bookings" , method=RequestMethod.GET)
	public @ResponseBody List<Booking> getBookings() throws ParseException {
		
		
		
		return bookingSvc.getBookings();
		
		
	}
	
	
	
	// will generate a token for booking upon submission of the booking data
	
	@PostMapping("/submitBooking")
	public Booking submitBooking(@RequestBody Booking booking) {
		
		
		
//		Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(booking.getBookingSlot());
//	    calendar.add(Calendar.HOUR, 4);
//	    Timestamp ts = new Timestamp(calendar.getTimeInMillis());
	    
	    booking.setBookingSlot(booking.getBookingSlot());
		
		String token = bookingSvc.getToken(booking.getFullName(), booking.getBookingSlot());
		
		booking.setBookingToken(token);
		
		Booking savedBooking = new Booking();
		
		savedBooking = bookingSvc.submitBooking(booking);
		
		
		Map<String,Object> model = new HashMap<>();
		MailRequest mailRequest = new MailRequest();
		
		model.put("token",savedBooking.getBookingToken());
		model.put("slot",savedBooking.getBookingSlot());
		model.put("name",savedBooking.getFullName());
		model.put("service",savedBooking.getBookingType());
		
		mailRequest.setTo("abhinavsudam1@gmail.com");
		mailRequest.setFrom("panda@hungrypanda.us");
		mailRequest.setSubject("Seva Booking Confirmation");
		
		// creating a new thread so that the code does not wait on the sending out of the email
		
		new Thread(() -> {
			
			MailResponse mailResp = new MailResponse();
			
			mailResp = mailSvc.sendEmail(mailRequest, model);
		    
		    logger.info("mail status: " + mailResp.getMessage());
			
		}).start();
		
	    
		
		
		return savedBooking ;
	}
	
	
	// test mapping
	
	
	@PostMapping("/send-email")
	public MailResponse sendEmail(@RequestBody MailRequest request) {
		
		Map<String,Object> model = new HashMap<>();
		model.put("token", "JE92839-33");
		
		return mailSvc.sendEmail(request, model);
		
	}
	

}
