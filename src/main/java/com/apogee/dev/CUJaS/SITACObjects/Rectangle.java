package com.apogee.dev.CUJaS.SITACObjects;

public class Rectangle extends Figure {
    public String name = "Rectangle";
    public Point start;
    public double horizontal, vertical;

    public Rectangle(Object start, double horizontal, double vertical, String... name) {
        super(name);
        this.start = as_point(start);
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
