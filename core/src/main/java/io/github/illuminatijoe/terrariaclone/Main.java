package io.github.illuminatijoe.terrariaclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.illuminatijoe.terrariaclone.entities.Player;
import io.github.illuminatijoe.terrariaclone.world.Map;

public class Main extends ApplicationAdapter {
    public SpriteBatch batch;
    public float delta;
    public Player player;
    public Map map;
    public CollisionChecker collisionChecker;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Map();
        collisionChecker = new CollisionChecker(map);
        player = new Player(map);
    }

    @Override
    public void render() {
        // Process
        delta = Gdx.graphics.getDeltaTime();

        player.update(delta);
        map.update(delta);

        batch.setProjectionMatrix(player.getCamera().combined);

        // Draw
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        map.draw(batch, player.camera);
        player.draw(batch);

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        player.getCamera().viewportWidth = width;
        player.getCamera().viewportHeight = height;
        player.getCamera().update();
    }

    @Override
    public void dispose() {
        map.dispose();
        batch.dispose();
        Assets.dispose();
    }
}
