package com.travix.medusa.busyflights.controller;


import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.clients.CrazyairClient;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.clients.ToughjetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class BusyFlightsController {

    @Autowired
    CrazyairClient crazyairClient;

    @Autowired
    ToughjetClient toughjetClient;

    private final RestTemplate restTemplate;

    public BusyFlightsController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(value="/flights",method = RequestMethod.GET)
    public List<BusyFlightsResponse> getFlights(@Valid BusyFlightsRequest busyFlightsRequest){

        List<BusyFlightsResponse> busyFlightsResponses  = new ArrayList<>();

        // call Crazy air
        CrazyAirRequest crazyAirRequest = CrazyAirRequest.fromBusyFlightRequest(busyFlightsRequest);
        List<BusyFlightsResponse>  crazyairResponses = crazyairClient.call(restTemplate, crazyAirRequest);
        busyFlightsResponses.addAll(crazyairResponses);


        // call Tough Jet
        ToughJetRequest toughJetRequest = ToughJetRequest.fromBusyFlightRequest(busyFlightsRequest);
        List<BusyFlightsResponse>  toughjetResponses = toughjetClient.call(restTemplate, toughJetRequest );
        busyFlightsResponses.addAll(toughjetResponses);


        // sort them
        busyFlightsResponses.sort(Comparator.comparing(BusyFlightsResponse::getFare));

        return busyFlightsResponses;
    }

}
