package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.model.EntityManager;
import com.lukzdev.grow.model.FaceManager;
import com.lukzdev.grow.model.PhysicsObject;
import com.lukzdev.grow.view.WorldRenderer;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class Tree {

    private Array<Branch> branches;

    // Trunk that take hits
    private Array<Trunk> trunk;

    private EntityManager entityManager;

    public Tree(EntityManager entityManager) {
        this.entityManager = entityManager;

        branches = new Array<Branch>();
        trunk = new Array<Trunk>();
    }

    public void addBranch(Branch branch) {
        if(branch instanceof Trunk) {
            trunk.add((Trunk) branch);
        }

        branches.add(branch);
    }

    /**
     * Pushes all tree branches with specified force to crash opponents into planet.
     * @param force
     */
    public void smackPlanet(float force) {
        for(int i = 0; i < branches.size; i++) {
            Body body = branches.get(i).getBody();
            body.applyLinearImpulse(force, 0, body.getPosition().x, body.getPosition().y, true);
        }
    }

    // Temp
    private Vector2 tempVec2 = new Vector2();

    /**
     * Removes last branch from tree and give some push to the trunk
     */
    public void getHit(Enemy enemy) {
        if(branches.size == 0) return;

        // Remove last branch
        Entity branch = branches.pop();

        entityManager.removeEntity(branch);
        ((PhysicsObject)branch).setFlagForDelete(true);

        // Push trunk
        tempVec2.set(trunk.first().getPosition()).sub(enemy.getPosition()).nor().scl(40);

        trunk.first().getBody().applyLinearImpulse(tempVec2, trunk.first().getBody().getWorldCenter(), true);

        // Sad face!
        FaceManager.setFace(FaceManager.FACES.SAD);

        // Bad shake
        WorldRenderer.SHAKE_TIME = 0.2f;
    }

    public Array<Trunk> getTrunk() {
        return trunk;
}
}
