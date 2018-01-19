/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author slatz8075
 */
public class MainGame implements Screen{

    //create a puzzle game to display/ switch screens
    private PuzzleGame gameManager;
    // our game needs a hero
    private Player player;
    //create a new map to store the screens
    private Map map;
    //create a variables for the dimentions of the map (dimentions in terms of # of screens)
    static int mapWidth;
    static int mapHeight;
    //create variables for the dimentions of the screen
    static int ScreenTileWidth;
    static int ScreenTileHeight;
    
    // sprite batch
    private SpriteBatch batch;
    // camera and viewport
    private OrthographicCamera camera;
    private Viewport view;
    
    //variables for storing the screen that the player is at
    int currentScreenX;
    int currentScreenY;
    //variables for storing the startying position that the player is at
    int startX;
    int startY;

    /**
     * @param args the command line arguments
     */
    //this is the constructor to create the map and the sceens inside of it when started 
    public MainGame(PuzzleGame puzzleGame) {
        //INITILIZATION
        //Save the puzzle game so that we can talk to it
        this.gameManager = puzzleGame;
        //create a blank file
        FileReader file = null;
        
        //try to find the info file
        try {
            // creating the file reader
            file = new FileReader("Initilization.txt");
        } catch (Exception e) {
            //handle any errors
            e.printStackTrace();
            //exit the program
            System.exit(0);
        }
        //use a Scanner with the file
        Scanner scanner = new Scanner(file);
        //take in the variables for width and height of the map
        mapWidth = Integer.parseInt(scanner.nextLine());
        mapHeight = Integer.parseInt(scanner.nextLine());
        //create a map of these dimentions
        map = new Map(mapWidth, mapHeight);
        //take in the variables for width and height of screens
        ScreenTileWidth = Integer.parseInt(scanner.nextLine());
        ScreenTileHeight = Integer.parseInt(scanner.nextLine());
        //take in the starting position (screen wise) of the player
        currentScreenX = Integer.parseInt(scanner.nextLine());
        currentScreenY = Integer.parseInt(scanner.nextLine());
        //take in the position of the player (XY position wise on the screen)
        startX = Integer.parseInt(scanner.nextLine());
        startY = Integer.parseInt(scanner.nextLine());
        //create a player at this current position on the screen
        player = new Player(startX, startY, currentScreenX, currentScreenY, 0, 1);
        //this is counting the columns of screens in the map[][] 
        for (int mapRow = mapHeight - 1; mapRow >= 0; mapRow--) {
            //System.out.println("map row" + mapRow);
            //this is counting the number of column spots for the screens in the map[][]
            for (int mapCol = mapWidth - 1; mapCol >= 0; mapCol--) {
                //System.out.println("mapCol" + mapCol);
                //we are now at a new screen, so initilize it
                MapScreen screen = new MapScreen(ScreenTileWidth, ScreenTileHeight);
                //this is counting the number of rows in the map[][]for this row for this scecific screen
                for (int screenRow = ScreenTileHeight - 1; screenRow >= 0; screenRow--) {
                    //scan in this whole line as a string
                    String Line = scanner.nextLine();
                    //this is counting the number of columns in the map[][]for this row for this specific screen
                    for (int screenCol = ScreenTileWidth - 1; screenCol >= 0; screenCol--) {
                        //set the position in the [][] equal to screenCol's #
                        char ex = Line.charAt(screenCol);
                        screen.setTile(screenRow, screenCol, Integer.parseInt("" + ex));
                    }
                }
                //now put this screen into the map[][]
                map.setScreen(mapRow, mapCol, screen);
                //we have now passed a screen worth of tiles, so move past the blank space ()separating the screens
                scanner.nextLine();
            }
        }
        // initialize the spritebatch
        this.batch = puzzleGame.getBatch();
        //create the camera proportional to the mapScreen
        this.camera = new OrthographicCamera(ScreenTileWidth*1000, ScreenTileHeight*1000);
        //do the same for the viewport using the camera
        this.view = new FitViewport(ScreenTileWidth*1000, ScreenTileHeight*1000, camera);
        //finally apply the viewport
        view.apply();
        // move the camera to the center
        this.camera.position.set(ScreenTileWidth*1000/2, ScreenTileHeight*1000/2, 0);
        // make sure to apply the changes
        this.camera.update();
    }
    
    @Override
    public void show() {
        // this would be what happens when the game wakes up from being hidden
        // i.e. when you minimized the game and reopenned it
    }

    // the main game loop for this screen
    @Override
    public void render(float deltaTime) {
        // update the player
        player.update(deltaTime);
        /**
        //THIS NEEDS MODIFICATION
        // check for collisions and fix them
        for(Rectangle block: world.getBlocks()){
            player.fixCollision(block);
        }
        */
       
        // clears the screen in a black colour
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
        
        
        // render the map at our current screen
        map.render(camera, currentScreenX, currentScreenY);
        
        //WAITING ON RYAN
        /*
        if(player.getX() > 200){
            camera.position.x = player.getX();
        }
        */
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // ask the SpriteBatch to start taking notes of what to draw
        batch.begin();
        
        //WAITING OR RYAN
        /*
	// ask the player to draw themself
        player.render(batch);
        */
        //Ask the player to draw themself
        player.render(batch);
        // tell the SpriteBatch we are good to draw everything now
	batch.end();
    }

    // used when the window is resized.... we haven't use it here
    @Override
    public void resize(int width, int height) {
        this.view.update(width, height);
    }

    // if the game could pause, what do you need to happen?
    @Override
    public void pause() {
        
    }

    // when the game is unpaused, what do you need to happen?
    @Override
    public void resume() {
        
    }

    // when the game needs to be minimized, what needs to happen?
    @Override
    public void hide() {
        
    }

    // get rid of heavy objects...
    @Override
    public void dispose() {
        
    }
}
