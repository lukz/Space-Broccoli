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
public class Enemy extends Entity implements PhysicsObject {

    // Config
    private static float SPEED = 4f;

    // Physics things
    private Body body;
    private boolean flagForDelete = false;

    private EntityManager entityManager;
    private Planet planet;
    private Tree tree;

    public Enemy(float x, float y, float width, float height, GameWorld gameWorld) {
        super(x, y, width, height);

        this.planet = gameWorld.getPlanet();
        this.tree = gameWorld.getTree();
        this.entityManager = gameWorld.getEntityManager();

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(12f)
                        .friction(1f)
                        .restitution(0.1f)
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
    private Vector2 tempVec2 = new Vector2();

    @Override
    public void update(float delta) {
        // Pull position from physics world
        position.set(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD));

        // Calculate force direction to move entity to tree
        int direction = (int) Math.signum(tree.getTrunk().getPosition().x - getPosition().x);

        tempVec2.set(getPosition()).sub(planet.getPosition()).rotate(-90 * direction).nor().scl(SPEED + MathUtils.random(SPEED / 2f));

        body.applyForce(tempVec2, body.getWorldCenter(), true);
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
