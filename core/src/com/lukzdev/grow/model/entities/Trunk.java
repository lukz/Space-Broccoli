package com.lukzdev.grow.model.entities;

import com.lukzdev.grow.model.Box2DWorld;
import com.lukzdev.grow.model.GameWorld;
import com.lukzdev.grow.model.PhysicsObject;

/**
 * Trunk it's a special branch that can get hits from enemies! Take care of it!
 *
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class Trunk extends Branch {

    private Tree tree;

    public Trunk(float x, float y, float width, float height, Box2DWorld box2DWorld, Tree tree) {
        super(x, y, width, height, box2DWorld);

        this.tree = tree;
    }


    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {
        if(psycho2 instanceof Enemy) {
            tree.getHit((Enemy)psycho2);
        }
    }

}
