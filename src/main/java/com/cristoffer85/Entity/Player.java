package com.cristoffer85.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.cristoffer85.Main.KeyHandler;
import com.cristoffer85.Entity.EntityResources.PlayerSprite;
import com.cristoffer85.Entity.EntityResources.PlayerMovement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private int x, y;
    private final int size;
    private final int moveSpeed;
    private final int acceleration = 1;
    private final int deceleration = 1;

    private int velocityX = 0;
    private int velocityY = 0;

    private PlayerSprite playerSprite;

    private boolean isMoving = false;
    private int lastDirection = 0;

    private final int scale = 1; // Scale factor for rendering (note: if changing, adjust render method (offset) accordingly)

    private PlayerMovement playerMovement;

    public Player(int x, int y, int size, int moveSpeed, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.moveSpeed = moveSpeed;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        initializePlayerSprite();
        playerMovement = new PlayerMovement(this, acceleration, deceleration);
    }

    private void initializePlayerSprite() {
        playerSprite = new PlayerSprite("/TestCharx9.png", 64, 64, 4, 9);
    }

    public void move(KeyHandler keyHandler, Rectangle boundary, List<Rectangle> straightObstacles, List<Line2D> diagonalObstacles) {
        playerMovement.move(keyHandler, boundary, straightObstacles, diagonalObstacles);
    }

    public void render(Graphics g) {
        // Determine the scaled size of the sprite
        int scaledSize = size * scale;
    
        // Draw out collision box/Debug
        g.setColor(Color.RED);
        g.drawRect(x - (scaledSize - size) / 2, y - (scaledSize - size) / 2, size, size);   
    
        // OFFSET: Adjust for scaling to center the sprite (num value is the offset)
        int renderX = x - scaledSize - 1;
        int renderY = y - scaledSize - 16;
    
        // Determine direction of movement and render the sprite
        int direction = playerSprite.determineDirection(velocityX, velocityY, lastDirection);
        playerSprite.render(g, renderX, renderY, scaledSize, direction, isMoving, scale);
    }
}