package stock.components.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import stock.components.model.StockInfoDeserializer;
import stock.components.model.StockInfos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuyang on 12/2/18.
 */
public class StockInfoUtility {
    static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockInfos.class, new StockInfoDeserializer());
    }

    static private final String stockInfoURL = "https://finance.yahoo.com/_finance_doubledown/api/resource/searchassist;searchTerm=%s?bkt=%%5B%%22fn-sa-4%%22%%2C%%22fdw-strm-ctrl%%22%%5D&device=desktop&feature=canvassOffnet%%2CenableChartiqFeedback%%2CenableCrypto%%2CnewContentAttribution%%2CrelatedVideoFeature%%2CvideoNativePlaylist%%2CenableESG%%2Clivecoverage%%2CenableSingleRail%%2CenableSKTVLrec%%2CretirementCalc&intl=us&lang=en-US&partner=none&prid=84ia5k1d81t4p&region=US&site=finance&tz=Asia%%2FShanghai&ver=0.102.1109&returnMeta=true";

    static public StockInfos getStockInfo(String symbol) {
        String url = String.format(stockInfoURL, symbol);

        StockInfos ret = new StockInfos();

        NetworkUtil.NetworkResp resp = NetworkUtil.getResponseForURL(url);
        if (resp.isSucceed()) {
            Gson gson = gsonBuilder.create();
            String respStr = resp.getResp();
            ret = gson.fromJson(respStr, StockInfos.class);
        }

        return ret;
    }

    static private void getCompany(String writePath, String filePath) throws IOException {
        List<String> contents = IOUtility.readSystemFileContent(filePath);
        Set<String> procSet = new HashSet<>();

        File writeFile = new File(writePath);
        if (writeFile.exists()) {
            List<String> processSymbols = IOUtility.readSystemFileContent(writePath);
            for (String s: processSymbols) {
                procSet.add(s);
            }
        }

        List<String> symbols = new ArrayList<>();
        for (String s: contents) {
            if (procSet.contains(s)) {
                continue;
            }
            symbols.add(s);
        }

        int cnt = 0;

        for (String s : symbols) {
            if (s.equals("")) {
                continue;
            }
            cnt++;
            System.out.println(cnt);

            StockInfos infos = getStockInfo(s);
            for (StockInfos.StockInfo info: infos.getStockInfoList()) {
                if (s.equals(info.getSymbol().toLowerCase())) {
                    String name = info.getName();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true));
                    writer.write(s + "\t" + name + "\n");
                    writer.close();
                    break;
                }
            }
        }
    }

    static public void main(String[] args) throws Exception {
//        System.out.println(getStockInfo("aapl"));
        getCompany("/Users/yuyang/Downloads/Data/etf_name.txt", "/Users/yuyang/Downloads/Data/etf.txt");
    }
}
