package org.c1.utils;

public enum CardinalDirection {
    NORTH(0f),
    SOUTH((float)Math.PI),
    EAST((float)Math.PI/2f),
    WEST(-(float)Math.PI/2f);

    static {
        NORTH.setNext(WEST);
        WEST.setNext(SOUTH);
        SOUTH.setNext(EAST);
        EAST.setNext(NORTH);
    }

    private final float angle;

    private CardinalDirection next;

     CardinalDirection(float angle) {
         this.angle = angle;
    }

    public CardinalDirection next() {
        return next;
    }

    private void setNext(CardinalDirection next) {
        this.next = next;
    }

    public float angle() {
        return angle;
    }
}
