package com.grpc.mysmarthomelights.model;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

/**
 * Device
 *
 * @author (c) 2017, New Means of Payment, BBVA
 */
public class Device {

    private String name;
    private boolean status;
    private Gpio gpio;

    public Device(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public Device(String name, String status) {
        this.name = name;
        setStatus(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatus(String statusString) {
        this.status = (statusString.equals("on") ? true : false);
    }

    public void setGpio(Gpio gpio) {
        this.gpio = gpio;
    }

    public void updatePinValue() {
        if (gpio != null) {
            try {
                gpio.setValue(status);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
