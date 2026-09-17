package tsvdh.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.game.MainMenu;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.game.classic.ClassicGame;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.game.tower_defense.TowerDefenseGame;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private Game currentGame;
    private final Map<String, Texture> textures = new HashMap<>();
    private PersistentDataManager dataManager;

    @Override
    public void create() {
        loadTextures(Gdx.files.internal("assets"));
        dataManager = new PersistentDataManager("scores.json");
        currentGame = new MainMenu(textures, dataManager);
        currentGame.create();
    }

    @Override
    public void resize(int width, int height) {
        currentGame.resize(width, height);
    }

    @Override
    public void render() {
        if (currentGame == null) {
            currentGame = new MainMenu(textures, dataManager);
            currentGame.create();
        }

        currentGame.render();

        if (!currentGame.gameShouldExit())
            return;

        currentGame.dispose();

        if (currentGame instanceof MainMenu) {
            var nextGame = ((MainMenu) currentGame).getNextGame();

            if (nextGame == ClassicGame.class) {
                currentGame = new ClassicGame(textures, dataManager);
            }
            else if (nextGame == TowerDefenseGame.class) {
                currentGame = new TowerDefenseGame(textures, dataManager);
            }
            else throw new RuntimeException("Illegal next game");
        }
        else
            currentGame = new MainMenu(textures, dataManager);

        currentGame.create();
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
