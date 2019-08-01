package ru.tretyakov.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tretyakov.base.ScaledTouchUpButtons;
import ru.tretyakov.math.Rect;

public class ButtonNext extends ScaledTouchUpButtons {
    private MainShip mainShip;

    public ButtonNext(TextureAtlas atlas, MainShip mainShip) {
        super(atlas.findRegion("button_new_game"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.6f);
    }

    @Override
    public void action() {
        destroy();
        mainShip.reset();
    }
}
