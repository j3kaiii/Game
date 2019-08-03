package ru.tretyakov.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tretyakov.base.ScaledTouchUpButtons;
import ru.tretyakov.math.Rect;
import ru.tretyakov.screen.GameScreen;

public class ButtonNext extends ScaledTouchUpButtons {
    GameScreen gameScreen;

    public ButtonNext(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.05f);
        setTop(-0.012f);
    }

    @Override
    public void action() {
        gameScreen.resetGame();
    }
}
