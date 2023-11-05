package com.apogee.dev.CUJaS.SITACObjects;

public class Rectangle extends Figure {
    public Point start;
    public double horizontal, vertical;

    public Rectangle(Object start, double horizontal, double vertical, String... name) {
        super(name);
        this.start = as_point(start);
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public String toString() {
        return this.name + " ("
                + this.start + " "
                + this.horizontal + " "
                + this.vertical + ")";
    }
}
