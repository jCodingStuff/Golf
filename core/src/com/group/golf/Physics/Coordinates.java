package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */
public class Coordinates {
    double[] coord;
    Coordinates next;
    Coordinates prev;

    public Coordinates(double[] coord) {
        this.coord = coord;
        next = null;
    }
}
