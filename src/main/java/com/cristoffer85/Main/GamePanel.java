package com.cristoffer85.Main;

import com.cristoffer85.Entity.Obstacle;
import com.cristoffer85.Tile.Tile;
import com.cristoffer85.Entity.Player;
import com.cristoffer85.States.MainMenuState;
import com.cristoffer85.States.StatesResources.StateDefinitions;
import com.cristoffer85.States.GameState;
import com.cristoffer85.States.InitialState;
import com.cristoffer85.States.PauseState;
import com.cristoffer85.States.SettingsState;
import com.cristoffer85.Main.MainResources.SaveLoadReset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

public class GamePanel extends JPanel {
    private Player player;
    private KeyHandler keyHandler;

    private MainMenuState mainMenuState;
    private GameState gameState;
    private PauseState pauseState;
    private InitialState initialState;
    private SettingsState settingsState;

    private String profileName;
    private StateDefinitions currentState;

    public GamePanel() {
        // Initialize player
        player = new Player(30, 30, 64, 6);                            // Set player starting x, starting y, sprite size, moving speed

        // Initialize obstacles
        Obstacle.addObstacles();

        // Initialize key handler
        keyHandler = new KeyHandler(this);

        // Initialize tilesheet and map
        Tile.loadTilesheet("/TileSheet.png", 64, 64);          // Load TileSheet.png from file, set tile width and tile height. Map rendering will adjust to these values.
        Tile.tilesByMapSize("/MainWorld.txt");                                      // Load map.txt from file. Set whatever size you want for the map in the text file. Right now current 128x128 tiles.                               

        // ---------- Initialize different states ----------
        initialState = new InitialState(this);
        mainMenuState = new MainMenuState(this);
        gameState = new GameState(player, 960, 540, 1); // Default values for 1920x1080 resolution, the scale is 2 by default for scaling the view up to "SNES" style
        pauseState = new PauseState(this);
        settingsState = new SettingsState(this); 

        // ..and add them to the "card" layout.
        setLayout(new CardLayout());
        add(initialState, StateDefinitions.INITIAL_STATE.name());
        add(mainMenuState, StateDefinitions.MAIN_MENU.name());
        add(gameState, StateDefinitions.GAME.name());
        add(pauseState, StateDefinitions.PAUSE_MENU.name());
        add(settingsState, StateDefinitions.SETTINGS_MENU.name());
        //---------------------------------------------------

        // Main Game loop
        Timer timer = new Timer(16, e -> {
                // IF GameState: Update game state
            if (currentState == StateDefinitions.GAME) {
                List<Rectangle> straightObstacles = Obstacle.getStraightObstacles();
                List<Line2D> diagonalObstacles = Obstacle.getDiagonalObstacles();
                player.move(keyHandler, straightObstacles, diagonalObstacles);
                gameState.repaint();
            }
        });
        timer.start();
    }

    // ## Helper Methods ##
    public StateDefinitions getCurrentState() {
        return currentState;
    }

    public void initializeGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void changeGameState(StateDefinitions newState) {
        if (newState == StateDefinitions.PAUSE_MENU) {
            pauseState.freezeGameBackground(this, gameState);
        }
        currentState = newState;
        ((CardLayout) getLayout()).show(this, newState.name());
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void saveGame() {
        SaveLoadReset.saveGame(player, profileName);
    }

    public void loadGame() {
        SaveLoadReset.loadGame(player, this, profileName);
    }

    public void resetGame() {
        SaveLoadReset.resetGame(this, 960, 540, 2, profileName); // Default values
    }

    public void changeResolution(int width, int height) {
        gameState.updateResolution(width, height);
    }
}