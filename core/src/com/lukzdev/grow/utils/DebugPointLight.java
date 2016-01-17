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
public class DebugPointLight extends PointLight {
    public DebugPointLight (RayHandler rayHandler, int rays, Color color, float distance, float x, float y) {
        super(rayHandler, rays, color, distance, x, y);
    }

    public void drawEdge(ShapeRenderer renderer) {
        if (isSoft()) {
            int numVertices = softShadowMesh.getNumVertices();
            // default mesh edge
            renderer.setColor(Color.CYAN);
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(segments[i], segments[i + 1], segments[i + 8], segments[i + 9]);
            }
            renderer.setColor(Color.RED);
            // soft mesh edge
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(segments[i + 4], segments[i + 5], segments[i + 12], segments[i + 13]);
            }
        } else {
            int numVertices = lightMesh.getNumVertices();
            renderer.setColor(Color.CYAN);
            for (int i = 4; i < numVertices * 4 - 4; i += 4) {
                renderer.line(segments[i], segments[i + 1], segments[i + 4], segments[i + 5]);
            }
        }
    }
    public void drawRays(ShapeRenderer renderer) {
        float sx = getX();
        float sy = getY();
        if (isSoft()) {
            int numVertices = softShadowMesh.getNumVertices();
            renderer.setColor(Color.WHITE);
            for (int i = 0; i < numVertices * 4 - 8; i += 8) {
                renderer.line(sx * Box2DWorld.BOX_TO_WORLD, sy * Box2DWorld.BOX_TO_WORLD, segments[i + 4] * Box2DWorld.BOX_TO_WORLD, segments[i + 5] * Box2DWorld.BOX_TO_WORLD);
            }
        } else {
            // rays
            renderer.setColor(Color.WHITE);
            int numVertices = lightMesh.getNumVertices();
            for (int i = 4; i < numVertices * 4; i += 4) {
                renderer.line(sx * Box2DWorld.BOX_TO_WORLD, sy * Box2DWorld.BOX_TO_WORLD, segments[i] * Box2DWorld.BOX_TO_WORLD, segments[i + 1] * Box2DWorld.BOX_TO_WORLD);
            }
        }
    }
    public void setDebugColors (Color ray, Color hardEdge, Color softEdge) {}
    public void debugDraw (ShapeRenderer renderer) {}

    public boolean isSleeping () {
        return false;
    }
}

