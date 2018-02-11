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

    static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HistoryData.class, new HistoryDataDeserializer());
        System.out.println("");
    }


    public static HistoryData getHistoryData(String url) throws Exception {

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

    public static void main(String[] args) throws Exception {
        HistoryData data = getHistoryData("https://query1.finance.yahoo.com/v8/finance/chart/TQQQ?range=1d&includePrePost=false&interval=2m&corsDomain=finance.yahoo.com&.tsrc=finance");

        System.out.println(data);
        System.out.println("hello");
    }
}
