package com.example.labocred.dial;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by LaboCred on 12/04/2017.
 */

public class SwitchApp {
    /**
     *
     * @param applicationContext
     * @param pack : le nom de l'appli à lancer (ex: bluetooth, dial, waveleft, waveright).
     */
    public SwitchApp(final Context applicationContext, final String pack) {
        Intent intent;

        // On vérifie si l'appli est bien présente sur l'appareil.
        android.content.pm.PackageManager packageManager = applicationContext.getPackageManager();
        PackageInfo info;

        try {
            // On essaye de récupèrer l'appli.
            info = packageManager.getPackageInfo("com.example.labocred." + pack, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        boolean installed = info != null;

        // Si l'appli est présente, on la démarre.
        if (installed) {
            // On récupère l'intent nécessaire pour lancer l'appli.
            intent = packageManager.getLaunchIntentForPackage("com.example.labocred." + pack);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            applicationContext.startActivity(intent);

        } else {
            // Sinon on prévient qu'elle n'est pas présente.
            Toast.makeText(applicationContext,
                    "L'application \"" + pack + "\" n'existe pas ou n'est pas installée.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
