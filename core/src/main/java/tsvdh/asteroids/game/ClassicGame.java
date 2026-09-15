package tsvdh.asteroids.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.logic.Alien;
import tsvdh.asteroids.logic.AlienSpawner;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.AsteroidSpawner;
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
    private Collection<Laser> shipLasers = new LinkedList<>();
    private AsteroidSpawner asteroidSpawner = new AsteroidSpawner();
    private Collection<Asteroid> asteroids = new LinkedList<>();
    private AlienSpawner alienSpawner = new AlienSpawner();
    private Collection<Alien> aliens = new LinkedList<>();
    private Collection<Laser> alienLasers = new LinkedList<>();

    private int lives;
    private int score;

    private GlyphLayout gameOverText;
    private GlyphLayout livesText;
    private GlyphLayout scoreText;

    public ClassicGame(Map<String, Texture> textures) {
        super(textures);
    }

    @Override
    public void create() {
        viewPort = new FitViewport(WORLD_SIZE, WORLD_SIZE);
        super.create();
        lives = 3;
        gameOverText = new GlyphLayout(screenMessageFont, "Game over");
        livesText = new GlyphLayout(screenNormalFont, "");
        scoreText = new GlyphLayout(screenNormalFont, "");
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
                .ifPresent(laser -> alienLasers.add(laser));
        });
        alienLasers.forEach(Laser::logic);

        handleCollisions();

        shipLasers.removeIf(GameObject::isDestroyed);
        asteroids.removeIf(GameObject::isDestroyed);
        alienLasers.removeIf(GameObject::isDestroyed);
        aliens.removeIf(GameObject::isDestroyed);

        livesText.setText(screenNormalFont, String.format("Lives: %s", lives));
        scoreText.setText(screenNormalFont, String.format("Score: %s", score));
    }

    private void handleCollisions() {
        if (gameOver())
            return;

        Collection<Asteroid> newAsteroids = new LinkedList<>();

        asteroids.forEach(asteroid -> {
            if (asteroid.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable()) {
                lives--;
                ship.destroy();
            }

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
            if (alienLaser.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable()) {
                lives--;
                ship.destroy();
            }
        });
        aliens.forEach(alien -> {
            if (alien.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable()) {
                lives--;
                ship.destroy();
            }
        });

        asteroids.addAll(newAsteroids);
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

        spriteBatch.set

        if (gameOver())
            drawText(screenMessageFont, gameOverText, WORLD_SIZE / 2, WORLD_SIZE / 2, true);

        float textMargin = WORLD_SIZE / 50;
        drawText(screenNormalFont, livesText,
            textMargin,
            WORLD_SIZE - textMargin,
            false);
        drawText(screenNormalFont, scoreText,
            WORLD_SIZE - textMargin - scoreText.width,
            WORLD_SIZE - textMargin,
            false);

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return lives <= 0;
    }
}
