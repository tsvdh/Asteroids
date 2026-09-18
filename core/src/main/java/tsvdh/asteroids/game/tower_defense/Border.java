package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.logic.GameObject;

import java.util.Map;

public class Border extends GameObject {

    public Border(Map<String, Texture> textures) {
        super(textures);
    }

    @Override
    public String getTextureName() {
        return "assets/borders/border1.png";
    }
}
