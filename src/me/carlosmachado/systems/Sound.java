package me.carlosmachado.systems;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

    private AudioClip clip;

    public static final Sound die = new Sound("/sound/die.wav");
    public static final Sound hit = new Sound("/sound/hit.wav");
    public static final Sound point = new Sound("/sound/point.wav");
    public static final Sound swoosh = new Sound("/sound/swoosh.wav");
    public static final Sound wing = new Sound("/sound/wing.wav");

    private Sound(String name) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(name));
        } catch (Exception e) {
            ErrorMessage.print(e);
        }
    }

    public void play() {
        try {
            new Thread() {
                public void run() {
                    clip.play();
                }
            }.start();
        } catch (Exception e) {
            ErrorMessage.print(e);
        }
    }

    public void loop() {
        try {
            new Thread() {
                public void run() {
                    clip.loop();
                }
            }.start();
        } catch (Exception e) {
            ErrorMessage.print(e);
        }
    }
}
