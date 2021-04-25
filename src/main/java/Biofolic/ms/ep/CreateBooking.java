package Biofolic.ms.ep;

import java.util.List;

public class CreateBooking {
    String start;
    String end;
    String calendar_id;
    String type;
    String heading;
    List<Product> products;

    public CreateBooking(String start, String end, String calendar_id, String type, String heading, List<Product> products) {
        this.start = start;
        this.end = end;
        this.calendar_id = calendar_id;
        this.type = type;
        this.heading = heading;
        this.products = products;
    }

    public CreateBooking() {
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
