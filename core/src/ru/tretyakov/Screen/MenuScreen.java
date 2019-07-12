package ru.tretyakov.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.Base.BaseScreen;

public class MenuScreen extends BaseScreen {
    Texture img, backgroundImage;
    Vector2 myPos, targetPos, touch;
    float dist;

    @Override
    public void show() {
        super.show();
        backgroundImage = new Texture("space.jpg");
        img = new Texture("badlogic.jpg");
        myPos = new Vector2();
        targetPos = new Vector2();
        touch = new Vector2();
        dist = 0.0f;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // проверяем расстояние до финиша
        dist = touch.cpy().sub(myPos).len();
        if (dist > 1.0f) {
            System.out.println(dist);
            myPos.add(targetPos.nor());
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundImage, 0,0);
        batch.draw(img,myPos.x,myPos.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        backgroundImage.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        // определяем вектор движения от нашей позиции к указаной точке
        targetPos = touch.cpy().sub(myPos);
        System.out.println("x - " + targetPos.x + " y - " + targetPos.y);
        return false;
    }
}
