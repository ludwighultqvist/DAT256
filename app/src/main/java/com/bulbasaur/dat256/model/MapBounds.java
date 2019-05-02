package com.bulbasaur.dat256.model;

public class MapBounds {

    private Coordinates bottomLeft, topRight;

    public MapBounds(double blLat, double blLon, double trLat, double trLon) {
        this(new Coordinates(blLat, blLon), new Coordinates(trLat, trLon));
    }

    public MapBounds(Coordinates bottomLeft, Coordinates topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public Coordinates getBottomLeft() {
        return bottomLeft;
    }

    public Coordinates getTopRight() {
        return topRight;
    }
}
