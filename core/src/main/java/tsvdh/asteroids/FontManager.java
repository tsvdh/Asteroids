package tsvdh.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontManager {

    private final FreeTypeFontGenerator generator;
    private final Map<String, BitmapFont> fonts = new HashMap<>();
    private final Map<String, Float> cameraFractions = new HashMap<>();
    private float cameraSize;

    public FontManager(float cameraSize) {
        this.cameraSize = cameraSize;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Connection.ttf"));
    }

    public void addFont(String name, Color color, float cameraFraction) {
        cameraFractions.put(name, cameraFraction);

        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = (int) (cameraSize * cameraFraction);
        parameters.color = color;
        fonts.put(name, generator.generateFont(parameters));
    }

    public void resizeFonts(float newCameraSize) {
        cameraSize = newCameraSize;

        fonts.keySet().forEach(name -> {
            BitmapFont oldFont = fonts.get(name);
            var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameters.size = (int) (cameraSize * cameraFractions.get(name));
            parameters.color = oldFont.getColor();
            fonts.put(name, generator.generateFont(parameters));
        });
    }

    public BitmapFont getFont(String name) {
        return fonts.get(name);
    }
}
