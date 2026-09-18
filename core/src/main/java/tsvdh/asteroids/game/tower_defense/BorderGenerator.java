package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.GameObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class BorderGenerator {

    private final Map<String, Texture> textures;
    private final float worldSize;
    private final float partSize;

    BorderGenerator(Map<String, Texture> textures, float worldSize, float partSize) {
        this.textures = textures;
        this.worldSize = worldSize;
        this.partSize = partSize;
    }

    Collection<Border> makeBorderParts() {
        var borderParts = new LinkedList<Border>();

        int numPartsPerSide = (int) (worldSize / partSize);
        float startOffset = partSize / 2;

        for (int xModifier = 0; xModifier < 2; xModifier++) {
            for (int y = 0; y < numPartsPerSide; y++) {
                var pos = new Vector2(worldSize * xModifier, startOffset + partSize * y);
                var forward = xModifier == 0 ? new Vector2(1, 0) : new Vector2(-1, 0);
                borderParts.add(new Border(textures, pos, forward, partSize, y % 2 == 0));
            }
        }
        for (int yModifier = 0; yModifier < 2; yModifier++) {
            for (int x = 0; x < numPartsPerSide; x++) {
                var pos = new Vector2(startOffset + partSize * x, worldSize * yModifier);
                var forward = yModifier == 0 ? new Vector2(0, 1) : new Vector2(0, -1);
                borderParts.add(new Border(textures, pos, forward, partSize, x % 2 == 0));
            }
        }

        return borderParts;
    }

    static class Border extends GameObject {

        public Border(Map<String, Texture> textures, Vector2 pos, Vector2 forward, float size, boolean flipped) {
            super(textures);
            setPos(pos);
            setSize(size);
            if (flipped)
                getSprite().flip(true, false);
            setForward(forward);
        }

        @Override
        public String getTextureName() {
            return "assets/borders/border2.png";
        }
    }
}
