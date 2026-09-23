package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.Damageable;
import tsvdh.asteroids.game.tower_defense.Point;
import tsvdh.asteroids.logic.RectangularGameObject;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;
import tsvdh.asteroids.util.text_manager.Text;

import java.util.Map;

public abstract class Building extends RectangularGameObject implements Damageable {

    static int NEW_ID = 0;

    Point point;
    private int health;
    private AbsoluteTextManager textManager;
    private int id;
    Texture detailTexture;
    Sprite detailSprite;

    protected Building(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager) {
        super(textures);
        setSize(90);
        point = Point.fromPos(pos);
        setPos(point.toPos());
        health = getInitialHealth();
        id = NEW_ID++;
        this.textManager = textManager;
        textManager.addText(getTextId(), String.valueOf(health),
                            getPos().cpy().add(new Vector2(40, 40)), "building", Text.AlignMode.LEFT_DOWN);
        detailTexture = textures.get(getDetailTextureName());
        detailSprite = makeSprite(detailTexture);
        detailSprite.setScale(100);
        detailSprite.setCenter(getPos().x, getPos().y);
    }

    @Override
    public void setPos(Vector2 newPos) {
        point = Point.fromPos(newPos);
        super.setPos(point.toPos());
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void damage() {
        health--;
        textManager.changeText(getTextId(), String.valueOf(health));
        if (health == 0) {
            textManager.removeText(getTextId());
            super.destroy();
        }
    }

    @Override
    public void damageMax() {
        while (health > 0)
            damage();
    }

    public Point getPoint() {
        return point;
    }

    protected abstract int getInitialHealth();

    protected String getTextId() {
        return String.format("building-%s", id);
    }

    protected abstract String getDetailTextureName();

    public Sprite getDetailSprite() {
        return detailSprite;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        detailSprite.draw(batch);
    }
}
