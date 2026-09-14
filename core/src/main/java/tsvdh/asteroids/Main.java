package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    public static float WORLD_SIZE = 1000;
    public static float BUFFER_SIZE = 50;

    SpriteBatch spriteBatch;
    FitViewport viewPort;

    Map<String, Texture> textures;

    Ship ship;
    Collection<Laser> shipLasers;
    AsteroidSpawner asteroidSpawner;
    Collection<Asteroid> asteroids;

    int lives;
    int score;

    BitmapFont normalFont;
    BitmapFont messageFont;
    GlyphLayout gameOverText;
    GlyphLayout livesText;
    GlyphLayout scoreText;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewPort = new FitViewport(WORLD_SIZE, WORLD_SIZE);
        textures = new HashMap<>();
        loadTextures(Gdx.files.internal("assets"));
        ship = new Ship(textures);
        shipLasers = new LinkedList<>();
        asteroidSpawner = new AsteroidSpawner();
        asteroids = new LinkedList<>();
        lives = 3;
        setNormalFont();
        setMessageFont();
        gameOverText = new GlyphLayout(messageFont, "Game over");
        livesText = new GlyphLayout(normalFont, "");
        scoreText = new GlyphLayout(normalFont, "");
    }

    private void setNormalFont() {
        var generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Connection.ttf"));
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = (int) WORLD_SIZE / 20;
        normalFont = generator.generateFont(parameters);
        generator.dispose();
    }

    private void setMessageFont() {
        var generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Connection.ttf"));
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = (int) WORLD_SIZE / 10;
        parameters.color = Color.RED;
        messageFont = generator.generateFont(parameters);
        generator.dispose();
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
        if (notGameOver()) {
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

        livesText.setText(normalFont, String.format("Lives: %s", lives));
        scoreText.setText(normalFont, String.format("Score: %s", score));
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        if (notGameOver())
            ship.draw(spriteBatch);

        shipLasers.forEach(laser ->laser.draw(spriteBatch));
        asteroids.forEach(asteroid -> asteroid.draw(spriteBatch));

        if (!notGameOver())
            drawText(messageFont, gameOverText, WORLD_SIZE / 2, WORLD_SIZE / 2, true);

        float textMargin = WORLD_SIZE / 50;
        drawText(normalFont, livesText,
            textMargin,
            WORLD_SIZE - textMargin,
            false);
        drawText(normalFont, scoreText,
            WORLD_SIZE - textMargin - scoreText.width,
            WORLD_SIZE - textMargin,
            false);

        spriteBatch.end();
    }

    private void drawText(BitmapFont font, GlyphLayout text, float x, float y, boolean centered) {
        if (centered) {
            float originX = x - (text.width / 2);
            float originY = y + (text.height / 2);
            font.draw(spriteBatch, text, originX, originY);
        } else {
            font.draw(spriteBatch, text, x, y);
        }
    }

    private void handleCollisions() {
        asteroids.forEach(asteroid -> {
            if (notGameOver() && asteroid.getCollider().overlaps(ship.getCollider())) {
                lives--;
                ship.destroy();
            }

            shipLasers.forEach(laser -> {
                if (asteroid.getCollider().overlaps(laser.getCollider())) {
                    score += 10;
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

    boolean notGameOver() {
        return lives > 0;
    }
}
