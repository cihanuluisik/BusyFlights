package com.travix.medusa.busyflights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.BeanToMapConvertor;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.clients.CrazyairClient;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.clients.ToughjetClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(value = {BusyFlightsController.class, CrazyairClient.class, ToughjetClient.class, BeanToMapConvertor.class})
public class BusyFlightsServiceTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BusyFlightsController busyFlightsController;

    @Autowired
    private MockRestServiceServer server;

    @Value(value="${crazyair.service.url}")
    private String crazyairUrl;

    @Value(value="${toughjet.service.url}")
    private String toughjetUrl;

    private BusyFlightsRequest busyFlightsRequest;
    private String crazyairUrlWithParameters;
    private String toughjetUrlWithParameters;


    @Before
    public void setUp() throws Exception {
        busyFlightsRequest = new BusyFlightsRequest("LON", "IST", LocalDate.of(2018, 1, 1)
                , LocalDate.of(2018, 1, 15), 1);

        crazyairUrlWithParameters = crazyairUrl + "?origin=LON&destination=IST&departureDate=2018-01-01&returnDate=2018-01-15&passengerCount=1";
        toughjetUrlWithParameters = toughjetUrl+ "?from=LON&to=IST&outboundDate=2018-01-01&inboundDate=2018-01-15&numberOfAdults=1";
    }

    @Test
    public void givenBothSuppliersReturnNoneThenReturnEmptyList() throws Exception {

        this.server.expect(requestTo(crazyairUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess("[]", APPLICATION_JSON));

        this.server.expect(MockRestRequestMatchers.requestTo(toughjetUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess("[]", APPLICATION_JSON));

        List<BusyFlightsResponse> flights = busyFlightsController.getFlights(busyFlightsRequest);

        assertThat(flights).hasSize(0);

    }


   @Test
    public void givenOnlyCrazyairReturnsOneFlightThenReturnThatOnly() throws Exception {

        CrazyAirResponse crazyAirResponse = new CrazyAirResponse("British Airways", 100.1234, "E", "LON",
                "IST", "2018-01-01T10:15:30", "2018-01-15T10:17:30");

       this.server.expect(requestTo(crazyairUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(asList(crazyAirResponse)), APPLICATION_JSON));

       this.server.expect(MockRestRequestMatchers.requestTo(toughjetUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess("[]", APPLICATION_JSON));

        List<BusyFlightsResponse> flights = busyFlightsController.getFlights(busyFlightsRequest);

        assertThat(flights).hasSize(1);
        assertThat(flights.get(0).getSupplier()).isEqualTo("CrazyAir");
        assertThat(flights.get(0).getDepartureAirportCode()).isEqualTo(crazyAirResponse.getDepartureAirportCode());
        assertThat(flights.get(0).getDepartureDate()).isEqualTo(crazyAirResponse.getDepartureDate());
        assertThat(flights.get(0).getArrivalDate()).isEqualTo(crazyAirResponse.getArrivalDate());
        assertThat(flights.get(0).getDestinationAirportCode()).isEqualTo(crazyAirResponse.getDestinationAirportCode());
        assertThat(flights.get(0).getAirline()).isEqualTo(crazyAirResponse.getAirline());
        assertThat(flights.get(0).getFare()).isEqualTo(new BigDecimal("100.12"));

    }

   @Test
    public void givenOnlyToughjetReturnsOneFlightThenReturnThatOnly() throws Exception {


       this.server.expect(requestTo(crazyairUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess("[]", APPLICATION_JSON));

       ToughJetResponse toughJetResponse  = new ToughJetResponse("American Airways", 100.444, 20.00, .50,  "LON",
               "IST", "2018-01-01T10:18:30", "2018-01-15T10:20:30");

       this.server.expect(requestTo(toughjetUrlWithParameters))
                .andExpect(method(GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(asList(toughJetResponse)), APPLICATION_JSON));

        List<BusyFlightsResponse> flights = busyFlightsController.getFlights(busyFlightsRequest);

        assertThat(flights).hasSize(1);
        assertThat(flights.get(0).getSupplier()).isEqualTo("ToughJet");
        assertThat(flights.get(0).getDepartureAirportCode()).isEqualTo(toughJetResponse.getDepartureAirportName());
        assertThat(flights.get(0).getDepartureDate()).isEqualTo(toughJetResponse.getInboundDateTime());
        assertThat(flights.get(0).getArrivalDate()).isEqualTo(toughJetResponse.getOutboundDateTime());
        assertThat(flights.get(0).getDestinationAirportCode()).isEqualTo(toughJetResponse.getArrivalAirportName());
        assertThat(flights.get(0).getAirline()).isEqualTo(toughJetResponse.getCarrier());
        assertThat(flights.get(0).getFare()).isEqualTo(new BigDecimal("70.22"));

    }

   @Test
    public void givenBothAirlinesReturnSomeFlightsThenReturnAllSortedByFare() throws Exception {


       CrazyAirResponse crazyAirResponse = new CrazyAirResponse("British Airways", 100.1234, "E", "LON",
               "IST", "2018-01-01T10:15:30", "2018-01-15T10:17:30");

       CrazyAirResponse crazyAirResponse2 = new CrazyAirResponse("British Airways", 50, "E", "LON",
               "IST", "2018-01-01T10:15:30", "2018-01-15T10:17:30");

       this.server.expect(requestTo(crazyairUrlWithParameters))
               .andExpect(method(GET))
               .andRespond(withSuccess(objectMapper.writeValueAsString(asList(crazyAirResponse, crazyAirResponse2)), APPLICATION_JSON));



       ToughJetResponse toughJetResponse  = new ToughJetResponse("American Airways", 100.444, 20.00, .50,  "LON",
               "IST", "2018-01-01T10:18:30", "2018-01-15T10:20:30");

       this.server.expect(requestTo(toughjetUrlWithParameters))
               .andExpect(method(GET))
               .andRespond(withSuccess(objectMapper.writeValueAsString(asList(toughJetResponse)), APPLICATION_JSON));


       List<BusyFlightsResponse> flights = busyFlightsController.getFlights(busyFlightsRequest);

        assertThat(flights).hasSize(3);
        assertThat(flights.get(0).getFare()).isEqualTo(new BigDecimal("50.00"));
        assertThat(flights.get(1).getFare()).isEqualTo(new BigDecimal("70.22"));
        assertThat(flights.get(2).getFare()).isEqualTo(new BigDecimal("100.12"));

    }




}
