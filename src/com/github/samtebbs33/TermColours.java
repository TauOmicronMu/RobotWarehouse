package com.github.samtebbs33;

/**
 * Created by samtebbs on 02/02/2016.
 */
public class TermColours {

    public static final String COL_RESET = "\u001B[0m",
            COL_RED = "\u001B[31m",
            COL_GREEN = "\u001B[32m",
            COL_BLUE = "\u001B[34m",
            COL_CYAN = "\u001B[36m",
            COL_PURPLE = "\u001B[35m",
            COL_YELLOW = "\u001B[33m",
            COL_BROWN = "\u001B[37m",
            COL_GREY = "\u001B[30m";
    public static final String[] COLOURS = {COL_RED, COL_GREEN, COL_BLUE, COL_CYAN, COL_PURPLE, COL_YELLOW, COL_BROWN, COL_GREY};

    public static String colourString(String str, String colour) {
        return colour + str + COL_RESET;
    }

}
