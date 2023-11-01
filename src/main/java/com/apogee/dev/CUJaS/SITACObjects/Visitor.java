package com.apogee.dev.CUJaS.SITACObjects;

//TODO: Implement Visitor pattern
public class Visitor {
    public Visitor() {
        return;
    }

    public void visit(Figure f) {
        System.out.println("Figure");
    }
}
