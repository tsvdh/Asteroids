package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.game.classic.ClassicGame;
import tsvdh.asteroids.game.Game;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private Game currentGame;
    private Map<String, Texture> textures = new HashMap<>();

    public Main() {
        currentGame = new ClassicGame();
    }

    @Override
    public void create() {
        loadTextures(Gdx.files.internal("assets"));
        currentGame.setTextures(textures);
        currentGame.create();
    }

    @Override
    public void resize(int width, int height) {
        currentGame.resize(width, height);
    }

    @Override
    public void render() {
        currentGame.render();
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
