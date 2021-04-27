package Biofolic.ms.ep.controller;

import Biofolic.ms.ep.entity.BookingTime;
import Biofolic.ms.ep.entity.ProductTime;
import Biofolic.ms.ep.entity.ProductWeek;
import Biofolic.ms.ep.entity.ProductWeekResponse;
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
}
