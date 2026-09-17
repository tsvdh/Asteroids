package tsvdh.asteroids.util.font_manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public abstract class FontManager {

    final FreeTypeFontGenerator generator;
    final Map<String, BitmapFont> fonts = new HashMap<>();

    FontManager(String fontTypePath) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal(fontTypePath));
    }

    public BitmapFont getFont(String name) {
        return fonts.get(name);
    }

    public void dispose() {
        generator.dispose();
        fonts.values().forEach(BitmapFont::dispose);
    }
}
