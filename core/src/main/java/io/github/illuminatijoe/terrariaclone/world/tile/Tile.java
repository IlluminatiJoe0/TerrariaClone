package io.github.illuminatijoe.terrariaclone.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.illuminatijoe.terrariaclone.Assets;
import io.github.illuminatijoe.terrariaclone.entities.Item;
import io.github.illuminatijoe.terrariaclone.entities.Items;
import io.github.illuminatijoe.terrariaclone.world.Map;

public class Tile {
    public Vector2 position;
    public Rectangle bounds;
    public Texture texture;
    public Map map;

    public boolean solid;

    public Tile(Vector2 position, Map map) {
        texture = Assets.dirt;

        this.position = position;
        this.map = map;

        this.solid = true;

        Vector2 pos = position.cpy().scl(16);
        bounds = new Rectangle((int)pos.x, (int)pos.y, texture.getWidth(), texture.getHeight());
    }

    public Texture getTexture() {
        return texture;
    }

    public void draw(SpriteBatch batch, Vector2 position) {
        batch.draw(texture, position.x, position.y);
    }

    public boolean isSolid() {
        return solid;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void destroy() {
        dropItem(map.items, new Item(new Vector2(
            position.x * Map.tileSize + 4,
            position.y * Map.tileSize + 4), map));
    }

    public void place() {

    }

    public void dropItem(Items items, Item item) {
        items.addItem(item);
    }

    public void dispose() {
        texture.dispose();
    }
}
