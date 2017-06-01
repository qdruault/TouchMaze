package nf28.touchmaze.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nf28.touchmaze.R;
import nf28.touchmaze.contact.ContactsAdapter;
import nf28.touchmaze.contact.RosterContactList;
import nf28.touchmaze.contact.RosterStatusType;
import nf28.touchmaze.login.DialogHandler;

public class InvitationActivity extends AppCompatActivity {

    private DialogHandler dialogHandler;
    private AbstractXMPPConnection connection;
    private RosterContactList contactList;
    private ContactsAdapter contactsAdapter;
    private Map<Presence.Mode, RosterStatusType> statusTypes;
    private ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        // On récupère la listeView.
        contactListView = (ListView) findViewById(R.id.list_contacts);

        // On récupère la connexion.
        dialogHandler = (DialogHandler)getApplicationContext();
        connection = dialogHandler.getConn();

        statusTypes = new HashMap<>();
        contactList = new RosterContactList();

        // On récupère la liste des contacts.
        Roster roster = Roster.getInstanceFor(connection);
        // On ajoute les listeners.
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        roster.addRosterListener(new RosterListener() {
            public void entriesAdded(Collection<String> addresses) {
                Roster roster = Roster.getInstanceFor(connection);
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
                Roster roster = Roster.getInstanceFor(connection);
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

        // On la met à jour.
        for (RosterEntry entry : roster.getEntries()) {
            contactList.contactInfoChanged(entry);
            contactList.presenceChanged(roster.getAllPresences(entry.getUser()).get(0));
        }

        contactsAdapter = new ContactsAdapter(this, R.layout.item_contact, contactList);
        contactListView.setAdapter(contactsAdapter);
    }

    /**
     * Lance un thread qui met à jour la liste de contacts.
     */
    private void updateListOnUIThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactsAdapter.notifyDataSetChanged();
            }
        });
    }

    // Lance l'invitation pour "jouer".
    public void startGame(View v) {
        // On récupère le nom de l'autre joueur.
        String partner = (String)v.getTag(R.string.tag_partner);
        // On récupère son propre rôle.
        String role = (String)v.getTag(R.string.tag_role);
        Intent intentGame;
        // Pour l'hote = guide.
        if ("host".equals(role)) {
            intentGame = new Intent(this, GameMapActivity.class);
        } else {
            // Pour celui qui rejoint la partie = explorateur.
            intentGame = new Intent(this, GameMazeActivity.class);
        }

        intentGame.putExtra("PARTNER", partner);
        startActivity(intentGame);
    }
}
