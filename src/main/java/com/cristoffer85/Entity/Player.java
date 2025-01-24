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
    // Player's position and movement properties
    private int x, y;
    private final int size;
    private final int moveSpeed;
    private final int acceleration = 1;
    private final int deceleration = 1;
    private int velocityX = 0;
    private int velocityY = 0;
    private boolean isMoving = false;
    private int lastDirection = 0;
    private PlayerMovement playerMovement;

    // Player's sprite and collision properties
    private PlayerSprite playerSprite;
    private final int collisionBoxSize;
    private final int collisionBoxOffsetX = 21;
    private final int collisionBoxOffsetY = 40;

    public Player(int x, int y, int size, int moveSpeed, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.moveSpeed = moveSpeed;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        // Set Collision box size
        this.collisionBoxSize = size / 3;

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
        // Draw out collision box/Debug
        g.setColor(Color.RED);
        g.drawRect(x + collisionBoxOffsetX, y + collisionBoxOffsetY, collisionBoxSize, collisionBoxSize);    
    
        // Determine direction of movement and render the sprite
        int direction = playerSprite.determineDirection(velocityX, velocityY, lastDirection);
        playerSprite.render(g, x, y, size, direction, isMoving);
    }
}