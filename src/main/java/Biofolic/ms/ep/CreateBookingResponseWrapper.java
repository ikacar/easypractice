package Biofolic.ms.ep;

public class CreateBookingResponseWrapper {
    String message;
    CreateBookingResponse data;

    public CreateBookingResponseWrapper() {
    }

    public CreateBookingResponse getData() {
        return data;
    }

    public void setData(CreateBookingResponse data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
