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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.sbat.booking.dto.UserResponse;
import com.sbat.booking.model.AuthenticationRequest;
import com.sbat.booking.model.AuthenticationResponse;
import com.sbat.booking.model.Booking;
import com.sbat.booking.model.User;
import com.sbat.booking.model.UserDeleteResponse;
import com.sbat.booking.service.BookingService;
import com.sbat.booking.service.MailService;
import com.sbat.booking.service.UserService;
import com.sbat.booking.util.JwtUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class BookingApi {

	Logger logger = LoggerFactory.getLogger(BookingApi.class);

	@Autowired
	private BookingService bookingSvc;

	@Autowired
	private MailService mailSvc;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userSvc;

	@Autowired
	private JwtUtil jwtTokenUtil;


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

	@RequestMapping(value="/slots" , method=RequestMethod.GET)
	@ApiOperation(value = "Get available slots for selected temple service",
	notes = "Provide booking Date and type of service to get the available slots")
	public @ResponseBody List<String> getSlots(@RequestParam("selectedDate") String selectedDate, @RequestParam("bookingType") String bookingType) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.s");

		Timestamp ts = new Timestamp(((java.util.Date)df.parse(selectedDate)).getTime());

		System.out.println("Data from URL: " + ts);

		return bookingSvc.getSlot(ts,bookingType);


	}


	@RequestMapping(value="/bookings" , method=RequestMethod.GET)
	@ApiOperation(value = "Get bookings for all temple services",
	notes = "Retrieves all the bookings for all the temple services")
	public @ResponseBody List<Booking> getBookings() throws ParseException {



		return bookingSvc.getBookings();


	}



	// will generate a token for booking upon submission of the booking data

	@PostMapping("/bookings")
	@ApiOperation(value = "Submit bookings for temple services",
	notes = "Provide devotee info to submit a booking")
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
		mailRequest.setBcc("sudamsrivatsa@gmail.com");
		mailRequest.setSubject("Seva Booking Confirmation");

		// creating a new thread so that the code does not wait on the sending out of the email

		new Thread(() -> {

			MailResponse mailResp = new MailResponse();

			mailResp = mailSvc.sendEmail(mailRequest, model);

			logger.info(" " + mailResp.getMessage());

		}).start();




		return savedBooking;
	}


	// test mapping


	//	@PostMapping("/send-email")
	//	public MailResponse sendEmail(@RequestBody MailRequest request) {
	//		
	//		Map<String,Object> model = new HashMap<>();
	//		model.put("token", "JE92839-33");
	//		
	//		return mailSvc.sendEmail(request, model);
	//		
	//	}


//	@PostMapping("/validate-user")
//	@ApiOperation(value = "Validate user access to the admin portal",
//	notes = "Authenticates users to access the admin portal",
//	response = UserResponse.class)
//	public ResponseEntity<?> validateUser(@RequestBody AuthenticationRequest authReq) throws Exception {
//
//		try {
//			authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
//		} catch (BadCredentialsException e) {
//			// TODO Auto-generated catch block
//			throw new Exception("Incorrect username or password", e);
//		}
//
//		final UserDetails userDetails = userDetailsService
//				.loadUserByUsername(authReq.getUsername());
//
//		
//
//		return ResponseEntity.ok(userDetails);
//
//	}


	@PostMapping("/authenticate")
	@ApiOperation(value = "Validates User",
	notes = "Authenticates users to access the admin portal and also provides a jwt for OAuth service to service secure communication",
	response = UserResponse.class)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authReq) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		} catch (BadCredentialsException e) {
			// TODO Auto-generated catch block
			//throw new Exception("Incorrect username or password", e);
			
			return ResponseEntity.ok(new AuthenticationResponse("Incorrect username (or) password",false));
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authReq.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt,true));

	}
	
	@RequestMapping(value = "/users" , method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete user by providing user id",
	notes = "Delete user",
	response = UserDeleteResponse.class)
	public UserDeleteResponse delUser(@RequestParam("id")  int id) {



		return userSvc.deleteUser(id);
	
	}
	
	
	@RequestMapping(value="/users" , method=RequestMethod.GET)
	@ApiOperation(value = "Get users who have temple portal access",
	notes = "Retrieves all the users for the temple services")
	public @ResponseBody List<User> getUsers() throws ParseException {



		return userSvc.getUsers();


	}
	
	@PostMapping("/users")
	@ApiOperation(value = "Create or update a user to access the temple portal access",
	notes = "Create (or) update a user with access to the temple services")
	public User createUser(@RequestBody User user) {



		return userSvc.createUser(user);


	}
	
	
	

}
