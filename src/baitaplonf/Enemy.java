/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitaplonf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author tanbt
 */
public class Enemy {
    private int x, y;
    private final Color color;
    private static final int SPEED = 2;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 20;

    public Enemy(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public void moveTowards(int targetX, int targetY) {
        if (x < targetX) {
            x += SPEED;
        } else {
            x -= SPEED;
        }

        if (y < targetY) {
            y += SPEED;
        } else {
            y -= SPEED;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.fillRect(x + 15, y - 10, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}