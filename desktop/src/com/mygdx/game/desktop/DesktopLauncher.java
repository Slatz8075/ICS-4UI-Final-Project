package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.PuzzleGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
            
              //Texture Packer not important anymore
              //TexturePacker.process("Raw", "Packed", "Player");
             
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PuzzleGame(), config);
	}
}
