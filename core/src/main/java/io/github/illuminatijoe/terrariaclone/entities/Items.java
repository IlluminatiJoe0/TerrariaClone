package io.github.illuminatijoe.terrariaclone.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Items {
    public Array<Item> items;

    public Items() {
        items = new Array<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.removeValue(item, true);
    }

    public Array<Item> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public void draw(SpriteBatch batch) {
        for (Item item : items) {
            item.draw(batch);
        }
    }

    public void update(float delta) {
        for (Item item : items) {
            item.update(delta);
        }
    }

    public void dispose() {
        for (Item item : items) {
            item.dispose();
        }
    }
}
