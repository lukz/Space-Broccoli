package com.lukzdev.grow.model.entities.face;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;
import com.lukzdev.grow.model.entities.Entity;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-18.
 */
public class CrossedEyeEntity extends Entity implements PhysicsObject {

    public static final float WIDTH = 10;
    public static final float HEIGHT = 80;

    private Body body;
    private boolean flagForDelete;

    public CrossedEyeEntity(float x, float y, Box2DWorld box2DWorld) {
        super(x, y, WIDTH, HEIGHT);

        this.body = box2DWorld.getBodyBuilder()
                .fixture(box2DWorld.getFixtureDefBuilder()
                        .boxShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX,
                                getBounds().getHeight() / 2 * Box2DWorld.WORLD_TO_BOX,
                                new Vector2(0, 0), 0)
                        .density(1f)
                        .friction(0.5f)
                        .restitution(1f)
                        .categoryBits(Box2DWorld.CATEGORY.FACE)
                        .maskBits(Box2DWorld.FACE_MASK)
                        .build())
                .fixture(box2DWorld.getFixtureDefBuilder()
                        .boxShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX,
                                getBounds().getHeight() / 2 * Box2DWorld.WORLD_TO_BOX,
                                new Vector2(0, 0), MathUtils.degRad * 90)
                        .density(1f)
                        .friction(0.5f)
                        .restitution(1f)
                        .categoryBits(Box2DWorld.CATEGORY.FACE)
                        .maskBits(Box2DWorld.FACE_MASK)
                        .build())
                .angularDamping(2)
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
