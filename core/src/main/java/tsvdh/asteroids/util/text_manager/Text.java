package tsvdh.asteroids.util.text_manager;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public record Text(GlyphLayout glyphLayout, Vector2 pos, String fontName, AlignMode mode, TextHolder textHolder) {

    public enum AlignMode {
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN,
        CENTERED
    }

    public static class TextHolder {
        public String text;
        public TextHolder(String text) {
            this.text = text;
        }
    }
}


