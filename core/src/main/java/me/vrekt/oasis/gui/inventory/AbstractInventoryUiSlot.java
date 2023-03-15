package me.vrekt.oasis.gui.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImage;
import me.vrekt.oasis.item.Item;

/**
 * Contains basic information about what is in a slot for the GUI(s)
 */
public abstract class AbstractInventoryUiSlot {

    protected final VisImage imageItem;
    protected final Tooltip tooltip;
    protected Item item;
    protected boolean occupied;

    public AbstractInventoryUiSlot(Stack stack, VisImage item) {
        this.imageItem = item;
        tooltip = new Tooltip.Builder("Empty Slot").target(stack).build();
        tooltip.setAppearDelayTime(0.35f);
    }

    protected void reset() {
        this.occupied = false;
        this.imageItem.setDrawable((Drawable) null);
        this.tooltip.setText("Empty Slot");
        this.item = null;
    }

    protected void setItem(Item item) {
        this.imageItem.setDrawable(new TextureRegionDrawable(item.getTexture()));
        this.item = item;

        this.tooltip.setText(item.getItemName());
        this.occupied = true;
    }

    protected abstract void removeItem();

}