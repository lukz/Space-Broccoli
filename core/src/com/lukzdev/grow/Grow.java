package com.lukzdev.grow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.lukzdev.grow.screens.GameScreen;

public class Grow extends Game {
	private FPSLogger log;
	
	@Override
	public void create () {
		G.game = this;

		log = new FPSLogger();

		// No assets to load so go straight to the game
		G.game.setScreen(new GameScreen());
	}

	@Override
	public void render() {
		super.render();
		log.log();
	}

}
