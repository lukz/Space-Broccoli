package com.lukzdev.grow.screens;

import com.badlogic.gdx.Screen;
import com.lukzdev.grow.G;

public class LoadingScreen implements Screen {

    public LoadingScreen() {

    }

    @Override
    public void render(float delta) {
        /** If assets loaded go to MainMenu */
        if(G.assets.update()) {
            G.game.setScreen(new GameScreen());
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
