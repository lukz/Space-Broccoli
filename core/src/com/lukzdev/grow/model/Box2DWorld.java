package com.lukzdev.grow.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.utils.BodyBuilder;
import com.lukzdev.grow.utils.FixtureDefBuilder;
import com.lukzdev.grow.utils.JointBuilder;

import java.util.Iterator;


public class Box2DWorld {

    /*
     * Statics for calculation pixel to box2d metrics and vice versa
     */
    public static final float WORLD_TO_BOX = 0.01f; // 100px = 1m
    public static final float BOX_TO_WORLD = 100f;


    /*
     * Masks and categories used to filter collisions
     */
    public final static short BRANCH_MASK = CATEGORY.PLANET | CATEGORY.ENEMY;
    public final static short FACE_MASK = CATEGORY.FACE;

    public final static class CATEGORY {
        public final static short PLANET = 0x0001;
        public final static short BRANCH = 0x0002;
        public final static short ENEMY = 0x0004;
        public final static short FACE = 0x0008;
    };

    private World world;

    private FixtureDefBuilder fixtureDefBuilder;
    private BodyBuilder bodyBuilder;
    private JointBuilder jointBuilder;

    private Box2DDebugRenderer debugRenderer;

    private Array<Body> bodies = new Array<Body>();

    public Box2DWorld(Vector2 gravity) {
        world = new World(gravity, true);
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);

        bodyBuilder = new BodyBuilder(world);
        fixtureDefBuilder = new FixtureDefBuilder();
    }

    public void update(float dt) {
        world.step(dt, 15, 5);
        sweepDeadBodies();
    }

    /*
	 * Bodies should be removed after world step to prevent simulation crash
	 */
	public void sweepDeadBodies() {
		world.getBodies(bodies);
		for (Iterator<Body> iter = bodies.iterator(); iter.hasNext();) {
			Body body = iter.next();
			if (body != null && (body.getUserData() instanceof PhysicsObject)) {
                PhysicsObject data = (PhysicsObject) body.getUserData();
				if (data.getFlagForDelete()) {
					getWorld().destroyBody(body);
				}
			}
		}
	}

    /*
	 * Box2D debug renderer
     */
    public void debugRender(Camera cam) {
        debugRenderer.render(world, cam.combined.cpy().scl(BOX_TO_WORLD));
    }

    public World getWorld() {
        return world;
    }

    public BodyBuilder getBodyBuilder() {
        return bodyBuilder;
    }

    public FixtureDefBuilder getFixtureDefBuilder() {
        return fixtureDefBuilder;
    }

    public JointBuilder getJointBuilder() {
        return jointBuilder;
    }
}
