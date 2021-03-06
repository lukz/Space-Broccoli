package com.lukzdev.grow.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lukzdev.grow.G;
import com.lukzdev.grow.model.entities.Planet;
import com.lukzdev.grow.model.entities.Tree;
import com.lukzdev.grow.utils.Constants;

public class GameWorld implements ContactListener {

    private Box2DWorld box2DWorld;
    private EntityManager entityManager;
    private FaceManager faceManager;

    private TreeGenerator treeGenerator;
    private EnemyGenerator enemyGenerator;

    private Planet planet;
    private Tree tree;

    // Keep game state
    public static enum GameState { WAITING_TO_START, IN_GAME, FINISH };
    private GameState gameState = GameState.WAITING_TO_START;

    public GameWorld() {
        box2DWorld = new Box2DWorld(new Vector2(0, Constants.GRAVITY));

        entityManager = new EntityManager();
        enemyGenerator = new EnemyGenerator(this);

        // Pass all collisions through this class
        box2DWorld.getWorld().setContactListener(this);

        initializeObjects();
    }

    public void initializeObjects() {
        // Planet
        planet = new Planet(G.TARGET_WIDTH / 2, - G.TARGET_HEIGHT / 2, this);
        entityManager.addEntity(planet);

        // Tree
        treeGenerator = new TreeGenerator();
        tree = treeGenerator.buildTree(this);

        // Smile!?
        faceManager = new FaceManager(this);
    }

    public void update(float delta) {
        box2DWorld.update(delta);

        // Spawn some enemies
        enemyGenerator.update(delta);

        // Update entities logic
        entityManager.update(delta);

        faceManager.update(delta);

        // Easter egg
        if(tree.getTrunk().first().getPosition().dst(planet.getPosition()) > 700f) {
            faceManager.setFace(FaceManager.FACES.SICK);
        }
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

    public Tree getTree() {
        return tree;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void dispose() {
        entityManager.dispose();
    }
}
