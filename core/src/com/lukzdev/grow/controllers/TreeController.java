package com.lukzdev.grow.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.Box2DWorld;
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

        // Unproject to wold units
        cam.unproject(tempVec3);
        System.out.println(tempVec3.x);
        System.out.println(G.TARGET_WIDTH / 2);
        // Hit proper side
        if(tempVec3.x <= G.TARGET_WIDTH / 2) {
            gameWorld.getTree().smackPlanet(-1.5f);
        } else {
            gameWorld.getTree().smackPlanet(1.5f);
        }

        return true;
    }

}
