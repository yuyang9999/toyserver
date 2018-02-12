package stock.components.utility;

import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by yuyang on 12/2/18.
 */
public class NetworkUtil {



    @Data
    public static class NetworkResp {
        private boolean succeed;
        private String errorMsg;
        private String resp;

        public NetworkResp(Boolean succeed, String msg) {
            this.succeed = succeed;
            if (succeed) {
                resp = msg;
            } else {
                errorMsg = msg;
            }
        }
    }

    public static NetworkResp getResponseForURL(String url) {
        NetworkResp ret = null;

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        StringBuffer result = new StringBuffer();
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            ret = new NetworkResp(false, e.getMessage());
        }

        if (response != null && response.getStatusLine().getStatusCode() != 200) {
            ret = new NetworkResp(false, response.getStatusLine().toString());
        }

        //get the result
        if (ret == null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e) {
                ret = new NetworkResp(false, e.getMessage());
            }
        }

        if (ret == null) {
            return new NetworkResp(true, result.toString());
        }

        return ret;
    }
}
