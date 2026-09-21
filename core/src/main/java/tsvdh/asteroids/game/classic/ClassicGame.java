package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.logic.Alien;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.util.text_manager.Text;
import tsvdh.asteroids.util.text_manager.RelativeTextManager;

import java.time.Duration;
import java.time.Instant;
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
    private Instant gameOverInstant;

    private RelativeTextManager screenTextManager;

    public ClassicGame(Map<String, Texture> textures, PersistentDataManager dataManager) {
        super(textures, dataManager);
    }

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
        super.create();
        ship = new Ship(textures, new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2), Duration.ofMillis(200));
        ship.setForward(new Vector2(0, 1));
        lives = 3;

        screenTextManager = new RelativeTextManager(spriteBatch, "assets/fonts/Connection.ttf", viewPort);

        screenTextManager.addFont("normal", Color.WHITE, 0.05f);
        screenTextManager.addFont("warning", Color.RED, 0.1f);

        float textMargin = 0.02f;
        screenTextManager.addText("lives", String.format("Lives: %s", lives),
                                  new Vector2(textMargin, 1 - textMargin),
                                  "normal", Text.AlignMode.RIGHT_DOWN);
        screenTextManager.addText("score", "Score: 0",
                                  new Vector2(1 - textMargin, 1 - textMargin),
                                  "normal", Text.AlignMode.LEFT_DOWN);
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
    }

    @Override
    protected void handleCollisions() {
        if (gameOver())
            return;

        Collection<Asteroid> newAsteroids = new LinkedList<>();

        asteroids.forEach(asteroid -> {
            if (asteroid.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable())
                destroyShip();

            shipLasers.forEach(laser -> {
                if (asteroid.getCollider().overlaps(laser.getCollider())) {
                    score += asteroid.getScore();
                    screenTextManager.changeText("score", String.format("Score: %s", score));
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

    private float handleOutOfBounds(float val) {
        float boundsToBoundsLength = WORLD_SIZE + 2 * BUFFER_SIZE;
        if (val < -BUFFER_SIZE)
            val += boundsToBoundsLength;
        if (val > WORLD_SIZE + BUFFER_SIZE)
            val -= boundsToBoundsLength;
        return val;
    }

    @Override
    protected void handleOutOfBounds(GameObject gameObject) {
        Vector2 pos = gameObject.getPos();
        pos.x = handleOutOfBounds(pos.x);
        pos.y = handleOutOfBounds(pos.y);
        gameObject.setPos(pos);
    }

    @Override
    protected void handleOutOfBounds() {
        handleOutOfBounds(ship);
        shipLasers.forEach(this::handleOutOfBounds);
        asteroids.forEach(this::handleOutOfBounds);
        aliens.forEach(this::handleOutOfBounds);
        alienLasers.forEach(this::handleOutOfBounds);
    }

    private void destroyShip() {
        lives--;
        screenTextManager.changeText("lives", String.format("Lives: %s", lives));
        ship.destroy();
        ship.setPos(new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));

        if (gameOver()) {
            gameOverInstant = Instant.now();
            screenTextManager.addText("gameOver", "Game over",
                                      new Vector2(0.5f, 0.5f),
                                      "warning", Text.AlignMode.CENTERED);
            dataManager.data.classicScore = Math.max(score, dataManager.data.classicScore);
            dataManager.write();
        }
    }

    @Override
    protected void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        if (notGameOver() && ship.shouldDraw())
            ship.draw(spriteBatch);

        shipLasers.forEach(laser -> laser.draw(spriteBatch));
        asteroids.forEach(asteroid -> asteroid.draw(spriteBatch));
        aliens.forEach(alien -> alien.draw(spriteBatch));
        alienLasers.forEach(laser -> laser.draw(spriteBatch));

        screenTextManager.draw();

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return lives <= 0;
    }

    @Override
    public boolean gameShouldExit() {
        return gameOverInstant != null
            && Duration.between(gameOverInstant, Instant.now()).toMillis() > 5000;
    }

    @Override
    public void dispose() {
        screenTextManager.dispose();
    }
}
