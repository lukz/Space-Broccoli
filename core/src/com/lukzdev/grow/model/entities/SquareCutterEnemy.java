package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class SquareCutterEnemy extends Enemy {

    // Config
    private static float SPEED = 3f;

    public SquareCutterEnemy(float x, float y, float width, float height, GameWorld gameWorld) {
        super(x, y, width, height, gameWorld);

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .boxShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX,
                                getBounds().getHeight() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(15f)
                        .friction(1f)
                        .restitution(0f)
                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
                        .build())
//                .fixedRotation()
                .angularDamping(0f)
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .type(BodyDef.BodyType.DynamicBody)
                .userData(this)
                .build();
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    // Temp
    protected Vector2 tempVec2 = new Vector2();

    @Override
    public void update(float delta) {
        super.update(delta);

        // Calculate angular impulse direction to move entity to tree
        int direction = -(int) Math.signum(tree.getTrunk().getPosition().x - getPosition().x);

        body.setAngularVelocity(direction * SPEED);
    }

}
