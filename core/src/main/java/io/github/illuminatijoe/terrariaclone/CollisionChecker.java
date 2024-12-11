package io.github.illuminatijoe.terrariaclone;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.illuminatijoe.terrariaclone.entities.AbstractEntity;
import io.github.illuminatijoe.terrariaclone.util.CollisionDirection;
import io.github.illuminatijoe.terrariaclone.world.Map;
import io.github.illuminatijoe.terrariaclone.world.tile.Tile;

import java.util.HashMap;

public class CollisionChecker {
    public Map map;

    public CollisionChecker(Map map) {
        this.map = map;
    }

    public HashMap<Vector2, Tile> getSurroundingTiles(Vector2 position) {
        HashMap<Vector2, Tile> surroundingTiles = new HashMap<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Vector2 tilePos = new Vector2(
                    MathUtils.floor((position.x / 16) + i),
                    MathUtils.floor((position.y / 16) + j)
                );
                Tile tile = map.getTile(tilePos);
                if (tile != null) {
                    surroundingTiles.put(tilePos, tile);
                }
            }
        }

        return surroundingTiles;
    }

    public Array<CollisionDirection> checkCollisionDirection(AbstractEntity entity) {
        Vector2 position = entity.position;
        Vector2 velocity = entity.velocity;
        Rectangle hitbox = entity.hitbox;

        HashMap<Vector2, Tile> surroundingTiles = map.collisionChecker.getSurroundingTiles(position);

        Array<CollisionDirection> collisions = new Array<>();

        for (Tile tile : surroundingTiles.values()) {
            if (tile.isSolid() && tile.getBounds().overlaps(hitbox)) {
                Rectangle tileBounds = tile.getBounds();

                if (hitbox.y < tileBounds.y &&
                    hitbox.y + hitbox.height < tileBounds.y + tileBounds.height &&
                    velocity.y > 0) {
                    collisions.add(CollisionDirection.UP);
                }
                if (hitbox.y > tileBounds.y &&
                    hitbox.y + hitbox.height > tileBounds.y + tileBounds.height &&
                    velocity.y < 0) {
                    collisions.add(CollisionDirection.DOWN);
                }
                if (hitbox.x < tileBounds.x &&
                    hitbox.x + hitbox.width < tileBounds.x + tileBounds.width &&
                    velocity.x > 0) {
                    collisions.add(CollisionDirection.RIGHT);
                }
                if (hitbox.x > tileBounds.x &&
                    hitbox.x + hitbox.width > tileBounds.x + tileBounds.width &&
                    velocity.x < 0) {
                    collisions.add(CollisionDirection.LEFT);
                }
            }
        }

        return collisions;
    }
}
