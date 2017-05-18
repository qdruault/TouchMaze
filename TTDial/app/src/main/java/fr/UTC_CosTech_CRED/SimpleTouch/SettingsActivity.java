package fr.UTC_CosTech_CRED.SimpleTouch;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;


public class SettingsActivity extends AppCompatActivity {
    private DialogApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

            // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
               .replace(android.R.id.content, new PrefsFragment()).commit();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);




    }

    @Override
    public void onUserInteraction() {
        app.resetIdleTimer("SettingsActivity");
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);
        }
    }
}

