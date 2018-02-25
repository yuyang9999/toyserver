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

    @RequestMapping("one_day")
    HistoryData oneDayStockData(@RequestParam(value = "symbol") String symbol) throws Exception {
        return FetchHistoryUtil.getOneDayHistory(symbol);
    }

    @RequestMapping("five_days")
    HistoryData fiveDaysStockData(@RequestParam(value="symbol") String symbol) throws Exception {
        return FetchHistoryUtil.getFiveDayHistory(symbol);
    }

    @RequestMapping("one_month")
    HistoryData oneMonthStockData(@RequestParam(value = "symbol") String symbol) throws Exception {
        return FetchHistoryUtil.getOneMonthHistody(symbol);
    }

    @RequestMapping("six_months")
    HistoryData sixMonthsStockData(@RequestParam(value = "symbol") String symbol) throws Exception {
        return FetchHistoryUtil.getSixMonthHistory(symbol);
    }

    @RequestMapping("analysis/one_day")
    List<AnalysisPosNeg.Result> getOneDayAnalysisResult(@RequestParam(value = "symbol") String symbol,
                                                        @RequestParam(value = "type") String type) throws Exception {
        HistoryData historyData = FetchHistoryUtil.getOneDayHistory(symbol);

        List<AnalysisPosNeg.Result> ret = new ArrayList<>();

        if (type.equals("trail_stop")) {
            AnalysisPosNeg analysis = new AnalysisPosNeg(historyData);
            ret = analysis.calculateGain(1, 10, 0);
        }

        return ret;
    }

}
