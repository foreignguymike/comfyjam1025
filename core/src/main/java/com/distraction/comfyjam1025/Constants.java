package com.distraction.comfyjam1025;

import com.badlogic.gdx.graphics.Color;

public class Constants {

    public static final String TITLE = "The Heart Left Behind";
    public static final int WIDTH = 320;
    public static final int HEIGHT = 180;
    public static final int SCALE = 3;
    public static final int SWIDTH = WIDTH * SCALE;
    public static final int SHEIGHT = HEIGHT * SCALE;
    public static final boolean FULLSCREEN = false;

    public static final Color[] COLORS = {
        Color.valueOf("390c14"), // 0
        Color.valueOf("2a3636"), // 1
        Color.valueOf("465544"), // 2
        Color.valueOf("677a5d"), // 3
        Color.valueOf("8a9c73"), // 4
        Color.valueOf("babe85"), // 5
        Color.valueOf("ebda8d"), // 6
        Color.valueOf("fff4b0"), // 7
        Color.valueOf("4d2c17"), // 8
        Color.valueOf("71441c"), // 9
        Color.valueOf("957128"), // 10
        Color.valueOf("b8993d"), // 11
        Color.valueOf("d8bf63"), // 12
        Color.valueOf("bc9e24"), // 13
        Color.valueOf("e8c128"), // 14
        Color.valueOf("ffdd3d"), // 15
        Color.valueOf("5f521a"), // 16
        Color.valueOf("817b1a"), // 17
        Color.valueOf("a4a51e"), // 18
        Color.valueOf("d5c529"), // 19
        Color.valueOf("83391d"), // 20
        Color.valueOf("b65f21"), // 21
        Color.valueOf("db9421"), // 22
        Color.valueOf("ffbc30"), // 23
        Color.valueOf("d68533"), // 24
        Color.valueOf("ebaa46"), // 25
        Color.valueOf("facb67"), // 26
        Color.valueOf("ffe58c"), // 27
        Color.valueOf("ac4427"), // 28
        Color.valueOf("d96624"), // 29
        Color.valueOf("fc8e26"), // 30
        Color.valueOf("c64e2c"), // 31
        Color.valueOf("ef6544"), // 32
        Color.valueOf("e06d34"), // 33
        Color.valueOf("f4893d"), // 34
        Color.valueOf("fba74e"), // 35
        Color.valueOf("fcba62") // 36
    };

    public static final Color BLACK = COLORS[0];

    public static final Color SCENE_1_BG = COLORS[12];
    public static final Color SCENE_1_TREE_BG = COLORS[11];
    public static final Color SCENE_1_GRASS = COLORS[17];

    public static final Color PUZZLE_BG = COLORS[11];
    public static final Color PUZZLE_PIECE_BG = COLORS[27];
    public static final Color PUZZLE_PIECE_BORDER = COLORS[1];
}
