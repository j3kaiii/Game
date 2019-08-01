package ru.tretyakov.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tretyakov.base.ScaledTouchUpButtons;
import ru.tretyakov.math.Rect;
import ru.tretyakov.screen.GameScreen;

public class ButtonPlay extends ScaledTouchUpButtons {

    private Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
        setBottom(worldBounds.getBottom() + 0.04f);
        setLeft(worldBounds.getLeft() + 0.04f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
