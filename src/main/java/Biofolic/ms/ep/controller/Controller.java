package Biofolic.ms.ep.controller;

import Biofolic.ms.ep.entity.*;
import Biofolic.ms.ep.service.EPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/ep")
public class Controller {

    @Autowired
    EPService epService;

    @GetMapping("/ping")
    public String ping(){
        return "success";
    }
    //todo promeni return parametar
    @PostMapping("/product/times")
    public ResponseEntity< List<ProductWeekResponse>> getBookedDays(@RequestBody List<ProductWeek> productsWeekList) throws ParseException {

            return epService.getAvailable(productsWeekList);

    }

    @PostMapping("/product/book")
    public ResponseEntity<String> bookProduct(@RequestBody ProductTime productTime) throws ParseException {
        String response = epService.bookProduct(productTime);
        if(response==null){
            return new ResponseEntity("Already booked!",HttpStatus.NOT_FOUND);
        }
        if(response.equals("connection")){
            return new ResponseEntity("Connection to EasyPractice problem",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/product/book/all")
    public ResponseEntity<String> bookAllProduct(@RequestBody List<ProductTime> productTimeList) throws ParseException {
        List<ProductTime> response = epService.bookAllProduct(productTimeList);
        if(response==null || response.isEmpty()){
            return new ResponseEntity("Connection to EasyPractice problem",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    @PostMapping("/product/book/update")
    public ResponseEntity<Boolean> updateBooking(@RequestBody BookingTime bookingTime) throws ParseException {
        boolean response = epService.updateBooking(bookingTime);
        if(!response){
            return new ResponseEntity("Already booked!",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/product/book/delete")
    public ResponseEntity<Boolean> updateBooking(@RequestBody String bookingId) throws ParseException {
        boolean response = epService.deleteBooking(bookingId);
        if(!response){
            return new ResponseEntity("Unable to delete booking!",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/calendar/bookings")
    public ResponseEntity<List<DateBooked>> getBooked(@RequestBody CheckBookings checkBookings){
        if(checkBookings==null
                || checkBookings.getStartDate()==null
                || checkBookings.getEndDate()==null
                || checkBookings.getCalendarId()==null
                || checkBookings.getStartDate().equals("")
                || checkBookings.getEndDate().equals("")
                || checkBookings.getCalendarId().equals("") ){
            return new ResponseEntity("Invalid data!", HttpStatus.BAD_REQUEST);
        }
        List<DateBooked> bookings = epService.getBookedDates(checkBookings);
        if(bookings==null) return new ResponseEntity("Connection to EasyPractice failed!", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(bookings,HttpStatus.OK);
    }
}
