package com.maxheapsize.jpm;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SmartMeterControllerTest {

  private MockMvc mockMvc;
  private String expectedString;

  @Before
  public void setup() {
    SmartMeterController smartMeterController = new SmartMeterController();
    smartMeterController.readingBuffer = new ReadingBuffer();
    SmartMeterReading smartMeterReading = new SmartMeterReading();
    smartMeterReading.date = new Date();
    expectedString = "{\"date\":" + smartMeterReading.date.getTime() + ",\"meterTotal\":{\"value\":0,\"unit\":\"wh\"},\"meterOne\":{\"value\":0,\"unit\":\"wh\"},\"meterTwo\":{\"value\":0,\"unit\":\"wh\"},\"power\":{\"value\":0,\"unit\":\"wh\"},\"complete\":false}";
    smartMeterController.readingBuffer.setSmartMeterReading("testdevice", smartMeterReading);
    this.mockMvc = standaloneSetup(smartMeterController).build();
  }

  @Test
  public void testGetDevice() throws Exception {
    this.mockMvc.perform(get("/testdevice"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedString))
        .andExpect(content().contentType("application/json"));

  }

  @Test
  public void testGetDevices() throws Exception {
    this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"devices\":[\"testdevice\"]}"))
        .andExpect(content().contentType("application/json"));

  }

}
