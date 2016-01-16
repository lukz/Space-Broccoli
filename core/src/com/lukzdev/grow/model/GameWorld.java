package com.lukzdev.grow.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.G;
import com.lukzdev.grow.controllers.Box2DDrag;
import com.lukzdev.grow.model.entities.Branch;
import com.lukzdev.grow.model.entities.Planet;
import com.lukzdev.grow.model.entities.Trunk;
import com.lukzdev.grow.utils.Constants;

public class GameWorld implements ContactListener {

    private Box2DWorld box2DWorld;
    private EntityManager entityManager;

    private Planet planet;

    // Keep game state
    public static enum GameState { WAITING_TO_START, IN_GAME, FINISH };
    private GameState gameState = GameState.WAITING_TO_START;

    public GameWorld() {
        box2DWorld = new Box2DWorld(new Vector2(0, Constants.GRAVITY));

        entityManager = new EntityManager();

        // Pass all collisions through this class
        box2DWorld.getWorld().setContactListener(this);

        initializeObjects();
    }

    public void initializeObjects() {
        // Planet
        planet = new Planet(G.TARGET_WIDTH / 2, - G.TARGET_HEIGHT / 2, box2DWorld);
        entityManager.addEntity(planet);

        // Static trunk holds whole tree
        Trunk trunk = new Trunk(planet.getPosition().x, planet.getPosition().y + planet.getBounds().height / 2, 50, 150, box2DWorld);
        entityManager.addEntity(trunk);

        Branch branch = new Branch(trunk.getPosition().x, trunk.getPosition().y + 100, trunk.getBounds().width, trunk.getBounds().height * 2, box2DWorld);
        entityManager.addEntity(branch);

        // Put branch inside trunk
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(trunk.getBody(), branch.getBody(),
                new Vector2(trunk.getPosition().x * Box2DWorld.WORLD_TO_BOX,
                        (trunk.getPosition().y + (trunk.getBounds().height / 2) * 0.8f) * Box2DWorld.WORLD_TO_BOX));

        weldJoint.localAnchorA.set(0, (trunk.getBounds().height / 2 * 0.8f) * Box2DWorld.WORLD_TO_BOX);
        weldJoint.localAnchorB.set(0,
                -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        weldJoint.frequencyHz = 5;

        box2DWorld.getWorld().createJoint(weldJoint);

        branch.getBody().applyAngularImpulse(100, true);
    }

    public void update(float delta) {
        box2DWorld.update(delta);
        entityManager.update(delta);
    }

    public void draw(SpriteBatch batch) {
        entityManager.draw(batch);
    }

    public void resetGame() {
        entityManager.reset();
    }

    @Override
    public void beginContact(Contact contact) {
        Object ent1 = contact.getFixtureA().getBody().getUserData();
        Object ent2 = contact.getFixtureB().getBody().getUserData();

        if(!(ent1 instanceof PhysicsObject) || !(ent2 instanceof PhysicsObject)) {
            return;
        }

        PhysicsObject physo1 = (PhysicsObject)ent1;
        PhysicsObject physo2 = (PhysicsObject)ent2;

        physo1.handleBeginContact(physo2, this);
        physo2.handleBeginContact(physo1, this);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Box2DWorld getBox2DWorld() {
        return box2DWorld;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void dispose() {
        entityManager.dispose();
    }
}
