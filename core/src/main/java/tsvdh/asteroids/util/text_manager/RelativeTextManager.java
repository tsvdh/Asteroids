package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import tsvdh.asteroids.util.Text;
import tsvdh.asteroids.util.font_manager.FontManager;
import tsvdh.asteroids.util.font_manager.RelativeFontManager;

public class RelativeTextManager extends TextManager {

    private final RelativeFontManager fontManager;
    private final Viewport viewport;

    public RelativeTextManager(Batch batch, String fontPath, Viewport viewport) {
        super(batch);
        fontManager =  new RelativeFontManager(fontPath, viewport.getWorldHeight());
        this.viewport = viewport;
    }

    @Override
    FontManager getFontManager() {
        return fontManager;
    }

    public void addFont(String name, Color color, float drawAreaFraction) {
        fontManager.addFont(name, color, drawAreaFraction);
    }

    public void resizeFonts() {
        fontManager.resizeFonts(viewport.getWorldHeight());
        texts.values().forEach(text -> {
            BitmapFont font = fontManager.getFont(text.fontName());
            text.glyphLayout().setText(font, text.text());
        });
    }

    @Override
    public void draw() {
        texts.values().forEach(this::drawAtScreenSpace);
    }

    void drawAtScreenSpace(Text text) {
        Vector2 pos = text.pos();
        Vector2 oldPos = text.pos().cpy();

        float cameraSize = viewport.getWorldHeight();

        pos.x = viewport.getCamera().position.x - cameraSize / 2 + pos.x * cameraSize;
        pos.y = viewport.getCamera().position.y - cameraSize / 2 + pos.y * cameraSize;

        drawAtWorldSpace(text);

        pos.set(oldPos);
    }
}
