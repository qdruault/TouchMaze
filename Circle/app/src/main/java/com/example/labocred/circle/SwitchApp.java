package com.example.labocred.circle;

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

        //On vérifie si l'appli est bien présente sur l'appareil

        android.content.pm.PackageManager mPm = applicationContext.getPackageManager();
        PackageInfo info;

        try {
            info = mPm.getPackageInfo("com.example.labocred." + pack, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        boolean installed = info != null;

        //Si l'appli est présente, on la démarre

        if (installed) {
            i = applicationContext.getPackageManager().getLaunchIntentForPackage("com.example.labocred." + pack); //pack équivaut au nom de l'appli à lancer
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            applicationContext.startActivity(i);
        }else{

            //Sinon on prévient qu'elle n'est pas présente
            Toast.makeText(applicationContext, "L'application \"" + pack + "\" n'existe pas ou n'est pas installée.", Toast.LENGTH_SHORT).show();
        }
    }
}