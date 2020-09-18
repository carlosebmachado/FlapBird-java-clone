package me.carlosmachado.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import me.carlosmachado.entities.Entity;
import me.carlosmachado.entities.Bird;
import me.carlosmachado.systems.UI;
import me.carlosmachado.systems.Generator;
import me.carlosmachado.systems.ErrorMessage;
import me.carlosmachado.systems.Sound;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    private static final long serialVersionUID = 1L;
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int SCREEN_WIDTH = 270;
    public static final int SCREEN_HEIGHT = 480;

    private final BufferedImage curFrame;

    public static List<Entity> entities;
    public static Bird player;

    public static Generator generator;
    private BufferedImage background;
    
    public static final int PLAYING = 0;
    public static final int LOSE = 1;
    public static final int FIRST_START = 2;
    public static int state = FIRST_START;

    public UI ui;

    public static double score = 0;

    public Game() {
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        initFrame();
        curFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // objects
        try {
            background = ImageIO.read(getClass().getResource("/sprite/background.png"));
        } catch (IOException e) {
            ErrorMessage.print(e);
        }
        entities = new ArrayList<>();
        player = new Bird(SCREEN_WIDTH / 4, (int)(Game.SCREEN_HEIGHT / 4 * 2.5), 34, 24, 2);
        generator = new Generator();
        ui = new UI();

        entities.add(player);
    }

    public void initFrame() {
        frame = new JFrame("Flappy Bird");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            ErrorMessage.print(e);
        }
    }

    public static void restartGame() {
        score = 0;
        entities.clear();
        player = new Bird(Game.SCREEN_WIDTH / 4, (int)(Game.SCREEN_HEIGHT / 4 * 2.5), 34, 24, 2);
        player.isPressed = true;
        entities.add(Game.player);
        generator = new Generator();
    }

    public void tick() {
        switch(state){
            case PLAYING:
                generator.tick();
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    e.tick();
                }
                break;
            case LOSE:
                if(player.isPressed){
                    Game.restartGame();
                    Sound.swoosh.play();
                    state = PLAYING;
                }
            case FIRST_START:
                if(player.isPressed){
                    Sound.swoosh.play();
                    state = PLAYING;
                }
                break;
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = curFrame.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // game render
        g.drawImage(background, 0, 0, null);
        Collections.sort(entities, Entity.nodeSorter);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        switch(state){
            case PLAYING:
                ui.render(g);
                break;
            case LOSE:
                ui.loseScreen(g);
                break;
            case FIRST_START:
                ui.startScreen(g);
                break;
        }
        //

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(curFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.isPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.isPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.isPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        player.isPressed = false;
    }

    public static void main(String args[]) {
        Game game = new Game();
        game.start();
    }

}
