package Biofolic.ms.ep.entity;

import java.util.List;

public class CreateBooking {
    String notes;
    String start;
    String end;
    String calendar_id;
    String type;
    String heading;
    String client_id;
    List<Product> products;

    public CreateBooking(String notes, String start, String end, String calendar_id, String type, String heading, String client_id, List<Product> products) {
        this.notes = notes;
        this.start = start;
        this.end = end;
        this.calendar_id = calendar_id;
        this.type = type;
        this.heading = heading;
        this.client_id = client_id;
        this.products = products;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public CreateBooking() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CreateBooking(String calendar_id, String test_api_booking, List<Product> products, String other_booking, String suggestedTime, String endTime) {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
