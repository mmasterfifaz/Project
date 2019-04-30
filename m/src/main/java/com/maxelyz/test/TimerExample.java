/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.test;

import java.util.Timer;

/**
 *
 * @author admin
 */
public class TimerExample {

    public static void main(String[] args) {
        Timer timer1 = new Timer();             // Get timer 1
        Timer timer2 = new Timer();             // get timer 2

        long delay1 = 5 * 1000;                   // 5 seconds delay
        long delay2 = 3 * 1000;                   // 3 seconds delay

// Schedule the two timers to run with different delays.
//        timer1.schedule(new Task("object1"), 0, delay1);
//        timer2.schedule(new Task("Object2"), 0, delay2);
    }
}
