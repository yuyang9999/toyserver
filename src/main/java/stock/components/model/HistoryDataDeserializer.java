package stock.components.model;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by yuyang on 11/2/18.
 */
public class HistoryDataDeserializer implements JsonDeserializer<HistoryData>{
    @Override
    public HistoryData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        System.out.println(jsonElement);

        JsonObject object = ((JsonObject) jsonElement).getAsJsonObject("chart");
        JsonElement result = object.getAsJsonArray("result").get(0);
        JsonArray timestamps = ((JsonObject)result).getAsJsonArray("timestamp");

        JsonElement quotes = ((JsonObject)result).getAsJsonObject("indicators").getAsJsonArray("quote").get(0);
        JsonArray open = ((JsonObject)quotes).getAsJsonArray("open");
        JsonArray close = ((JsonObject)quotes).getAsJsonArray("close");
        JsonArray high = ((JsonObject)quotes).getAsJsonArray("high");
        JsonArray low = ((JsonObject)quotes).getAsJsonArray("close");
        JsonArray volume = ((JsonObject)quotes).getAsJsonArray("volume");

        return new HistoryData(timestamps, open, close, high, low, volume);
    }
}
