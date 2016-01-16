package com.lukzdev.grow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.lukzdev.grow.screens.LoadingScreen;

public class Grow extends Game {
	private FPSLogger log;
	
	@Override
	public void create () {
		G.game = this;
		G.assets = new Assets();

		log = new FPSLogger();

		G.game.setScreen(new LoadingScreen());
	}

	@Override
	public void render() {
		super.render();
		log.log();
	}

}
