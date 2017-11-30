package com.grpc.mysmarthomelights;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grpc.mysmarthomelights.model.Device;

import java.io.IOException;

/**
 * DeviceValueEventListener
 *
 * @author (c) 2017, New Means of Payment, BBVA
 */
public class DeviceValueEventListener implements ValueEventListener {

    private static final String TAG = DeviceValueEventListener.class.getSimpleName();

    private Device device;

    public DeviceValueEventListener(Device device) {
        this.device = device;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.device.setStatus(dataSnapshot.getValue().toString());

        this.device.updatePinValue();

        Log.d(TAG, "data changed new value " + dataSnapshot.toString() + " in " + device.getName());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
