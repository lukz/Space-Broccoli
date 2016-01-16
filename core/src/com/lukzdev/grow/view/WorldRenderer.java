package com.lukzdev.grow.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.GameWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2015-11-16.
 */
public class WorldRenderer {

    private GameWorld gameWorld;

    private OrthographicCamera cam;
    private Viewport viewport;
    private SpriteBatch batch;

    // Manager used to follow player
//    private CameraManager cameraManager;


    public WorldRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, G.TARGET_WIDTH,
                G.TARGET_HEIGHT);

//        viewport = new ExtendViewport(Rocket.VIRTUAL_WIDTH, 0, Rocket.VIRTUAL_WIDTH, Rocket.VIRTUAL_HEIGHT * 10, cam);
        viewport = new ExtendViewport(G.TARGET_WIDTH, G.TARGET_HEIGHT, cam);

        batch = new SpriteBatch();

//        cameraManager = new CameraManager(cam, gameWorld.getPlayer());
    }

    public void render(float delta) {
        // Update cam
//        cameraManager.update(delta);

        batch.setProjectionMatrix(cam.combined);

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Render world
        gameWorld.draw(batch);

        batch.end();

        // Debug render
        if (G.DEBUG) {
            gameWorld.getBox2DWorld().debugRender(cam);
        }


    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void dispose() {
        batch.dispose();
    }
}
