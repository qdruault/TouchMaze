package com.example.labocred.circle;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.android.processes.AndroidProcesses;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TestBoxActivity extends AppCompatActivity implements View.OnClickListener {

    boolean flagThread = true;
    Intent sendData = new Intent();

    public enum Tacticons {
        SPIRALE,
        CIRCLE,
        WAVE,
        SPLIT,
        SNOW;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On précise l'activité.
        setContentView(R.layout.activity_test_box);
        // Bindings.
        ButterKnife.bind(this);

        Button b1 = (Button) findViewById(R.id.launchSequence);
        b1.setOnClickListener(this);

        Button b2 = (Button) findViewById(R.id.stopSequence);
        b2.setEnabled(false);

        // Vérifie que l'application Bluetooth est bien allumée et connectée à un boîtier.
        boolean isBluetoothOn = false;

        // On récupère toutes les applications qui sont en cours.
        List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(getApplicationContext());
        for (int i = 0; i < processes.size(); i++) {
            // On regarde si l'application qui gère la connexion au boitier est lancée.
            if(processes.get(i).processName.equals("com.example.labocred.bluetooth")) {
                isBluetoothOn = true;
            }
        }

        // Si elle n'est pas lancée.
        if(!isBluetoothOn){
            // On la lance via la classe SwitchApp.
            new SwitchApp(getApplicationContext(),"bluetooth");
        }

        // Initialisation de la barre d'action.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
        }
    }

    // Création du menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Sélection menu.
    public boolean onOptionsItemSelected(MenuItem item){
        byte[] stop = {27,1,0,0};
        sendData.putExtra("BStream", stop);
        sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        // Envoie un "message" à l'appli gérant la connexion au boitier.
        sendBroadcast(sendData);

        switch (item.getItemId()){
            // Ouvre l'application correspondante.
            case R.id.menu:
                new SwitchApp(getApplicationContext(),"bluetooth");
                return true;
            case R.id.Dial:
                new SwitchApp(getApplicationContext(),"dial");
                return true;
            case R.id.WaveLeft:
                new SwitchApp(getApplicationContext(),"waveleft");
                return true;
            case R.id.WaveRight:
                new SwitchApp(getApplicationContext(),"waveright");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Gère la mise en arrière-plan de l'appli + la sortie du Thread.
    @Override
    public void onPause(){
        flagThread = false;
        // Attend 100 ms.
        TouchConverter.stopThread();
        byte[] stop = {27,1,0,0};
        sendData.putExtra("BStream", stop);
        sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        // On envoie STOP à l'appli bluetooth.
        sendBroadcast(sendData);
        super.onPause();
    }

    // Réinitialisation de l'appli au retour en avant-plan.
    public void onResume(){
        super.onResume();
        Button b1 = (Button)findViewById(R.id.launchSequence);
        Button b2 = (Button)findViewById(R.id.stopSequence);
        if(b2.isEnabled()){
            // On remet en config de départ.
            b1.setEnabled(true);
            b2.setEnabled(false);
        }
        flagThread = true;
    }


    // Thread d'allumage de la séquence.
    private class ReceiverThread extends Thread{
        @Override
        public void run() {
            while (flagThread) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.SPIRALE);
            }
        }
    }

    // Thread d'allumage du CIRCLE.
    private class CircleThread extends Thread{
        @Override
        public void run() {
            while (flagThread) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.CIRCLE);
            }
        }
    }

    // Thread d'allumage du WAVE.
    private class WaveThread extends Thread{
        @Override
        public void run() {
            while (flagThread) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.WAVE);
            }
        }
    }

    // Thread d'allumage du SPLIT.
    private class SplitThread extends Thread{
        @Override
        public void run() {
            while (flagThread) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.SPLIT);
            }
        }
    }

    // Thread d'allumage du SNOW.
    private class SnowThread extends Thread{
        @Override
        public void run() {
            while (flagThread) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.SNOW);
            }
        }
    }

    // Gestion des boutons concernant la séquence.
    public void onClick(View v){
        switch (v.getId()){
            // Clic sur "circle".
            case R.id.circle:
                // On active le bouton "stopper".
                Button b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(true);
                // On crée le thread qui va lancer la séquence.
                CircleThread circleThread = new CircleThread();
                circleThread.start();
                // On lève le flag.
                flagThread = true;
                break;
            // Clic sur "circle".
            case R.id.wave:
                // On active le bouton "stopper".
                b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(true);
                // On crée le thread qui va lancer la séquence.
                WaveThread waveThread = new WaveThread();
                waveThread.start();
                // On lève le flag.
                flagThread = true;
                break;
            case R.id.split:
                // On active le bouton "stopper".
                b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(true);
                // On crée le thread qui va lancer la séquence.
                SplitThread splitThread = new SplitThread();
                splitThread.start();
                // On lève le flag.
                flagThread = true;
                break;
            case R.id.snow:
                // On active le bouton "stopper".
                b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(true);
                // On crée le thread qui va lancer la séquence.
                SnowThread snowThread = new SnowThread();
                snowThread.start();
                // On lève le flag.
                flagThread = true;
                break;
            // Clic sur "lancer".
            case R.id.launchSequence:
                // On désactive le bouton "lancer".
                Button b1 = (Button)findViewById(R.id.launchSequence);
                b1.setEnabled(false);
                // On active le bouton "stopper".
                b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(true);
                // On crée le thread qui va lancer la séquence.
                ReceiverThread receiverThread = new ReceiverThread();
                receiverThread.start();
                // On lève le flag.
                flagThread = true;
                break;
            // Clic sur "stopper".
            case R.id.stopSequence:
                // On baisse le flag.
                flagThread = false;
                // On active le bouton "lancer".
                b1 = (Button) findViewById(R.id.launchSequence);
                b1.setEnabled(true);
                // On désactive le bouton "stopper".
                b2 = (Button)findViewById(R.id.stopSequence);
                b2.setEnabled(false);
                break;
        }
    }

    // Méthode d'envoi des données à l'appli Bluetooth.
    public void traitementData(Tacticons t) {
        // Création du tableau de bytes à envoyer.
        byte[] data;
        data = new byte[4];
        // On le remplit.
        switch (t) {
            case SPIRALE:
                data = TouchConverter.SetToByte();
                break;
            case CIRCLE:
                data = Circle.SetToByte();
                break;
            case WAVE:
                data = Wave.SetToByte();
                break;
            case SPLIT:
                data = Split.SetToByte();
                break;
            case SNOW:
                data = Snow.SetToByte();
                break;
        }


        sendData.putExtra("BStream", data);
        sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        // On l'envoie à l'appli bluetooth.
        sendBroadcast(sendData);
    }
}
