package me.vrekt.oasis.gui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.tommyettinger.textra.TypingLabel;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.entity.player.sp.OasisPlayerSP;
import me.vrekt.oasis.gui.GameGui;
import me.vrekt.oasis.gui.Gui;
import me.vrekt.oasis.gui.GuiType;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

/**
 * Inventory Gui
 */
public final class InventoryGui extends Gui {

    private final Stage stage;
    private final VisTable rootTable;
    private final OasisPlayerSP player;

    private final LinkedList<InventoryUiSlot> slots = new LinkedList<>();

    // the item description of whatever item is clicked
    private final TypingLabel itemDescription;

    public InventoryGui(GameGui gui, Asset asset) {
        super(gui, asset, "inventory");
        this.player = gui.getGame().getPlayer();

        stage = new Stage();
        rootTable = new VisTable(true);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);
        TableUtils.setSpacingDefaults(rootTable);

        rootTable.setBackground(new TextureRegionDrawable(asset.get("book_tab")));
        final VisTable primary = new VisTable(true);
        final VisTable secondary = new VisTable(true);

        secondary.top().padTop(52).padLeft(32).left();
        this.itemDescription = new TypingLabel("", new Label.LabelStyle(gui.getMedium(), Color.BLACK));
        this.itemDescription.setVisible(false);
        this.itemDescription.setWrap(true);
        this.itemDescription.setWidth(200);
        secondary.add(itemDescription).width(200).left();

        // populate a total of 21 inventory slots
        primary.top().padTop(52).padLeft(84);
        final TextureRegionDrawable drawable = new TextureRegionDrawable(asset.get("inventory_slot"));
        for (int i = 1; i < 22; i++) {
            final VisImage slot = new VisImage(drawable);
            final VisImage item = new VisImage();

            // create a separate container for the item image... so it doesn't get stretched.
            final VisTable itemTable = new VisTable(true);
            itemTable.add(item);
            final Stack overlay = new Stack(slot, new Container<Table>(itemTable));

            this.slots.add(new InventoryUiSlot(overlay, item));
            primary.add(overlay).size(48, 48);
            if (i % 3 == 0) primary.row();
        }

        //  new Tooltip.Builder("Empty").target(image).build().setAppearDelayTime(0.1f);


        VisSplitPane splitPane = new VisSplitPane(primary, secondary, false);
        rootTable.add(splitPane).fill().expand();

        rootTable.pack();
        stage.addActor(rootTable);
    }

    @Override
    public void update() {
        stage.getViewport().apply();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.getCamera().update();
        stage.draw();


        // update ui based on player inventory status
        player.getInventory().getSlots().forEach((slot, item) -> {
            final InventoryUiSlot ui = slots.get(slot);
            // only update this inventory slot IF the last item does not match the current
            if (ui.lastItemId != item.getItem().getItemId()) {
                ui.setItem(new TextureRegionDrawable(item.getItem().getTexture()), item.getItem().getItemId());
                ui.setToolTipText(item.getItem().getItemName());
                ui.setDescription(item.getItem().getDescription());
            }
        });
    }

    @Override
    public void show() {
        super.show();
        gui.hideGui(GuiType.HUD);
        rootTable.setVisible(true);
        gui.getMultiplexer().addProcessor(stage);
    }

    @Override
    public void hide() {
        super.hide();
        gui.showGui(GuiType.HUD);
        rootTable.setVisible(false);
        gui.getMultiplexer().removeProcessor(stage);
    }

    /**
     * Handles data required for the UI inventory slot
     */
    private final class InventoryUiSlot {
        private final VisImage item;
        private final Tooltip tooltip;

        // item description of whatever is in this slot
        private String itemDescription = StringUtils.EMPTY;
        // the last item in this slot, for comparison when updating
        private long lastItemId = -1;

        public InventoryUiSlot(Stack stack, VisImage item) {
            this.item = item;
            tooltip = new Tooltip.Builder("Empty Slot").target(stack).build();
            tooltip.setAppearDelayTime(0.35f);

            // add click action
            stack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    InventoryGui.this.itemDescription.setText("[BLACK]" + itemDescription);
                    InventoryGui.this.itemDescription.restart();
                    if (!InventoryGui.this.itemDescription.isVisible()) {
                        InventoryGui.this.itemDescription.setVisible(true);
                    }
                }
            });

        }

        void setToolTipText(String text) {
            tooltip.setText(text);
        }

        void setItem(TextureRegionDrawable drawable, long itemId) {
            item.setDrawable(drawable);
            this.lastItemId = itemId;
        }

        void setDescription(String text) {
            this.itemDescription = text;
        }

    }

}
