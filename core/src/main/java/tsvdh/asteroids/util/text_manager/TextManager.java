package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.util.Text;
import tsvdh.asteroids.util.font_manager.FontManager;

import java.util.HashMap;
import java.util.Map;

abstract class TextManager {

    final Batch batch;
    final Map<String, Text> texts = new HashMap<>();

    public TextManager(Batch batch) {
        this.batch = batch;
    }

    abstract FontManager getFontManager();

    public void addText(String textName, String text, Vector2 pos, String fontName, boolean centered) {
        BitmapFont font = getFontManager().getFont(fontName);
        var glyphLayout = new GlyphLayout(font, text);
        texts.put(textName, new Text(glyphLayout, pos, font, centered));
    }

    public void removeText(String textName) {
        texts.remove(textName);
    }

    public void changeText(String textName, String newText) {
        Text text = texts.get(textName);
        text.glyphLayout().setText(text.font(), newText);
    }

    public void changePos(String textName, Vector2 newPos) {
        texts.get(textName).pos().set(newPos);
    }

    public void draw() {
        texts.values().forEach(this::drawAtWorldSpace);
    }

    void drawAtWorldSpace(Text text) {
        Vector2 pos = text.pos();

        if (text.centered()) {
            float originX = pos.x - (text.glyphLayout().width / 2);
            float originY = pos.y + (text.glyphLayout().height / 2);

            text.font().draw(batch, text.glyphLayout(), originX, originY);
        } else {
            text.font().draw(batch, text.glyphLayout(), pos.x, pos.y);
        }
    }

    public void dispose() {
        getFontManager().dispose();
    }
}
