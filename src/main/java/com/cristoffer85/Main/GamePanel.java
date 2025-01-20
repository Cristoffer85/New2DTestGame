package com.cristoffer85.Main;

import com.cristoffer85.Entities.Player;
import com.cristoffer85.Entities.Obstacle;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Player player;
    private Obstacle obstacle;
    private KeyHandler keyHandler;

    public GamePanel() {
        player = new Player(50, 50, 20, 5);
        obstacle = new Obstacle(new Rectangle(200, 200, 50, 50));

        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);

        setFocusable(true);
        setPreferredSize(new Dimension(600, 600));

        Timer timer = new Timer(16, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        player.move(keyHandler, getBounds(), obstacle.getRectangle());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        player.render(g);
        obstacle.render(g);
    }
}