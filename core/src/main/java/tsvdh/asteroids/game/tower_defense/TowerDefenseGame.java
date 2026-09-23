package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.game.tower_defense.buildings.AsteroidTurret;
import tsvdh.asteroids.game.tower_defense.buildings.Building;
import tsvdh.asteroids.game.tower_defense.buildings.DefenseTurret;
import tsvdh.asteroids.game.tower_defense.buildings.MineBuilding;
import tsvdh.asteroids.game.tower_defense.buildings.MineLaserManager;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.RectangularGameObject;
import tsvdh.asteroids.logic.Ship;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;
import tsvdh.asteroids.util.text_manager.RelativeTextManager;
import tsvdh.asteroids.util.text_manager.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TowerDefenseGame extends Game {

    private final static float WORLD_SIZE = 2000;

    private final static float[] ZOOM_LEVELS = { 1000, 1500, 2000 };
    private final static int STARTING_ZOOM = 0;
    private static float CAMERA_SIZE = ZOOM_LEVELS[STARTING_ZOOM];
    private int currentZoom;

    private static final int MINE_BUILDING_COST = 1;
    private static final int ASTEROID_TURRET_COST = 1;
    private static final int DEFENSE_TURRET_COST = 1;

    private Ship ship;
    private final Collection<Laser> shipLasers = new LinkedList<>();
    private final Collection<BorderGenerator.Border> borders = new LinkedList<>();
    private final Collection<RectangularGameObject> ironPatches = new LinkedList<>();
    private final Collection<ToughAsteroid> asteroids = new LinkedList<>();
    private AsteroidSpawner asteroidSpawner;
    private final Collection<Laser> turretLasers = new LinkedList<>();
    private final Collection<Alien> aliens = new LinkedList<>();
    private AlienManager alienManager;

    private final Collection<MineBuilding> mineBuildings = new LinkedList<>();
    private final Collection<AsteroidTurret> asteroidTurrets = new LinkedList<>();
    private final Collection<DefenseTurret> defenseTurrets = new LinkedList<>();

    private GameObject worldBackground;
    private GameObject outOfBoundsBackground;
    private BorderGenerator borderGenerator;

    private int score;
    private float iron;
    private Instant gameOverInstant;
    boolean canMine;

    private MineLaserManager mineLaserManager;

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

    private Collection<Building> getAllBuildings() {
        var list = new LinkedList<Building>();
        list.addAll(mineBuildings);
        list.addAll(asteroidTurrets);
        list.addAll(defenseTurrets);
        return list;
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

    private void makeIronPatches() {
        var iron = new RectangularGameObject(textures) {
            @Override
            public String getTextureName() {
                return "assets/iron.png";
            }
        };
        iron.setSize(1000);
        iron.setPos(new Vector2(500, 500));

        ironPatches.add(iron);
    }

    @Override
    public void create() {
        super.create();
        currentZoom = STARTING_ZOOM;
        ship = new Ship(textures, new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2),
                        Duration.ofMillis(500), 5);

        borderGenerator = new BorderGenerator(textures, WORLD_SIZE, 500);
        borders.addAll(borderGenerator.makeBorderParts());
        makeBackground();

        makeIronPatches();

        asteroidSpawner = new AsteroidSpawner(WORLD_SIZE, Duration.ofMillis(2000), asteroids);
        alienManager = new AlienManager(WORLD_SIZE, aliens, (List<AsteroidTurret>) asteroidTurrets, ship);

        mineLaserManager = new MineLaserManager(viewPort);

        worldTextManager = new AbsoluteTextManager(spriteBatch, "assets/fonts/Connection.ttf");
        worldTextManager.addFont("building", Color.BLACK, 20);

        screenTextManager = new RelativeTextManager(spriteBatch, "assets/fonts/Connection.ttf", viewPort);
        screenTextManager.addFont("normal", Color.WHITE, 0.05f);
        screenTextManager.addFont("warning_big", Color.RED, 0.1f);
        float textMargin = 0.02f;
        screenTextManager.addText("lives", String.format("Lives: %s", ship.getHealth()),
                                  new Vector2(textMargin, 1 - textMargin),
                                  "normal", Text.AlignMode.RIGHT_DOWN);
        screenTextManager.addText("iron", "Iron: 0",
                                  new Vector2(textMargin, 1 - textMargin - 0.05f),
                                  "normal", Text.AlignMode.RIGHT_DOWN);
        screenTextManager.addText("score", "Score: 0",
                                  new Vector2(1 - textMargin, 1 - textMargin),
                                  "normal", Text.AlignMode.LEFT_DOWN);
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
            zoomOut();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET))
            zoomIn();

        if (Gdx.input.isKeyPressed(Input.Keys.M) && canMine)
            addIron(ship.mine());

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            buildMineBuilding();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            buildAsteroidTurret();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            buildDefenseTurret();
    }

    @Override
    protected void logic() {
        ship.logic();
        shipLasers.forEach(Laser::logic);

        asteroidSpawner.spawn(textures);
        asteroids.forEach(Asteroid::logic);

        alienManager.spawn(textures, score);
        alienManager.retarget();

        mineBuildings.forEach(mineMachine -> addIron(mineMachine.mine()));
        asteroidTurrets.forEach(AsteroidTurret::logic);
        defenseTurrets.forEach(DefenseTurret::logic);
        turretLasers.forEach(Laser::logic);
        aliens.forEach(Alien::logic);

        handleCollisions();
        handleOutOfBounds();

        asteroids.forEach(asteroid -> {
            if (asteroid.isDestroyed() && asteroid.addsToScore())
                score += asteroid.getScore();
        });
        screenTextManager.changeText("score", String.format("Score: %s", score));

        shipLasers.removeIf(GameObject::isDestroyed);
        asteroids.removeIf(GameObject::isDestroyed);
        mineBuildings.removeIf(GameObject::isDestroyed);
        asteroidTurrets.removeIf(GameObject::isDestroyed);
        defenseTurrets.removeIf(GameObject::isDestroyed);
        turretLasers.removeIf(GameObject::isDestroyed);
        aliens.removeIf(GameObject::isDestroyed);
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

        ironPatches.forEach(iron -> iron.draw(spriteBatch));

        getAllBuildings().forEach(building -> building.draw(spriteBatch));
        turretLasers.forEach(laser -> laser.draw(spriteBatch));
        aliens.forEach(alien -> alien.draw(spriteBatch));

        if (notGameOver())
            ship.draw(spriteBatch);

        shipLasers.forEach(laser -> laser.draw(spriteBatch));
        asteroids.forEach(asteroid -> asteroid.draw(spriteBatch));

        borders.forEach(border -> border.draw(spriteBatch));
        worldTextManager.draw();
        screenTextManager.draw();

        spriteBatch.end();

        mineLaserManager.draw();
    }

    @Override
    protected boolean gameOver() {
        return ship.getHealth() <= 0;
    }

    @Override
    protected void handleCollisions() {
        if (gameOver())
            return;

        canMine = false;
        ironPatches.forEach(patch -> {
            if (patch.getCollider().contains(ship.getCollider()))
                canMine = true;
        });

        asteroids.forEach(asteroid -> {
            if (asteroid.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable())
                damageShip();

            shipLasers.forEach(laser -> {
                if (asteroid.getCollider().overlaps(laser.getCollider())) {
                    asteroid.damage();
                    laser.destroy();
                }
            });

            getAllBuildings().forEach(building -> {
                if (asteroid.getRectangleCollider().overlaps(building.getCollider())) {
                    building.damage();
                    asteroid.damageMax();
                }
            });
        });

        aliens.forEach(alien -> {
            if (alien.getCollider().overlaps(ship.getCollider()) && ship.isVulnerable()) {
                damageShip();
                alien.damageMax();
            }

            turretLasers.forEach(laser -> {
                if (alien.getCollider().overlaps(laser.getCollider())) {
                    alien.damage();
                    laser.destroy();
                }
            });

            shipLasers.forEach(laser -> {
                if (alien.getCollider().overlaps(laser.getCollider())) {
                    alien.damageMax();
                    laser.destroy();
                }
            });

            getAllBuildings().forEach(building -> {
                if (alien.getRectangleCollider().overlaps(building.getCollider())) {
                    building.damage();
                    alien.damageMax();
                }
            });
        });
    }

    private boolean isDimensionOutOfBounds(float val) {
        return 0 > val || val > WORLD_SIZE;
    }

    @Override
    protected void handleOutOfBounds(GameObject gameObject) {
        if (isDimensionOutOfBounds(gameObject.getPos().x)
            || isDimensionOutOfBounds(gameObject.getPos().y))
        {
            if (gameObject instanceof Ship) {
                ship.resetMovement();
                damageShip();
            } else if (gameObject instanceof Damageable)
                ((Damageable) gameObject).damageMax();
            else
                gameObject.destroy();
        }
    }

    @Override
    protected void handleOutOfBounds() {
        handleOutOfBounds(ship);
        asteroids.forEach(this::handleOutOfBounds);
    }

    @Override
    public boolean gameShouldExit() {
        return gameOverInstant != null
            && Duration.between(gameOverInstant, Instant.now()).toSeconds() > 10000;
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

    void addIron(float extraIron) {
        iron += extraIron;
        screenTextManager.changeText("iron", String.format("Iron: %.1f", iron));
    }

    void removeIron(float iron) {
        addIron(-iron);
    }

    private void damageShip() {
        ship.damage();
        screenTextManager.changeText("lives", String.format("Lives: %s", ship.getHealth()));
        ship.setPos(clampToWorld(ship.getPos(), 100));

        if (gameOver()) {
            gameOverInstant = Instant.now();
            screenTextManager.addText("gameOver", "Game over",
                                      new Vector2(0.5f, 0.5f),
                                      "warning_big", Text.AlignMode.CENTERED);
            dataManager.data.towerDefenseScore = Math.max(score, dataManager.data.towerDefenseScore);
            dataManager.write();
        }
    }

    private void buildMineBuilding() {
        if (iron < MINE_BUILDING_COST || !canMine || isSpotTaken(ship.getPos()))
            return;

        removeIron(MINE_BUILDING_COST);
        mineBuildings.add(new MineBuilding(textures, ship.getPos(), worldTextManager));
    }

    private void buildAsteroidTurret() {
        if (iron < ASTEROID_TURRET_COST || isSpotTaken(ship.getPos()))
            return;

        removeIron(ASTEROID_TURRET_COST);
        asteroidTurrets.add(new AsteroidTurret(textures, ship.getPos(), worldTextManager,
                                               asteroids, mineLaserManager));
    }

    private void buildDefenseTurret() {
        if (iron < DEFENSE_TURRET_COST || isSpotTaken(ship.getPos()))
            return;

        removeIron(DEFENSE_TURRET_COST);
        defenseTurrets.add(new DefenseTurret(textures, ship.getPos(), worldTextManager,
                                             aliens, turretLasers));
    }

    private boolean isSpotTaken(Vector2 pos) {
        Point point = Point.fromPos(pos);

        for (Building building : getAllBuildings()) {
            if (building.getPoint().equals(point))
                return true;
        }
        return false;
    }
}
