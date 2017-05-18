package fr.UTC_CosTech_CRED.SimpleTouch.util;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

/**
 * Created by Binova on 11/12/2015.
 */
public class RosterContactList extends ArrayList<RosterContactInfo> {

    public RosterContactList() {
        super();
    }

    public RosterContactInfo findByName(String name) {
        for (RosterContactInfo contact: this) {
            if (contact.name.equals(name)) {
                return contact;
            }
        }

        return null;
    }

    public void presenceChanged(Presence presence) {
        String name = presence.getFrom();
        if (presence.getFrom().lastIndexOf('/') > 0) {
            name = presence.getFrom().substring(0, presence.getFrom().lastIndexOf('/'));
        }
        RosterContactInfo contact = findByName(name);

        if (!(contact instanceof RosterContactInfo)) {
            contact = new RosterContactInfo();
            this.add(contact);
            contact.name = name;
        }
        contact.presence = presence;
    }

    public void contactInfoChanged(RosterEntry rosterEntry) {
        RosterContactInfo contact = findByName(rosterEntry.getUser());

        if (!(contact instanceof RosterContactInfo)) {
            contact = new RosterContactInfo();
            this.add(contact);
            contact.name = rosterEntry.getUser();
        }

        contact.rosterEntry = rosterEntry;
    }
}
