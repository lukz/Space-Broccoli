package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.model.EntityManager;
import com.lukzdev.grow.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class Tree {

    private Array<Branch> branches;

    // Simply first branch
    private Branch trunk;

    private EntityManager entityManager;

    public Tree(EntityManager entityManager) {
        this.entityManager = entityManager;

        branches = new Array<Branch>();
    }

    public void addBranch(Branch branch) {
        if(branches.size == 0) {
            trunk = branch;
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

    /**
     * Removes last branch from tree
     */
    public void getHit() {
        if(branches.size == 0) return;

        Entity branch = branches.pop();

        entityManager.removeEntity(branch);
        ((PhysicsObject)branch).setFlagForDelete(true);
    }

    public Branch getTrunk() {
        return trunk;
}
}
