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
    public static ConnectedThread bluetoothThread;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (DialogApp) getApplication();
        try {
            bluetoothThread = new ConnectedThread(app.socket);
            bluetoothThread.run();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        public final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {}

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                mmOutStream.flush();
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
