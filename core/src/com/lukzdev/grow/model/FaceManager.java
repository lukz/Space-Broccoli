package com.lukzdev.grow.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.lukzdev.grow.model.entities.Entity;
import com.lukzdev.grow.model.entities.face.CrossedEyeEntity;
import com.lukzdev.grow.model.entities.face.EyeEntity;
import com.lukzdev.grow.model.entities.face.MouthEntity;

/**
 * Some weird shit. I don't have enough time to comment on that.
 *
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-18.
 */
public class FaceManager {

    public static enum FACES {
      SMILE, SAD, ANGRY, SICK
    };

    public static FACES face = FACES.SMILE;

    //Config
    private static float MOUTH_ENTITIES = 20;
    private static float SMILE_MOVE_SPEED = 10;
    private static float SAD_TIME = 1;

    private Array<Entity> eyeEntities = new Array();
    private Array<Entity> crossedEyeEntities = new Array();
    private Array<Entity> mouthEntities = new Array();
    private Vector2 mouthPos;

    private static float sadTimeCountdown = 0;

    private GameWorld gameWorld;

    public FaceManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        // Eyes
        Entity leftEye = new EyeEntity(gameWorld.getPlanet().getPosition().x, gameWorld.getPlanet().getPosition().y, gameWorld.getBox2DWorld());
        gameWorld.getEntityManager().addEntity(leftEye);
        eyeEntities.add(leftEye);

        Entity rightEye = new EyeEntity(gameWorld.getPlanet().getPosition().x, gameWorld.getPlanet().getPosition().y, gameWorld.getBox2DWorld());
        gameWorld.getEntityManager().addEntity(rightEye);
        eyeEntities.add(rightEye);

        // Crossed eyes
        Entity leftCrossedEye = new CrossedEyeEntity(gameWorld.getPlanet().getPosition().x, gameWorld.getPlanet().getPosition().y, gameWorld.getBox2DWorld());
        gameWorld.getEntityManager().addEntity(leftCrossedEye);
        crossedEyeEntities.add(leftCrossedEye);

        Entity rightCrossedEye = new CrossedEyeEntity(gameWorld.getPlanet().getPosition().x, gameWorld.getPlanet().getPosition().y, gameWorld.getBox2DWorld());
        gameWorld.getEntityManager().addEntity(rightCrossedEye);
        crossedEyeEntities.add(rightCrossedEye);

        // Mouth
        mouthPos = new Vector2(gameWorld.getPlanet().getPosition().x, gameWorld.getPlanet().getPosition().y + gameWorld.getPlanet().getBounds().height / 2 * 0.70f);

