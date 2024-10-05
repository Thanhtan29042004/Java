/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitaplonf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final Timer timer;
    private final Tank playerTank;
    private final ArrayList<Bullet> bullets;
    private final ArrayList<Enemy> enemyTanks;
    private final Random random;
    private boolean gameOver;

    public Game() {
        playerTank = new Tank(100, 400, Color.GREEN);
        bullets = new ArrayList<>();
        enemyTanks = new ArrayList<>();
        random = new Random();
        gameOver = false;

        // Tạo một số tank địch ban đầu
        spawnEnemy();
        spawnEnemy();
        spawnEnemy();

        timer = new Timer(16, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
    }

    private void spawnEnemy() {
        // Đảm bảo kích thước cửa sổ đã được khởi tạo
        int screenWidth = getWidth() > 0 ? getWidth() : 800;  // Sử dụng giá trị mặc định nếu getWidth() là 0
        int screenHeight = getHeight() > 0 ? getHeight() : 600;  // Sử dụng giá trị mặc định nếu getHeight() là 0

        // Tạo vị trí ngẫu nhiên cho tank địch
        int x = random.nextInt(screenWidth - 40) + 20;  // Giới hạn vị trí theo kích thước cửa sổ
        int y = random.nextInt(screenHeight - 40) + 20;

        enemyTanks.add(new Enemy(x, y, Color.RED));
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over", getWidth() / 2 - 40, getHeight() / 2);
            return;
        }

        playerTank.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Enemy enemyTank : enemyTanks) {
            enemyTank.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        // Cập nhật đạn
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.getX() > getWidth() || bullet.getY() > getHeight()) {
                bullets.remove(i);
                i--;
            }
        }

        // Cập nhật tank địch
        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemyTank = enemyTanks.get(i);
            enemyTank.moveTowards(playerTank.getX(), playerTank.getY());

            // Kiểm tra va chạm giữa đạn và địch
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if (enemyTank.getBounds().intersects(bullet.getBounds())) {
                    enemyTanks.remove(i);
                    bullets.remove(j);
                    i--;
                    spawnEnemy(); // Sinh ra địch mới
                    break;
                }
            }

            // Kiểm tra va chạm giữa địch và người chơi
            if (enemyTank.getBounds().intersects(playerTank.getBounds())) {
                gameOver = true; // Người chơi thua
                return;
            }

            // Nếu tank địch ra khỏi màn hình thì loại bỏ nó
            if (enemyTank.getX() < 0 || enemyTank.getX() > getWidth() ||
                    enemyTank.getY() < 0 || enemyTank.getY() > getHeight()) {
                enemyTanks.remove(i);
                i--;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            playerTank.moveLeft();
        }
        if (key == KeyEvent.VK_RIGHT) {
            playerTank.moveRight(getWidth());
        }
        if (key == KeyEvent.VK_UP) {
            playerTank.moveUp();
        }
        if (key == KeyEvent.VK_DOWN) {
            playerTank.moveDown(getHeight());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        bullets.add(new Bullet(playerTank.getX(), playerTank.getY(), mouseX, mouseY));  // Bắn đạn về phía tọa độ chuột
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Game game = new Game();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
