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

    public WorldRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, G.TARGET_WIDTH,
                G.TARGET_HEIGHT);

        viewport = new ExtendViewport(G.TARGET_WIDTH, G.TARGET_HEIGHT, cam);

        batch = new SpriteBatch();
    }

    public void render(float delta) {
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
