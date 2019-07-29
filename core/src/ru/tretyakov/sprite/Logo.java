package ru.tretyakov.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.base.Sprite;


public class Logo extends Sprite {

    public static final float V_SPEED = 0.01f;

    private Vector2 end;
    private Vector2 buf;
    private Vector2 v;

    public Logo(TextureRegion region) {
        super(region);
        v = new Vector2();
        end = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void update(float delta) {

        buf.set(end);
        if (buf.sub(pos).len() > V_SPEED) {
            pos.add(v);
        } else {
            pos.set(end);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        end.set(touch);
        v.set(touch.sub(pos)).setLength(V_SPEED);
        return false;
    }
}
