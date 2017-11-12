package com.travix.medusa.busyflights.domain.crazyair;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;

import javax.validation.Valid;

public class CrazyAirRequest {

    private CrazyAirRequest(String origin, String destination, String departureDate, String returnDate, int passengerCount) {
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.passengerCount = passengerCount;
    }

    private String origin;
    private String destination;
    private String departureDate;
    private String returnDate;
    private int passengerCount;

    public static CrazyAirRequest fromBusyFlightRequest(BusyFlightsRequest busyFlightsRequest) {
        return new CrazyAirRequest( busyFlightsRequest.getOrigin()
                                                            , busyFlightsRequest.getDestination()
                                                            , busyFlightsRequest.getDepartureDate().toString()
                                                            , busyFlightsRequest.getReturnDate().toString()
                                                            , busyFlightsRequest.getNumberOfPassengers());
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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(final String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(final String returnDate) {
        this.returnDate = returnDate;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(final int passengerCount) {
        this.passengerCount = passengerCount;
    }
}
