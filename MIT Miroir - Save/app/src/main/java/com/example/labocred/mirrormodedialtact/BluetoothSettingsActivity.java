package com.example.labocred.mirrormodedialtact;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by LaboCred on 30/03/2017.
 */

public class BluetoothSettingsActivity extends AppCompatActivity {
    public static int REQUEST_ENABLE_BT = 1;

    public DialogApp app;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver pairingReceiver;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevicesAdapter devicesAdapter;
    private MenuItem refreshMenuItem;

    @Bind(R.id.bluetooth_devices_list)
    ListView devicesListView;

    @Bind(R.id.bluetooth_progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_settings);
        ButterKnife.bind(this);

        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Bluetooth");
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            finish();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        devices = new ArrayList<>(pairedDevices);

        devicesAdapter = new BluetoothDevicesAdapter(this, R.layout.item_bluetooth_device, devices);
        devicesListView.setAdapter(devicesAdapter);


        pairingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    boolean newDevice = true;
                    for(BluetoothDevice d: devices) {
                        if (d.getAddress().equals(device.getAddress())) {
                            newDevice = false;
                            connectToBox(device);
                            break;
                        }
                    }
                    if (newDevice) {
                        devices.add(device);
                        devicesAdapter.notifyDataSetChanged();
                    }
                }
                else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                }
                else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    refreshMenuItem.setIcon(R.drawable.ic_autorenew_white_48dp);
                    progressBar.setVisibility(View.GONE);
                    progressBar.animate().cancel();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    refreshMenuItem.setIcon(R.drawable.ic_pause_circle_outline_white_48dp);
                    progressBar.animate().start();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        };

        registerReceiver(pairingReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST));
        registerReceiver(pairingReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.refresh_bluetooth_device_list:
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                } else {
                    bluetoothAdapter.startDiscovery();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_settings, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_bluetooth_device_list);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(pairingReceiver);
        super.onDestroy();
    }

    private void connectToBox(BluetoothDevice device) {
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Déconnexion");

        try {
            if (app.socket != null) {
                app.socket.close();
            }
            final BluetoothSocket trySocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            progressDialog.setMessage("Connection au boîtier");
            progressDialog.show();

            new Thread() {
                @Override
                public void run() {
                    bluetoothAdapter.cancelDiscovery();
                    try {
                        trySocket.connect();
                        app.socket = trySocket;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BluetoothSettingsActivity.this,"Connection effectuée", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BluetoothSettingsActivity.this, "Impossible de se connecter au boîtier", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    } finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                devicesAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnectBox (BluetoothDevice device) {
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Déconnexion");
        progressDialog.show();

        try {
            app.socket.close();
            app.socket = null;
            Toast.makeText(BluetoothSettingsActivity.this, "Déconnexion effectuée", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BluetoothSettingsActivity.this, "Une erreur inattendue s'est produite", Toast.LENGTH_SHORT).show();
        } finally {
            devicesAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public void onDeviceItemClicked(View v) {
        int position = (int)v.getTag();
        BluetoothDevice device = devicesAdapter.getItem(position);

        if (app.socket != null && app.socket.getRemoteDevice().getAddress().equals(device.getAddress())) {
            disconnectBox(device);
        } else {
            connectToBox(device);
        }
    }

    public void onTestBoxButton(View v) {
        if (app.socket == null) {
            Toast.makeText(this, "Vous devez d'abord vous connecter à un boîtier", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, TestBoxActivity.class));
        }
    }
}
