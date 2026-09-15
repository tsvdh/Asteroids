package tsvdh.asteroids.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;

import java.util.Collection;
import java.util.Map;

public abstract class Game extends ApplicationAdapter {

     SpriteBatch spriteBatch = new SpriteBatch();

     Map<String, Texture> textures;

     FitViewport viewPort;

     BitmapFont screenNormalFont;
     BitmapFont screenMessageFont;

    public Game(Map<String, Texture> textures) {
        this.textures = textures;
    }

    @Override
    public void create() {
        if (viewPort == null)
            throw new RuntimeException("'viewport' must be set before calling this");
        setScreenNormalFont();
        setScreenMessageFont();
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

     abstract void input();
     abstract void logic();
     abstract void draw();

     void standardInput(Ship ship, Collection<Laser> shipLasers) {
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

     abstract boolean gameOver();

     boolean notGameOver() {
        return !gameOver();
    }

    private void setScreenNormalFont() {
        var generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Connection.ttf"));
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = viewPort.getScreenHeight() / 20;
        screenNormalFont = generator.generateFont(parameters);
        generator.dispose();
    }

    private void setScreenMessageFont() {
        var generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Connection.ttf"));
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = viewPort.getScreenHeight() / 10;
        parameters.color = Color.RED;
        screenMessageFont = generator.generateFont(parameters);
        generator.dispose();
    }

    void drawText(BitmapFont font, GlyphLayout text, float x, float y, boolean centered) {
        if (centered) {
            float originX = x - (text.width / 2);
            float originY = y + (text.height / 2);

            font.draw(spriteBatch, text, originX, originY);
        } else {
            font.draw(spriteBatch, text, x, y);
        }
    }
}
