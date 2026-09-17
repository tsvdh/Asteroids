package tsvdh.asteroids.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public record Text(GlyphLayout glyphLayout, Vector2 pos, BitmapFont font, AlignMode mode) {

    public enum AlignMode {
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN,
        CENTERED
    }
}


