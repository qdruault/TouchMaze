package com.example.labocred.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by LaboCred on 27/04/2017.
 */

public class BluetoothReceiverSettings extends Activity{
    private DialogApp app;
    // La connexion au Bluetooth.
    public static ConnectedThread bluetoothThread;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (DialogApp) getApplication();
        try {
            // On crée la connexion en bluetooth.
            bluetoothThread = new ConnectedThread(app.socket);
            bluetoothThread.run();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Thread gérant la connexion au Bluetooth.
     */
    public static class ConnectedThread extends Thread {
        // La socket.
        private final BluetoothSocket mmSocket;
        // Flux d'entrée de la socket.
        private final InputStream mmInStream;
        // Flux de sortie de la socket.
        public final OutputStream mmOutStream;

        /**
         * Constructeur
         * @param socket : la socket à utiliser.
         */
        public ConnectedThread(BluetoothSocket socket) {
            // On initialise la socket.
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because member streams are final
            try {
                // On récupère les flux d'entrée et de sortie de la socket.
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            // On initialise les flux d'E/S.
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {}

        /**
         * Permet de lever les picots.
         * @param bytes : un tableau représentant les picots à lever.
         */
        public void write(byte[] bytes) {
            try {
                // Envoie un flux de données pour lever les picots.
                mmOutStream.write(bytes);
                mmOutStream.flush();
            } catch (IOException e) { }
        }

        /**
         * Ferme la connexion bluetooth.
         */
        public void cancel() {
            try {
                // Ferme la socket.
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
