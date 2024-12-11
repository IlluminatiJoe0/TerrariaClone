package io.github.illuminatijoe.terrariaclone.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.illuminatijoe.terrariaclone.CollisionChecker;
import io.github.illuminatijoe.terrariaclone.entities.Items;
import io.github.illuminatijoe.terrariaclone.util.OpenSimplex2S;
import io.github.illuminatijoe.terrariaclone.world.tile.Tile;

import java.util.HashMap;

public class Map {
    HashMap<Vector2, Tile> tiles;
    public Items items;
    public CollisionChecker collisionChecker;

    public static int tileSize = 16;
    public static float gravity = 1.1f;

    // Terrain Generation
    public static int worldWidth = 1028;
    public static int worldHeight = 128;
    public static int seed = (int) (Math.random() * 10e6);
    public static int terrainFrequency = 60;
    public static int rangeLimiter = 6;

    public Map() {
        tiles = new HashMap<>();
        init();
        collisionChecker = new CollisionChecker(this);
        items = new Items();
    }

    public void addTile(Vector2 position, Tile tile) {
        if (tiles.containsKey(position)) return;
        tiles.put(position, tile);
    }

    public Tile getTile(Vector2 position) {
        if (!tiles.containsKey(position)) return null;
        return tiles.get(position);
    }

    public void removeTile(Vector2 position) {
        if (!tiles.containsKey(position)) return;
        Tile tile = tiles.get(position);
        tile.destroy();
        tiles.remove(position);
    }

    public void clear() {
        tiles.clear();
    }

    public HashMap<Vector2, Tile> getTiles() {
        return tiles;
    }

    public void init() {
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                // TODO: Implement world gen
                float noise = (OpenSimplex2S.noise2(seed, (double) x / terrainFrequency, 0) / rangeLimiter + 1) / 2 * worldHeight;

                if (y < noise) {
                    Vector2 pos = new Vector2(x, y);
                    Tile tile = new Tile(pos, this);
                    addTile(pos, tile);
                }
            }
        }
    }

    public void draw(SpriteBatch batch, Camera camera) {
        Vector2 camPosition = new Vector2(camera.position.x, camera.position.y);
        Vector2 topLeft = camPosition.cpy().sub(camera.viewportWidth / 2, camera.viewportHeight / 2);
        Vector2 bottomRight = camPosition.cpy().add(camera.viewportWidth / 2, camera.viewportHeight / 2);

        Vector2 topLeftPos = new Vector2(MathUtils.floor(topLeft.x / tileSize), MathUtils.floor(topLeft.y / tileSize));
        Vector2 bottomRightPos = new Vector2(MathUtils.ceil(bottomRight.x / tileSize), MathUtils.ceil(bottomRight.y / tileSize));

        for (int x = (int) topLeftPos.x; x < bottomRightPos.x; x++) {
            for (int y = (int) topLeftPos.y; y < bottomRightPos.y; y++) {
                Vector2 position = new Vector2(x, y);
                if (tiles.containsKey(position)) {
                    try {
                        Tile tile = tiles.get(position);
                        tile.draw(batch, position.cpy().scl(16));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        items.draw(batch);
    }

    public void update(float delta) {
        items.update(delta);
    }

    public void dispose() {
        for (Tile tile : tiles.values()) {
            tile.dispose();
        }

        items.dispose();
    }
}
