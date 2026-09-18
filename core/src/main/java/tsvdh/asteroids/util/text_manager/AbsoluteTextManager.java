package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.util.Text;
import tsvdh.asteroids.util.font_manager.AbsoluteFontManager;
import tsvdh.asteroids.util.font_manager.FontManager;

public class AbsoluteTextManager extends TextManager {

    private final AbsoluteFontManager fontManager;

    public AbsoluteTextManager(Batch batch, String fontPath) {
        super(batch);
        fontManager = new AbsoluteFontManager(fontPath);
    }

    @Override
    FontManager getFontManager() {
        return fontManager;
    }

    public void addFont(String name, Color color, int size) {
        fontManager.addFont(name, color, size);
    }

    @Override
    public void draw() {
        texts.values().forEach(this::drawAtWorldSpace);
    }
}
