package com.maxheapsize.jpm;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class ReadingBuffer {
    private SmartMeterReading smartMeterReading;

    @Value(value = "${influxdburl}")
    public String influxdburl;

    @Value(value = "${influxdbuser}")
    public String influxdbuser;

    @Value(value = "${influxdbpw}")
    public String influxdbpassword;

    @Value(value = "${influxdbdatabase}")
    public String influxdbdatabase;




    public SmartMeterReading getSmartMeterReading() {
        return smartMeterReading;
    }

    public void setSmartMeterReading(SmartMeterReading smartMeterReading) {
        this.smartMeterReading = smartMeterReading;

        if (influxdburl!=null) {
            InfluxDB influxDB = InfluxDBFactory.connect(influxdburl, influxdbuser, influxdbpassword);
            Serie serie = new Serie.Builder(influxdbdatabase).columns("one", "two", "total", "now").values(
                    smartMeterReading.consumptionOne.value,
                    smartMeterReading.consumptionTwo.value,
                    smartMeterReading.consumptionTotal.value,
                    smartMeterReading.consumptionNow.value).build();
            influxDB.write("jpm", TimeUnit.MILLISECONDS, serie);
        }
    }

    public SmartMeterReading getSmartMeterReadingInKwh() {
        SmartMeterReading smartMeterReadingInKwh = new SmartMeterReading();
        smartMeterReadingInKwh.consumptionTotal.value = smartMeterReading.consumptionTotal.value.divide(new BigDecimal(1000), BigDecimal.ROUND_DOWN);
        smartMeterReadingInKwh.consumptionTotal.unit = "kWh";
        smartMeterReadingInKwh.consumptionOne.value = smartMeterReading.consumptionOne.value.divide(new BigDecimal(1000), BigDecimal.ROUND_DOWN);
        smartMeterReadingInKwh.consumptionOne.unit = "kWh";
        smartMeterReadingInKwh.consumptionTwo.value = smartMeterReading.consumptionTwo.value.divide(new BigDecimal(1000), BigDecimal.ROUND_DOWN);
        smartMeterReadingInKwh.consumptionTwo.unit = "kWh";
        smartMeterReadingInKwh.consumptionNow.value = smartMeterReading.consumptionNow.value;
        smartMeterReadingInKwh.consumptionNow.unit = smartMeterReading.consumptionNow.unit;
        return smartMeterReadingInKwh;
    }

}
