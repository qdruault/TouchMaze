package nf28.touchmaze.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

import nf28.touchmaze.R;
import nf28.touchmaze.login.DialogHandler;

/**
 * Created by Binova on 10/12/2015.
 */
public class ContactsAdapter extends ArrayAdapter<RosterContactInfo> {

    Context context;
    DialogHandler dialogHandler;
    List<RosterContactInfo> items;

    /**
     * Constructeur
     * @param context
     * @param textViewResourceId
     */
    public ContactsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        dialogHandler = (DialogHandler)context.getApplicationContext();
    }

    /**
     * Constructeur avec la liste de contacts
     * @param context
     * @param resource
     * @param items
     */
    public ContactsAdapter(Context context, int resource, List<RosterContactInfo> items) {
        super(context, resource, items);
        this.context = context;
        dialogHandler = (DialogHandler)context.getApplicationContext();
        this.items = items;
    }


    /**
     * Génère la vue pour un contact.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Récupère le item_contact.xml.
        View rowView = inflater.inflate(R.layout.item_contact, parent, false);
        // Récupère les différents champs.
        TextView nameView = (TextView) rowView.findViewById(R.id.contact_name);
        TextView invitationView = (TextView) rowView.findViewById(R.id.contact_invitation_active);
        TextView statusView = (TextView) rowView.findViewById(R.id.contact_status);
        Button startDialogButton = (Button) rowView.findViewById(R.id.btn_start_game);

        // On récupère le contact à afficher.
        RosterEntry entry = items.get(position).rosterEntry;
        if(entry != null) {
            // On met son nom.
            nameView.setText(entry.getUser().substring(0, entry.getUser().indexOf("@")));
        }

        Presence presence = items.get(position).presence;
        if (presence != null) {
            // On met son statut.
            RosterStatusType statusType = presence.isAvailable() ? RosterStatusType.get(presence.getMode()) : RosterStatusType.getUnavailable();
            String bareJID = items.get(position).rosterEntry.getUser();

            nameView.setTextColor(statusType.getColor());
            statusView.setTextColor(statusType.getColor());
            statusView.setText(statusType.getText());
            startDialogButton.setVisibility(View.VISIBLE);
            // On ajoute dans le Tag l'id du contact pour le récupérer après.
            startDialogButton.setTag(bareJID);
            invitationView.setText(dialogHandler.hasInvited(bareJID) ? "vous invite" : "");

        }

        return rowView;
    }
}
