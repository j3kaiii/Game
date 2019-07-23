package ru.tretyakov.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.tretyakov.base.ScaledTouchUpButtons;
import ru.tretyakov.math.Rect;

public class ButtonExit extends ScaledTouchUpButtons {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.15f);
        setRight(worldBounds.getRight() - 0.04f);
        setBottom(worldBounds.getBottom() + 0.04f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
