package Biofolic.ms.ep.entity;

import java.util.List;

public class EPBookedResponse {
    List<DateBooked> data;

    public List<DateBooked> getData() {
        return data;
    }

    public void setData(List<DateBooked> data) {
        this.data = data;
    }

    public EPBookedResponse() {
    }
}
