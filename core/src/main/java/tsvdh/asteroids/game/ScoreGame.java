package tsvdh.asteroids.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.Map;

public abstract class ScoreGame extends Game {

    protected ScoreManager scoreManager;
    protected String name;

    protected ScoreGame(Map<String, Texture> textures, ScoreManager scoreManager, String name) {
        super(textures);
        this.scoreManager = scoreManager;
        this.name = name;
    }

    @Override
    public void dispose() {
        super.dispose();
        scoreManager.dispose();
    }
}
