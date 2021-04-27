package Biofolic.ms.ep.service;

import Biofolic.ms.ep.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EPService {
    @Value("#{${easy.practice.token}}")
    String epToken;
    @Value("#{${easy.practice.address}}")
    String epAddress;
    String calendar_id="76994";


    public ResponseEntity< List<ProductWeekResponse>> getAvailable(List<ProductWeek> productsWeekList){
        try{
            List<ProductWeekResponse> productWeekResponseList = new ArrayList<>();
            //todo provera da nije empty
            for (ProductWeek productWeek : productsWeekList) {

                ProductWeekResponse productWeekResponse = new ProductWeekResponse();
                productWeekResponse.setProduct(productWeek.getProduct());
                String toDate = this.getToDate(productWeek.getStartDate());

                HttpHeaders headers = getAuthorizationHeader();

                String getBookedDatesAddress = epAddress + "online-booking/available-dates?from="+productWeek.getStartDate()+"&product_ids[]="+productWeek.getProduct()+"&to="+toDate+"&calendar_id="+calendar_id;

                HttpEntity entity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                ResponseEntity<EPDateResponse> dates;
                dates = restTemplate.exchange(getBookedDatesAddress, HttpMethod.GET, entity,EPDateResponse.class);

                if(!dates.getBody().getData().isEmpty()){
                    productWeekResponse.setTimes(findTimes(dates.getBody(),productWeek.getProduct(),calendar_id));
                }
                productWeekResponseList.add(productWeekResponse);
            }

            return new ResponseEntity(productWeekResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String bookProduct(ProductTime productTime){


        try{
            String date = productTime.getSuggestedTime().substring(0,10);
//            System.out.println("date: " + productTime.getSuggestedTime());
            //uzmi datum, za njega vidi slobodne dane i savi ih u int availablle. napravi response entity od njih i provri da li je available > 2 ako jeste salji ih nazad, ako nije, nastavi u for petlji
            String getBookedTimesAddress = epAddress + "/online-booking/available-times?date="+date+"&product_ids[]="+productTime.getProductId()+"&calendar_id="+calendar_id+"&page_size=16";
            HttpHeaders headers = getAuthorizationHeader();

            HttpEntity entityTime = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();


            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<EPDateResponse> timesReposnse;

            timesReposnse = restTemplate.exchange(getBookedTimesAddress, HttpMethod.GET, entityTime,EPDateResponse.class);
            List<EPDate> times = timesReposnse.getBody().getData();
            if(times.isEmpty()) return null;
            boolean foundedTime=false;
            for (EPDate time : times) {
                if(time.getDate().equals(productTime.getSuggestedTime())){
                    if(time.isAvailable()){

                        foundedTime=true;
                    }
                    else return null;
                }
            }
            if(foundedTime){
                String createAppointment = epAddress + "bookings";

                List<Product> products = new ArrayList<>();
                products.add(new Product(productTime.getProductId()));
                String endTime =productTime.getSuggestedTime().substring(11,13);
                int endTimeInt =Integer.parseInt(endTime);
                endTimeInt++;
//                System.out.println(endTimeInt);
                if(endTimeInt>23) return null;
                endTime=productTime.getSuggestedTime().substring(0,11)+ String.valueOf(endTimeInt) + productTime.getSuggestedTime().substring(13);
//                System.out.println(productTime.getSuggestedTime());
//                System.out.println(endTime);
                CreateBooking booking = new CreateBooking();
                booking.setCalendar_id(calendar_id);
                booking.setStart(productTime.getSuggestedTime());
                booking.setEnd(endTime);
                booking.setHeading(productTime.getName());
                booking.setProducts(products);
                booking.setType("other_booking");
                HttpEntity entityBook = new HttpEntity<>(booking ,headers);
                RestTemplate restTemplateBook = new RestTemplate();

                restTemplateBook.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                ResponseEntity<CreateBookingResponseWrapper> response;

                response = restTemplateBook.exchange(createAppointment, HttpMethod.POST, entityBook,CreateBookingResponseWrapper.class);
                if(response.getStatusCode().is2xxSuccessful()) return response.getBody().getData().getId();
                else return "connection";
            }
            return null;
        }
        catch(Exception e){
            return "connection";
        }

    }
    private String getToDate(String startDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        cal.setTime(sdf.parse(startDate));// all done
        cal.add(Calendar.DAY_OF_MONTH,7);
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String toDate = dateFormat.format(date);
        return toDate;
    }
    private List<String> findTimes(EPDateResponse dates, String product, String calendar_id){
        List<String> timesResponse = new ArrayList<>();
        for (EPDate epDate : dates.getData()) {
            String getBookedTimesAddress = epAddress + "/online-booking/available-times?date="+epDate.getDate()+"&product_ids[]="+product+"&calendar_id="+calendar_id;

            HttpHeaders headers = getAuthorizationHeader();

            HttpEntity entityTime = new HttpEntity<>( headers);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<EPDateResponse> times;

            times = restTemplate.exchange(getBookedTimesAddress, HttpMethod.GET, entityTime,EPDateResponse.class);

            for (EPDate time : times.getBody().getData()) {
                timesResponse.add(time.getDate().toString());
                if(timesResponse.size()==3){
                    return timesResponse;
                }
            }

        }
        return timesResponse;
    }
    public HttpHeaders getAuthorizationHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + epToken);
        return headers;
    }
    public boolean updateBooking(BookingTime bookingTime){
        return true;
    }
    public boolean deleteBooking(String bookingId){
        String deleteBookingAddress = epAddress + "bookings/"+bookingId;
//        System.out.println(deleteBookingAddress);

        HttpHeaders headers = getAuthorizationHeader();

        HttpEntity entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity response;

        response = restTemplate.exchange(deleteBookingAddress, HttpMethod.DELETE, entity,ResponseEntity.class);
        if(response.getStatusCode().is2xxSuccessful()) return true;
        return false;
    }
}
