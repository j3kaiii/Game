package ru.tretyakov.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tretyakov.base.Sprite;

public class GameOverSprite extends Sprite {
    private TextureAtlas atlas;

    public GameOverSprite(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }
}
