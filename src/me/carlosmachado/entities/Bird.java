package me.carlosmachado.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import me.carlosmachado.main.Game;
import me.carlosmachado.systems.ErrorMessage;
import me.carlosmachado.systems.Sound;

public class Bird extends Entity {

    public boolean isPressed = false;

    private BufferedImage[] sprites;

    private final double GRAVITY = 0.25;
    private final double IMPULSE = 5;
    private double movement = 0;

    private final int UP = 0;
    private final int MID = 1;
    private final int DOWN = 2;
    private int eyeDir = MID;

    private final int MAX_ANIM_FRAMES = 5, MAX_ANIM_INDEX = 3;
    private int curAnimFrames = 0, curAnimIndex = 0;

    public Bird(int x, int y, int width, int height, double speed) {
        super(x, y, width, height, speed, null);
        sprites = new BufferedImage[3];
        try {
            sprites[0] = ImageIO.read(getClass().getResource("/sprite/downflap.png"));
            sprites[1] = ImageIO.read(getClass().getResource("/sprite/midflap.png"));
            sprites[2] = ImageIO.read(getClass().getResource("/sprite/upflap.png"));
        } catch (IOException e) {
            ErrorMessage.print(e);
        }
        this.x -= sprites[0].getWidth() / 2;
    }

    @Override
    public void tick() {
        depth = 2;
        if (!isPressed) {
            movement += GRAVITY;
        } else if (isPressed && y > 0) {
            movement = -IMPULSE;
            Sound.wing.play();
        }
        y += movement;

        if (movement > 1.5) {
            eyeDir = DOWN;
        } else if (movement < 0) {
            eyeDir = UP;
        } else {
            eyeDir = MID;
        }

        // verifica colisão
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e != this) {
                // game over se colidiu
                if (Entity.isColidding(this, e)) {
                    Game.state = Game.LOSE;
                    Sound.die.play();
                    Sound.hit.play();
                    return;
                }
            }
        }

        curAnimFrames++;
        if (curAnimFrames >= MAX_ANIM_FRAMES) {
            curAnimFrames = 0;
            curAnimIndex++;
            if (curAnimIndex >= MAX_ANIM_INDEX) {
                curAnimIndex = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        switch (eyeDir) {
            case DOWN:
                g2.rotate(Math.toRadians(20), getX() + width / 2, getY() + height / 2);
                g2.drawImage(sprites[curAnimIndex], getX(), getY(), null);
                g2.rotate(Math.toRadians(-20), getX() + width / 2, getY() + height / 2);
                break;
            case UP:
                g2.rotate(Math.toRadians(-20), getX() + width / 2, getY() + height / 2);
                g2.drawImage(sprites[curAnimIndex], getX(), getY(), null);
                g2.rotate(Math.toRadians(20), getX() + width / 2, getY() + height / 2);
                break;
            default:
                g2.drawImage(sprites[curAnimIndex], getX(), getY(), null);
                break;
        }
    }

}
