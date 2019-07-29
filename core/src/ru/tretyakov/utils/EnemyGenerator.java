package ru.tretyakov.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.math.Rect;
import ru.tretyakov.math.Rnd;
import ru.tretyakov.pool.EnemyPool;
import ru.tretyakov.sprite.Enemy;

public class EnemyGenerator {

    private static final float ENEMY_SMALL_HEIGHT = 0.1F;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.02F;
    private static final float ENEMY_SMALL_BULLET_VY = -0.6F;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1.5f;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.15F;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.04F;
    private static final float ENEMY_MEDIUM_BULLET_VY = -0.5F;
    private static final int ENEMY_MEDIUM_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_MEDIUM_HP = 15;

    private static final float ENEMY_BIG_HEIGHT = 0.2F;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.08F;
    private static final float ENEMY_BIG_BULLET_VY = -0.4F;
    private static final int ENEMY_BIG_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 5f;
    private static final int ENEMY_BIG_HP = 30;

    private float generateInterval = 4f;
    private float generateTimer;

    private TextureRegion[] enemySmallRegions;
    private TextureRegion[] enemyMediumRegions;
    private TextureRegion[] enemyBigRegions;

    private TextureRegion bulletRegion;

    private Vector2 enemySmallSpeed = new Vector2(0, -0.2f);
    private Vector2 enemyMediumSpeed = new Vector2(0, -0.08f);
    private Vector2 enemyBigSpeed = new Vector2(0, -0.01f);

    private EnemyPool enemyPool;
    private Rect worldBounds;

    public EnemyGenerator(EnemyPool enemyPool, TextureAtlas atlas, Rect worldBounds) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        TextureRegion region0 = atlas.findRegion("enemy0");
        enemySmallRegions = Regions.split(region0,1,2,2);
        TextureRegion region1 = atlas.findRegion("enemy1");
        enemyMediumRegions = Regions.split(region1,1,2,2);
        TextureRegion region2 = atlas.findRegion("enemy2");
        enemyBigRegions = Regions.split(region2,1,2,2);
        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0;
            Enemy enemy = enemyPool.obtain();
            float type = (float)Math.random();
            if (type < 0.5f) {
                enemy.set(enemySmallRegions, enemySmallSpeed, bulletRegion, ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY, ENEMY_SMALL_DAMAGE, ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT, ENEMY_SMALL_HP);

            } else if (type < 0.8f) {
                enemy.set(enemyMediumRegions, enemyMediumSpeed, bulletRegion, ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY, ENEMY_MEDIUM_DAMAGE, ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT, ENEMY_MEDIUM_HP);
            } else {
                enemy.set(enemyBigRegions, enemyBigSpeed, bulletRegion, ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY, ENEMY_BIG_DAMAGE, ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT, ENEMY_BIG_HP);
            }
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(),
                    worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }
    }
}
