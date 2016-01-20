package com.lukzdev.grow.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.view.WorldRenderer;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class TreeController extends InputAdapter {

    private GameWorld gameWorld;
    private OrthographicCamera cam;

    // Temp
    private Vector3 tempVec3 = new Vector3();


    public TreeController(GameWorld gameWorld, WorldRenderer renderer) {
        this.gameWorld = gameWorld;
        this.cam = renderer.getCam();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tempVec3.set(screenX, screenY, 0);

        // Unproject to world units
        cam.unproject(tempVec3);

        // Hit proper side of planet with tree
        if(tempVec3.x <= G.TARGET_WIDTH / 2) {
            gameWorld.getTree().smackPlanet(-1.5f);
        } else {
            gameWorld.getTree().smackPlanet(1.5f);
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

        // Hit proper side of planet with tree
        switch (keycode) {
            case Input.Keys.LEFT:
                gameWorld.getTree().smackPlanet(-1.5f);
                return true;
            case Input.Keys.RIGHT:
                gameWorld.getTree().smackPlanet(1.5f);
                return true;
        }

        return false;
    }
}
