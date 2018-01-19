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
    //Instance variables for which tile the player is on
    private int worldRow;
    private int worldColumn;
    private MapScreen world;


    // constructor - we need to know where the player starts
    public Player(float x, float y, int row, int col, int DirX, int DirY) {
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
        // my collision rectangle is at the x,y value passed in
        // it has the width and height of the standing picture
        this.bounds = new Rectangle(x, y, standR.getRegionWidth(), standR.getRegionHeight());
        //Enter in the starting tile
        this.worldRow = row;
        this.worldColumn = col;
        //???, What is this used for, or is it unfinished
        this.distanceTraveledX = 0;
        this.distanceTraveledY = 0;

        this.world = new MapScreen(this.worldRow, this.worldColumn);
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
            this.dx = 20;
            this.elapsed = this.elapsed + deltaTime;
            this.directionX = 2;
            if (!Gdx.input.isKeyPressed(Input.Keys.UP) 
                    || !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                this.directionY = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.dx = -20;
            this.elapsed = this.elapsed + deltaTime;
            this.directionX = 1;
            if (!Gdx.input.isKeyPressed(Input.Keys.UP) 
                    || !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                this.directionY = 0;
            }
        } else {
            this.dx = 0;
            this.elapsed = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.dy = 20;
            this.elapsed = this.elapsed + deltaTime;
            this.directionY = 2;
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) 
                    || !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.directionX = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.dy = -20;
            this.elapsed = this.elapsed + deltaTime;
            this.directionY = 1;
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) 
                    || !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.directionX = 0;
            }
        } else {
            this.dy = 0;
            this.elapsed = 0;
        }/**
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

        // tel, the screen to mve to the next one and update the players position
        if(this.x == (this.world.getWidth())*1000){
            this.worldRow++;
            // bring the player to the other edge of the screen
            this.x = 100;
        }
        if(this.x == 0){
            this.worldRow--;
            // bring the player to the other edge of the screen
            this.x = (this.world.getWidth())*1000-100;
        }
        if(this.y == (this.world.getHeight())*1000){
            this.worldColumn++;
            // bring the player to the other edge of the screen
            this.x = 100;
        }
        if(this.y == 0){
            this.worldColumn--;
            // bring the player to the other edge of the screen
            this.x = (this.world.getHeight())*1000-100;
        }

        this.x = this.x + this.dx;
        this.y = this.y + this.dy;
    }

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

    public void render(SpriteBatch batch){
        //Check if the player is standing
        if (this.dx == 0){
            //Determine which direction the player is standing
            //If the player is facing left
            if(directionX == 1){
                batch.draw(standL, x, y);
            //If the player is facing Right
            }else if(directionX == 2){
                batch.draw(standR, x, y);
            //If the player is facing up
            }else if(directionY == 2){
                batch.draw(standU, x, y);
            //If the player is facing down
            }else{
                batch.draw(standD, x, y);
            }
        //If the player is moving, one of the four animations play, even if
        //they're moving diagonally
        //If the player is moving to the right
        }else if(this.dx > 0){
            batch.draw(runR.getKeyFrame(elapsed, true), x, y);
        //If the player is moving to the left
        }else if(this.dx < 0){
            batch.draw(runL.getKeyFrame(elapsed, true), x, y);
        //If the player is moving upwards
        }else if(this.dy < 0){
            batch.draw(runU.getKeyFrame(elapsed, true), x, y);
        //If the player is moving downwards
        }else if(this.dy > 0){
            batch.draw(runD.getKeyFrame(elapsed, true), x, y);
        }
    }
    
    // get rid of heavy objects
    public void dispose(){
        atlas.dispose();
    }
    public void setWorldRow(int row) {
        this.worldRow = row;
    }

    public void setWorldCol(int col) {
        this.worldColumn = col;
    }

    public void setScreen(MapScreen places) {
        this.world = places;
    }

    public float getPlayerX() {
        return this.x;
    }

    public float getPlayerY() {
        return this.y;
    }

    public int getWorldCol() {
        return this.worldColumn;
    }

    public int getWorldRow() {
        return this.worldRow;
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

