package com.apogee.dev.CUJaS.SITACObjects;

public class Ellipse extends Figure {
    public String name = "Ellipse";
    public Point center;
    public double hradius, vradius;

    // accepts either Point or double[] for center
    public Ellipse(Object center, double hradius, double vradius, String... name) {
        super(name);
        this.center = as_point(center);
        this.hradius = hradius / 2;
        this.vradius = vradius / 2;
    }

    @Override
    public String toString() {
        return this.name + " ("
                + this.center + " "
                + this.hradius + " "
                + this.vradius + ")";
    }
}

