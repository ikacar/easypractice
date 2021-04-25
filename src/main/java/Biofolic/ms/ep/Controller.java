package Biofolic.ms.ep;

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
    @GetMapping("/product/times")
    public ResponseEntity< List<ProductWeekResponse>> getBookedDays(@RequestBody List<ProductWeek> productsWeekList) throws ParseException {

            return epService.getAvailable(productsWeekList);

    }

    @PostMapping("/product/book")
    public ResponseEntity<String> bookProduct(@RequestBody ProductTime productTime) throws ParseException {
        String response = epService.bookProduct(productTime);
        if(response==null){
            return new ResponseEntity("Alreaady booked!",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
