package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.EntityManager;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public abstract class Enemy extends Entity implements PhysicsObject {

    // Physics things
    protected Body body;
    protected boolean flagForDelete = false;

    protected EntityManager entityManager;
    protected Planet planet;
    protected Tree tree;

    public Enemy(float x, float y, float width, float height, GameWorld gameWorld) {
        super(x, y, width, height);

        this.planet = gameWorld.getPlanet();
        this.tree = gameWorld.getTree();
        this.entityManager = gameWorld.getEntityManager();
    }

    @Override
    public void update(float delta) {
        // Pull position from physics world
        position.set(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD));
    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {
        if(psycho2 instanceof Branch) {

            // Time to say goodbye
            entityManager.removeEntity(this);
            this.setFlagForDelete(true);
        }
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean getFlagForDelete() {
        return flagForDelete;
    }

    @Override
    public void setFlagForDelete(boolean flag) {
        flagForDelete = flag;
    }

    @Override
    public void dispose() {

    }
}
