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
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01F;
    private static final float ENEMY_SMALL_BULLET_VY = -0.6F;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1.5f;
    private static final int ENEMY_SMALL_HP = 1;

    private float generateInterval = 4f;
    private float generateTimer;

    private TextureRegion[] enemySmallRegions;
    private TextureRegion bulletRegion;

    private Vector2 enemySmallSpeed = new Vector2(0, -0.2f);

    private EnemyPool enemyPool;
    private Rect worldBounds;

    public EnemyGenerator(EnemyPool enemyPool, TextureAtlas atlas, Rect worldBounds) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        TextureRegion region0 = atlas.findRegion("enemy0");
        enemySmallRegions = Regions.split(region0,1,2,2);
        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0;
            Enemy enemy = enemyPool.obtain();
            enemy.set(enemySmallRegions, enemySmallSpeed, bulletRegion, ENEMY_SMALL_BULLET_HEIGHT,
                    ENEMY_SMALL_BULLET_VY, ENEMY_SMALL_DAMAGE, ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HEIGHT, ENEMY_SMALL_HP);
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + 0.05f, worldBounds.getRight() - 0.05f);
            enemy.setBottom(worldBounds.getTop());
        }
    }
}
