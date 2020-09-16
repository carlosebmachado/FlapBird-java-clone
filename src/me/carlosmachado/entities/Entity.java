package me.carlosmachado.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Entity {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;

    public int depth;

    public boolean debug = false;

    protected BufferedImage sprite;

    public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public static Comparator<Entity> nodeSorter = (Entity n0, Entity n1) -> {
        if (n1.depth < n0.depth) {
            return +1;
        }
        if (n1.depth > n0.depth) {
            return -1;
        }
        return 0;
    };

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void tick() {
    }

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean isColidding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
        Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

        return e1Mask.intersects(e2Mask);
    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX(), getY(), null);
    }

}
