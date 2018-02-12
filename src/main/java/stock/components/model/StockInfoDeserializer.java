package stock.components.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by yuyang on 12/2/18.
 */
public class StockInfoDeserializer implements JsonDeserializer<StockInfo> {
    @Override
    public StockInfo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        System.out.println();
        return null;
    }
}
