package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import tsvdh.asteroids.logic.Ship;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    SpriteBatch spriteBatch;
    FitViewport viewPort;

    Map<String, Texture> textures;

    Ship ship;

    Sprite test;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewPort = new FitViewport(100, 100);
        textures = new HashMap<>();
        loadTextures(Gdx.files.internal("assets"));
        ship = new Ship(textures);
        test = new Sprite(textures.get("assets/ship.png"));
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
    }

    private void logic() {
        ship.logic();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        ship.draw(spriteBatch);

        spriteBatch.end();
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
