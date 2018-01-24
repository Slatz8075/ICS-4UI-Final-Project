/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author slatz8075
 */
public class Map {
    //create a 2D array of screens 
    private MapScreen[][] screens;
    //create the shape renderer (this will be used to render our map)
    private ShapeRenderer shapeRenderer;

    //initilizer for the map
    public Map(int width, int height) {
        //create the map[][] to the desired dimensions
        screens = new MapScreen[height][width];
        //initilize the shape renderer for the render method
        shapeRenderer = new ShapeRenderer();
    }

    //setter for the screen
    public void setScreen(int row, int col, MapScreen Screen) {
        //set the screen at the desired part in the map array
        screens[row][col] = Screen;
    }

    //getter for the screen - returning the ascreen at this desired position
    public MapScreen getScreen(int row, int col) {
        //return the screen
        return screens[row][col];
    }

    //for the render method pass in the camera, and the screen we are currently looking at
    public void render(OrthographicCamera camera, int currentScreenY, int currentScreenX) {
        //use the shape renderer at our position
        shapeRenderer.setProjectionMatrix(camera.combined);      
        //begin our shape rendering, saying that our shape type is filled
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //get our current screen
        MapScreen CurrentScreen = screens[currentScreenX][currentScreenY];
        //move through this array generating solid blocks at 1's and now blocks at 0's
        //simonanuiously generate a leaf texture at the 1's and sand at the zeroes
        //use for loops to navigate through this screen's tiles and display the above accordingly
        //this for loop moves through the columns
        for (int ScreenRow = 0; ScreenRow < CurrentScreen.getHeight(); ScreenRow++) {
            //this for loop moves through the rows
            for (int ScreenColumn = 0; ScreenColumn < CurrentScreen.getWidth(); ScreenColumn++) {
                //check to see if this is a block
                if (CurrentScreen.getTile(ScreenColumn, ScreenRow) == 1) {
                    //now generate a block 
                    //Ex: ScreenCol * 1000 scales up the array by 1000
                    //since 1000 to 1 is the ratio of pixcel to block, blocks should be 1000, 1000
                    shapeRenderer.rect(ScreenColumn * 1000, ScreenRow * 1000, 1000, 1000);
                    //now generate the block texture
                    
                }else if(CurrentScreen.getTile(ScreenColumn, ScreenRow) == 0){
                    //now we know it is not a block
                    //all we need to do is generate the texture
                }
            }
        }
        shapeRenderer.end();
    }
}
