package com.lukzdev.grow.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.model.entities.Branch;
import com.lukzdev.grow.model.entities.Entity;
import com.lukzdev.grow.model.entities.Tree;
import com.lukzdev.grow.model.entities.Trunk;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class TreeGenerator {

    private static final float JOINT_ANCHOR_ALPHA = 0.8f;
    private static final float PLANET_JOINT_ANCHOR_ALPHA = 0.97f;

    public static Tree buildTree(GameWorld gameWorld) {
        Tree tree = new Tree(gameWorld.getEntityManager());

        float freqDelta = -3;
        float freq = 20;

        // First branch
        Entity previousBranch = createTrunk(gameWorld, tree, 25, 100, freq);

        // Array used to hold branch layers
        Array<Entity> lastLayer = new Array<Entity>();
        Array<Entity> currLayer = new Array<Entity>();

        lastLayer.add(previousBranch);

        /**
         * Cool settings:
         * - branches: 2, maxDeg: 45, layers: 5
         * - branches: 2, maxDeg: 30, layers: 3
         * - branches: 3, maxDeg: 30, layers: 3
         */
        // Config
        float maxDegDeviation = 30;
        float deviationDegradationPerLayer = 0;
        int branches = 3;

        float currMaxDegDeviation = maxDegDeviation;

        // For each layer
        for(int i = 0; i <= 3; i++) {
            freq -= freqDelta;

            // Degrade angle
            currMaxDegDeviation = Math.max(0, currMaxDegDeviation + deviationDegradationPerLayer);

            int spacesBetweenBranches = branches - 1;
            float angleBetweenBranches = currMaxDegDeviation * 2 / spacesBetweenBranches;

            // For every branch in layer
            for(int j = 0; j < lastLayer.size; j++) {
                Entity currBranch = lastLayer.get(j);

                // Create new branches
                for(int x = 0; x < branches; x++) {
                    float referenceAngle = -currMaxDegDeviation + angleBetweenBranches * x;

                    // New branch
                    Branch newBranch = createBranch(gameWorld, currBranch, currBranch.getBounds().width * 0.8f,
                            currBranch.getBounds().height * 0.8f, freq, referenceAngle);

                    // Add to current layer to use in next iteration
                    currLayer.add(newBranch);

                    // Add branch to returned tree
                    tree.addBranch(newBranch);
                }

            }

            // Swap entities in arrays
            lastLayer.clear();
            lastLayer.addAll(currLayer);
            currLayer.clear();
        }

        return tree;
    }

    private static Branch createTrunk(GameWorld world, Tree tree, float width, float height, float frequency) {
        // Create new branch
        Branch branch = new Trunk(world.getPlanet().getPosition().x,
                world.getPlanet().getPosition().y + world.getPlanet().getBounds().height / 2 + height / 2, width,
                height, world.getBox2DWorld(), tree);
        world.getEntityManager().addEntity(branch);

        // Connect to previous branch by weld joint
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(((PhysicsObject) world.getPlanet()).getBody(), branch.getBody(),
                new Vector2(world.getPlanet().getPosition().x * Box2DWorld.WORLD_TO_BOX,
                        (world.getPlanet().getPosition().y + (world.getPlanet().getBounds().height / 2) *
                                PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX));

        weldJoint.localAnchorA.set(0, (world.getPlanet().getBounds().height / 2 *
                PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
        weldJoint.localAnchorB.set(0,
                -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        // Set some fancy param to make it soft (wobble, wobble, wobble...)
        weldJoint.frequencyHz = frequency;

        world.getBox2DWorld().getWorld().createJoint(weldJoint);

        return branch;
    }

    private static Branch createBranch(GameWorld world, Entity sourceBranch, float width, float height, float frequency,
                                       float referenceAngle) {
        // Create new branch
        Branch branch = new Branch(sourceBranch.getPosition().x, sourceBranch.getPosition().y +
                sourceBranch.getBounds().height / 2 + height / 2, width, height, world.getBox2DWorld());
        world.getEntityManager().addEntity(branch);

        // Connect to previous branch by weld joint
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(((PhysicsObject) sourceBranch).getBody(), branch.getBody(),
                new Vector2(sourceBranch.getPosition().x * Box2DWorld.WORLD_TO_BOX,
                        (sourceBranch.getPosition().y + (sourceBranch.getBounds().height / 2) * JOINT_ANCHOR_ALPHA) *
                                Box2DWorld.WORLD_TO_BOX));

        weldJoint.localAnchorA.set(0, (sourceBranch.getBounds().height / 2 * JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
        weldJoint.localAnchorB.set(0,
                -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        weldJoint.referenceAngle = referenceAngle * MathUtils.degRad;

        // Set some fancy param to make it soft (wobble, wobble, wobble...)
        weldJoint.frequencyHz = frequency;

        world.getBox2DWorld().getWorld().createJoint(weldJoint);

        return branch;
    }
}
