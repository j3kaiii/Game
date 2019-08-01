package ru.tretyakov.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;
import ru.tretyakov.pool.ExplosionsPool;
import ru.tretyakov.sprite.Bullet;
import ru.tretyakov.sprite.Explosion;

public abstract class Ship extends Sprite {

    protected TextureRegion bulletRegion;

    protected Rect worldBounds;
    protected BulletPool bulletPool;

    protected Vector2 v;
    protected Vector2 v0;
    protected Vector2 bulletV;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float bulletHeight;

    private float damageAnimateInterval = 0.12f;
    private float damageAnimateTimer = damageAnimateInterval;

    protected ExplosionsPool explosionsPool;

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

    public void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this,bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        laser.play();
    }

    @Override
    public void update(float delta) {
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval) frame = 0;
    }

    @Override
    public void dispose() {
        laser.dispose();
    }

    @Override
    public void destroy() {
        boom();
        hp = 0;
        super.destroy();
    }

    public void setDamage(Bullet bullet) {
        frame = 1;
        damageAnimateTimer = 0f;
        hp -= bullet.getDamage();
        System.out.println(getClass().getName() + " hp " + hp);
        if (hp <= 0) destroy();
    }

    public void boom() {
        Explosion explosion = explosionsPool.obtain();
        explosion.set(getHeight(), pos);
    }
}
