package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
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

    public void addText(String textName, String text, Vector2 pos, String fontName, Text.AlignMode mode) {
        BitmapFont font = getFontManager().getFont(fontName);
        var glyphLayout = new GlyphLayout(font, text);
        texts.put(textName, new Text(glyphLayout, pos, fontName, mode, new Text.TextHolder(text)));
    }

    public void removeText(String textName) {
        texts.remove(textName);
    }

    public void changeText(String textName, String newText) {
        Text text = texts.get(textName);
        text.textHolder().text = newText;
        BitmapFont font = getFontManager().getFont(text.fontName());
        text.glyphLayout().setText(font, newText);
    }

    public void changePos(String textName, Vector2 newPos) {
        texts.get(textName).pos().set(newPos);
    }

    public abstract void draw();

    void drawAtWorldSpace(Text text) {
        Vector2 newPos = text.pos().cpy();

        switch (text.mode()) {
            case CENTERED -> {
                newPos.x -= text.glyphLayout().width / 2;
                newPos.y += text.glyphLayout().height / 2;
            }
            case RIGHT_DOWN -> {}
            case LEFT_DOWN -> {
                newPos.x -= text.glyphLayout().width;
            }
            case LEFT_UP -> {
                newPos.x -= text.glyphLayout().width;
                newPos.y += (text.glyphLayout().height / 2);
            }
            case RIGHT_UP -> {
                newPos.y += (text.glyphLayout().height / 2);
            }
        }

        BitmapFont font = getFontManager().getFont(text.fontName());
        font.draw(batch, text.glyphLayout(), newPos.x, newPos.y);
    }

    public void dispose() {
        getFontManager().dispose();
    }
}
