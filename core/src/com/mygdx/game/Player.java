/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author slatz8075
 */
public class Player {
    // player location variables
    private float x;
    private float y;
    // player movement variables
    private float dx;
    private float dy;

    // the amount of time an animation has been running
    private float elapsed;

    // animation variables for moving
    private Animation<TextureRegion> runR;
    private Animation<TextureRegion> runL;
    private Animation<TextureRegion> runU;
    private Animation<TextureRegion> runD;
    // pictures when standing still
    private TextureRegion standR;
    private TextureRegion standL;
    private TextureRegion standU;
    private TextureRegion standD;

    // texture atlas that will help load in the images from the big image
    // this was created from running the texture packer (in Desktop Launcher)
    private TextureAtlas atlas;

    // the collision rectangle to help us fix collisions
    private Rectangle bounds;
    
    private Texture player;
    //Right = 2, Left = 1, Up = 2, Down = 1, no direction = 0
    private int directionX;
    private int directionY;
    //???
    private int distanceTraveledX;
    private int distanceTraveledY;
    //these integers store the row and column (screen wise)that the player is ocupying
    private int currentScreenRow;
    private int currentScreenColumn;
    //create the map so that we can access the screens and this the tiles
    private Map map;


    //the player starts with its x and y, its screen position, its direction, and the map 
    public Player(float x, float y, int screenColumn, int screenRow, int DirX, int DirY, Map map) {
        // sets the income position
        this.x = x;
        this.y = y;
        // player starts standing still
        this.dx = 0;
        this.dy = 0;
        // no animation going on, so no time yet
        this.elapsed = 0;
        // load in the texture atlast to start finding pictures
        this.atlas = new TextureAtlas("packed/player.atlas");
        // finding the standing picture and load it in
        this.standR = atlas.findRegion("StandR");
        this.standD = atlas.findRegion("StandD");
        this.standU = atlas.findRegion("StandU");
        this.standL = atlas.findRegion("StandL");
        // create a run animation by finding every picture named run
        // the atlas has an index from each picture to order them correctly
        // this was done by naming the pictures in a certain way (run_1, run_2, etc.)
        runR = new Animation(1f / 10f, atlas.findRegions("RunR"));
        runL = new Animation(1f / 10f, atlas.findRegions("RunL"));
        runU = new Animation(1f / 10f, atlas.findRegions("RunU"));
        runD = new Animation(1f / 10f, atlas.findRegions("RunD"));
        //Theses variables are created just in case something calls for the
        //players direction before the player moves, might be unnecessary
        this.directionX = DirX;
        this.directionY = DirY;
      
        //???, What is this used for, or is it unfinished
        this.distanceTraveledX = 0;
        this.distanceTraveledY = 0;
        // my collision rectangle is at the x,y value passed in
        // it has the width and height of the standing picture
        this.bounds = new Rectangle(x, y, standR.getRegionWidth(), standR.getRegionHeight());
        
        //store the screen row and column
        this.currentScreenRow = screenRow - 1;
        this.currentScreenColumn = screenColumn - 1;
        System.out.println("Player starting row: " + currentScreenRow);
        System.out.println("Player starting column: " + currentScreenColumn);
        //store the map 
        this.map = map;
    }
    
    public float getX(){
        return x;
    }
    
