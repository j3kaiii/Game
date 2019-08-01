package ru.tretyakov.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.base.Ship;
import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;
import ru.tretyakov.pool.ExplosionsPool;

public class MainShip extends Ship {

    private static final int INVALID_POINTER = -1;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;


    private boolean pressedLeft = false;
    private boolean pressedRight = false;



    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionsPool explosionsPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionsPool = explosionsPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        reloadInterval = 0.2f;
        laser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        v = new Vector2();
        v0 = new Vector2(0.3f, 0f);
        bulletV = new Vector2(0, 0.6f);
        bulletHeight = 0.01f;
        damage = 1;
        hp = 10;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.15f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0;
            shoot();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    public boolean isBulletCollision(Rect bullet) {
        return ! (
                bullet.getLeft() > getRight()
                        || bullet.getRight() < getLeft()
                        || bullet.getBottom() > pos.y
                        || bullet.getTop() < getBottom()
        );
    }

    public void reset() {
        this.hp = 10;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                moveLeft();
                pressedLeft = true;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                moveRight();
                pressedRight = true;
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                if (!pressedRight) {
                    stop();
                }
                pressedLeft = false;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                if (!pressedLeft) {
                    stop();
                }
               pressedRight = false;
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public void moveRight() {
        v.set(v0);
    }

    public void moveLeft() {
        v.set(v0).rotate(180);
    }

    public void stop() {
        v.setZero();
    }
}
