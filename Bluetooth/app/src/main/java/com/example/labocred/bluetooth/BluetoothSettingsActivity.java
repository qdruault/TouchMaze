package com.example.labocred.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
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

    public static DialogApp app;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver pairingReceiver;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevicesAdapter devicesAdapter;
    static BluetoothSettingsActivity BSA;
    private MenuItem refreshMenuItem;

    // Flag pour les tests.
    private boolean testMode = true;

    @Bind(R.id.bluetooth_devices_list)
    ListView devicesListView;

    @Bind(R.id.bluetooth_progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // La vue.
        setContentView(R.layout.activity_bluetooth_settings);
        // Binding.
        ButterKnife.bind(this);

        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Bluetooth");
        }

        // Si on n'est pas en phase de test, on se connecte au bluetooth.
        if (!testMode) {
            Log.d("Bluetooth", "Mode réel");
            // Récupère le bluetooth.
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                finish();
            }

            if (!bluetoothAdapter.isEnabled()) {
                // Ouvre une fenetre pour se connecter au bluetooth.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            // Récupère tous les appareils auquel on est déjà apparié.
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            devices = new ArrayList<>(pairedDevices);

            devicesAdapter = new BluetoothDevicesAdapter(this, R.layout.item_bluetooth_device, devices);
            devicesListView.setAdapter(devicesAdapter);

            // Methode onReceive(Context context, Intent intent).
            pairingReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // On récupère l'action.
                    String action = intent.getAction();

                    // Appareil bluetooth détecté.
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // On récupère l'appareil détecté.
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        boolean newDevice = true;
                        // On parcourt chaque appareil auquel on est déjà apparié.
                        for(BluetoothDevice d: devices) {
                            // S'ils ont la même adresse MAC.
                            if (d.getAddress().equals(device.getAddress())) {
                                newDevice = false;
                                // On se connecte au boitier.
                                connectToBox(device);
                                break;
                            }
                        }
                        // S'il n'est pas dans la liste.
                        if (newDevice) {
                            // On l'ajoute.
                            devices.add(device);
                            devicesAdapter.notifyDataSetChanged();
                        }
                    }
                    // Connexion établie.
                    else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    }
                    // Déconnexion.
                    else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    }
                    // Détection terminée.
                    else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        refreshMenuItem.setIcon(R.drawable.ic_autorenew_white_48dp);
                        progressBar.setVisibility(View.GONE);
                        progressBar.animate().cancel();
                        // Début de la détection.
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

        } else {
            // On est en test.
            Log.d("Bluetooth", "Mode de test");
        }
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

    /**
     * Connexion au boitier.
     * @param device : le boitier.
     */
    private void connectToBox(BluetoothDevice device) {
        // Fenetre d'attente.
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Déconnexion");

        try {
            // On ferme la socket.
            if (app.socket != null) {
                app.socket.close();
            }
            // Crée une socket.
            final BluetoothSocket trySocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            progressDialog.setMessage("Connexion au boîtier");
            progressDialog.show();

            new Thread() {
                @Override
                public void run() {
                    bluetoothAdapter.cancelDiscovery();
                    try {
                        // Connexion à la socket.
                        trySocket.connect();
                        // On stocke la socket.
                        app.socket = trySocket;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BluetoothSettingsActivity.this,"Connexion effectuée", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        // Problème de connexion.
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

    /**
     * Décoonnexion du boitier.
     * @param device : le boitier.
     */
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
            Toast.makeText(this, "Vous devez d'abord sélectionner un boîtier", Toast.LENGTH_SHORT).show();
        } else {
            final String [] items = new String[] {"Dial","WaveLeft","WaveRight","Circle"};
            final Integer[] icons = new Integer[] {R.drawable.ic_grid_on_black_24dp, R.drawable.ic_blur_linear_black_left_24dp,
                    R.drawable.ic_blur_linear_black_right_24dp,R.drawable.ic_refresh_black_24dp};
            ListAdapter adapter = new ArrayAdapterWithIcon(this, items, icons);

            new AlertDialog.Builder(this).setTitle("Sélectionnez l'application")
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item ) {
                            switch (item){
                                case 0:
                                    new SwitchApp(getApplicationContext(),"dial");
                                    break;
                                case 1:
                                    new SwitchApp(getApplicationContext(),"waveleft");
                                    break;
                                case 2:
                                    new SwitchApp(getApplicationContext(),"waveright");
                                    break;
                                case 3:
                                    new SwitchApp(getApplicationContext(),"circle");
                                    break;
                            }
                        }
                    }).show();
        }
    }
}
