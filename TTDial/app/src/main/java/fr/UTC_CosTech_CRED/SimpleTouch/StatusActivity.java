package fr.UTC_CosTech_CRED.SimpleTouch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;
import fr.UTC_CosTech_CRED.SimpleTouch.util.StatusModeAdapter;

public class StatusActivity extends AppCompatActivity {

    public DialogApp app;

    @Bind(R.id.mode_spinner)
    Spinner modeSpinner;

    @Bind(R.id.status_edit_text)
    EditText statusEditText;

    @Bind(R.id.change_status_button)
    AppCompatButton changeStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Ajoute les différents status.
        ArrayList<Presence.Mode> modes = new ArrayList<Presence.Mode>();
        modes.add(Presence.Mode.available);
        modes.add(Presence.Mode.away);
        modes.add(Presence.Mode.dnd);
        StatusModeAdapter adapter = new StatusModeAdapter(this, R.layout.item_status_mode, modes);
        modeSpinner.setAdapter(adapter);

        for (int i = 0; i < modes.size(); i++) {
            if (adapter.getItem(i) == app.getMode()) {
                final int j = i;
                modeSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        modeSpinner.setSelection(j);
                    }
                });
                break;
            }
        }

        statusEditText.setText(app.getStatus());

    }

    public void onSaveStatus(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        app.setMode((Presence.Mode) modeSpinner.getAdapter().getItem(modeSpinner.getSelectedItemPosition()));
        // Mise à jour du statut.
        app.setStatus(statusEditText.getText().toString());
        app.updatePresence();
        Toast.makeText(this, R.string.your_status_has_been_successfully_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUserInteraction() {
        app.resetIdleTimer("StatusActivity");
    }
}
