package me.vrekt.oasis.item.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import me.vrekt.oasis.item.Item;

public abstract class ItemWeapon extends Item {

    protected float baseDamage = 1.0f, rotationAngle, criticalHitChance, criticalHitDamage;
    protected float swingCooldown, lastSwing, swingTime, range;
    protected boolean isSwinging, isResettingSwing, drawSwipe;
    protected Sprite swipe;

    protected Rectangle bounds;

    public ItemWeapon(String itemName, int itemId, String description) {
        super(itemName, itemId, description);
    }

    public boolean isOnSwingCooldown(float tick) {
        if (getLastSwing() == 0.0f) {
            return false;
        }
        return tick - getLastSwing() < swingCooldown;
    }

    public float getCriticalHitDamage() {
        return criticalHitDamage;
    }

    public float getCriticalHitChance() {
        return criticalHitChance;
    }

    public boolean isCriticalHit() {
        final float percentage = ((float) (Math.random() * 100));
        return percentage <= criticalHitChance;
    }

    public float getLastSwing() {
        return lastSwing;
    }

    public void setLastSwing(float lastSwing) {
        this.lastSwing = lastSwing;
    }

    public void swingItem(float delta) {
        if (isResettingSwing) return;
        rotationAngle += delta * 360f / swingTime;
        isSwinging = true;
        drawSwipe = true;
    }

    public float getRange() {
        return range;
    }

    public Rectangle getBounds() {
        if (range != 0.0f && sprite != null) {
            bounds.set(sprite.getBoundingRectangle());
            bounds.height += range;
            return bounds;
        } else if (sprite != null) {
            return sprite.getBoundingRectangle();
        }
        return Rectangle.tmp;
    }

    public boolean isSwinging() {
        return isSwinging;
    }

    @Override
    public void update(float delta) {
        if (isSwinging) {
            swingItem(delta);
            if (rotationAngle >= 60) {
                isSwinging = false;
                isResettingSwing = true;
            }
        } else if (isResettingSwing) {
            rotationAngle -= delta * 360f / swingTime;
            if (rotationAngle <= 0.0f) {
                isResettingSwing = false;
            }
        }
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    public float getBaseDamage() {
        return baseDamage;
    }
}
