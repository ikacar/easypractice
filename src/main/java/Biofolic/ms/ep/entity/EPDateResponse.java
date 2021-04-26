package Biofolic.ms.ep.entity;

import java.util.List;

public class EPDateResponse {
    List<EPDate> data;

    public EPDateResponse() {
    }

    public List<EPDate> getData() {
        return data;
    }

    public void setData(List<EPDate> data) {
        this.data = data;
    }
}
