package com.example.labocred.mirrormodedialtact;

/**
 * Created by LaboCred on 29/03/2017.
 */

public interface TactileDialogViewHolder {

    class OPTIONS{
        public boolean isSurfaceSquared = true;         // D : La grille reste un carré (en fonction des définitions de largeur, longueur)
        public int     surfaceVerticalSize = 0;         // D : hauteur de la grille (% par rapport à la hauteur de l'écran)
        public int     surfaceHorizontalSize = 0;       // D : largeur de la grille (% par rapport à la largeur de l'écran)
        public int     surfaceHorizontalPosition = 0;   // D : position du centre de la grille sur la largeur de l'écran
        public int     surfaceVerticalPosition = 0;     // D : position du centre de la grille sur la hauteur de l'écran
        public int     touchRadius = 20;                // D : dimension de la zone touchée

    }
    void onDialogTouch(DialogTouchEvent event);
    OPTIONS GetOptions();
}
