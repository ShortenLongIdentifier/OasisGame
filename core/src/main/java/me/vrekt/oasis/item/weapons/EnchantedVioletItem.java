package me.vrekt.oasis.item.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.asset.settings.OasisGameSettings;

public final class EnchantedVioletItem extends ItemWeapon {

    public static final int ID = 4;
    public static final String TEXTURE = "enchanted_violet";
    public static final String NAME = "Enchanted Violet";

    private ParticleEffect swingEffect;

    private long startTime;

    public EnchantedVioletItem() {
        super(NAME, ID, "A special blade with magical abilities.");
        this.baseDamage = 1.5f;
        this.rotationAngle = 0.0f;
        this.swingCooldown = 0.25f;
        this.swingTime = .75f;
        this.criticalHitChance = 15.0f;
        this.criticalHitDamage = 6.5f;
        this.range = 0.8f;
    }

    @Override
    public void load(Asset asset) {
        this.sprite = new Sprite(asset.get(TEXTURE));
        this.sprite.setScale(2.0f);
        this.sprite.setSize(sprite.getRegionWidth() * OasisGameSettings.SCALE, sprite.getRegionHeight() * OasisGameSettings.SCALE);
        this.swipe = new Sprite(asset.get("swipetest", 2));

        this.swingEffect = new ParticleEffect();
        this.swingEffect.load(Gdx.files.internal("world/asset/swordparticle"), asset.getAtlasAssets());
        this.swingEffect.start();

        this.bounds = new Rectangle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!isSwinging) {
            swingEffect.reset();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            // TODO: Account for rotation angle height changes within the item bounding box
            this.draw(batch, sprite.getWidth(), sprite.getHeight(), rotationAngle);
        }
    }
}