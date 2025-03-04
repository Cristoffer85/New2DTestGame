package com.cristoffer85.Main;

import com.cristoffer85.Data.SaveLoadResetGame;
import com.cristoffer85.Entity.Obstacle;
import com.cristoffer85.Entity.Player.Player;
import com.cristoffer85.States.MainMenuState;
import com.cristoffer85.States.StatesResources.StateDefinitions;
import com.cristoffer85.States.CharacterState;
import com.cristoffer85.States.GameState;
import com.cristoffer85.States.InitialState;
import com.cristoffer85.States.PauseState;
import com.cristoffer85.States.SettingsState;
import com.cristoffer85.Map.MapHandler;
import com.cristoffer85.Map.Tile;
import com.cristoffer85.Objects.GameObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

public class GamePanel extends JPanel {
    private String profileName;
    private Player player;
    private KeyHandler keyHandler;
    private MapHandler mapHandler;
    private EventHandler eventHandler;

    private StateDefinitions currentState;
    private MainMenuState mainMenuState;
    private GameState gameState;
    private PauseState pauseState;
    private InitialState initialState;
    private SettingsState settingsState;
    private CharacterState characterState;

    public GamePanel() {
        // --------- Initializization of diverse game components ---------
        // NOTE: Do **NOT** change order of below initializations. 
        // It will mess up Events* and Assets* among two things.
        
        // Player
        player = new Player(30, 30, 64, 6);                            // Set player starting x, starting y, sprite size, moving speed
        // Keyhandler
        keyHandler = new KeyHandler(this);
        // Tilesheet
        Tile.loadTilesheet("/TileSheet.png", 64, 64);          // Load TileSheet.png from file, set tile width and tile height. Map rendering will adjust to these values.
        // Load initial map
        mapHandler = new MapHandler("MainWorld");                             // Load map.txt from file. Set whatever size you want for the map in the text file. Mainworld right now = 128x128 tiles.
        // Event handler
        eventHandler = new EventHandler(player, mapHandler);
        // Sets up individual events per map
        mapHandler.setEventHandler(eventHandler);

        // --------- Different states ---------------------------
        initialState = new InitialState(this);
        mainMenuState = new MainMenuState(this);
        gameState = new GameState(player, 1920, 1080, eventHandler);// Initial value of 1920x1080 resolution
        gameState.setScaleFactor(0.5);                                       // Initial value of scalefactor .5 for 'SNES' style
        pauseState = new PauseState(this);
        settingsState = new SettingsState(this); 
        characterState = new CharacterState(this, gameState, player);

        // ..and add them to the "card" layout...
        setLayout(new CardLayout());
        add(initialState, StateDefinitions.INITIAL_STATE.name());
        add(mainMenuState, StateDefinitions.MAIN_MENU.name());
        add(gameState, StateDefinitions.GAME.name());
        add(pauseState, StateDefinitions.PAUSE_MENU.name());
        add(settingsState, StateDefinitions.SETTINGS_MENU.name());
        add(characterState, StateDefinitions.CHARACTER_STATE.name());

        // Sets up individual assets per map (after gameState is created)
        AssetSetter assetSetter = new AssetSetter(gameState);
        mapHandler.setAssetSetter(assetSetter);
        //------------------------------------------------------------------

        // --------- Main Game loop ----------------------------------------
        Timer timer = new Timer(16, e -> {

                // Every timer tick (In GAME-state) Check if: Player move - Collide - Key press - Repaint 
                if (currentState == StateDefinitions.GAME) {
                    List<Rectangle> straightObstacles = Obstacle.getStraightObstacles();
                    List<Line2D> diagonalObstacles = Obstacle.getDiagonalObstacles();
                    List<GameObjects> gameObjects = gameState.getObjects();
                    player.move(keyHandler, straightObstacles, diagonalObstacles, gameObjects);
                    eventHandler.checkEvents();
                    gameState.repaint();
                }
            });
        timer.start();
        //------------------------------------------------------------------
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

    public CharacterState getCharacterState() {
        return characterState;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (eventHandler != null) {
            eventHandler.setPlayer(player);
        }
    }

    public void saveGame() {
        SaveLoadResetGame.saveGame(player, profileName, mapHandler.getCurrentMap());
    }

    public void loadGame() {
        SaveLoadResetGame.loadGame(player, this, profileName);
    }

    public void resetGame() {
        SaveLoadResetGame.resetGame(this, profileName);
    }

    public void loadMap(String mapName) {
        mapHandler.loadMap(mapName);
    }

    public void changeResolution(int width, int height) {
        gameState.updateResolution(width, height);
    }

    public void setScaleFactor(double scaleFactor) {
        gameState.setScaleFactor(scaleFactor);
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}