package stock.components.data_fetcher;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyu on 18/2/18.
 */
public class CnnFearGreedIdx {
    private static final String url = "http://money.cnn.com/data/fear-and-greed/";
    private static final Pattern pat = Pattern.compile(": (\\d+) \\(");

    @Data
    public static class FearGreedIndex {
        private float now;
        private float preClose;
        private float oneWeekAgo;
        private float oneMonthAgo;
        private float oneYearAgo;
        private String updateDate;

    }


    public static FearGreedIndex getFearGreedIndex() {
        FearGreedIndex ret = new FearGreedIndex();

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }

        Elements elements = document.select("#needleChart ul li");

        if (elements.size() != 5) {
            return null;
        }

        List<Float> values = new ArrayList<>();

        for (Element elem: elements) {
            String text = elem.text();
            Matcher matcher = pat.matcher(text);
            if (matcher.find()) {
                values.add(Float.parseFloat(matcher.group(1)));
            }
        }

        ret.setNow(values.get(0));
        ret.setPreClose(values.get(1));
        ret.setOneWeekAgo(values.get(2));
        ret.setOneMonthAgo(values.get(3));
        ret.setOneYearAgo(values.get(4));

        elements = document.select("#needleAsOfDate");
        if (elements.size() == 1) {
            String text = elements.get(0).text();
            ret.setUpdateDate(text);
        }

        return ret;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getFearGreedIndex());
    }
}
