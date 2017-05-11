package com.example.labocred.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by LaboCred on 12/04/2017.
 */

public class SwitchApp {
    public SwitchApp(final Context applicationContext, final String pack) {
        Intent i;

        PackageManager mPm = applicationContext.getPackageManager();
        PackageInfo info;

        try {
            info = mPm.getPackageInfo("com.example.labocred." + pack, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        Boolean installed = info != null;

        if (installed) {
            i = applicationContext.getPackageManager().getLaunchIntentForPackage("com.example.labocred." + pack); //pack équivaut au nom de l'appli à lancer
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            applicationContext.startActivity(i);
        }else{
            Toast.makeText(applicationContext, "L'application \"" + pack + "\" n'existe pas ou n'est pas installée.", Toast.LENGTH_SHORT).show();
        }
    }
}