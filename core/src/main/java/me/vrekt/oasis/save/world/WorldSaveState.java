package me.vrekt.oasis.save.world;

import com.google.gson.*;
import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.entity.Entity;
import me.vrekt.oasis.save.entity.EntitySaveState;
import me.vrekt.oasis.world.OasisWorld;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorldSaveState {

    // the actual world for saving
    private transient OasisWorld world;

    // world map
    private transient String worldMap;
    private transient String worldName;

    // list of entities in this world state
    private final transient List<EntitySaveState> entities = new ArrayList<>();

    public WorldSaveState(OasisWorld world) {
        this.world = world;
    }

    public WorldSaveState() {
    }

    public List<EntitySaveState> getEntities() {
        return entities;
    }

    public String getWorldMap() {
        return worldMap;
    }

    public String getWorldName() {
        return worldName;
    }

    public static final class WorldSaver implements JsonSerializer<WorldSaveState> {
        @Override
        public JsonElement serialize(WorldSaveState src, Type typeOfSrc, JsonSerializationContext context) {
            final OasisWorld world = src.world;
            final JsonObject base = new JsonObject();

            base.addProperty("worldIn", Asset.TUTORIAL_WORLD);
            base.addProperty("worldName", world.getWorldName());
            final JsonArray entities = new JsonArray();
            final EntitySaveState save = new EntitySaveState();

            // iterate through all entities in the world and save their states
            for (Entity entity : world.getEntities().values()) {
                save.reset(entity);
                entities.add(context.serialize(save));
            }

            base.add("entities", entities);
            return base;
        }
    }

    public static final class WorldLoader implements JsonDeserializer<WorldSaveState> {
        @Override
        public WorldSaveState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final WorldSaveState save = new WorldSaveState();
            final JsonObject base = json.getAsJsonObject();

            // load map name
            save.worldMap = base.get("worldIn").getAsString();
            save.worldName = base.get("worldName").getAsString();

            // load all entities within the world
            final JsonArray entities = base.getAsJsonArray("entities");
            for (JsonElement element : entities) {
                final JsonObject object = element.getAsJsonObject();
                save.entities.add(context.deserialize(object, EntitySaveState.class));
            }
            return save;
        }
    }

}
