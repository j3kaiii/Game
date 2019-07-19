package ru.tretyakov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    public static final float V_SPEED = 0.01f;

    private Vector2 myPos;
    private Vector2 end;
    private Vector2 buf;
    private Vector2 v;

    private Texture img;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        v = new Vector2();
        myPos = new Vector2();
        end = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        buf.set(end);
        if (buf.sub(myPos).len() > V_SPEED) {
            myPos.add(v);
        } else {
            myPos.set(end);
        }
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, myPos.x, myPos.y, 0.15f, 0.15f);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        end.set(touch);
        v.set(touch.sub(myPos)).setLength(V_SPEED);
        return false;
    }
}
