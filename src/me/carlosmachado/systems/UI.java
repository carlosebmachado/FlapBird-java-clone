package me.carlosmachado.systems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import me.carlosmachado.main.Game;

public class UI {

    private BufferedImage message;
    private BufferedImage gameover;

    public UI() {
        try {
            message = ImageIO.read(getClass().getResource("/sprite/message.png"));
            gameover = ImageIO.read(getClass().getResource("/sprite/gameover.png"));
        } catch (IOException e) {
            ErrorMessage.print(e);
        }
    }

    private void setDefaultText(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 32));
    }

    private void drawCentralizedText(Graphics g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        int x = Game.SCREEN_WIDTH / 2 - width / 2;
        g.drawString(text, x, y);
    }

    private void drawCentralizedText(Graphics g, String text) {
        int height = g.getFontMetrics().getHeight();
        int y = Game.SCREEN_HEIGHT / 2 + height / 2;
        drawCentralizedText(g, text, y);
    }

    private void drawCentralizedImage(Graphics g, BufferedImage image) {
        int x = Game.SCREEN_WIDTH / 2 - message.getWidth() / 2;
        int y = Game.SCREEN_HEIGHT / 2 - message.getHeight() / 2;
        g.drawImage(image, x, y, null);
    }

    public void startScreen(Graphics g) {
        drawCentralizedImage(g, message);
    }

    public void loseScreen(Graphics g) {
        drawCentralizedImage(g, gameover);
        setDefaultText(g);
        drawCentralizedText(g, "SCORE: " + (int) Game.score, 200);
        drawCentralizedText(g, "TAP");
    }

    public void render(Graphics g) {
        setDefaultText(g);
        drawCentralizedText(g, "" + (int) Game.score, 80);
    }

}
