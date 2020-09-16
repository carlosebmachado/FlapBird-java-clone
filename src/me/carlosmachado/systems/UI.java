package me.carlosmachado.systems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import me.carlosmachado.main.Game;

public class UI {

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 32));
        int width = g.getFontMetrics().stringWidth("" + (int) Game.score);
        g.drawString("" + (int) Game.score, Game.SCREEN_WIDTH / 2 - width / 2, 80);
    }

}
