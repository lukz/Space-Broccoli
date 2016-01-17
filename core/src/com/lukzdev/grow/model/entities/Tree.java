package com.lukzdev.grow.model.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-17.
 */
public class Tree {

    private Array<Branch> branches;

    // Simply first branch
    private Branch trunk;

    public Tree() {
        branches = new Array<Branch>();
    }

    public void addBranch(Branch branch) {
        if(branches.size == 0) {
            trunk = branch;
        }

        branches.add(branch);
    }

    /**
     * Pushes all tree branches with specified force to smack crash opponents into planet.
     * @param force
     */
    public void smackPlanet(float force) {
        for(int i = 0; i < branches.size; i++) {
            Body body = branches.get(i).getBody();
            body.applyLinearImpulse(force, 0, body.getPosition().x, body.getPosition().y, true);
//                treeGenerator.trunk.getBody().applyLinearImpulse(-100, 0,
//                        treeGenerator.trunk.getBody().getPosition().x, treeGenerator.trunk.getBody().getPosition().y, true);
        }
    }


}
