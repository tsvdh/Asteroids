package tsvdh.asteroids.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;
import tsvdh.asteroids.util.PersistentDataManager;

import java.util.Collection;
import java.util.Map;

public abstract class Game extends ApplicationAdapter {

    protected Map<String, Texture> textures;
    protected FitViewport viewPort;

    protected SpriteBatch spriteBatch;

    protected PersistentDataManager dataManager;

    public void setTextures(Map<String, Texture> textures) {
        this.textures = textures;
    }

    protected abstract float getWorldSize();
    protected abstract float getCameraSize();

    public Game(Map<String, Texture> textures, PersistentDataManager dataManager) {
        this.textures = textures;
        this.dataManager = dataManager;
    }

    @Override
    public void create() {
        viewPort = new FitViewport(getCameraSize(), getCameraSize());
        spriteBatch = new SpriteBatch();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    protected abstract void input();
    protected abstract void logic();
    protected abstract void draw();

    protected void standardInput(Ship ship, Collection<Laser> shipLasers) {
        if (notGameOver()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                ship.thrust();
            else
                ship.noThrust();

            if (Gdx.input.isKeyPressed(Input.Keys.A))
                ship.rotateCounterClockwise();

            if (Gdx.input.isKeyPressed(Input.Keys.D))
                ship.rotateClockwise();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
                ship.shootLaser(shipLasers, textures);
        }
    }

    protected abstract boolean gameOver();

    protected boolean notGameOver() {
        return !gameOver();
    }

    protected abstract void handleCollisions();

    protected abstract void handleOutOfBounds(GameObject gameObject);
    protected abstract void handleOutOfBounds();

    public abstract boolean gameShouldExit();

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
