package com.sbat.booking.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbat.booking.model.Booking;
import com.sbat.booking.repository.BookingRepository;
import com.sbat.booking.util.BookingUtil;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;



	final List<String> weekdayHours=Arrays.asList("09:00-10:00","10:00-11:00","11:00-11:30","18:30-19:00","19:00-20:00","20:00-20:30");

	final List<String> weekendHours=Arrays.asList("09:00-10:00","10:00-11:00","11:00-12:00","12:00-13:00","13:00-14:00",
			"14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-20:30");



	public String getToken(String fullName,Timestamp bookingSlot) {

		String token = "";

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(bookingSlot);

		Random rand = new Random();

		String token_fullName = fullName.substring(0, 2).toUpperCase();

		String token_year = Integer.toString(calendar.get(Calendar.YEAR)).substring(2);

		String token_month = BookingUtil.normalizeString(Integer.toString((calendar.get(Calendar.MONTH) + 1)));

		String token_day = BookingUtil.normalizeString(Integer.toString(calendar.get(Calendar.DATE)));

		String token_hour=BookingUtil.normalizeString(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));

		String token_random = Integer.toString(rand.nextInt(99));

		token = token.concat(token_fullName).concat(token_year).concat(token_month).concat(token_day)
				.concat(token_hour).concat("-").concat(token_random);

		return token;
	}



	public List<String> getSlot(Timestamp bookingDate, String bookingType) {

		List<String> currentDaySchedule = new ArrayList<String>();

		List<String> currentDayScheduleModified = new ArrayList<String>();

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(bookingDate);

		boolean isWeekDay = weekDayCheck(calendar);

		System.out.println("is weekday: " + isWeekDay);

		if (isWeekDay) {

			currentDaySchedule.addAll(weekdayHours);

		}

		else {

			currentDaySchedule.addAll(weekendHours);

		}

		currentDayScheduleModified.addAll(currentDaySchedule);

		List<Booking> currentDayBookings = new ArrayList<Booking>();

		currentDayBookings.addAll(getDayBookings(bookingDate,bookingType));


		for (String slot : currentDaySchedule)
		{
			int slotCount = 0;

			for (Booking booking : currentDayBookings) {

				Calendar c = Calendar.getInstance();

				c.setTime(booking.getBookingSlot());

				String slotStart = slot.substring(0, slot.indexOf("-"));
				String slotEnd = slot.substring(slot.indexOf("-") + 1, slot.length());






				if ((slotStart.substring(slotStart.indexOf(":") + 1, slotStart.length())).equalsIgnoreCase("00") &&
						(slotEnd.substring(slotEnd.indexOf(":") + 1, slotEnd.length())).equalsIgnoreCase("00"))

					//if ((slot.substring(slot.indexOf(":") + 1, slot.length())).equalsIgnoreCase("30")) 

				{

					String slotHours = slotStart.substring(0, slotStart.indexOf(":"));

					String slotMinutes = slotStart.substring(slotStart.indexOf(":") + 1,slotStart.length());



					if (BookingUtil.normalizeString(Integer.toString(c.get(Calendar.HOUR_OF_DAY))).equalsIgnoreCase(slotHours) && BookingUtil.normalizeString(Integer.toString(c.get(Calendar.MINUTE))).equalsIgnoreCase(slotMinutes) ) {

						slotCount = slotCount + 1;

						if (slotCount > 6) {


							currentDayScheduleModified.remove(slot);

							break;
						}

					}

				}

				else  {

					String slotHours = slotStart.substring(0, slotStart.indexOf(":"));

					String slotMinutes = slotStart.substring(slotStart.indexOf(":") + 1,slotStart.length());

					if (BookingUtil.normalizeString(Integer.toString(c.get(Calendar.HOUR_OF_DAY))).equalsIgnoreCase(slotHours) && BookingUtil.normalizeString(Integer.toString(c.get(Calendar.MINUTE))).equalsIgnoreCase(slotMinutes) ) {

						slotCount = slotCount + 1;

						if (slotCount > 3) {

							currentDayScheduleModified.remove(slot);

							break;
						}

					}

				}

			}

		}


		return currentDayScheduleModified;
	}


	public Boolean weekDayCheck(Calendar bookingDate) {

		if ( bookingDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				bookingDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

			return false;
		}

		return true;
	}


	public List<Booking> getDayBookings(Timestamp bookingDate, String bookingType) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(bookingDate);

		int bookingDay_end = calendar.get(Calendar.DATE) + 1;

		calendar.set(Calendar.DATE,bookingDay_end);

		Timestamp bookingDate_end = new Timestamp(calendar.getTimeInMillis());	  

		List<Booking> selectedDayBookings = new ArrayList<Booking>();

		System.out.println("booking start date: "+ bookingDate);
		System.out.println("booking end date: "+ bookingDate_end);

		selectedDayBookings.addAll(bookingRepo.findByBookingSlot(bookingDate, bookingDate_end,bookingType));

		System.out.println("db call returned no. of records: " + selectedDayBookings.size());

		return selectedDayBookings;
	}
	
	
	public List<Booking> getBookings(){
		
		
		return bookingRepo.findAll();
	}


	public Booking submitBooking(Booking booking) {

		Booking saveBooking = new Booking();

		saveBooking = bookingRepo.save(booking);

		return saveBooking;
	}

}
