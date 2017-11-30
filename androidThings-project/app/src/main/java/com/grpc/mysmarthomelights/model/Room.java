package com.grpc.mysmarthomelights.model;

import java.util.ArrayList;

/**
 * Room
 *
 * @author (c) 2017, New Means of Payment, BBVA
 */
public class Room {

    private String name;
    private ArrayList<Device> devices;

    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }
}
