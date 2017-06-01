package nf28.touchmaze.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

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

        // On la met à jour.
        for (RosterEntry entry : roster.getEntries()) {
            contactList.contactInfoChanged(entry);
            contactList.presenceChanged(roster.getAllPresences(entry.getUser()).get(0));
        }

        contactsAdapter = new ContactsAdapter(this, R.layout.item_contact, contactList);
        contactListView.setAdapter(contactsAdapter);
    }
}
