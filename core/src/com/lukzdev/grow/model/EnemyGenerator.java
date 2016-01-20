package com.lukzdev.grow.model;

import com.badlogic.gdx.math.MathUtils;
import com.lukzdev.grow.model.entities.*;
import com.lukzdev.grow.model.entities.enemy.RoundEnemy;
import com.lukzdev.grow.model.entities.enemy.SquareCutterEnemy;
import com.lukzdev.grow.model.entities.enemy.SquareEnemy;

/**
 * Generate enemies
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-18.
 */
public class EnemyGenerator {

    // Config
    private float TIME_BETWEEN_SPAWN = .5f;

    // Starting time before first spawn
    private float timeTillSpawn = 2;

    private GameWorld gameWorld;

    public EnemyGenerator(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void update(float delta) {
        timeTillSpawn -= delta;

        // Spawn enemies!
        if(timeTillSpawn <= 0) {

            // Random planet side
            int direction = MathUtils.randomSign();

            Planet planet = gameWorld.getPlanet();

            // Calculate position on random planet side
            float posX = planet.getPosition().x + planet.getBounds().width / 2 * 1.5f * direction;
            float posY = planet.getPosition().y;

            // Create enemy
            Enemy newEnemy = null;

            switch(MathUtils.random(2)) {
                case 0:
                    newEnemy = new SquareEnemy(posX, posY, 40, 40, gameWorld);
                    break;
                case 1:
                    newEnemy = new SquareCutterEnemy(posX, posY, 40, 40, gameWorld);
                    break;
                case 2:
                    newEnemy = new RoundEnemy(posX, posY, MathUtils.random(40, 50), gameWorld);
                    break;
            }

            // Add enemy to world
            if(newEnemy != null) {
                gameWorld.getEntityManager().addEntity(newEnemy);
            }

            // Reset spawn countdown
            timeTillSpawn = TIME_BETWEEN_SPAWN + MathUtils.random(-1, 1);
        }

    }
}