        for(int i = 0; i < MOUTH_ENTITIES; i++) {
            Entity entity = new MouthEntity(gameWorld.getPlanet().getPosition().x + i, gameWorld.getPlanet().getPosition().y, gameWorld.getBox2DWorld());

            gameWorld.getEntityManager().addEntity(entity);
            mouthEntities.add(entity);
        }
    }

    // Temp
    private Vector2 tempVec2 = new Vector2();

    public void update(float delta) {
        float faceWidth = mouthEntities.size * MouthEntity.RADIUS * 2;
        float faceHeight = 70;

        // Update Mouth
        switch(face) {
            case SMILE:
                float minDeg = 180;
                float maxDeg = 360;

                updateEyes(faceWidth, faceHeight);

                for(int i = 0; i < mouthEntities.size; i++) {
                    Entity entity = mouthEntities.get(i);
                    Body body = ((PhysicsObject) entity).getBody();

                    float posX = MathUtils.lerp(mouthPos.x - faceWidth / 2f, mouthPos.x + faceWidth / 2f, (i + 1) / (float) mouthEntities.size);
                    float posY = MathUtils.sinDeg(MathUtils.lerp(minDeg, maxDeg, (i + 1) / (float) mouthEntities.size))
                            * faceHeight + mouthPos.y + faceHeight / 2f;

                    tempVec2.set(posX, posY).sub(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                            .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED);

                    body.setLinearVelocity(tempVec2.x, tempVec2.y);
                }
                break;

            case SAD:
                minDeg = 0;
                maxDeg = 180;

                updateEyes(faceWidth, faceHeight);

                for(int i = 0; i < mouthEntities.size; i++) {
                    Entity entity = mouthEntities.get(i);
                    Body body = ((PhysicsObject) entity).getBody();

                    float posX = MathUtils.lerp(mouthPos.x - faceWidth / 2f, mouthPos.x + faceWidth / 2f, (i + 1) / (float) mouthEntities.size);
                    float posY = MathUtils.sinDeg(MathUtils.lerp(minDeg, maxDeg, (i + 1) / (float) mouthEntities.size))
                            * faceHeight + mouthPos.y - faceHeight / 2f;

                    tempVec2.set(posX, posY).sub(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                            .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED);

                    body.setLinearVelocity(tempVec2.x, tempVec2.y);
                }


                sadTimeCountdown -= delta;
                if(sadTimeCountdown <= 0) {
                    face = FACES.SMILE;
                }

               break;

            // Easter egg when simulation crashes
            case SICK:
                minDeg = 0;
                maxDeg = 180;
                faceHeight = 25f;

                updateCrossedEyes(faceWidth, faceHeight);

                for(int i = 0; i < mouthEntities.size; i++) {
                    Entity entity = mouthEntities.get(i);
                    Body body = ((PhysicsObject) entity).getBody();

                    float posX = MathUtils.lerp(mouthPos.x - faceWidth / 2f, mouthPos.x + faceWidth / 2f, (i + 1) / (float) mouthEntities.size);
                    float posY = MathUtils.sinDeg(MathUtils.lerp(minDeg, maxDeg, (i + 1) / (float) mouthEntities.size * 6f))
                            * faceHeight + mouthPos.y - faceHeight / 2f;

                    tempVec2.set(posX, posY).sub(body.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                            .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED);

                    body.setLinearVelocity(tempVec2.x, tempVec2.y);
                }

                break;
        }
    }

    private void updateEyes(float faceWidth, float faceHeight) {
        activateEyes();

        // Update left eye
        Body leftEyeBody = ((PhysicsObject)eyeEntities.get(0)).getBody();

        tempVec2.set(mouthPos.x - faceWidth * 0.2f,
                mouthEntities.get((int)(mouthEntities.size / 2 - mouthEntities.size * 0.2f)).getPosition().y + faceHeight);
        tempVec2.sub(leftEyeBody.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED);

        leftEyeBody.setLinearVelocity(tempVec2.x, tempVec2.y);

        // Update right eye
        Body rightEyeBody = ((PhysicsObject)eyeEntities.get(1)).getBody();

        tempVec2.set(mouthPos.x + faceWidth * 0.2f,
                mouthEntities.get((int)(mouthEntities.size / 2 + mouthEntities.size * 0.2f)).getPosition().y + faceHeight);
        tempVec2.sub(rightEyeBody.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED);

        rightEyeBody.setLinearVelocity(tempVec2.x, tempVec2.y);
    }

    private void updateCrossedEyes(float faceWidth, float faceHeight) {
        activateCrossedEyes();

        // Update left eye
        Body leftEyeBody = ((PhysicsObject)crossedEyeEntities.get(0)).getBody();

        tempVec2.set(mouthPos.x - faceWidth * 0.2f,
                mouthEntities.get((int)(mouthEntities.size / 2 - mouthEntities.size * 0.2f)).getPosition().y + faceHeight * 3);
        tempVec2.sub(leftEyeBody.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED * 2);

        leftEyeBody.setLinearVelocity(tempVec2.x, tempVec2.y);

        // Update right eye
        Body rightEyeBody = ((PhysicsObject)crossedEyeEntities.get(1)).getBody();

        tempVec2.set(mouthPos.x + faceWidth * 0.2f,
                mouthEntities.get((int)(mouthEntities.size / 2 + mouthEntities.size * 0.2f)).getPosition().y + faceHeight * 3);
        tempVec2.sub(rightEyeBody.getPosition().scl(Box2DWorld.BOX_TO_WORLD))
                .scl(Box2DWorld.WORLD_TO_BOX).scl(SMILE_MOVE_SPEED * 2);

        rightEyeBody.setLinearVelocity(tempVec2.x, tempVec2.y);
    }

    private void activateEyes() {
        for(int i = 0; i < crossedEyeEntities.size; i++) {
            Body body = ((PhysicsObject)crossedEyeEntities.get(i)).getBody();
            if(body.isActive()) {
                body.setTransform(0, 0, 0);
                body.setActive(false);
            }
        }

        for(int i = 0; i < eyeEntities.size; i++) {
            Body body = ((PhysicsObject)eyeEntities.get(i)).getBody();
            if(!body.isActive()) {
                body.setActive(true);
            }
        }
    }

    private void activateCrossedEyes() {
        for(int i = 0; i < eyeEntities.size; i++) {
            Body body = ((PhysicsObject)eyeEntities.get(i)).getBody();
            if(body.isActive()) {
                body.setTransform(0, 0, 0);
                body.setActive(false);
            }
        }

        for(int i = 0; i < crossedEyeEntities.size; i++) {
            Body body = ((PhysicsObject)crossedEyeEntities.get(i)).getBody();
            if(!body.isActive()) {
                body.setActive(true);
            }
        }
    }

    public static void setFace(FACES newFace) {
        if(face == FACES.SICK) return;

        switch (newFace) {
            case SAD:
                sadTimeCountdown = SAD_TIME;
            default:
                face = newFace;
        }
    }

}
