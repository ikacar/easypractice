package Biofolic.ms.ep;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

                //handluj parse exception
                String toDate = this.getToDate(productWeek.getStartDate());
                System.out.println("start date: " + productWeek.getStartDate() + " to date: " + toDate);

                HttpHeaders headersTokenQuery = new HttpHeaders();
                headersTokenQuery.setContentType(MediaType.APPLICATION_JSON);
                headersTokenQuery.add("Authorization", "Bearer " + epToken);


                String getBookedDatesAddress = epAddress + "online-booking/available-dates?from="+productWeek.startDate+"&product_ids[]="+productWeek.product+"&to="+toDate+"&calendar_id="+calendar_id;

                System.out.println(getBookedDatesAddress);
                System.out.println("auth " + headersTokenQuery.get("Authorization"));
                HttpEntity entity = new HttpEntity<>(headersTokenQuery);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                ResponseEntity<EPDateResponse> dates;

                dates = restTemplate.exchange(getBookedDatesAddress, HttpMethod.GET, entity,EPDateResponse.class);

//                EPDateResponse dates = restTemplate.getForObject(getBookedDatesAddress, EPDateResponse.class, entity);
                //ako ima vise od tri available dana sledece nedelje, znaci da sigurno ima termina, uzmi prvi termine za prva tri dana
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

        String date = productTime.getSuggestedTime().substring(0,10);
        System.out.println("date: " + productTime.getSuggestedTime());
        //uzmi datum, za njega vidi slobodne dane i savi ih u int availablle. napravi response entity od njih i provri da li je available > 2 ako jeste salji ih nazad, ako nije, nastavi u for petlji
        String getBookedTimesAddress = epAddress + "/online-booking/available-times?date="+date+"&product_ids[]="+productTime.getProductId()+"&calendar_id="+calendar_id;
        HttpHeaders headersTime = new HttpHeaders();
        headersTime.setContentType(MediaType.APPLICATION_JSON);

        headersTime.add("Authorization", "Bearer " + epToken);

        HttpEntity entityTime = new HttpEntity<>( headersTime);
        RestTemplate restTemplate = new RestTemplate();

//            EPDateResponse times = restTemplate.getForObject(getBookedTimesAddress, EPDateResponse.class, entityTime);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<EPDateResponse> timesReposnse;

        timesReposnse = restTemplate.exchange(getBookedTimesAddress, HttpMethod.GET, entityTime,EPDateResponse.class);
        List<EPDate> times = timesReposnse.getBody().getData();
        if(times.isEmpty()) return null;
        boolean foundedTime=false;
        for (EPDate time : times) {
            if(time.getDate().equals(productTime.getSuggestedTime())){
                System.out.println("nasao sam ga");
                if(time.isAvailable()){
                    System.out.println("tajm " + time.getDate());
                    System.out.println("tajm " + time.isAvailable());

                    foundedTime=true;
                }
                else return null;
            }
        }
        if(foundedTime){
            String createAppointment = epAddress + "bookings";
            CreateBooking booking = new CreateBooking();
            booking.setCalendar_id(calendar_id);
            booking.setHeading("test api booking");
            List<Product> products = new ArrayList<>();
            products.add(new Product(productTime.productId));
            booking.setProducts(products);
            booking.setType("other_booking");
            booking.setStart(productTime.suggestedTime);
            String endTime =productTime.suggestedTime.substring(11,13);
            int endTimeInt =Integer.parseInt(endTime);
            endTimeInt++;
            if(endTimeInt>23) return null;

            endTime=productTime.suggestedTime.substring(0,11)+ String.valueOf(endTimeInt) + productTime.suggestedTime.substring(13);
            System.out.println(endTime);
            booking.setEnd(endTime);
            HttpEntity entityBook = new HttpEntity<>( booking ,headersTime);
            RestTemplate restTemplateBook = new RestTemplate();
//            HttpClient httpClient = HttpClientBuilder.create().build();
//
//            restTemplateBook.setRequestFactory(new
//                    HttpComponentsClientHttpRequestFactory(httpClient));
//            EPDateResponse times = restTemplate.getForObject(getBookedTimesAddress, EPDateResponse.class, entityTime);

            restTemplateBook.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<CreateBookingResponseWrapper> response;

            response = restTemplateBook.exchange(createAppointment, HttpMethod.POST, entityBook,CreateBookingResponseWrapper.class);
            if(response.getStatusCode().is2xxSuccessful()) return response.getBody().getData().getId();
        }
        return null;
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

            System.out.println("date: " + epDate.getDate());
            //uzmi datum, za njega vidi slobodne dane i savi ih u int availablle. napravi response entity od njih i provri da li je available > 2 ako jeste salji ih nazad, ako nije, nastavi u for petlji
            String getBookedTimesAddress = epAddress + "/online-booking/available-times?date="+epDate.getDate()+"&product_ids[]="+product+"&calendar_id="+calendar_id;
            HttpHeaders headersTime = new HttpHeaders();
            headersTime.setContentType(MediaType.APPLICATION_JSON);



            headersTime.add("Authorization", "Bearer " + epToken);

            HttpEntity entityTime = new HttpEntity<>( headersTime);
            RestTemplate restTemplate = new RestTemplate();

//            EPDateResponse times = restTemplate.getForObject(getBookedTimesAddress, EPDateResponse.class, entityTime);

            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<EPDateResponse> times;

            times = restTemplate.exchange(getBookedTimesAddress, HttpMethod.GET, entityTime,EPDateResponse.class);

            for (EPDate time : times.getBody().getData()) {
                System.out.println("time: " + time.getDate());
                timesResponse.add(time.getDate().toString());
                if(timesResponse.size()==3){
                    return timesResponse;
                }
            }

        }
        return timesResponse;
    }
}
