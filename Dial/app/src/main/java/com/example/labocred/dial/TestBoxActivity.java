package com.example.labocred.dial;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaredrummler.android.processes.AndroidProcesses;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TestBoxActivity extends AppCompatActivity implements TactileDialogViewHolder {

    @Bind(R.id.test_tactile_area)
    MainLayout tactileArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // La vue.
        setContentView(R.layout.activity_test_box);
        // Bindings.
        ButterKnife.bind(this);

        boolean launch = false;

        // On récupère la liste des applis en cours d'execution.
        List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(getApplicationContext());
        for (int i = 0; i < processes.size(); i++) {
            // Si on trouve celle qui gère le bluetooth OK.
            if(processes.get(i).processName.equals("com.example.labocred.bluetooth")) {
                launch = true;
            }
        }

        // Sinon on la lance.
        if(!launch){
            new SwitchApp(getApplicationContext(),"bluetooth");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
        }

        tactileArea.setDialogViewHolder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Pour changer d'application.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu:
                new SwitchApp(getApplicationContext(),"bluetooth");
                return true;
            case R.id.waveLeft:
                new SwitchApp(getApplicationContext(),"waveleft");
                return true;
            case R.id.waveRight:
                new SwitchApp(getApplicationContext(),"waveright");
                return true;
            case R.id.Circle:
                new SwitchApp(getApplicationContext(),"circle");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Réagit à l'evenement de toucher.
     * @param event : l'évenement.
     */
    public void onDialogTouch(DialogTouchEvent event) {
        // Ecris dans la console les propriétés de l'event.
        String message = event.makeMessage();
        byte[] data;
        String lastMessage = "";
        if (!message.equals(lastMessage)) {
            // Récupère le tableau de bytes correspondnt aux picots à lever.
            data = TouchConverter.SetToByte(event.getAffectedBoxes());
            Intent sendData = new Intent();
            sendData.putExtra("BStream", data);
            sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
            // Envoie l'info à l'appli qui gère le bluetooth.
            sendBroadcast(sendData);
        }
    }
}
