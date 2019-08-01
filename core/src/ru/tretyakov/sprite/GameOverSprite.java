package ru.tretyakov.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tretyakov.base.Sprite;
import ru.tretyakov.math.Rect;

public class GameOverSprite extends Sprite {

    public GameOverSprite(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.8f);
        setRight(worldBounds.getHalfWidth() - getHalfWidth());
        setBottom(worldBounds.getHalfHeight() - getHalfHeight());
    }
}
