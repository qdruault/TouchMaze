package com.example.labocred.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.example.labocred.bluetooth.BluetoothSettingsActivity.app;

/**
 * Created by LaboCred on 03/05/2017.
 */

public class BluetoothReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.labocred.bluetooth.StreamBluetooth")){
            try {
                Bundle extras = intent.getExtras();
                byte[] bytes = extras.getByteArray("BStream");
                new BluetoothReceiverSettings.ConnectedThread(app.socket).write(bytes);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
