package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.RoundGameObject;
import tsvdh.asteroids.logic.Ship;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.util.text_manager.Text;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;
import tsvdh.asteroids.util.text_manager.RelativeTextManager;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class TowerDefenseGame extends Game {

    private final static float WORLD_SIZE = 2000;

    private final static float[] ZOOM_LEVELS = { 1000, 1500, 2000 };
    private final static int STARTING_ZOOM = 0;
    private static float CAMERA_SIZE = ZOOM_LEVELS[STARTING_ZOOM];
    private int currentZoom;

    private Ship ship;
    private final Collection<Laser> shipLasers = new LinkedList<>();
    private final Collection<BorderGenerator.Border> borders = new LinkedList<>();
    private final Collection<RoundGameObject> ironPatches = new LinkedList<>();

    private GameObject worldBackground;
    private GameObject outOfBoundsBackground;
    private BorderGenerator borderGenerator;

    private int lives;
    private int score;
    private int iron;
    private Instant gameOverInstant;

    private AbsoluteTextManager worldTextManager;
    private RelativeTextManager screenTextManager;

    public TowerDefenseGame(Map<String, Texture> textures, PersistentDataManager dataManager) {
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

    private void makeBackground() {
        worldBackground = new GameObject(textures) {
            @Override
            public String getTextureName() {
                return "assets/backgrounds/background_black.png";
            }
        };
        worldBackground.setSize(WORLD_SIZE);
        worldBackground.setPos(new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));

        outOfBoundsBackground = new GameObject(textures) {
            @Override
            public String getTextureName() {
                return "assets/backgrounds/background_gray.png";
            }
        };
        outOfBoundsBackground.setSize(WORLD_SIZE * 2);
        outOfBoundsBackground.setPos(new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));
    }

    private void makeOrePatches() {
        Random rng = new Random(0);
        
    }

    @Override
    public void create() {
        super.create();
        currentZoom = STARTING_ZOOM;
        ship = new Ship(textures, new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));
        lives = 5;

        borderGenerator = new BorderGenerator(textures, WORLD_SIZE, 500);
        borders.addAll(borderGenerator.makeBorderParts());
        makeBackground();

        makeOrePatches();

        worldTextManager = new AbsoluteTextManager(spriteBatch, "assets/fonts/Connection.ttf");
        worldTextManager.addFont("normal", Color.WHITE, 30);
        // worldTextManager.addText("test", "Hello world!",
        //                          new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2),
        //                          "normal", Text.AlignMode.CENTERED);

        screenTextManager = new RelativeTextManager(spriteBatch, "assets/fonts/Connection.ttf", viewPort);
        screenTextManager.addFont("normal", Color.WHITE, 0.05f);
        float textMargin = 0.02f;
        screenTextManager.addText("lives", String.format("Lives: %s", lives),
                                  new Vector2(textMargin, 1 - textMargin),
                                  "normal", Text.AlignMode.RIGHT_DOWN);
    }

    private void zoomIn() {
        changeZoom(Math.max(0, currentZoom - 1));
    }

    private void zoomOut() {
        changeZoom(Math.min(ZOOM_LEVELS.length - 1, currentZoom + 1));
    }

    private void changeZoom(int zoomLevel) {
        currentZoom = zoomLevel;
        CAMERA_SIZE = ZOOM_LEVELS[currentZoom];

        viewPort.setWorldSize(CAMERA_SIZE, CAMERA_SIZE);
        screenTextManager.resizeFonts();
    }

    @Override
    protected void input() {
        standardInput(ship, shipLasers);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET))
            zoomIn();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET))
            zoomOut();
    }

    @Override
    protected void logic() {
        ship.logic();

        handleCollisions();
        handleOutOfBounds();
    }

    @Override
    protected void draw() {
        ScreenUtils.clear(Color.BLACK);

        Vector2 cameraPos = clampToWorld(ship.getPos(), (CAMERA_SIZE / 2) - 100);
        viewPort.getCamera().position.set(new Vector3(cameraPos, 0));
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        outOfBoundsBackground.draw(spriteBatch);
        worldBackground.draw(spriteBatch);

        ship.draw(spriteBatch);

        borders.forEach(border -> border.draw(spriteBatch));
        worldTextManager.draw();
        screenTextManager.draw();

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return false;
    }

    @Override
    protected void handleCollisions() {

    }

    @Override
    protected void handleOutOfBounds(GameObject gameObject) {

    }

    @Override
    protected void handleOutOfBounds() {

    }

    @Override
    public boolean gameShouldExit() {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        worldTextManager.dispose();
    }

    private Vector2 clampToWorld(Vector2 pos, float margin) {
        Vector2 clamped = new Vector2();
        clamped.x = Math.clamp(pos.x, margin, WORLD_SIZE - margin);
        clamped.y = Math.clamp(pos.y, margin, WORLD_SIZE - margin);
        return clamped;
    }
}
