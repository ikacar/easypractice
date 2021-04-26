package Biofolic.ms.ep.entity;

public class BookingTime {
    String suggestedTime;
    String bookingId;

    public BookingTime() {
    }

    public String getSuggestedTime() {
        return suggestedTime;
    }

    public void setSuggestedTime(String suggestedTime) {
        this.suggestedTime = suggestedTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
