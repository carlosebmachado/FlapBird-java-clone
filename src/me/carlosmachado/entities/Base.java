package me.carlosmachado.entities;

import java.awt.image.BufferedImage;
import me.carlosmachado.main.Game;

public class Base extends Entity {

    public Base(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    @Override
    public void tick() {
        depth = 1;
        x -= speed;
        if (x + width < 0) {
            Game.entities.remove(this);
        }
    }
}
