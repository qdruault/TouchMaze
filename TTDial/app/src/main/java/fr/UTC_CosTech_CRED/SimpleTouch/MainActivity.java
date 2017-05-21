package fr.UTC_CosTech_CRED.SimpleTouch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.UTC_CosTech_CRED.SimpleTouch.util.ContactsAdapter;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;
import fr.UTC_CosTech_CRED.SimpleTouch.util.RosterContactList;
import fr.UTC_CosTech_CRED.SimpleTouch.util.RosterStatusType;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DialogApp app;
    private AbstractXMPPConnection conn;
    private RosterContactList contactList;
    private ContactsAdapter contactsAdapter;
    private Map<Presence.Mode, RosterStatusType> statusTypes;

    @Bind(R.id.contact_list)
    ListView contactListView;

    @Bind(R.id.toolbar_main)
    Toolbar toolbar;

    @Bind(R.id.add_contact)
    public FloatingActionButton addContactButton;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;


    ImageView statusImageView;
    TextView statusTextView;
    TextView statusNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        app = (DialogApp)getApplicationContext();
        app.updatePresence();
        conn = app.getConn();
        statusTypes = new HashMap<>();

        contactList = new RosterContactList();

        Roster roster = Roster.getInstanceFor(app.getConn());
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        roster.addRosterListener(new RosterListener() {
            public void entriesAdded(Collection<String> addresses) {
                Roster roster = Roster.getInstanceFor(conn);
                for (RosterEntry entry : roster.getEntries()) {
                    contactList.contactInfoChanged(entry);
                    contactList.presenceChanged(roster.getAllPresences(entry.getUser()).get(0));
                }
                updateListOnUIThread();
            }
            public void entriesDeleted(Collection<String> addresses) {
                return;
            }
            public void entriesUpdated(Collection<String> addresses) {
                Roster roster = Roster.getInstanceFor(conn);
                for (RosterEntry entry : roster.getEntries()) {
                    contactList.contactInfoChanged(entry);
                    contactList.presenceChanged(roster.getAllPresences(entry.getUser()).get(0));
                }
                updateListOnUIThread();
            }
            public void presenceChanged(Presence presence) {
                contactList.presenceChanged(presence);
                if (contactsAdapter != null) {
                    updateListOnUIThread();
                }
            }
        });

        for (RosterEntry entry : roster.getEntries()) {
            contactList.contactInfoChanged(entry);
            contactList.presenceChanged(roster.getAllPresences(entry.getUser()).get(0));
        }

        contactsAdapter = new ContactsAdapter(this, R.layout.item_contact, contactList);
        contactListView.setAdapter(contactsAdapter);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeader = navigationView.getHeaderView(0);
        statusImageView = (ImageView) navigationHeader.findViewById(R.id.nav_header_image);
        statusTextView = (TextView) navigationHeader.findViewById(R.id.nav_header_status);
        statusNameTextView = (TextView) navigationHeader.findViewById(R.id.nav_header_view_name);

        Presence.Mode mode = app.getMode();
        RosterStatusType statusType = mode == Presence.Mode.dnd ? RosterStatusType.getUnavailable() : RosterStatusType.get(mode);
        statusImageView.setImageResource(statusType.drawable);
        statusTextView.setText(app.getStatus());
        String username = app.getConn().getUser();
        statusNameTextView.setText(username.substring(0, username.indexOf("@")));
    }

    public void startDialog(View v) {
        Intent i = new Intent(this, TactileDialogActivity.class);
        i.putExtra("PARTNER", (String)v.getTag());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Pour changer de statut.
        if (id == R.id.status_drawer_button) {
            Intent i = new Intent(this, StatusActivity.class);
            startActivity(i);
        } else if (id == R.id.status_settings_button) {
            // Pour charger des paramètres.
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.log_out_drawer_button) {
            // Pour se connecter.
            app.disconnect();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onAddClick(View view) {
        new AddPartnerDialogFragment().show(getFragmentManager(), "AddNewPartner");
    }

    public static class AddPartnerDialogFragment extends DialogFragment {

        public AddPartnerDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_add_partner, null);
            final EditText addPartnerEditText = (EditText)layout.findViewById(R.id.add_partner_edittext);
            builder.setTitle("Ajouter un nouveau contact")
                    .setView(layout)
                    .setPositiveButton("Ajout", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // On récupère la connexion.
                            AbstractXMPPConnection connection = ((DialogApp)getActivity().getApplication()).getConn();
                            // On prépare la requete à envoyer.
                            Presence presence = new Presence(Presence.Type.subscribe);
                            // On ajoute le destinataire.
                            presence.setTo(addPartnerEditText.getText().toString() + "@" + connection.getHost());
                            String feedback = getResources().getString(R.string.we_will_add_a_contact_once_a_person_logs_in);
                            try {
                                // On envoie la requete.
                                connection.sendStanza(presence);
                            } catch (SmackException.NotConnectedException e){
                                feedback = getResources().getString(R.string.error_adding_contact);
                            }
                            Snackbar.make(((MainActivity)getActivity()).addContactButton, feedback, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    })
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }

    public void updateListOnUIThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onUserInteraction() {
        app.resetIdleTimer("MainActivity");
    }
}
