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

    // Specifies how far into the body should be set joint anchor (0 - 1)
    private static final float JOINT_ANCHOR_ALPHA = 0.8f;

    // Same as @JOINT_ANCHOR_ALPHA but specifies where tree should be connected to planet
    private static final float PLANET_JOINT_ANCHOR_ALPHA = 0.97f;

    public static Tree buildTree(GameWorld gameWorld) {
        Tree tree = new Tree(gameWorld.getEntityManager());

        // How frequency should change per layer
        float freqDelta = -3;

        // Starting frequency (how strong joint should be)
        float freq = 20;

        // First branch
        Trunk previousBranch = (Trunk)createBranch(gameWorld, tree, true, null, 25, 100, freq, 0);
        tree.addBranch(previousBranch);

        // Array used to hold branch layers
        Array<Entity> lastLayer = new Array<Entity>();
        Array<Entity> currLayer = new Array<Entity>();

        lastLayer.add(previousBranch);

        /**
         * Config
         * Cool settings:
         * - branches: 2, maxDeg: 45, layers: 5
         * - branches: 2, maxDeg: 30, layers: 3
         * - branches: 3, maxDeg: 30, layers: 3
         */
        // Specifies max angle between first and last branch growing from current branch
        float maxDegDeviation = 30;
        // Specifies how @maxDegDeviation should change per layer
        float deviationDegradationPerLayer = 0;
        // Branches per layer
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
                    Branch newBranch = null;

                    // First layer is still trunk!
                    if(j == 0) {
                        newBranch = createBranch(gameWorld, tree, true, currBranch, currBranch.getBounds().width * 0.8f,
                                currBranch.getBounds().height * 0.8f, freq, referenceAngle);
                    } else {
                        newBranch = createBranch(gameWorld, tree, false, currBranch, currBranch.getBounds().width * 0.8f,
                                currBranch.getBounds().height * 0.8f, freq, referenceAngle);
                    }

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

    /**
     * Create new branch. If sourceBranch is null it assumes it's trunk.
     * @param world
     * @param tree
     * @param sourceBranch
     * @param width
     * @param height
     * @param frequency
     * @param referenceAngle
     * @return
     */
    private static Branch createBranch(GameWorld world, Tree tree, boolean isTrunk, Entity sourceBranch, float width, float height, float frequency,
                                       float referenceAngle) {

        Branch branch = null;

        if(isTrunk || sourceBranch == null) {
            branch = new Trunk(world.getPlanet().getPosition().x,
                    world.getPlanet().getPosition().y + world.getPlanet().getBounds().height / 2 + height / 2, width,
                    height, world.getBox2DWorld(), tree);
            world.getEntityManager().addEntity(branch);
        } else {
            // Create new branch
            branch = new Branch(sourceBranch.getPosition().x, sourceBranch.getPosition().y +
                    sourceBranch.getBounds().height / 2 + height / 2, width, height, world.getBox2DWorld());
            world.getEntityManager().addEntity(branch);
        }

        // Connect to previous branch by weld joint
        WeldJointDef weldJoint = new WeldJointDef();

        // If there is no source branch, connect to planet
        if(sourceBranch == null) {
            weldJoint = new WeldJointDef();
            weldJoint.initialize(((PhysicsObject) world.getPlanet()).getBody(), branch.getBody(),
                    new Vector2(world.getPlanet().getPosition().x * Box2DWorld.WORLD_TO_BOX,
                            (world.getPlanet().getPosition().y + (world.getPlanet().getBounds().height / 2) *
                                    PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX));

            weldJoint.localAnchorA.set(0, (world.getPlanet().getBounds().height / 2 *
                    PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
            weldJoint.localAnchorB.set(0, -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        } else {
            weldJoint.initialize(((PhysicsObject) sourceBranch).getBody(), branch.getBody(),
                    new Vector2(sourceBranch.getPosition().x * Box2DWorld.WORLD_TO_BOX,
                            (sourceBranch.getPosition().y + (sourceBranch.getBounds().height / 2) * JOINT_ANCHOR_ALPHA) *
                                    Box2DWorld.WORLD_TO_BOX));

            weldJoint.localAnchorA.set(0, (sourceBranch.getBounds().height / 2 * JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
            weldJoint.localAnchorB.set(0, -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);
        }

        // Set branch angle
        weldJoint.referenceAngle = referenceAngle * MathUtils.degRad;

        // Set some fancy param to make it soft (wobble, wobble, wobble...)
        weldJoint.frequencyHz = frequency;

        world.getBox2DWorld().getWorld().createJoint(weldJoint);

        return branch;
    }
}
