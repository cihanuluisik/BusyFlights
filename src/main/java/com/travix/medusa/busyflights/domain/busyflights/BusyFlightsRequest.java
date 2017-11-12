package com.travix.medusa.busyflights.domain.busyflights;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

public class BusyFlightsRequest {

    @Pattern(regexp = "([A-Z]{3}$)")
    private String origin;

    @Pattern(regexp = "([A-Z]{3}$)")
    private String destination;

    @DateTimeFormat(iso = DATE)
    private LocalDate departureDate;

    @DateTimeFormat(iso = DATE)
    private LocalDate returnDate;

    @Min(1)
    @Max(9999)
    private int numberOfPassengers;

    @AssertTrue(message="'returnDate' must not be before 'departureDate'")
    private boolean isDateRangeValid() {
        return returnDate ==null || departureDate ==null || returnDate.compareTo(departureDate) >=0;
    }

    public BusyFlightsRequest(String origin, String destination, LocalDate departureDate, LocalDate returnDate, int numberOfPassengers) {
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPassengers = numberOfPassengers;
    }

    public BusyFlightsRequest() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(final LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(final LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(final int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
}
