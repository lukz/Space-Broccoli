package com.lukzdev.grow.utils;


import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lukzdev.grow.model.Box2DWorld;

/**
 * @author @evilentity
 */
public class DebugConeLight extends ConeLight {
    public DebugConeLight (RayHandler rayHandler, int rays, Color color, float distance, float x, float y, float direction, float cone) {
        super(rayHandler, rays, color, distance, x, y, direction, cone);
    }

    public void drawRays(ShapeRenderer renderer) {
        float sx = getX();
        float sy = getY();
        renderer.setColor(Color.WHITE);
        if (isSoft()) {
            int numVertices = softShadowMesh.getNumVertices();
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(sx * Box2DWorld.BOX_TO_WORLD, sy * Box2DWorld.BOX_TO_WORLD,
                        segments[i + 4] * Box2DWorld.BOX_TO_WORLD, segments[i + 5] * Box2DWorld.BOX_TO_WORLD);
            }
        } else {
            // rays
            int numVertices = lightMesh.getNumVertices();
            for (int i = 4; i < numVertices * 4; i += 4) {
                renderer.line(sx * Box2DWorld.BOX_TO_WORLD, sy * Box2DWorld.BOX_TO_WORLD,
                        segments[i] * Box2DWorld.BOX_TO_WORLD, segments[i + 1] * Box2DWorld.BOX_TO_WORLD);
            }
        }
    }

    public void drawEdge(ShapeRenderer renderer) {
        if (isSoft()) {
            int numVertices = softShadowMesh.getNumVertices();
            renderer.setColor(1, 1, 0, .25f);
            // soft mesh edge
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(segments[i + 4] * Box2DWorld.BOX_TO_WORLD, segments[i + 5] * Box2DWorld.BOX_TO_WORLD,
                        segments[i + 12] * Box2DWorld.BOX_TO_WORLD, segments[i + 13] * Box2DWorld.BOX_TO_WORLD);
            }
            // default mesh edge
            renderer.setColor(1, 0, 0, .25f);
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(segments[i] * Box2DWorld.BOX_TO_WORLD, segments[i + 1] * Box2DWorld.BOX_TO_WORLD,
                        segments[i + 8] * Box2DWorld.BOX_TO_WORLD, segments[i + 9] * Box2DWorld.BOX_TO_WORLD);
            }
        } else {
            int numVertices = lightMesh.getNumVertices();
            renderer.setColor(1, 0, 0, .25f);
            for (int i = 4; i < numVertices * 4 - 4; i += 4) {
                renderer.line(segments[i] * Box2DWorld.BOX_TO_WORLD, segments[i + 1] * Box2DWorld.BOX_TO_WORLD,
                        segments[i + 4] * Box2DWorld.BOX_TO_WORLD, segments[i + 5] * Box2DWorld.BOX_TO_WORLD);
            }
        }
    }
}

