package tsvdh.asteroids.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.classic.ClassicGame;
import tsvdh.asteroids.game.tower_defense.TowerDefenseGame;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.util.text_manager.Text;
import tsvdh.asteroids.util.text_manager.RelativeTextManager;

import java.util.Map;

public class MainMenu extends Game {

    private final static float WORLD_SIZE = 2000;
    private final static float CAMERA_SIZE = 2000;

    private final static int SCORE_FOR_TD = 3000;

    private Class<? extends Game> nextGame;

    private RelativeTextManager screenTextManager;

    public MainMenu(Map<String, Texture> textures, PersistentDataManager dataManager) {
        super(textures, dataManager);
    }

    @Override
    public void create() {
        super.create();

        screenTextManager = new RelativeTextManager(spriteBatch, "assets/fonts/PixelOperator.ttf", viewPort);

        screenTextManager.addFont("normal", Color.WHITE, 0.08f);
        screenTextManager.addFont("inactive", Color.DARK_GRAY, 0.08f);
        screenTextManager.addFont("header", Color.WHITE, 0.15f);

        screenTextManager.addText("header", "Asteroids",
                                  new Vector2(0.5f, 0.66f),
                                  "header", Text.AlignMode.CENTERED);

        screenTextManager.addText("classic", String.format("(1) Classic: %s", dataManager.data.classicScore),
                                  new Vector2(0.5f, 0.5f),
                                  "normal", Text.AlignMode.CENTERED);

        boolean towerDefenseUnlocked = dataManager.data.classicScore >= SCORE_FOR_TD;
        String towerDefenseText = towerDefenseUnlocked
            ? String.format("(2) Tower Defense: %s", dataManager.data.towerDefenseScore)
            : String.format("(2) Reach %s to unlock", SCORE_FOR_TD);
        screenTextManager.addText("towerDefense", towerDefenseText,
                                  new Vector2(0.5f, 0.5f - 0.08f),
                                  towerDefenseUnlocked ? "normal" : "inactive", Text.AlignMode.CENTERED);
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
    protected void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            nextGame = ClassicGame.class;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            nextGame = TowerDefenseGame.class;
    }

    @Override
    protected void logic() {

    }

    @Override
    protected void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        screenTextManager.draw();

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return false;
    }

    @Override
    protected void handleCollisions() {}

    @Override
    protected void handleOutOfBounds(GameObject gameObject) {}

    @Override
    protected void handleOutOfBounds() {}

    @Override
    public boolean gameShouldExit() {
        return nextGame != null;
    }

    public Class<? extends Game> getNextGame() {
        return nextGame;
    }
}
