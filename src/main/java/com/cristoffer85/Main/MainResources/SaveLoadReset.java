package com.cristoffer85.Main.MainResources;

import com.cristoffer85.Entity.Obstacle;
import com.cristoffer85.Entity.Player.Player;
import com.cristoffer85.States.GameState;
import com.cristoffer85.States.SettingsState;
import com.cristoffer85.States.StatesResources.StateDefinitions;
import com.cristoffer85.Main.GamePanel;
import com.cristoffer85.Main.EventHandler;

import java.io.*;

public class SaveLoadReset {

    public static void saveGame(Player player, String profileName, String currentMap) {
        CRUDProfile saveData = new CRUDProfile(player.getX(), player.getY(), currentMap);
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
            CRUDProfile saveData = (CRUDProfile) ois.readObject();
            player.setX(saveData.getPlayerX());
            player.setY(saveData.getPlayerY());
            gamePanel.loadMap(saveData.getCurrentMap());
            System.out.println("Game loaded successfully from " + filePath);
            gamePanel.changeGameState(StateDefinitions.GAME);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void resetGame(GamePanel gamePanel, String profileName) {
        Player player = new Player(30, 30, 64, 6);
        Obstacle.addObstacles();
        gamePanel.loadMap("MainWorld");
        EventHandler eventHandler = new EventHandler(player);
        GameState gameState = new GameState(player, gamePanel.getWidth(), gamePanel.getHeight(), eventHandler);
        gamePanel.add(gameState, StateDefinitions.GAME.name());
        gamePanel.setPlayer(player);
        gamePanel.initializeGameState(gameState);
        gamePanel.setScaleFactor(SettingsState.SCALE_FACTORS_MAP.get("SNES"));
    }
}