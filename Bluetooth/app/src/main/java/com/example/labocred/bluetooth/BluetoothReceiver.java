package com.example.labocred.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.example.labocred.bluetooth.BluetoothSettingsActivity.app;

/**
 * Created by LaboCred on 03/05/2017.
 */

/**
 * Classe permettant "d'écouter" les messages qu'on lui envoit.
 */
public class BluetoothReceiver extends BroadcastReceiver{
    /**
     * A la réception d'un message.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Si l'action est StreamBluetoth.
        if(intent.getAction().equals("com.example.labocred.bluetooth.StreamBluetooth")){
            try {
                // On extrait le tableau de bytes passé en extra.
                Bundle extras = intent.getExtras();
                byte[] bytes = extras.getByteArray("BStream");
                // On se connecte à la socket du bluetooth et on lui envoie le message.
                new BluetoothReceiverSettings.ConnectedThread(app.socket).write(bytes);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        // Action pour les tests.
        if(intent.getAction().equals("com.example.labocred.bluetooth.Test")){
            try {
                // On extrait le tableau de bytes passé en extra.
                Bundle extras = intent.getExtras();
                String picots = extras.getString("Picots");
                // On fait l'affichage des picots à lever.
                Log.d("Picots", picots);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
