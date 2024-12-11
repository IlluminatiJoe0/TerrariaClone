package io.github.illuminatijoe.terrariaclone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.illuminatijoe.terrariaclone.Assets;
import io.github.illuminatijoe.terrariaclone.util.CollisionDirection;
import io.github.illuminatijoe.terrariaclone.world.Map;
import io.github.illuminatijoe.terrariaclone.world.tile.Tile;

public class Player extends AbstractEntity {
    public OrthographicCamera camera;
    public Map map;
//    public Rectangle hitbox;
    public boolean isOnGround = false;

    public float cameraLerp = 12f;
    public boolean jumping = false;
    public float jumpHeight = 0.4f;
//    public Vector2 position;
    public Vector2 direction;
//    public Vector2 velocity;
    public float speed;

    public Player(Map map) {
        this.position = new Vector2((float) Map.worldWidth*16 /2, Map.worldHeight*16);
        this.direction = new Vector2();
        this.velocity = new Vector2();
        this.speed = 200f;
        this.map = map;

        this.hitbox = new Rectangle(position.x, position.y, 15, 15);

        this.camera = new OrthographicCamera();
        this.camera.position.set(position.x, position.y, 0);
        camera.zoom = 0.5f;
        this.camera.update();
    }

    public void update(float delta) {
        getInputDirection();
        getMouseInput();
        moveAndCollide(delta);
        updateCamera(delta);
    }

    private void getMouseInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            map.removeTile(getWorldMousePosition(camera));
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 pos = getWorldMousePosition(camera);
            map.addTile(pos, new Tile(pos, this.map));
        }
    }

    private Vector2 getWorldMousePosition(Camera camera) {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        return new Vector2(MathUtils.floor(mousePos.x / 16), MathUtils.floor(mousePos.y / 16));
    }

    public void updateCamera(float delta) {
        camera.position.x = MathUtils.lerp(camera.position.x, position.x, cameraLerp * delta);
        camera.position.y = MathUtils.lerp(camera.position.y, position.y, cameraLerp * delta);
        camera.update();
    }

    public void getInputDirection() {
        direction.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x += -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y += -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (isOnGround) {
                jumping = true;
                isOnGround = false;
            }
        }
    }

    public void moveAndCollide(float delta) {
        Vector2 prevPosition = new Vector2(position.x, position.y);

        // Gravity
        if (!isOnGround) {
            velocity.y -= Map.gravity * 30 * delta;
        }

        // Jump
        if (jumping) {
            velocity.y = jumpHeight * 1_200 * delta;
            jumping = false;
        }

        if (direction == Vector2.Zero) {
            velocity.x = MathUtils.lerp(velocity.x, 0, speed * delta);
        } else {
            velocity.x = direction.x * speed * delta;
        }

        // Gravity limiting
        if (velocity.y < -0.6f * 750 * delta) {
            velocity.y = -0.6f * 750 * delta;
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

        // Update the player's position to match the hitbox
        position.set(hitbox.x, hitbox.y);

        hitbox.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.player, position.x, position.y);
    }

    public Camera getCamera() {
        return this.camera;
    }

}
