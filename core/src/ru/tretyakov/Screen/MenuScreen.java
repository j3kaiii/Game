package ru.tretyakov.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.Base.BaseScreen;

public class MenuScreen extends BaseScreen {
    Texture img, backgroundImage;
    Vector2 myPos, targetPos, touch;
    float dist, dX, dY;
    float step;
    boolean lefrMove, rightMove, upMove, downMove;
    boolean mouseControl;

    @Override
    public void show() {
        super.show();
        backgroundImage = new Texture("space.jpg");
        img = new Texture("badlogic.jpg");
        myPos = new Vector2();
        targetPos = new Vector2();
        touch = new Vector2();
        dist = 0.0f; dX = 0.0f; dY = 0.0f;
        step = 0.0f;
        mouseControl = true;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // управление стрелками
        updateArrowsMotion();
        // управление мышкой - движение в точку клика
        updateClickMotion();

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

    // определяем точку, куда полетит картинка после клика мышки
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // если был простой клик (не захват), разрешаем картинке движение
        mouseControl = true;
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        //  ----- эти переменные нужны для Drag'n'Drop -----
        dX = touch.x - myPos.x;
        dY = touch.y - myPos.y;
        //  ------------------------------------------------
        // определяем вектор движения от нашей позиции к указаной точке
        targetPos = touch.cpy().sub(myPos);
        System.out.println("x - " + targetPos.x + " y - " + targetPos.y);
        return false;
    }


    // цепляем картинку к мышке и таскаем по экрану
    // пока зажата кнопка мышки прижат палец к экрану
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // нужно отключить движение картинки в сторону клика
        mouseControl = false;
        // если зажали мышку не на картинке, движения не будет
        if (dX > 0 && dX < img.getWidth() && dY > 0 && dY < img.getHeight()) {
            myPos.x = screenX - dX;
            myPos.y = Gdx.graphics.getHeight() - screenY - dY;
        }
        return true;
    }

    // проверяем, какая стрелка нажата
    // задаем движение в нужную сторону
    @Override
    public boolean keyDown(int keycode) {
        mouseControl = false;
        switch (keycode) {
            case Keys.LEFT : setLeftMove(true);
            break;
            case Keys.RIGHT : setRightMove(true);
            break;
            case Keys.UP : setUpMove(true);
            break;
            case Keys.DOWN : setDownMove(true);
        }
        return true;
    }

    // --------  эти методы задают движение в нужную нам сторону -----------
    private void setDownMove(boolean b) {
        if(upMove && b) upMove = false;
        downMove = b;
    }

    private void setUpMove(boolean b) {
        if(downMove && b) downMove = false;
        upMove = b;
    }

    private void setRightMove(boolean b) {
        if(lefrMove && b) lefrMove = false;
        rightMove = b;
    }

    private void setLeftMove(boolean b) {
        if(rightMove && b) rightMove = false;
        lefrMove = b;
    }

    // проверяем, какая стрелка отпущена
    // прекращаем двигаться в эту сторону
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.LEFT : setLeftMove(false);
                break;
            case Keys.RIGHT : setRightMove(false);
                break;
            case Keys.UP : setUpMove(false);
                break;
            case Keys.DOWN : setDownMove(false);
        }
        return true;
    }

    // после того, как мы решили, куда двигаться
    // начинаем движение
    public void updateArrowsMotion() {
        if (lefrMove) myPos.x -= 3; //* Gdx.graphics.getDeltaTime();
        if (rightMove) myPos.x += 3; //* Gdx.graphics.getDeltaTime();
        if (upMove) myPos.y += 3; //* Gdx.graphics.getDeltaTime();
        if (downMove) myPos.y -= 3; //* Gdx.graphics.getDeltaTime();
    }

    // движение картинки в сторону клика
    public void updateClickMotion() {
        //находим расстояние между точкой клика и картинкой
        dist = touch.cpy().sub(myPos).len();
        if (dist > 1.0f && mouseControl) {
            System.out.println(dist);
            //пока расстояние больше 1, двигаем картинку
            myPos.add(targetPos.nor());
        }
    }
}
