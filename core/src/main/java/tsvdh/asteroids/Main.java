package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.AsteroidSpawner;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    SpriteBatch spriteBatch;
    FitViewport viewPort;

    Map<String, Texture> textures;

    Ship ship;
    Collection<Laser> shipLasers;
    AsteroidSpawner asteroidSpawner;
    Collection<Asteroid> asteroids;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewPort = new FitViewport(100, 100);
        textures = new HashMap<>();
        loadTextures(Gdx.files.internal("assets"));
        ship = new Ship(textures);
        shipLasers = new LinkedList<>();
        asteroidSpawner = new AsteroidSpawner();
        asteroids = new LinkedList<>();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    private void input() {
        if (ship.notGameOver()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                ship.thrust();
            } else {
                ship.noThrust();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                ship.rotateCounterClockwise();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                ship.rotateClockwise();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                shipLasers.add(ship.shootLaser(textures));
            }
        }
    }

    private void logic() {
        ship.logic();
        shipLasers.forEach(Laser::logic);
        asteroidSpawner.spawn(asteroids, textures);
        asteroids.forEach(Asteroid::logic);

        handleCollisions();

        shipLasers.removeIf(GameObject::isDestroyed);
        asteroids.removeIf(GameObject::isDestroyed);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        if (ship.notGameOver())
            ship.draw(spriteBatch);
        shipLasers.forEach(laser ->laser.draw(spriteBatch));
        asteroids.forEach(asteroid -> asteroid.draw(spriteBatch));

        spriteBatch.end();
    }

    private void handleCollisions() {
        asteroids.forEach(asteroid -> {
            if (ship.notGameOver() && asteroid.getCollider().overlaps(ship.getCollider()))
                ship.destroy();

            shipLasers.forEach(laser -> {
                if (asteroid.getCollider().overlaps(laser.getCollider())) {
                    asteroid.destroy();
                    laser.destroy();
                }
            });
        });
    }

    private void loadTextures(FileHandle file) {
        for (FileHandle child : file.list()) {
            if (child.isDirectory())
                loadTextures(child);
            else if (child.extension().equals("png"))
                textures.put(child.path(), new Texture(child));
        }
    }
}
