package ru.tretyakov.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;
import ru.tretyakov.sprite.Bullet;

public abstract class Ship extends Sprite {

    protected TextureRegion bulletRegion;

    protected Rect worldBounds;
    protected BulletPool bulletPool;

    protected Vector2 direction;
    protected Vector2 speed;
    protected Vector2 bulletV;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float bulletHeight;

    protected int damage;
    protected int hp;

    protected Sound laser;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(direction, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0;
            shoot();
        }
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this,bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        laser.play();
    }

    @Override
    public void dispose() {
        laser.dispose();
    }
}
