//package cassebriques;

import java.awt.*;
import java.util.Arrays;
import java.util.ListIterator;

class BriqueRenforcee extends Brique {
    private final ListIterator<Color> colorList = Arrays.asList(Color.darkGray, Color.gray, Color.lightGray).listIterator();
    private final int SIMPLE=0;

    public BriqueRenforcee() {
        super();
        couleur = colorList.next();
    }

    public int choc() {
        if (colorList.hasNext())
            couleur = colorList.next();
        else 
            super.choc();

        return SIMPLE;
    }

}