package tsvdh.asteroids.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.FontManager;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;

import java.util.Collection;
import java.util.Map;

public abstract class Game extends ApplicationAdapter {

    protected Map<String, Texture> textures;
    protected FitViewport viewPort;

    protected SpriteBatch spriteBatch;
    protected FontManager screenFontManager;
    protected FontManager worldFontManager;

    public void setTextures(Map<String, Texture> textures) {
        this.textures = textures;
    }

    protected abstract float getWorldSize();
    protected abstract float getCameraSize();

    @Override
    public void create() {
        if (textures == null)
            throw new RuntimeException("'textures' must be set before calling this");
        if (viewPort == null)
            throw new RuntimeException("'viewport' must be set before calling this");

        spriteBatch = new SpriteBatch();
        screenFontManager = new FontManager(getCameraSize());
        screenFontManager.addFont("normal", Color.WHITE, 0.05f);
        screenFontManager.addFont("warning", Color.RED, 0.1f);
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

    protected abstract boolean gameOver();

    protected boolean notGameOver() {
        return !gameOver();
    }

    protected void drawTextWorldSpace(BitmapFont font, GlyphLayout text, float x, float y, boolean centered) {
        if (centered) {
            float originX = x - (text.width / 2);
            float originY = y + (text.height / 2);

            font.draw(spriteBatch, text, originX, originY);
        } else {
            font.draw(spriteBatch, text, x, y);
        }
    }

    protected void drawTextCameraSpace(BitmapFont font, GlyphLayout text, float x, float y, boolean centered) {
        drawTextWorldSpace(font, text,
            x + viewPort.getCamera().position.x,
            y + viewPort.getCamera().position.y,
            centered);
    }

    protected abstract float handleOutOfBounds(float val);

    protected void handleOutOfBounds(GameObject gameObject) {
        Vector2 pos = gameObject.getPos();
        pos.x = handleOutOfBounds(pos.x);
        pos.y = handleOutOfBounds(pos.y);
        gameObject.setPos(pos);
    }
}
