package me.vrekt.oasis.world.environment;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import me.vrekt.oasis.asset.settings.OasisGameSettings;

/**
 * Represents an environment object not put on the Tiled map.
 */
public final class EnvironmentObject implements Disposable {

    private final TextureRegion texture;
    private final Vector2 location;
    private Body collisionBody;

    private ParticleEffect effect;
    private boolean playEffect = true;

    public EnvironmentObject(TextureRegion texture, float x, float y) {
        this.texture = texture;
        this.location = new Vector2(x - ((texture.getRegionWidth() / 2f) * OasisGameSettings.SCALE), y - ((texture.getRegionHeight() / 3f) * OasisGameSettings.SCALE));
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public void setPlayEffect(boolean playEffect) {
        this.playEffect = playEffect;
    }

    public boolean playEffect() {
        return playEffect;
    }

    public void removeEffect() {
        effect.dispose();
        effect = null;
    }

    public void setCollisionBody(Body collisionBody) {
        this.collisionBody = collisionBody;
    }

    public Body getCollisionBody() {
        return collisionBody;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, location.x, location.y, texture.getRegionWidth() * OasisGameSettings.SCALE, texture.getRegionHeight() * OasisGameSettings.SCALE);
    }

    @Override
    public void dispose() {
        if (effect != null) effect.dispose();
    }
}
