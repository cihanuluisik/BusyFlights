package com.travix.medusa.busyflights.domain.toughjet;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;

import javax.validation.Valid;

public class ToughJetRequest {

    private String from;
    private String to;
    private String outboundDate;
    private String inboundDate;
    private int numberOfAdults;

    private ToughJetRequest(String from, String to, String outboundDate, String inboundDate, int numberOfAdults) {
        this.from = from;
        this.to = to;
        this.outboundDate = outboundDate;
        this.inboundDate = inboundDate;
        this.numberOfAdults = numberOfAdults;
    }

    public static ToughJetRequest fromBusyFlightRequest(BusyFlightsRequest busyFlightsRequest) {
        return new ToughJetRequest( busyFlightsRequest.getOrigin()
                , busyFlightsRequest.getDestination()
                , busyFlightsRequest.getDepartureDate().toString()
                , busyFlightsRequest.getReturnDate().toString()
                , busyFlightsRequest.getNumberOfPassengers());
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getOutboundDate() {
        return outboundDate;
    }

    public void setOutboundDate(final String outboundDate) {
        this.outboundDate = outboundDate;
    }

    public String getInboundDate() {
        return inboundDate;
    }

    public void setInboundDate(final String inboundDate) {
        this.inboundDate = inboundDate;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(final int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }
}
