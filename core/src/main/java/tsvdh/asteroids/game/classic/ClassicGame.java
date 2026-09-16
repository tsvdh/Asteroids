package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.logic.Alien;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class ClassicGame extends Game {

    private static final float WORLD_SIZE = 1000;
    private static final float BUFFER_SIZE = 50;
    private static final float CAMERA_SIZE = 1000;

    private Ship ship;
    private final Collection<Laser> shipLasers = new LinkedList<>();
    private final AsteroidSpawner asteroidSpawner = new AsteroidSpawner(WORLD_SIZE, BUFFER_SIZE);
    private final Collection<Asteroid> asteroids = new LinkedList<>();
    private final AlienSpawner alienSpawner = new AlienSpawner(WORLD_SIZE, BUFFER_SIZE);
    private final Collection<Alien> aliens = new LinkedList<>();
    private final Collection<Laser> alienLasers = new LinkedList<>();

    private int lives;
    private int score;

    private GlyphLayout gameOverText;
    private GlyphLayout livesText;
    private GlyphLayout scoreText;

    @Override
    protected float getWorldSize() {
        return WORLD_SIZE;
    }

    @Override
    protected float getCameraSize() {
        return CAMERA_SIZE;
    }

    @Override
    public void create() {
        viewPort = new FitViewport(CAMERA_SIZE, CAMERA_SIZE);
        super.create();
        ship = new Ship(textures, new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));
        lives = 3;
        gameOverText = new GlyphLayout(screenFontManager.getFont("warning"), "Game over");
        livesText = new GlyphLayout(screenFontManager.getFont("normal"), "");
        scoreText = new GlyphLayout(screenFontManager.getFont("normal"), "");
    }

    @Override
    protected void input() {
        standardInput(ship, shipLasers);
    }

    @Override
    protected void logic() {
        ship.logic();
        shipLasers.forEach(Laser::logic);

        asteroidSpawner.spawn(asteroids, textures);
        asteroids.forEach(Asteroid::logic);

        alienSpawner.spawn(aliens, textures);
        aliens.forEach(alien -> {
            alien.logic();
            alien.shootLaser(textures, ship)
                .ifPresent(alienLasers::add);
        });
        alienLasers.forEach(Laser::logic);

        handleCollisions();
        handleOutOfBounds();

        shipLasers.removeIf(GameObject::isDestroyed);
        asteroids.removeIf(GameObject::isDestroyed);
        alienLasers.removeIf(GameObject::isDestroyed);
        aliens.removeIf(GameObject::isDestroyed);

        livesText.setText(screenFontManager.getFont("normal"), String.format("Lives: %s", lives));
        scoreText.setText(screenFontManager.getFont("normal"), String.format("Score: %s", score));
    }

    private void handleCollisions() {
        if (gameOver())
            return;

        Collection<Asteroid> newAsteroids = new LinkedList<>();

        asteroids.forEach(asteroid -> {
            if (asteroid.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable())
                destroyShip();

            shipLasers.forEach(laser -> {
                if (asteroid.getCollider().overlaps(laser.getCollider())) {
                    score += asteroid.getScore();
                    asteroid.destroy();
                    laser.destroy();
                    asteroidSpawner.spawnFromDestroyed(asteroid, asteroids, newAsteroids, textures);
                }
            });

            aliens.forEach(alien -> {
                if (asteroid.getCollider().overlaps(alien.getCollider())) {
                    asteroid.destroy();
                    asteroidSpawner.spawnFromDestroyed(asteroid, asteroids, newAsteroids, textures);
                }
            });
        });

        shipLasers.forEach(shipLaser -> {
            alienLasers.forEach(alienLaser -> {
                if (shipLaser.getCollider().overlaps(alienLaser.getCollider())) {
                    shipLaser.destroy();
                    alienLaser.destroy();
                }
            });
            aliens.forEach(alien -> {
                if (shipLaser.getCollider().overlaps(alien.getCollider())) {
                    alien.destroy();
                    shipLaser.destroy();
                }
            });
        });
        alienLasers.forEach(alienLaser -> {
            if (alienLaser.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable())
                destroyShip();
        });
        aliens.forEach(alien -> {
            if (alien.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable())
                destroyShip();
        });

        asteroids.addAll(newAsteroids);
    }

    @Override
    protected float handleOutOfBounds(float val) {
        float boundsToBoundsLength = WORLD_SIZE + 2 * BUFFER_SIZE;
        if (val < -BUFFER_SIZE)
            val += boundsToBoundsLength;
        if (val > WORLD_SIZE + BUFFER_SIZE)
            val -= boundsToBoundsLength;
        return val;
    }

    private void handleOutOfBounds() {
        handleOutOfBounds(ship);
        shipLasers.forEach(this::handleOutOfBounds);
        asteroids.forEach(this::handleOutOfBounds);
        aliens.forEach(this::handleOutOfBounds);
        alienLasers.forEach(this::handleOutOfBounds);
    }

    private void destroyShip() {
        lives--;
        ship.destroy();
        ship.setPos(new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));
    }

    @Override
    protected void draw() {
        ScreenUtils.clear(Color.BLACK);

        // viewPort.getCamera().position.set(ship.getPos(), 0);
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        if (notGameOver() && ship.shouldDraw())
            ship.draw(spriteBatch);

        shipLasers.forEach(laser -> laser.draw(spriteBatch));
        asteroids.forEach(asteroid -> asteroid.draw(spriteBatch));
        aliens.forEach(alien -> alien.draw(spriteBatch));
        alienLasers.forEach(laser -> laser.draw(spriteBatch));

        if (gameOver())
            drawTextCameraSpace(screenFontManager.getFont("warning"), gameOverText, 0, 0, true);

        float textMargin = CAMERA_SIZE / 50;
        drawTextCameraSpace(screenFontManager.getFont("normal"), livesText,
            textMargin,
            CAMERA_SIZE - textMargin,
            false);
        drawTextCameraSpace(screenFontManager.getFont("normal"), scoreText,
            CAMERA_SIZE - textMargin - scoreText.width,
            CAMERA_SIZE - textMargin,
            false);

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return lives <= 0;
    }
}
