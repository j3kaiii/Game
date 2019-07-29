package ru.tretyakov.screen;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.tretyakov.base.BaseScreen;
import ru.tretyakov.base.Sprite;
import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;
import ru.tretyakov.pool.EnemyPool;
import ru.tretyakov.sprite.Background;
import ru.tretyakov.sprite.Bullet;
import ru.tretyakov.sprite.Enemy;
import ru.tretyakov.sprite.MainShip;
import ru.tretyakov.sprite.Star;
import ru.tretyakov.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyGenerator enemyGenerator;

    private Star[] starArray;
    private MainShip mainShip;
    private Music music;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));

        music.play();
        music.setLooping(true);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        enemyGenerator = new EnemyGenerator(enemyPool, atlas, worldBounds);
        mainShip = new MainShip(atlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedActiveSprites();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        this.worldBounds = worldBounds;
        for (Star s : starArray) {
            s.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        mainShip.dispose();
        music.dispose();
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    public void update(float delta) {
        for (Star s : starArray) {
            s.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        mainShip.update(delta);
        enemyGenerator.generate(delta);
    }

    private void checkCollisions() {
        List<Enemy> enemies = enemyPool.getActiveObjects();
        List<Bullet> bullets = bulletPool.getActiveObjects();
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                if (!enemies.get(i).isOutside(mainShip)) {
                    enemies.get(i).destroy();
                } else if (bullets.get(j).getOwner() == mainShip && !bullets.get(j).isOutside(enemies.get(i))) {
                    enemies.get(i).setDamage(bullets.get(j));
                    bullets.get(j).destroy();
                }
            }

        }
    }

    private void freeAllDestroyedActiveSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star s : starArray) {
            s.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        mainShip.draw(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return false;
    }
}
