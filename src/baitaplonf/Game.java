package baitaplonf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final Timer timer;
    private Tank playerTank;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemyTanks;
    private final Random random;
    private boolean gameOver;
    private int score = 0;

    public Game() {
        random = new Random();
        initGame();

        timer = new Timer(16, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
    }

    // Khởi tạo lại trò chơi
    private void initGame() {
        playerTank = new Tank(100, 400, Color.GREEN);
        bullets = new ArrayList<>();
        enemyTanks = new ArrayList<>();
        gameOver = false;
        score = 0;

        // Tạo một số tank địch ban đầu
        spawnEnemy();
        spawnEnemy();
        spawnEnemy();
    }

    private void spawnEnemy() {
        int screenWidth = getWidth() > 0 ? getWidth() : 800;
        int screenHeight = getHeight() > 0 ? getHeight() : 600;
        int x = random.nextInt(screenWidth - 40) + 20;
        int y = random.nextInt(screenHeight - 40) + 20;

        enemyTanks.add(new Enemy(x, y, Color.RED));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Hiển thị thông báo "Game Over" khi thua
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2 - 20);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press R to Restart", getWidth() / 2 - 80, getHeight() / 2 + 40);
            return; // Dừng vẽ các đối tượng khác khi game kết thúc
        }

        playerTank.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Enemy enemyTank : enemyTanks) {
            enemyTank.draw(g);
        }

        // Vẽ điểm số
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Kiểm tra nếu game đã kết thúc
        if (gameOver) {
            return; // Dừng cập nhật nếu game đã kết thúc
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
                    spawnEnemy();
                    score += 10;
                    break;
                }
            }

            // Kiểm tra va chạm giữa địch và người chơi
            if (enemyTank.getBounds().intersects(playerTank.getBounds())) {
                gameOver = true; // Người chơi thua
                repaint(); // Vẽ lại ngay lập tức để hiển thị thông báo "Game Over"
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

        if (!gameOver) {
            // Di chuyển khi game chưa kết thúc
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

        // Khi trò chơi kết thúc, kiểm tra phím 'R' để khởi động lại
        if (gameOver && key == KeyEvent.VK_R) {
            initGame();  // Khởi tạo lại trò chơi
            repaint();   // Vẽ lại màn hình ngay lập tức
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            bullets.add(new Bullet(playerTank.getX(), playerTank.getY(), mouseX, mouseY));  // Bắn đạn về phía tọa độ chuột
        }
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
