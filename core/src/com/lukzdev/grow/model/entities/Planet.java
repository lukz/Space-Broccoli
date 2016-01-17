package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.EntityManager;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;

/**
 * Applies gravity on each body
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class Planet extends Entity implements PhysicsObject {

    // Config
    private static final float RADIUS = G.TARGET_WIDTH / 2;

    // Physics things
    private Body body;
    private boolean flagForDelete = false;

    private EntityManager entityManager;

    public Planet(float x, float y, GameWorld gameWorld) {
        super(x, y, RADIUS * 2, RADIUS * 2);

        this.entityManager = gameWorld.getEntityManager();

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(1f)
                        .restitution(0.4f)
                        .build())
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .fixedRotation()
                .type(BodyDef.BodyType.StaticBody)
                .userData(this)
                .build();
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    // Temp
    private Vector2 gravityVec2 = new Vector2();

    @Override
    public void update(float delta) {
        // Pull position from physics world
        position.set(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD));

        // Apply planet gravity on bodies
        for(int i = 0; i < entityManager.getEntities().size; i++) {
            Entity entity = entityManager.getEntities().get(i);

            // We are need all physics objects with exception of planets
            if(!(entity instanceof PhysicsObject) || entity instanceof Planet) continue;

            Body entBody = ((PhysicsObject) entity).getBody();

            // Calculate gravity vector for that body
            gravityVec2.set(entBody.getPosition().sub(body.getPosition()));

            // Lets apply gravity! I know it's not right but who cares?! It looks good enough!
            entBody.applyForce(gravityVec2.scl(-entBody.getMass()), entBody.getWorldCenter(), true);
        }

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
