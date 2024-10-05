/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitaplonf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Tank {
    private int x, y;
    private final Color color;
    private static final int SPEED = 10;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    public Tank(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void moveLeft() {
        if (x - SPEED >= 0) {
            x -= SPEED;
        }
    }

    public void moveRight(int screenWidth) {
        if (x + SPEED + WIDTH <= screenWidth) {
            x += SPEED;
        }
    }

    public void moveUp() {
        if (y - SPEED >= 0) {
            y -= SPEED;
        }
    }

    public void moveDown(int screenHeight) {
        if (y + SPEED + HEIGHT <= screenHeight) {
            y += SPEED;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x + WIDTH / 2;
    }

    public int getY() {
        return y;
    }

    // Thêm phương thức getBounds
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
