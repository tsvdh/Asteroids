package tsvdh.asteroids.util.font_manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class RelativeFontManager extends FontManager {

    private final Map<String, Float> drawAreaFractions = new HashMap<>();
    private float drawAreaSize;

    public RelativeFontManager(String fontPath, float drawAreaSize) {
        super(fontPath);
        this.drawAreaSize = drawAreaSize;
    }

    public void addFont(String name, Color color, float drawAreaFraction) {
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = (int) (drawAreaSize * drawAreaFraction);
        parameters.color = color;
        fonts.put(name, generator.generateFont(parameters));
        drawAreaFractions.put(name, drawAreaFraction);
    }

    public void resizeFonts(float newDrawAreaSize) {
        drawAreaSize = newDrawAreaSize;

        fonts.keySet().forEach(name -> {
            BitmapFont oldFont = fonts.get(name);
            var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameters.size = (int) (drawAreaSize * drawAreaFractions.get(name));
            parameters.color = oldFont.getColor();
            fonts.put(name, generator.generateFont(parameters));
        });
    }
}
