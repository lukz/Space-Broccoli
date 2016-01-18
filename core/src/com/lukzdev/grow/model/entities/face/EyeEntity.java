package com.lukzdev.grow.model.entities.face;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;
import com.lukzdev.grow.model.entities.Entity;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-18.
 */
public class EyeEntity extends Entity implements PhysicsObject {

    public static final float RADIUS = 20;

    private Body body;
    private boolean flagForDelete;

    public EyeEntity(float x, float y, Box2DWorld box2DWorld) {
        super(x, y, RADIUS * 2, RADIUS * 2);

        this.body = box2DWorld.getBodyBuilder()
                .fixture(box2DWorld.getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(0.5f)
                        .restitution(1f)
                        .categoryBits(Box2DWorld.CATEGORY.FACE)
                        .maskBits(Box2DWorld.FACE_MASK)
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

    @Override
    public void dispose() {

    }
}
