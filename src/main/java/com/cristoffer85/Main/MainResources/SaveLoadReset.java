package com.cristoffer85.Main.MainResources;

import com.cristoffer85.Entity.Obstacle;
import com.cristoffer85.Entity.Player;
import com.cristoffer85.States.GameState;
import com.cristoffer85.States.StatesResources.StatesDefinitions;
import com.cristoffer85.Main.GamePanel;
import com.cristoffer85.Tile.TileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveLoadReset implements Serializable {
    private static final long serialVersionUID = 1L;

    private int playerX;
    private int playerY;

    public SaveLoadReset(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public static void saveGame(Player player, String profileName) {
        SaveLoadReset saveData = new SaveLoadReset(player.getX(), player.getY());
        String filePath = "profiles/" + profileName + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(saveData);
            System.out.println("Game saved successfully to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame(Player player, GamePanel gamePanel, String profileName) {
        String filePath = "profiles/" + profileName + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            SaveLoadReset saveData = (SaveLoadReset) ois.readObject();
            player.setX(saveData.getPlayerX());
            player.setY(saveData.getPlayerY());
            System.out.println("Game loaded successfully from " + filePath);
            gamePanel.setGameState(StatesDefinitions.GAME);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void resetGame(GamePanel gamePanel, int baseWidth, int baseHeight, int scaleFactor, String profileName) {
        Player player = new Player(30, 30, 64, 6);
        Obstacle.addObstacles();
        TileManager.tilesByMapSize("/MainWorld.txt");
        GameState gameState = new GameState(player, baseWidth, baseHeight, scaleFactor);
        gamePanel.add(gameState, StatesDefinitions.GAME.name());
        gamePanel.setPlayer(player);
        gamePanel.setGameState(gameState);
    }

    public static void createProfile(String profileName) {
        String filePath = "profiles/" + profileName + ".dat";
        try {
            Files.createFile(Paths.get(filePath));
            System.out.println("Profile created: " + profileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}