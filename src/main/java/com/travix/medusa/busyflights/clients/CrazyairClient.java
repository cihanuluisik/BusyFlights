package com.travix.medusa.busyflights.clients;

import com.travix.medusa.busyflights.domain.BeanToMapConvertor;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class CrazyairClient {

    @Value(value="${crazyair.service.url}")
    private String crazyairUrl;

    @Autowired
    BeanToMapConvertor beanToMapConvertor;

    public List<BusyFlightsResponse> call(RestTemplate restTemplate, CrazyAirRequest crazyAirRequest) {

       LinkedMultiValueMap<String, String> linkedMultiValueMap = beanToMapConvertor.converBeanToMultiMap(crazyAirRequest);

       ResponseEntity<List<CrazyAirResponse>> crazyAirResponses =
                restTemplate.exchange(
                        fromHttpUrl(crazyairUrl).queryParams(linkedMultiValueMap).build().toUri()
                        , GET , null
                        , new ParameterizedTypeReference<List<CrazyAirResponse>>() {});

        return crazyAirResponses
                .getBody()
                .stream()
                .map((CrazyAirResponse response ) -> new BusyFlightsResponse(response.getAirline()
                        , "CrazyAir"
                        , valueOf(response.getPrice()).setScale(2, HALF_UP)
                        , response.getDepartureAirportCode()
                        , response.getDestinationAirportCode()
                        , response.getDepartureDate()
                        , response.getArrivalDate()))
                .collect(Collectors.toList());

    }

}
