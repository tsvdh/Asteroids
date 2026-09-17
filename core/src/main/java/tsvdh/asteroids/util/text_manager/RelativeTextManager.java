package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    }

    @Override
    public void draw() {
        texts.values().forEach(text -> {
            Vector2 pos = text.pos();
            Vector2 oldPos = pos.cpy();

            Vector3 worldPos = viewport.getCamera().unproject(
                new Vector3(pos.x, viewport.getWorldHeight() - pos.y, 0)
            );
            pos.set(worldPos.x, worldPos.y);
            drawAtWorldSpace(text);

            pos.set(oldPos);
        });
    }
}
