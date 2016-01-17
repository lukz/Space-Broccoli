package com.lukzdev.grow.view;

import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.utils.DebugPointLight;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2015-11-16.
 */
public class WorldRenderer {

    private GameWorld gameWorld;

    private OrthographicCamera cam;
    private Viewport viewport;
    private SpriteBatch batch;

    private RayHandler rayHandler;
    private ShapeRenderer shapeRenderer;
    private DebugPointLight debugLight;

    public WorldRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, G.TARGET_WIDTH,
                G.TARGET_HEIGHT);

        viewport = new ExtendViewport(G.TARGET_WIDTH, G.TARGET_HEIGHT, cam);

        batch = new SpriteBatch();

        // Lights
//        RayHandler.setGammaCorrection(true);
//        RayHandler.useDiffuseLight(true);

        rayHandler = new RayHandler(gameWorld.getBox2DWorld().getWorld());
//        new PointLight(rayHandler, 256, new Color(1,1,1,1), G.TARGET_WIDTH * Box2DWorld.WORLD_TO_BOX, G.TARGET_WIDTH / 2 * Box2DWorld.WORLD_TO_BOX, G.TARGET_HEIGHT * Box2DWorld.WORLD_TO_BOX);
//        new DirectionalLight(rayHandler, 128, new Color(1,1,1,1), -60);

        debugLight = new DebugPointLight(rayHandler, 256, new Color(1,1,1,1), G.TARGET_WIDTH * Box2DWorld.WORLD_TO_BOX, G.TARGET_WIDTH / 2 * Box2DWorld.WORLD_TO_BOX, G.TARGET_HEIGHT * Box2DWorld.WORLD_TO_BOX);
        debugLight.setSoft(false);

        shapeRenderer = new ShapeRenderer();
    }

    public void render(float delta) {
        batch.setProjectionMatrix(cam.combined);

        // TODO: memory allocation!
        rayHandler.setCombinedMatrix(cam.combined.cpy().scl(Box2DWorld.BOX_TO_WORLD));

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();

        // Render world
        gameWorld.draw(batch);

        batch.end();

        // Render lights
//        rayHandler.updateAndRender();
        rayHandler.update();

        // Debug render
        if (G.DEBUG) {
            gameWorld.getBox2DWorld().debugRender(cam);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugLight.drawRays(shapeRenderer);
            shapeRenderer.end();
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
        rayHandler.dispose();
    }
}
