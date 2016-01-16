package com.lukzdev.grow.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.model.entities.Branch;
import com.lukzdev.grow.model.entities.Entity;
import com.lukzdev.grow.model.entities.Planet;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-16.
 */
public class TreeBuilder {

    private static final float JOINT_ANCHOR_ALPHA = 0.8f;
    private static final float PLANET_JOINT_ANCHOR_ALPHA = 0.97f;

    private Planet planet;
    private EntityManager entityManager;
    private Box2DWorld box2DWorld;

    public TreeBuilder(Planet planet, EntityManager entityManager, Box2DWorld box2DWorld) {
        this.planet = planet;
        this.entityManager = entityManager;
        this.box2DWorld = box2DWorld;
    }

    public void buildTree() {

        float freqDelta = -3;
        float freq = 20;

        // First branch
        Entity previousBranch = createFirstBranch(planet, 25, 100, freq);

        // Array used to hold branch layers
        Array<Entity> lastLayer = new Array<Entity>();
        Array<Entity> currLayer = new Array<Entity>();

        lastLayer.add(previousBranch);

        // For each layer
        for(int i = 0; i <= 5; i++) {
            freq -= freqDelta;

            for(int j = 0; j < lastLayer.size; j++) {
                Entity currBranch = lastLayer.get(j);

                for(int x = 0; x < 2; x++) {
                    Entity newBranch = createBranch(currBranch, currBranch.getBounds().width * 0.8f, currBranch.getBounds().height * 0.8f, freq, 0);
                    currLayer.add(newBranch);
                }

            }

            // Swap entities in arrays
            lastLayer.clear();
            lastLayer.addAll(currLayer);
            currLayer.clear();
        }

    }

    private Branch createFirstBranch(Entity planet, float width, float height, float frequency) {
        // Create new branch
        Branch branch = new Branch(planet.getPosition().x, planet.getPosition().y + planet.getBounds().height / 2 + height / 2, width, height, box2DWorld);
        entityManager.addEntity(branch);

        // Connect to previous branch by weld joint
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(((PhysicsObject) planet).getBody(), branch.getBody(),
                new Vector2(planet.getPosition().x * Box2DWorld.WORLD_TO_BOX,
                        (planet.getPosition().y + (planet.getBounds().height / 2) * PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX));

        weldJoint.localAnchorA.set(0, (planet.getBounds().height / 2 * PLANET_JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
        weldJoint.localAnchorB.set(0,
                -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        // Set some fancy param to make it soft (wobble, wobble, wobble...)
        weldJoint.frequencyHz = frequency;

        box2DWorld.getWorld().createJoint(weldJoint);

        return branch;
    }

    private Branch createBranch(Entity sourceBranch, float width, float height, float frequency, float referenceAngle) {
        // Create new branch
        Branch branch = new Branch(sourceBranch.getPosition().x, sourceBranch.getPosition().y + sourceBranch.getBounds().height / 2 + height / 2, width, height, box2DWorld);
        entityManager.addEntity(branch);

        // Connect to previous branch by weld joint
        WeldJointDef weldJoint = new WeldJointDef();
        weldJoint.initialize(((PhysicsObject) sourceBranch).getBody(), branch.getBody(),
                new Vector2(sourceBranch.getPosition().x * Box2DWorld.WORLD_TO_BOX,
                        (sourceBranch.getPosition().y + (sourceBranch.getBounds().height / 2) * JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX));

        weldJoint.localAnchorA.set(0, (sourceBranch.getBounds().height / 2 * JOINT_ANCHOR_ALPHA) * Box2DWorld.WORLD_TO_BOX);
        weldJoint.localAnchorB.set(0,
                -branch.getBounds().height / 2 * 0.8f * Box2DWorld.WORLD_TO_BOX);

        weldJoint.referenceAngle = referenceAngle * MathUtils.degRad;

        // Set some fancy param to make it soft (wobble, wobble, wobble...)
        weldJoint.frequencyHz = frequency;

        box2DWorld.getWorld().createJoint(weldJoint);

        return branch;
    }
}
