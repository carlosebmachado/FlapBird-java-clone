package me.carlosmachado.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import me.carlosmachado.entities.Entity;
import me.carlosmachado.entities.Player;
import me.carlosmachado.systems.UI;
import me.carlosmachado.systems.Generator;
import me.carlosmachado.systems.ErrorMessage;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    private static final long serialVersionUID = 1L;
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int SCREEN_WIDTH = 270;
    public static final int SCREEN_HEIGHT = 480;

    private final BufferedImage curFrame;

    public static List<Entity> entities;
    public static Player player;

    public static Generator generator;
    private BufferedImage background;

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
        player = new Player(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, 34, 24, 2);
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
        player = new Player(Game.SCREEN_WIDTH / 2 - 30, Game.SCREEN_HEIGHT / 2, 16, 16, 2);
        entities.add(Game.player);
        generator = new Generator();
    }

    public void tick() {
        generator.tick();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = curFrame.getGraphics();
        g.setColor(new Color(122, 102, 255));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // game render
        g.drawImage(background, 0, 0, null);
        Collections.sort(entities, Entity.nodeSorter);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        //

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(curFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        ui.render(g);
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
