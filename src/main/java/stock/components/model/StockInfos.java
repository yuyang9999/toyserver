package stock.components.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 12/2/18.
 */
@Data
public class StockInfos {
    @Data
    public static class StockInfo {
        private String symbol;
        private String name;
        private String exch;
        private String type;
        private String exchDisp;
        private String typeDisp;
    }

    private List<StockInfo> stockInfoList;

    public StockInfos() {
        stockInfoList = new ArrayList<>();
    }
}
