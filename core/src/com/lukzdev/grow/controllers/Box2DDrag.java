package com.lukzdev.grow.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.lukzdev.grow.model.Box2DWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class Box2DDrag extends InputAdapter {

    protected MouseJoint mouseJoint = null;

    protected Camera camera;
    protected World world;
    protected Body groundBody;

    public Box2DDrag(Camera cam, World world, Body groundBody) {
        this.camera = cam;
        this.world = world;
        this.groundBody = groundBody;
    }

    /** we instantiate this vector and the callback here so we don't irritate the GC **/
    protected Body hitBody = null;
    protected Vector3 testPoint = new Vector3();
    protected QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            // if the hit point is inside the fixture of the body we report it
            if (fixture.testPoint(testPoint.x * Box2DWorld.WORLD_TO_BOX, testPoint.y * Box2DWorld.WORLD_TO_BOX)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // translate the mouse coordinates to world coordinates
        camera.unproject(testPoint.set(screenX, screenY, 0));

        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        hitBody = null;
        world.QueryAABB(callback,
                testPoint.x * Box2DWorld.WORLD_TO_BOX - 0.0001f,
                testPoint.y * Box2DWorld.WORLD_TO_BOX - 0.0001f,
                testPoint.x * Box2DWorld.WORLD_TO_BOX + 0.0001f,
                testPoint.y * Box2DWorld.WORLD_TO_BOX + 0.0001f);

        // ignore kinematic bodies, they don't work with the mouse joint
        if (hitBody != null && hitBody.getType() == BodyDef.BodyType.KinematicBody) return false;

        // if we hit something we create a new mouse joint
        // and attach it to the hit body.
        if (hitBody != null) {
            MouseJointDef def = new MouseJointDef();
            def.bodyA = groundBody;
            def.bodyB = hitBody;
            def.collideConnected = true;
            def.target.set(testPoint.x*Box2DWorld.WORLD_TO_BOX, testPoint.y*Box2DWorld.WORLD_TO_BOX);
            def.maxForce = 1000.0f * hitBody.getMass();
            mouseJoint = (MouseJoint)world.createJoint(def);
            hitBody.setAwake(true);
            return true;
        }

        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // if a mouse joint exists we simply destroy it
        if (mouseJoint != null) {
            world.destroyJoint(mouseJoint);
            mouseJoint = null;
            return true;
        }
        return false;
    }

    // Temporary vector
    Vector2 target = new Vector2();

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // if a mouse joint exists we simply update
        // the target of the joint based on the new
        // mouse coordinates
        if (mouseJoint != null) {
            camera.unproject(testPoint.set(screenX, screenY, 0));
            mouseJoint.setTarget(target.set(testPoint.x*Box2DWorld.WORLD_TO_BOX, testPoint.y*Box2DWorld.WORLD_TO_BOX));
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
