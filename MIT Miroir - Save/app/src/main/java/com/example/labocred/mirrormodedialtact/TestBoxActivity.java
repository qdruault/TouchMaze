package com.example.labocred.mirrormodedialtact;

import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TestBoxActivity extends AppCompatActivity implements TactileDialogViewHolder {

    private DialogApp app;
    private ConnectedThread bluetoothThread;

    @Bind(R.id.test_tactile_area)
    MainLayout tactileArea;

    private String lastMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_box);
        ButterKnife.bind(this);

        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tactileArea.setDialogViewHolder(this);

        if (app.socket == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must connect to the box firstly")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            bluetoothThread = new ConnectedThread(app.socket);
            bluetoothThread.run();
        }
    }

    @Override
    public OPTIONS GetOptions(){return app.GetOptions();}

    public void onDialogTouch(DialogTouchEvent event) {
        String message = event.makeMessage();
        if (!message.equals(lastMessage)) {
            bluetoothThread.write(TouchConverter.SetToByte(event.getAffectedBoxes()));
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

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

        public void run() { }

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
