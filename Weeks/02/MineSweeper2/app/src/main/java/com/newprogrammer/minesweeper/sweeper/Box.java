package com.newprogrammer.minesweeper.sweeper;

import android.graphics.Bitmap;

/**
 * Created by Владимир on 23.12.2017.
 */
public enum Box {
    ZERO,
    NUM1,
    NUM2,
    NUM3,
    NUM4,
    NUM5,
    NUM6,
    NUM7,
    NUM8,
    BOMB,

    OPENED,
    CLOSED,
    FLAGED,
    BOMBED,
    NOBOMB;
    public Bitmap image;

    Box nextNumberBox() {
        return Box.values()[this.ordinal() + 1];
    }

    int getNumber() {
        int nr = ordinal();
        if (nr>=Box.NUM1.ordinal() && nr <= Box.NUM8.ordinal())
            return ordinal();
        return -1;
    }
}
