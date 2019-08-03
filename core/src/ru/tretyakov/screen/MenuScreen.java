package ru.tretyakov.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tretyakov.base.BaseScreen;
import ru.tretyakov.math.Rect;
import ru.tretyakov.sprite.Background;
import ru.tretyakov.sprite.ButtonExit;
import ru.tretyakov.sprite.ButtonPlay;
import ru.tretyakov.sprite.Star;

public class MenuScreen extends BaseScreen {

    private Game game;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    private static final int STAR_COUNT = 256;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private Star[] starArray;
    private Music music;

    public MenuScreen(Game game) {
        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
    }

    @Override
    public void show() {
        super.show();
        music.play();
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas, game);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star s : starArray) {
            s.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        atlas.dispose();
        music.dispose();
        super.dispose();
        bg.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }

    public void update(float delta) {
        for (Star s : starArray) {
            s.update(delta);
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star s : starArray) {
            s.draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }
}
