package com.travix.medusa.busyflights.clients;

import com.travix.medusa.busyflights.domain.BeanToMapConvertor;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class ToughjetClient {

    @Autowired
    BeanToMapConvertor beanToMapConvertor;

    @Value(value="${toughjet.service.url}")
    private String toughjetUrl;

    public List<BusyFlightsResponse> call(RestTemplate restTemplate, ToughJetRequest toughJetRequest) {

       LinkedMultiValueMap<String, String> linkedMultiValueMap = beanToMapConvertor.converBeanToMultiMap(toughJetRequest);

       ResponseEntity<List<ToughJetResponse>> toughjetRepsonses =
                restTemplate.exchange(
                        fromHttpUrl(toughjetUrl).queryParams(linkedMultiValueMap).build().toUri()
                        , GET , null
                        , new ParameterizedTypeReference<List<ToughJetResponse>>() {});

        return toughjetRepsonses
                .getBody()
                .stream()
                .map((ToughJetResponse response) -> new BusyFlightsResponse(response.getCarrier()
                                , "ToughJet"
                                , valueOf(response.getBasePrice())
                                .multiply(BigDecimal.ONE.subtract(valueOf(response.getDiscount())))
                                .add(valueOf(response.getTax())).setScale(2, HALF_UP)
                                , response.getDepartureAirportName()
                                , response.getArrivalAirportName()
                                , response.getInboundDateTime()
                                , response.getOutboundDateTime()))
                .collect(Collectors.toList());

    }



}
