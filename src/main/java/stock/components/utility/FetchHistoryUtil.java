package stock.components.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import stock.components.model.HistoryData;
import stock.components.model.HistoryDataDeserializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by yuyang on 11/2/18.
 */
public class FetchHistoryUtil  {
    static private final String oneDayURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=1d&includePrePost=false&interval=2m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String fiveDaysURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=5d&includePrePost=false&interval=15m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String oneMonthsURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=1mo&includePrePost=false&interval=30m&corsDomain=finance.yahoo.com&.tsrc=finance";
    static private final String sixMonthsURL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=6mo&includePrePost=false&interval=1d&corsDomain=finance.yahoo.com&.tsrc=finance";


    static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HistoryData.class, new HistoryDataDeserializer());
    }


    private static HistoryData getHistoryData(String url) throws Exception {

        HistoryData ret = new HistoryData();

        if (url == null || url.equals("")) {
            return ret;
        }

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        StringBuffer result = new StringBuffer();

        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            return ret;
        }

        //get the result
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        String data = result.toString();

        if (data.equals("")) {
            return ret;
        }

        Gson gson = gsonBuilder.create();
        ret = gson.fromJson(data, HistoryData.class);

        return ret;
    }

    public static HistoryData getOneDayHistory(String symbol) throws Exception {
        String fullUrl = String.format(oneDayURL, symbol);
        return getHistoryData(fullUrl);
    }

    public static HistoryData getFiveDayHistory(String symbol) throws Exception {
        String fullUrl = String.format(fiveDaysURL, symbol);
        return getHistoryData(fullUrl);
    }

    public static HistoryData getOneMonthHistody(String symbol) throws Exception {
        String fullUrl = String.format(oneMonthsURL, symbol);
        return getHistoryData(fullUrl);
    }

    public static HistoryData getSixMonthHistory(String symbol) throws Exception {
        String fullUrl = String.format(sixMonthsURL, symbol);
        return getHistoryData(fullUrl);
    }

    public static void main(String[] args) throws Exception {
        HistoryData data = getHistoryData("https://query1.finance.yahoo.com/v8/finance/chart/TQQQ?range=1d&includePrePost=false&interval=2m&corsDomain=finance.yahoo.com&.tsrc=finance");

        System.out.println(data);
        System.out.println("hello");
    }
}
