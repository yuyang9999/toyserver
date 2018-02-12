package stock.components.analysis;

import lombok.Data;
import stock.components.model.HistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 12/2/18.
 */
public class AnalysisPosNeg extends AnalysisBase {
    private HistoryData historyData;

    @Data
    public class Result {
        private float param;
        private float gain;
    }

    public AnalysisPosNeg(HistoryData data) {
        this.historyData = data;
    }

    public List<Result> calculateGain(int minPercent, int maxPercent, int startIdx) {
        List<Result> ret = new ArrayList<>();

        List<HistoryData.TimeSlotData> rows = historyData.getRows();
        List<Float> closes = new ArrayList<>();
        for (int i = startIdx; i < rows.size(); i++) {
            closes.add(rows.get(i).getClose());
        }

        for (int i = minPercent; i <= maxPercent; i++) {
            float gain = getGainForParam(((float)i)/100, closes);
            Result result = new Result();
            result.setGain(gain);
            result.setParam(i);

            ret.add(result);
        }

        return ret;
    }

    private float getGainForParam(float percent, List<Float> prices) {
        if (prices.size() == 0) {
            return 0.f;
        }

        float startPrice = prices.get(0);
        float lastPrice = prices.get(prices.size() - 1);

        int posSoldIdx = getSoldIndexForPositiveGain(prices, percent);
        int negSoldIdx = getSoldIndexForNegativeGain(prices, percent);

        float ret = 0.f;

        if (posSoldIdx == -1 && negSoldIdx == -1) {
            ret = 0.f;
        } else if (posSoldIdx != -1 && negSoldIdx != -1) {
            if (posSoldIdx < negSoldIdx) {
                //use neg
                ret = -(prices.get(negSoldIdx) - startPrice) / startPrice;
            } else {
                //use pos
                ret = (prices.get(posSoldIdx) - startPrice) / startPrice;
            }
        } else if (posSoldIdx != -1) {
            //use neg
            ret = -(lastPrice - startPrice) / startPrice;
        } else {
            //use pos
            ret = (lastPrice - startPrice) / startPrice;
        }

        return ret * 100;
    }

    static private int getSoldIndexForPositiveGain(List<Float> prices, float percent) {
        if (prices.size() == 0) {
            return -1;
        }

        float maxVal = prices.get(0);

        for (int i = 1; i < prices.size(); i++) {
            float price = prices.get(i);
            if (price > maxVal) {
                maxVal = price;
                continue;
            }

            if (price < maxVal * (1 - percent)) {
                return i;
            }
        }

        return -1;
    }

    static private int getSoldIndexForNegativeGain(List<Float> prices, float percent) {
        if (prices.size() == 0) {
            return -1;
        }

        float minVal = prices.get(0);
        for (int i = 1; i < prices.size(); i++) {
            float price = prices.get(i);
            if (price < minVal) {
                minVal = price;
                continue;
            }

            if (price > minVal * (1 + percent)) {
                return i;
            }
        }

        return -1;
    }
}
