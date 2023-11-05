package com.apogee.dev.CUJaS.SITACObjects;

public class Bullseye extends Figure {
    public Point center;
    public double hradius, vradius;
    public int rings;
    public double ring_distance;

    public Bullseye(Object center, double hradius, double vradius, int rings, double ring_distance, String... name) {
        super(name);
        this.center = as_point(center);
        this.hradius = hradius / 2;
        this.vradius = vradius / 2;
        this.rings = rings;
        this.ring_distance = ring_distance;
    }

    @Override
    public String toString() {
        return "(BullsEye) " + this.name + " "
                + this.center + " "
                + this.hradius + " "
                + this.vradius + " "
                + this.rings + " "
                + this.ring_distance;
    }
}
