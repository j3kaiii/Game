package ru.tretyakov.pool;

import ru.tretyakov.base.SpritesPool;
import ru.tretyakov.math.Rect;
import ru.tretyakov.sprite.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {

    private BulletPool bulletPool;
    private Rect worldBounds;

    public EnemyPool(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, worldBounds);
    }

}
