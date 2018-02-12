package stock.components.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.components.analysis.AnalysisPosNeg;
import stock.components.model.HistoryData;
import stock.components.utility.FetchHistoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 11/2/18.
 */
@RestController
@Slf4j
public class StockHistory {
    static private final String oneDayURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=1d&includePrePost=false&interval=2m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String fiveDaysURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=5d&includePrePost=false&interval=15m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String oneMonthsURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=1mo&includePrePost=false&interval=30m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String sixMonthsURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=6mo&includePrePost=false&interval=1d&corsDomain=finance.yahoo.com&.tsrc=finance";

    private HistoryData getStockData(String url) {
        HistoryData data = new HistoryData();
        try {
            data = FetchHistoryUtil.getHistoryData(url);
        } catch (Exception e) {
            log.error("{}", e);
        }

        return data;

    }

    @RequestMapping("one_day")
    HistoryData oneDayStockData(@RequestParam(value = "symbol") String symbol) {
        String fullURL = String.format(oneDayURL, symbol);
        return getStockData(fullURL);
    }

    @RequestMapping("five_days")
    HistoryData fiveDaysStockData(@RequestParam(value="symbol") String symbol) {
        String url = String.format(fiveDaysURL, symbol);
        return getStockData(url);
    }

    @RequestMapping("one_month")
    HistoryData oneMonthStockData(@RequestParam(value = "symbol") String symbol) {
        String url = String.format(oneMonthsURL, symbol);
        return getStockData(url);
    }

    @RequestMapping("six_months")
    HistoryData sixMonthsStockData(@RequestParam(value = "symbol") String symbol) {
        String url = String.format(sixMonthsURL, symbol);
        return getStockData(url);
    }

    @RequestMapping("analysis/one_day")
    List<AnalysisPosNeg.Result> getOneDayAnalysisResult(@RequestParam(value = "symbol") String symbol,
                                                        @RequestParam(value = "type") String type) {

        String url = String.format(oneDayURL, symbol);

        HistoryData historyData = getStockData(url);

        List<AnalysisPosNeg.Result> ret = new ArrayList<>();

        if (type.equals("trail_stop")) {
            AnalysisPosNeg analysis = new AnalysisPosNeg(historyData);
            ret = analysis.calculateGain(1, 10, 0);
        }

        return ret;
    }

}
