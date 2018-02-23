package stock.components.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 12/2/18.
 */
public class StockInfoDeserializer implements JsonDeserializer<StockInfos> {
    @Override
    public StockInfos deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray items = ((JsonObject)jsonElement).getAsJsonObject("data").getAsJsonArray("items");

        List<StockInfos.StockInfo> infos = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            JsonObject item = (JsonObject) items.get(i);
            StockInfos.StockInfo info = new StockInfos.StockInfo();
            info.setSymbol(item.get("symbol").getAsString());
            info.setName(item.get("name").getAsString());
            info.setExch(item.get("exch").getAsString());
            info.setType(item.get("type").getAsString());
            info.setExchDisp(item.get("exchDisp").getAsString());
            info.setTypeDisp(item.get("typeDisp").getAsString());

            infos.add(info);
        }

        StockInfos stInfos = new StockInfos();
        stInfos.setStockInfoList(infos);

        return stInfos;
    }
}
