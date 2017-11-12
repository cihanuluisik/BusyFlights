package com.travix.medusa.busyflights;

import com.travix.medusa.busyflights.controller.BusyFlightsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BusyFlightsApplication.class)
public class BusyFlightsApplicationTests {

	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void contextLoads() {
		assertThat(applicationContext).isNotNull();
		assertThat(applicationContext.getBean(BusyFlightsController.class)).isNotNull();
	}

}