    public float getY(){
        return y;
    }
    
    
    public void update(float deltaTime) {
        // movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.dx = 50;
            this.elapsed = this.elapsed + deltaTime;
            this.directionX = 2;
            if (!Gdx.input.isKeyPressed(Input.Keys.UP) 
                    || !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                this.directionY = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.dx = -50;
            this.elapsed = this.elapsed + deltaTime;
            this.directionX = 1;
            if (!Gdx.input.isKeyPressed(Input.Keys.UP) 
                    || !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                this.directionY = 0;
            }
        } else if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) 
                && !Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            this.dx = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.dy = 50;
            this.elapsed = this.elapsed + deltaTime;
            this.directionY = 2;
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) 
                    || !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.directionX = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.dy = -50;
            this.elapsed = this.elapsed + deltaTime;
            this.directionY = 1;
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) 
                    || !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.directionX = 0;
            }
        } else if(!Gdx.input.isKeyPressed(Input.Keys.UP)
                && !Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.dy = 0;
        }else{
            this.elapsed = 0;
        }

        /**
         *Replace getTileType with something else
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            String test = this.world.getTileType();
            if (test.equals("Puzzle")) {
                puzzleInteract(this.world.getTile);
            } else if (test.equals("Door")) {
                doorInteract(this.world.getTile);
            }
        }
        */
      
        //CHANGING OF SCREENS LOGIC (this part done by zac)
        // check if the player's x is at the edge of the screen
        if(this.x > (this.map.getScreen(currentScreenRow, currentScreenColumn).getWidth()*1000)){
            //therefore the player's current screen column needs to be modified
            this.currentScreenColumn--;
            //set the players position to be at the left, since it exited stage right
            this.x = 1;
            //now check to see if the palyer is exiting left
        }else if(this.x < 0){
            //add one to the screen column
            this.currentScreenColumn++;
            // bring the player to the other edge of the screen (minus 1 so that it is not teleported back instantly)
            this.x = (this.map.getScreen(currentScreenRow, currentScreenColumn).getWidth()*1000-1);
            //check if the player is at the bottom of the screen
        } else if(this.y < 0){
            //move down a screen
            this.currentScreenRow--;
            // bring the player to the other edge of the screen
            this.y = (map.getScreen(currentScreenRow, currentScreenColumn).getHeight()*1000-1);
            //now check if the player is at the top of the screen
        } else if(this.y > (this.map.getScreen(currentScreenRow, currentScreenColumn).getHeight()*1000)){
            //modifiy the screen row
            this.currentScreenRow++;
            // bring the player to the other edge of the screen
            this.y = 0;
        }
        
        System.out.println("Player X: " + this.x);
        System.out.println("Player Y: " + this.y);

        this.x = this.x + this.dx;
        this.y = this.y + this.dy;
    }

    /*
    public void fixCollision(Rectangle block) {
        // are they colliding?
        if (bounds.overlaps(block)) {
            // calculate how much the are overlaping
            float width = Math.min(bounds.x + bounds.width, block.x + block.width) - Math.max(bounds.x, block.x);
            float height = Math.min(bounds.y + bounds.height, block.y + block.height) - Math.max(bounds.y, block.y);
            // seperate the axis by finding the least amount of collision
            if (width < height) {
                // on the left
                if (this.x < block.x) {
                    // move the player to the left
                    this.x = this.x - width;
                // on the right
                } else {
                    // move the player to the right
                    this.x = this.x + width;
                }
            } else {
                // under it
                if (this.y < block.y) {
                    // move the player down
                    this.y = this.y - height;
                // above it
                } else {
                    // move the player up
                    this.y = this.y + height;
                }
            }
            // update the collision box to match the player
            bounds.setX(this.x);
            bounds.setY(this.y);
        }
    }
    */

    public void render(SpriteBatch batch){
        //Check if the player is standing
        if (this.dx == 0 && this.dy == 0){
            //Determine which direction the player is standing
            //If the player is facing left
            if(directionX == 1){
                batch.draw(standL, x, y, 1000, 1000);
            //If the player is facing Right
            }else if(directionX == 2){
                batch.draw(standR, x, y, 1000, 1000);
            //If the player is facing up
            }else if(directionY == 2){
                batch.draw(standU, x, y, 1000, 1000);
            //If the player is facing down
            }else{
                batch.draw(standD, x, y, 1000, 1000);
            }
        //If the player is moving, one of the four animations play, even if
        //they're moving diagonally
        //If the player is moving to the right
        }else if(this.dx > 0){
            batch.draw(runR.getKeyFrame(elapsed, true), x, y, 1000, 1000);
        //If the player is moving to the left
        }else if(this.dx < 0){
            batch.draw(runL.getKeyFrame(elapsed, true), x, y, 1000, 1000);
        //If the player is moving upwards
        }else if(this.dy > 0){
            batch.draw(runU.getKeyFrame(elapsed, true), x, y, 1000, 1000);
        //If the player is moving downwards
        }else if(this.dy < 0){
            batch.draw(runD.getKeyFrame(elapsed, true), x, y, 1000, 1000);
        }
    }
    
    // get rid of heavy objects
    public void dispose(){
        atlas.dispose();
    }
    
    /*
    public void setWorldRow(int row) {
        this.worldRow = row;
    }

    public void setWorldCol(int col) {
        this.worldColumn = col;
    }

    public void setScreen(MapScreen places) {
        this.world = places;
    }
    */

    /*
    public float getPlayerX() {
        return this.x;
    }

    public float getPlayerY() {
        return this.y;
    }
    */


    //these getters and setters are needed for the map redering so we know which screen to display
    public int getScreenCol() {
        System.out.println("Screen Column: " + currentScreenColumn);
        return this.currentScreenColumn;
    }

    public int getScreenRow() {
        System.out.println("Screen Row: " + currentScreenRow);
        return this.currentScreenRow;
    }

    public String getDiretion() {
        if (this.directionX == 2 && this.directionY == 0) {
            return "right";
        } else if (this.directionX == 2 && this.directionY == 1) {
            return "down-right";
        } else if (this.directionX == 2 && this.directionY == 2) {
            return "up-right";
        } else if (this.directionX == 1 && this.directionY == 0) {
            return "left";
        } else if (this.directionX == 1 && this.directionY == 1) {
            return "down-left";
        } else if (this.directionX == 1 && this.directionY == 2) {
            return "up-left";
        } else if (this.directionX == 0 && this.directionY == 2) {
            return "up";
        } else if (this.directionX == 0 && this.directionY == 1) {
            return "down";
        } else {
            return "error no direction found";
        }
    }

    /**
     * This isn't coded properly, I believe it's coded to believe a tile is the
     * square the player is standing on rather than the room he's in
    public void puzzleInteract(String puzzle) {

        //interact
        world.changePuzzleTile(this.x, this.y);
    }

    public void doorInteract(Tile Door) {
        //interact
        world.changeMap(this.worldRow, this.worldColumn, this.x, this.y);
    }
    */
}

