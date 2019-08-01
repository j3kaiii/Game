package ru.tretyakov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.tretyakov.base.BaseScreen;
import ru.tretyakov.math.Rect;
import ru.tretyakov.pool.BulletPool;
import ru.tretyakov.pool.EnemyPool;
import ru.tretyakov.pool.ExplosionsPool;
import ru.tretyakov.sprite.Background;
import ru.tretyakov.sprite.Bullet;
import ru.tretyakov.sprite.ButtonNext;
import ru.tretyakov.sprite.Enemy;
import ru.tretyakov.sprite.GameOverSprite;
import ru.tretyakov.sprite.MainShip;
import ru.tretyakov.sprite.Star;
import ru.tretyakov.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private ButtonNext buttonNext;

    private static final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;
    private GameOverSprite gameOverSprite;


    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionsPool explosionsPool;
    private EnemyGenerator enemyGenerator;

    private Star[] starArray;
    private MainShip mainShip;
    private Music music;
    private Sound explosionSound;

    private enum State {PLAYING, PAUSE, GAME_OVER}
    State state;
    State stateBuff;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

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
        explosionsPool = new ExplosionsPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionsPool, worldBounds);
        enemyGenerator = new EnemyGenerator(enemyPool, atlas, worldBounds);
        mainShip = new MainShip(atlas, bulletPool, explosionsPool);
        state = State.PLAYING;
        stateBuff = State.PLAYING;

        buttonNext = new ButtonNext(atlas, mainShip);
        gameOverSprite = new GameOverSprite(atlas);
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
        gameOverSprite.resize(worldBounds);
        background.resize(worldBounds);
        this.worldBounds = worldBounds;
        for (Star s : starArray) {
            s.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void pause() {
        super.pause();
        pauseOn();
    }

    @Override
    public void resume() {
        super.resume();
        pauseOff();
    }

    @Override
    public void dispose() {
        mainShip.dispose();
        gameOverSprite.dispose();
        music.dispose();
        explosionSound.dispose();
        atlas.dispose();
        bg.dispose();
        explosionsPool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    public void update(float delta) {
        if (state != State.PAUSE) {
            for (Star s : starArray) {
                s.update(delta);
            }
        }
        explosionsPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            mainShip.update(delta);
            enemyGenerator.generate(delta);
        }
    }

    private void checkCollisions() {
        if (state == State.GAME_OVER) return;
        List<Enemy> enemies = enemyPool.getActiveObjects();
        List<Bullet> bullets = bulletPool.getActiveObjects();
        for (Enemy enemy : enemies) {
           if (enemy.isDestroyed()) continue;
           float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
           if (enemy.pos.dst(mainShip.pos) < minDist) {
               enemy.destroy();
               mainShip.destroy();
               state = State.GAME_OVER;
           }
        }
        for (Bullet bullet : bullets) {
            if (bullet.isDestroyed()) continue;
            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.setDamage(bullet);
                    if (mainShip.isDestroyed()) state = State.GAME_OVER;
                    System.out.println(state);
                    bullet.destroy();
                }
            } else {
                for (Enemy enemy : enemies) {
                    if (enemy.isDestroyed()) continue;
                    if (enemy.isBulletCollision(bullet)) {
                        enemy.setDamage(bullet);
                        bullet.destroy();
                    }
                }
            }
        }
    }

    private void freeAllDestroyedActiveSprites() {
        explosionsPool.freeAllDestroyedActiveSprites();
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
        explosionsPool.drawActiveSprites(batch);
        if (state == State.PLAYING || (state == State.PAUSE && stateBuff != State.GAME_OVER)) {
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
            mainShip.draw(batch);
        }
        //if (state == State.GAME_OVER) {
           // System.out.println("тест draw - " + state);
            gameOverSprite.draw(batch);
       // }
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            if (state == State.PAUSE) {
                pauseOff();
            } else {
                pauseOn();
            }
        }
        if (state == State.GAME_OVER) return false;
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.GAME_OVER) return false;
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.GAME_OVER) return false;
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.GAME_OVER) return false;
        mainShip.touchUp(touch, pointer, button);
        return false;
    }

    private void pauseOn() {
        stateBuff = state;
        state = State.PAUSE;
        music.pause();
    }

    private void pauseOff() {
        state = stateBuff;
        music.play();
    }
}
