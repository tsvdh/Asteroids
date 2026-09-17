package tsvdh.asteroids.util.font_manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class AbsoluteFontManager extends FontManager {

    public AbsoluteFontManager(String fontTypePath) {
        super(fontTypePath);
    }

    public void addFont(String name, Color color, int size) {
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = size;
        parameters.color = color;
        fonts.put(name, generator.generateFont(parameters));
    }
}
