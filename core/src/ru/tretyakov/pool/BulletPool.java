package ru.tretyakov.pool;

import ru.tretyakov.base.SpritesPool;
import ru.tretyakov.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
