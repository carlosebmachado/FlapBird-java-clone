package me.carlosmachado.entities;

import java.awt.image.BufferedImage;

import me.carlosmachado.main.Game;
import me.carlosmachado.systems.Sound;

public class Pipe extends Entity {
    
    boolean enabled = true;

    public Pipe(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    @Override
    public void tick() {
        depth = 0;
        x -= speed;
        if (x + width < 0) {
            Game.entities.remove(this);
        }
        if (x + width < Game.SCREEN_WIDTH / 2 && enabled) {
            Game.score += 0.5;
            enabled = false;
            Sound.point.play();
        }
    }

}
