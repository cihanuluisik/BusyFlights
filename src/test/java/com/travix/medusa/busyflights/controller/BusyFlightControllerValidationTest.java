package com.travix.medusa.busyflights.controller;

import com.travix.medusa.busyflights.BusyFlightsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BusyFlightsApplication.class)
@AutoConfigureMockMvc
public class BusyFlightControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenOriginInWrongFormatGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?origin=&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?origin=123&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?origin=12AB&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?origin=ABCD&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?origin=AB&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?origin=abc&destination=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "origin", "must match \"([A-Z]{3}$)\"");
    }

    @Test
    public void givenDestinationInWrongFormatGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?destination=&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?destination=123&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?destination=12AB&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?destination=ABCD&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?destination=AB&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
        callServiceAndExpectError("/flights?destination=abc&origin=IST&departureDate=2011-12-03&returnDate=2011-12-30&numberOfPassengers=3", "destination", "must match \"([A-Z]{3}$)\"");
    }

    @Test
    public void givenDepartureDateInWrongFormatGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-122-03&returnDate=2011-12-30&numberOfPassengers=3", "departureDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'departureDate'");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-32&returnDate=2011-12-30&numberOfPassengers=3", "departureDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'departureDate'");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-13-13&returnDate=2011-12-30&numberOfPassengers=3", "departureDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'departureDate'");
    }

    @Test
    public void givenReturnDateInWrongFormatGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2013a-12-30&numberOfPassengers=3", "returnDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'returnDate'");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2011-12-32&numberOfPassengers=3", "returnDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'returnDate'");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2011-00-30&numberOfPassengers=3", "returnDate"
                , "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'returnDate'");
    }

    @Test
    public void givenInvalidNumberOfPassengersGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2013-12-30&numberOfPassengers=-100", "numberOfPassengers"
                , "must be greater than or equal to 1");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2013-12-30&numberOfPassengers=0", "numberOfPassengers"
                , "must be greater than or equal to 1");
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2011-12-03&returnDate=2013-12-30&numberOfPassengers=99999999", "numberOfPassengers"
                , "must be less than or equal to 9999");
    }

    @Test
    public void givenInvalidDateRangeGivesFieldError() throws Exception {
        callServiceAndExpectError("/flights?destination=ABC&origin=IST&departureDate=2013-12-03&returnDate=2013-12-02&numberOfPassengers=3", "dateRangeValid"
                , "'returnDate' must not be before 'departureDate'");
    }


    private void callServiceAndExpectError(String url, String fieldName, String messageStart) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field").value(fieldName))
                .andExpect(jsonPath("$.fieldErrors[0].message").value(startsWith(messageStart)));
    }

}