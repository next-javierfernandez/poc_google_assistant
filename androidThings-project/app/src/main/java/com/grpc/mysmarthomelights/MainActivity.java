package com.grpc.mysmarthomelights;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grpc.mysmarthomelights.model.Device;
import com.grpc.mysmarthomelights.model.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Room> roomList = new ArrayList<>();
    private List<Gpio> ledGpioList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRoomsData(FirebaseDatabase.getInstance().getReference("rooms"));
    }

    private void getRoomsData(DatabaseReference roomsDatabaseReference) {
        final PeripheralManagerService service = new PeripheralManagerService();

        roomsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot roomDataSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot devices = roomDataSnapshot.child("devices");
                    Room room = new Room(roomDataSnapshot.getKey());
                    roomList.add(room);

                    for (DataSnapshot deviceDataSnapshot : devices.getChildren()) {
                        Log.d(TAG,"room: "+ roomDataSnapshot.getKey() + " " + deviceDataSnapshot.getKey() + " " + deviceDataSnapshot.child("status").getValue().toString()); //displays the key for the node
                        Device device = new Device(deviceDataSnapshot.getKey(), deviceDataSnapshot.child("status").getValue().toString());
                        room.addDevice(device);

                        try {
                            Gpio gpioDevice = service.openGpio(deviceDataSnapshot.child("pin").getValue().toString());
                            gpioDevice.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                            device.setGpio(gpioDevice);
                        } catch (IOException e) {
                        }

                        deviceDataSnapshot.child("status").getRef().addValueEventListener(new DeviceValueEventListener(device));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (Gpio ledGpio : ledGpioList) {
            try {
                ledGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

}
