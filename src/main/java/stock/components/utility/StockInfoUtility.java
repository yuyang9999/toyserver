package stock.components.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import stock.components.model.HistoryData;
import stock.components.model.StockInfo;
import stock.components.model.StockInfoDeserializer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by yuyang on 12/2/18.
 */
public class StockInfoUtility {
    static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockInfo.class, new StockInfoDeserializer());
    }

    static private final String stockInfoBaseURL = "https://finance.yahoo.com/";
    static private final String stockInfoURL = "https://finance.yahoo.com/_finance_doubledown/api/resource/searchassist;searchTerm=%s?bkt=%%5B%%22fn-sa-4%%22%%2C%%22fdw-strm-ctrl%%22%%5D&device=desktop&feature=canvassOffnet%%2CenableChartiqFeedback%%2CenableCrypto%%2CnewContentAttribution%%2CrelatedVideoFeature%%2CvideoNativePlaylist%%2CenableESG%%2Clivecoverage%%2CenableSingleRail%%2CenableSKTVLrec%%2CretirementCalc&intl=us&lang=en-US&partner=none&prid=84ia5k1d81t4p&region=US&site=finance&tz=Asia%%2FShanghai&ver=0.102.1109&returnMeta=true";
    static private final String stockInfoUrl = "_finance_doubledown/api/resource/searchassist;searchTerm=%s?&feature=canvassOffnet,enableChartiqFeedback,enableCrypto,newContentAttribution,relatedVideoFeature,videoNativePlaylist,enableESG,livecoverage,enableSingleRail,enableSKTVLrec,retirementCalc&intl=us&lang=en-US&partner=none&prid=84ia5k1d81t4p&region=US&site=finance&tz=Asia/Shanghai&ver=0.102.1109&returnMeta=true";

    static public StockInfo getStockInfo(String symbol) {
        String url = String.format(stockInfoURL, symbol);
//        try {
//            url = URLEncoder.encode(url, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            return new StockInfo();
//        }
//        url = stockInfoBaseURL + url;

        StockInfo ret = new StockInfo();

        NetworkUtil.NetworkResp resp = NetworkUtil.getResponseForURL(url);
        if (resp.isSucceed()) {
            Gson gson = gsonBuilder.create();
            String respStr = resp.getResp();
            ret = gson.fromJson(respStr, StockInfo.class);
        }

        return ret;
    }


    static public void main(String[] args) throws Exception {
//        String text = URLDecoder.decode(stockInfoURL, "utf-8");
//        System.out.println(text);

//        System.out.println(String.format("%%s %s", "123"));

        System.out.println(getStockInfo("aapl"));
    }

}
