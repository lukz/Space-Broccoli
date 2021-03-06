package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class Branch extends Entity implements PhysicsObject {

    // Physics things
    private Body body;
    private boolean flagForDelete = false;

    public Branch(float x, float y, float width, float height, Box2DWorld box2DWorld) {
        super(x, y, width, height);

        this.body = box2DWorld.getBodyBuilder()
                .fixture(box2DWorld.getFixtureDefBuilder()
                        .boxShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX,
                                getBounds().getHeight() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(1f)
                        .restitution(0.8f)
                        .categoryBits(Box2DWorld.CATEGORY.BRANCH)
                        .maskBits(Box2DWorld.BRANCH_MASK)
                        .build())
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .type(BodyDef.BodyType.DynamicBody)
                .userData(this)
                .build();
    }

    @Override
    public void draw(SpriteBatch batch) {
    }

    @Override
    public void update(float delta) {
        // Pull position from physics world
        position.set(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD));

    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {

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
}
