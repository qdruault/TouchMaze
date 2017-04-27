package com.example.labocred.mirrormodedialtact;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.jivesoftware.smack.packet.IQ;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LaboCred on 29/03/2017.
 */

public class DialogApp extends Application{
    public static final String PREF_SURFACE_SQUARED = "pref_surface_squared";
    public static final String PREF_SURFACE_HORIZONTAL_SIZE = "pref_surface_horizontal_size";
    public static final String PREF_SURFACE_VERTICAL_SIZE = "pref_surface_vertical_size";
    public static final String PREF_SURFACE_VERTICAL_POSITION = "pref_surface_vertical_position";
    public static final String PREF_SURFACE_HORIZONTAL_POSITION = "pref_surface_horizontal_position";

    public BluetoothSocket socket;

    private Map<String, IQ> invitations;

    private SharedPreferences prefs;


    public void onCreate() {
        super.onCreate();

        invitations = new HashMap<>();

        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        if (!prefs.contains(PREF_SURFACE_SQUARED))  {            editor.putBoolean(PREF_SURFACE_SQUARED, true);        }
        if (!prefs.contains(PREF_SURFACE_HORIZONTAL_SIZE)) {     editor.putInt(PREF_SURFACE_HORIZONTAL_SIZE, 100);        }
        if (!prefs.contains(PREF_SURFACE_VERTICAL_SIZE)) {       editor.putInt(PREF_SURFACE_VERTICAL_SIZE, 50);        }
        if (!prefs.contains(PREF_SURFACE_HORIZONTAL_POSITION)) { editor.putInt(PREF_SURFACE_HORIZONTAL_POSITION, 50);        }
        if (!prefs.contains(PREF_SURFACE_VERTICAL_POSITION)) {   editor.putInt(PREF_SURFACE_VERTICAL_POSITION, 50);        }

        editor.apply();
    }

    public TactileDialogViewHolder.OPTIONS GetOptions(){
        TactileDialogViewHolder.OPTIONS o = new TactileDialogViewHolder.OPTIONS();

        o.isSurfaceSquared = prefs.getBoolean(PREF_SURFACE_SQUARED, true);
        o.surfaceVerticalSize = prefs.getInt(PREF_SURFACE_VERTICAL_SIZE, 50);
        o.surfaceHorizontalSize = prefs.getInt(PREF_SURFACE_HORIZONTAL_SIZE, 100);
        o.surfaceHorizontalPosition = prefs.getInt(PREF_SURFACE_HORIZONTAL_POSITION, 50);
        o.surfaceVerticalPosition = prefs.getInt(PREF_SURFACE_VERTICAL_POSITION, 50);

        return o;
    }
}

