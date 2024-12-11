package io.github.illuminatijoe.terrariaclone.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.illuminatijoe.terrariaclone.Assets;
import io.github.illuminatijoe.terrariaclone.util.CollisionDirection;
import io.github.illuminatijoe.terrariaclone.world.Map;

public class Item extends AbstractEntity {
    public Texture texture;
    public String name;
    public String description;
    public int amount;
    public boolean isOnGround = false;
    public Map map;
    //public Vector2 position;
    //public Vector2 velocity;
    //public Rectangle hitbox;

    public Item(Vector2 position, Map map) {
        this.map = map;
        this.position = new Vector2(position.x, position.y);
        this.hitbox = new Rectangle(position.x, position.y, 7, 7);
        this.texture = Assets.dirt;
        this.velocity = new Vector2((float) ((Math.random() - 0.5f) * 20), 1.25f);
    }

    public void moveAndCollide(float delta) {
        Vector2 prevPosition = new Vector2(position.x, position.y);

        // Gravity
        if (!isOnGround) {
            velocity.y -= Map.gravity * delta * 5;
        }

        velocity.x = MathUtils.lerp(velocity.x, 0, 0.6f);

        // Gravity limiting
        if (velocity.y < -0.4f * 10) {
            velocity.y = -0.4f * 10;
        }

        // Horizontal
        hitbox.x += velocity.x;
        Array<CollisionDirection> xCollisions = map.collisionChecker.checkCollisionDirection(this);
        if (xCollisions.contains(CollisionDirection.RIGHT, true) && velocity.x > 0) {
            hitbox.x = prevPosition.x;
            velocity.x = 0;
        }
        if (xCollisions.contains(CollisionDirection.LEFT, true) && velocity.x < 0) {
            hitbox.x = prevPosition.x;
            velocity.x = 0;
        }

        // Vertical
        hitbox.y += velocity.y;
        Array<CollisionDirection> yCollisions = map.collisionChecker.checkCollisionDirection(this);
        if (yCollisions.contains(CollisionDirection.UP, true) && velocity.y > 0) {
            hitbox.y = prevPosition.y;
            velocity.y = 0;
        }
        if (yCollisions.contains(CollisionDirection.DOWN, true) && velocity.y < 0) {
            hitbox.y = prevPosition.y;
            velocity.y = 0;
            isOnGround = true;
        } else {
            isOnGround = false;
        }

        position.set(hitbox.x, hitbox.y);

        hitbox.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 8, 8);
    }

    public void update(float delta) {
        moveAndCollide(delta);
    }

    public void dispose() {
        texture.dispose();
    }
}
