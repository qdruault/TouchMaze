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

    /**
     * Cherche un contact dans la liste.
     * @param name : le nom du contact à trouver.
     * @return
     */
    public RosterContactInfo findByName(String name) {
        for (RosterContactInfo contact: this) {
            if (contact.name.equals(name)) {
                return contact;
            }
        }

        return null;
    }

    public void presenceChanged(Presence presence) {
        // On récupère le nom de l'expéditeur.
        String name = presence.getFrom();
        if (presence.getFrom().lastIndexOf('/') > 0) {
            name = presence.getFrom().substring(0, presence.getFrom().lastIndexOf('/'));
        }
        // On cherche le contact dans la liste.
        RosterContactInfo contact = findByName(name);

        // Si on ne le trouve pas.
        if (!(contact instanceof RosterContactInfo)) {
            // On l'ajoute.
            contact = new RosterContactInfo();
            this.add(contact);
            contact.name = name;
        }

        // On met à jour.
        contact.presence = presence;
    }

    public void contactInfoChanged(RosterEntry rosterEntry) {
        // On cherche le contact.
        RosterContactInfo contact = findByName(rosterEntry.getUser());

        // Si on ne le trouve pas.
        if (!(contact instanceof RosterContactInfo)) {
            // On l'ajoute.
            contact = new RosterContactInfo();
            this.add(contact);
            contact.name = rosterEntry.getUser();
        }

        // On met à jour.
        contact.rosterEntry = rosterEntry;
    }
}
