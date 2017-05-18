package fr.UTC_CosTech_CRED.SimpleTouch.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

import fr.UTC_CosTech_CRED.SimpleTouch.R;

/**
 * Created by Binova on 10/12/2015.
 */
public class ContactsAdapter extends ArrayAdapter<RosterContactInfo> {

    Context context;
    DialogApp app;
    List<RosterContactInfo> items;

    public ContactsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        app = (DialogApp)context.getApplicationContext();
    }

    public ContactsAdapter(Context context, int resource, List<RosterContactInfo> items) {
        super(context, resource, items);
        this.context = context;
        app = (DialogApp)context.getApplicationContext();
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.item_contact, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.contact_name);
        TextView invitationView = (TextView) rowView.findViewById(R.id.contact_invitation_active);
        TextView statusView = (TextView) rowView.findViewById(R.id.contact_status);
        ImageButton startDialogButton = (ImageButton) rowView.findViewById(R.id.contact_start_dialog);
        ImageView statusImageView = (ImageView) rowView.findViewById(R.id.contact_status_image);

        RosterEntry entry = items.get(position).rosterEntry;
        if(entry != null) {
            nameView.setText(entry.getUser().substring(0, entry.getUser().indexOf("@")));
        }

        Presence presence = items.get(position).presence;
        if (presence != null) {
            RosterStatusType statusType = presence.isAvailable() ? RosterStatusType.get(presence.getMode()) : RosterStatusType.getUnavailable();
            String bareJID = items.get(position).rosterEntry.getUser();

            nameView.setTextColor(statusType.color);
            statusView.setTextColor(statusType.color);
            statusImageView.setImageResource(statusType.drawable);

            //todo:
            //if(statusType.canStartDialog || app.hasInvited(bareJID)) {
                startDialogButton.setVisibility(View.VISIBLE);
                startDialogButton.setTag(bareJID);
            //}
            invitationView.setText(app.hasInvited(bareJID) ? context.getResources().getString(R.string.invited_you) : "");

            statusView.setText(presence.getStatus());
        }

        return rowView;
    }
}
