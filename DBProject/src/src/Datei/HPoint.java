package src.Datei;

import java.io.Serializable;


/**
 * @author  mohamed
 */
class HPoint implements Serializable {

    /**
     * @uml.property  name="coord"
     * @uml.associationEnd  multiplicity="(0 -1)"
     */
    protected Comparable[] coord;

    protected HPoint(int n) {
        coord = new Comparable[n];
    }

    protected HPoint(Comparable[] x) {

        coord = new Comparable[x.length];
        for (int i = 0; i < x.length; ++i)
            coord[i] = x[i];
    }

    protected Object clone() {

        return new HPoint(coord);
    }

    protected boolean equals(HPoint p) {

        // seems faster than java.util.Arrays.equals(), which is not
        // currently supported by Matlab anyway
        for (int i = 0; i < coord.length; ++i)
            if (!coord[i].equals(p.coord[i]))
                return false;

        return true;
    }

    protected static double sqrdist(HPoint x, HPoint y) {

        double dist = 0;

        for (int i = 0; i < x.coord.length; ++i) {
            double diff = (x.coord[i].compareTo(y.coord[i]));
            dist += (diff * diff);
        }

        return dist;

    }

    protected static double eucdist(HPoint x, HPoint y) {

        return Math.sqrt(sqrdist(x, y));
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < coord.length; ++i) {
            s = s + coord[i] + " ";
        }
        return s;
    }

}