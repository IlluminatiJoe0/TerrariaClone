package io.github.illuminatijoe.terrariaclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture player = new Texture(Gdx.files.internal("player.png"));
    public static Texture dirt = new Texture((Gdx.files.internal("dirtBlock.png")));

    public static void dispose() {
        player.dispose();
        dirt.dispose();
    }
}
