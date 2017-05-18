package fr.UTC_CosTech_CRED.SimpleTouch.util;

import android.graphics.Color;

import org.jivesoftware.smack.packet.Presence;

import fr.UTC_CosTech_CRED.SimpleTouch.R;

/**
 * Created by Binova on 11/12/2015.
 */
public class RosterStatusType {
    public String text;
    public int color;
    public int drawable;
    public boolean canStartDialog;

    public RosterStatusType(String text, int color, int drawable, boolean canStartDialog) {
        this.text = text;
        this.color = color;
        this.drawable = drawable;
        this.canStartDialog = canStartDialog;
    }

    static public RosterStatusType get(Presence.Mode mode) {
        switch (mode) {
            case dnd:
                return new RosterStatusType(
                        "Ne pas déranger",
                        Color.parseColor("#cc0000"),
                        R.drawable.ic_pan_tool_black_48dp,
                        false
                );
            case away:
                return new RosterStatusType(
                        "Occupé",
                        Color.parseColor("#ff9900"),
                        R.drawable.ic_rowing_black_48dp,
                        false
                );
            case available:
            default:
                return new RosterStatusType(
                        "Disponible",
                        Color.parseColor("#009933"),
                        R.drawable.ic_check_circle_black_48dp,
                        true
                );
        }
    }

    static public RosterStatusType getUnavailable() {
        return new RosterStatusType(
                "Hors ligne",
                Color.GRAY,
                R.drawable.ic_highlight_off_black_48dp,
                false
        );
    }
}
