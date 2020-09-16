package me.carlosmachado.systems;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import me.carlosmachado.entities.Base;
import me.carlosmachado.entities.Pipe;
import me.carlosmachado.main.Game;

public class Generator {
    
    public final static int BASE_WIDTH = 336;
    public final static int BASE_HEIGHT = 112;
    public final static int PIPE_WIDTH = 52;
    public final static int PIPE_HEIGHT = 320;
    public final static int VELOCITY = 2;

    public final int MAX_FRAMES_PIPE = 120;
    public int framesPipe = 0;
    public final int MAX_FRAMES_BASE = BASE_WIDTH / VELOCITY;
    public int framesBase = 0;

    private BufferedImage[] spritesPipe;
    private BufferedImage spriteBase;

    public static Random rand = new Random();

    public Generator() {
        spritesPipe = new BufferedImage[2];
        try {
            spriteBase = ImageIO.read(getClass().getResource("/sprite/base.png"));
            spritesPipe[0] = ImageIO.read(getClass().getResource("/sprite/uppipe.png"));
            spritesPipe[1] = ImageIO.read(getClass().getResource("/sprite/downpipe.png"));
        } catch (IOException e) {
            ErrorMessage.print(e);
        }
        
        Game.entities.add(new Base(
                    0, Game.SCREEN_HEIGHT - BASE_HEIGHT / 2,
                    BASE_WIDTH, BASE_HEIGHT,
                    VELOCITY, spriteBase));
        Game.entities.add(new Base(
                    BASE_WIDTH, Game.SCREEN_HEIGHT - BASE_HEIGHT / 2,
                    BASE_WIDTH, BASE_HEIGHT,
                    VELOCITY, spriteBase));
        
    }

    public void tick() {
        framesPipe++;
        framesBase++;
        // gera os tubos
        if (framesPipe >= MAX_FRAMES_PIPE) {
            int pipeGap = -125 + rand.nextInt(200);
            int dist = 140;
            Pipe uppipe = new Pipe(
                    Game.SCREEN_WIDTH, -dist + pipeGap,
                    PIPE_WIDTH, PIPE_HEIGHT,
                    VELOCITY, spritesPipe[0]);
            Pipe downpipe = new Pipe(
                    Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT - PIPE_HEIGHT + dist + pipeGap,
                    PIPE_WIDTH, PIPE_HEIGHT,
                    VELOCITY, spritesPipe[1]);

            Game.entities.add(uppipe);
            Game.entities.add(downpipe);
            framesPipe = 0;
        }
        // gera as bases
        if (framesBase >= MAX_FRAMES_BASE) {
            Base base = new Base(
                    Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT - BASE_HEIGHT / 2,
                    BASE_WIDTH, BASE_HEIGHT,
                    VELOCITY, spriteBase);

            Game.entities.add(base);
            framesBase = 0;
        }
    }

}
