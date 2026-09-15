package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.Game;

import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private Game currentGame;
    private Map<String, Texture> textures;

    public Main() {
        loadTextures(Gdx.files.internal("assets"));
    }

    @Override
    public void create() {
        currentGame.create();
    }

    @Override
    public void render() {
        currentGame.render();
    }

    private void logic() {

    }

    private void draw() {

    }

    private void loadTextures(FileHandle file) {
        for (FileHandle child : file.list()) {
            if (child.isDirectory())
                loadTextures(child);
            else if (child.extension().equals("png"))
                textures.put(child.path(), new Texture(child));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
