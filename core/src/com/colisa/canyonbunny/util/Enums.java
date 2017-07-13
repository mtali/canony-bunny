package com.colisa.canyonbunny.util;


public class Enums {
    public enum VIEW_DIRECTION {LEFT, RIGHT}

    public enum JUMP_STATE {
        GROUNDED,           // Character is standing on the platform
        FALLING,            // Falling down
        JUMP_RISING,        // Rising in the air
        JUMP_FALLING        // Falling down after jump
    }
}
