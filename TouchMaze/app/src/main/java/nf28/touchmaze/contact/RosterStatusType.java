package nf28.touchmaze.contact;

import android.graphics.Color;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Binova on 11/12/2015.
 */
public class RosterStatusType {
    private String text;
    private int color;
    private boolean canStartDialog;

    /**
     * Constructeur
     * @param text
     * @param color
     * @param canStartDialog
     */
    public RosterStatusType(String text, int color, boolean canStartDialog) {
        this.text = text;
        this.color = color;
        this.canStartDialog = canStartDialog;
    }

    /**
     * Factory.
     * @param mode
     * @return
     */
    static public RosterStatusType get(Presence.Mode mode) {
        switch (mode) {
            case dnd:
                return new RosterStatusType(
                        "Ne pas déranger",
                        Color.parseColor("#cc0000"),
                        false
                );
            case away:
                return new RosterStatusType(
                        "Occupé",
                        Color.parseColor("#ff9900"),
                        false
                );
            case available:
            default:
                return new RosterStatusType(
                        "Disponible",
                        Color.parseColor("#009933"),
                        true
                );
        }
    }

    /**
     * Factory.
     * @return
     */
    static public RosterStatusType getUnavailable() {
        return new RosterStatusType(
                "Hors ligne",
                Color.GRAY,
                false
        );
    }

    // Getters et setters.
    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public boolean isAbleToStartDialog() {
        return canStartDialog;
    }
}
