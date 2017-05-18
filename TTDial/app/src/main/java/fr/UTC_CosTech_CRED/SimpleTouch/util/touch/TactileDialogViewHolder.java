package fr.UTC_CosTech_CRED.SimpleTouch.util.touch;

/**
 * Created by Binova on 23/12/2015.
 */



public interface TactileDialogViewHolder {
    class OPTIONS {
        public int     touchRadius = 0;                 // TT & D : rayon du cercle de toucher (en % de la largeur de l'écran)
        public boolean IsMyPos = true;                 // TT & D : voir mon/mes cercle/s
        public boolean IsParthnerPos = true;           // TT & D : voir les cercles du partenaire
        public boolean IsModeTouchThrough = true;     // => T : mode TouchThrough activé
        public boolean TTVisual = true;                // TT : TouchThrough, retour visuel
        public boolean TTVibr = true;                  // TT : TouchThrough, retour vibration
        public boolean TTSound = false;                 // TT : TouchThrough, retour sonore (beep)
        public int     DisconnectTimeOut = 300;         // TT & D : en seconde, temps d'inactivité avant déconnection

    };

    void onDialogTouch(DialogTouchEvent event);
    OPTIONS GetOptions();
}
