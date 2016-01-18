package com.lukzdev.grow.model.entities.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.EntityManager;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;
import com.lukzdev.grow.model.entities.Enemy;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class RoundEnemy extends Enemy {

    // Config
    private static float SPEED = 4f;

    private float actualSpeed = SPEED + MathUtils.random(SPEED / 2f);

    public RoundEnemy(float x, float y, float width, GameWorld gameWorld) {
        super(x, y, width, width, gameWorld);

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(12f)
                        .friction(1f)
                        .restitution(MathUtils.random(0.2f, 0.8f))
                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
                        .build())
//                .fixedRotation()
                .angularDamping(5f)
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

        // Calculate force direction to move entity to tree
        int direction = (int) Math.signum(tree.getTrunk().first().getPosition().x - getPosition().x);

        tempVec2.set(getPosition()).sub(planet.getPosition()).rotate(-90 * direction).nor().scl(actualSpeed);

        body.applyForce(tempVec2, body.getWorldCenter(), true);
    }

}
