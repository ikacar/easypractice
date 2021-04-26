package Biofolic.ms.ep.entity;

public class ProductWeek {
    String product;
    String startDate;

    public ProductWeek() {
    }

    public ProductWeek(String product, String startDate) {
        this.product = product;
        this.startDate = startDate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
