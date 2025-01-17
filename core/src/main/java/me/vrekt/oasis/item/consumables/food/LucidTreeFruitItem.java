package me.vrekt.oasis.item.consumables.food;

import com.badlogic.gdx.graphics.g2d.Sprite;
import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.entity.player.sp.OasisPlayerSP;
import me.vrekt.oasis.item.ItemRarity;
import me.vrekt.oasis.item.attribute.attributes.PlayerHealingAttribute;
import me.vrekt.oasis.item.consumables.ItemConsumable;

/**
 * Fruit from a lucid tree
 */
public final class LucidTreeFruitItem extends ItemConsumable {

    public static final int ID = 3;
    public static final String TEXTURE = "lucid_fruit";
    public static final String NAME = "Lucid Tree Fruit";

    public LucidTreeFruitItem() {
        super(NAME, ID, "Heals you by [GREEN]+20 [BLACK]HP.");
        addAttribute(new PlayerHealingAttribute(20.0f));
        this.allowedToConsume = false;
        this.rarity = ItemRarity.BASIC;

        setDescriptor(TEXTURE, NAME);
    }

    @Override
    public void load(Asset asset) {
        this.sprite = new Sprite(asset.get(TEXTURE));
    }

    @Override
    public void consume(OasisPlayerSP player) {
        if (!allowedToConsume) return;
        decreaseItemAmount();
        applyAllAttributes(player);
        player.setDidUseTutorialFruit(true);
    }
}
