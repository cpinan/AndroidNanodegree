package com.carlospinan;

public class MyJavaLibrary {

    private static final String[] JOKES = {
            "Why was the scarecrow given an award? Because he was outstanding in his field!",
            "Why did the tomato blush? Cos he saw the salad dressing.",
            "What is red and green and goes 100 MPH\nA. A frog in a blender"
    };

    public static String getJoke() {
        int length = JOKES.length;
        return JOKES[(int) (Math.random() * length)];
    }
}
