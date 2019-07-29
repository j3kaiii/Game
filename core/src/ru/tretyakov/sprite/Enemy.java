package ru.tretyakov.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.base.Ship;
import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;

public class Enemy extends Ship {

    private enum State {DESCENT, FIGHT}
    private State state;

    private Vector2 descentV = new Vector2(0, -0.15f);

    private MainShip mainShip;

    public Enemy(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        laser = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        state = State.DESCENT;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0;
                    shoot();
                }
                if (getBottom() < worldBounds.getBottom()) {
                    destroy();
                }
                break;
        }
        if (hp < 0) destroy();
    }

    public void set(TextureRegion[] regions,
                    Vector2 v0,
                    TextureRegion bulletRegion,
                    float bulletHeight,
                    float bulletVY,
                    int damage,
                    float reloadInterval,
                    float height,
                    int hp) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        v.set(descentV);
        reloadTimer = reloadInterval;
        state = State.DESCENT;
    }
}
