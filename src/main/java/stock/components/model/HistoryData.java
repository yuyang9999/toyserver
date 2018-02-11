package stock.components.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 11/2/18.
 */

@Data
public class HistoryData {
    @Data
    public class TimeSlotData {
        private Long time;
        private Float open;
        private Float close;
        private Float high;
        private Float low;
        private Long volume;
    }

    private List<TimeSlotData> rows;

    public HistoryData(JsonArray times, JsonArray opens, JsonArray closes,
                       JsonArray highs, JsonArray lows, JsonArray volumes) {
        rows = new ArrayList<TimeSlotData>();
        if (times.size() != opens.size() || times.size() != closes.size() ||
                times.size() != highs.size() || times.size() != lows.size() ||
                times.size() != volumes.size()) {
            return;
        }

        for (int i = 0; i < times.size(); i++) {

            JsonElement timeElem = times.get(i);
            JsonElement openElem = opens.get(i);
            JsonElement closeElem = closes.get(i);
            JsonElement highElem = highs.get(i);
            JsonElement lowElem = lows.get(i);
            JsonElement volumeElem = volumes.get(i);
            if (timeElem.isJsonNull() || openElem.isJsonNull() || closeElem.isJsonNull() ||
                    highElem.isJsonNull() || lowElem.isJsonNull() || volumeElem.isJsonNull()) {

                System.out.println("null find");
                continue;
            }

            Long time = times.get(i).getAsLong();
            Float open = opens.get(i).getAsFloat();
            Float close = closes.get(i).getAsFloat();
            Float high = highs.get(i).getAsFloat();
            Float low = lows.get(i).getAsFloat();
            Long volume = volumes.get(i).getAsLong();

            TimeSlotData slotData = new TimeSlotData();
            slotData.setTime(time);
            slotData.setOpen(open);
            slotData.setClose(close);
            slotData.setHigh(high);
            slotData.setLow(low);
            slotData.setVolume(volume);
            rows.add(slotData);
        }
    }

    public HistoryData() {
        rows = new ArrayList<TimeSlotData>();
    }
}
