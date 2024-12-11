package io.github.illuminatijoe.terrariaclone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEntity {
    public Vector2 position;
    public Vector2 velocity;
    public Rectangle hitbox;

//    public AbstractEntity(Vector2 position, Vector2 velocity, Rectangle hitbox) {
//        this.position = position;
//        this.velocity = velocity;
//        this.hitbox = hitbox;
//    }
//
//    public AbstractEntity() {
//        this.position = new Vector2(0, 0);
//        this.velocity = new Vector2(0, 0);
//        this.hitbox = new Rectangle(0, 0, 0, 0);
//    }
}
