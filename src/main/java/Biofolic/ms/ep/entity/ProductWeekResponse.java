package Biofolic.ms.ep.entity;

import java.util.List;

public class ProductWeekResponse {
    String product;
    List<String> times;
//    String timeOne;
//    String timeTwo;
//    String timeThree;

    public ProductWeekResponse() {
    }

    public ProductWeekResponse(String product, String timeOne, String timeTwo, String timeThree) {
        this.product = product;
//        this.timeOne = timeOne;
//        this.timeTwo = timeTwo;
//        this.timeThree = timeThree;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    //    public String getTimeOne() {
//        return timeOne;
//    }
//
//    public void setTimeOne(String timeOne) {
//        this.timeOne = timeOne;
//    }
//
//    public String getTimeTwo() {
//        return timeTwo;
//    }
//
//    public void setTimeTwo(String timeTwo) {
//        this.timeTwo = timeTwo;
//    }
//
//    public String getTimeThree() {
//        return timeThree;
//    }
//
//    public void setTimeThree(String timeThree) {
//        this.timeThree = timeThree;
//    }

}
