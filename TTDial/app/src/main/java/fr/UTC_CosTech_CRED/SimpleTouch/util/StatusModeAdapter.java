package fr.UTC_CosTech_CRED.SimpleTouch.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;

import java.util.List;

import fr.UTC_CosTech_CRED.SimpleTouch.R;

/**
 * Created by Binova on 24/12/2015.
 */
public class StatusModeAdapter extends ArrayAdapter<Presence.Mode> {
    List<Presence.Mode> modes;
    Context context;

    public StatusModeAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public StatusModeAdapter(Context context, int resource, List<Presence.Mode> objects) {
        super(context, resource, objects);
        this.modes = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_status_mode, parent, false);

        Presence.Mode mode = modes.get(position);
        RosterStatusType statusType = mode == Presence.Mode.dnd ? RosterStatusType.getUnavailable() : RosterStatusType.get(mode);

        ImageView image = (ImageView)rowView.findViewById(R.id.status_item_image);
        TextView text = (TextView)rowView.findViewById(R.id.status_item_text);
        image.setImageResource(statusType.drawable);
        text.setText(statusType.text);

        return rowView;
    }
}
