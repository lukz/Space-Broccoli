package com.lukzdev.grow.model;

import com.badlogic.gdx.math.MathUtils;
import com.lukzdev.grow.model.entities.Enemy;
import com.lukzdev.grow.model.entities.Planet;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-18.
 */
public class EnemyGenerator {

    // Config
    private float TIME_BETWEEN_SPAWN = 1.5f;

    private GameWorld gameWorld;

    private float timeTillSpawn = 0;

    public EnemyGenerator(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void update(float delta) {
        timeTillSpawn -= delta;

        // Spawn enemeies!
        if(timeTillSpawn <= 0) {

            // Random planet side
            int direction = MathUtils.randomSign();

            Planet planet = gameWorld.getPlanet();

            // Calculate position on random planet side
            float posX = planet.getPosition().x + planet.getBounds().width / 2 * 1.5f * direction;
            float posY = planet.getPosition().y;

            // Create enemy
            Enemy newEnemy = new Enemy(posX, posY, 50, 50, gameWorld);

            // Add enemy to wold
            gameWorld.getEntityManager().addEntity(newEnemy);

            // Reset spawn countdown
            timeTillSpawn = TIME_BETWEEN_SPAWN + MathUtils.random(-1, 1);
        }

    }
}
