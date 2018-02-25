package stock.components.data_fetcher;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyu on 19/2/18.
 */
@Slf4j
public class MitbbsStock {
    @Data
    public static class Item {
        private String title;
        private String creatorName;
        private Date createTime;
        private String refUrl;
    }


    private static final String firstUrl = "http://www.mitbbs.com/bbsdoc/Stock.html";

    public static void process(String url) throws IOException {
        //get the first page
        Document document = Jsoup.connect(url).get();

        List<Item> items = new ArrayList<>();

        //get the elements
        Elements elements = document.select(".taolun_leftright table tr");
        for (Element element: elements) {
            if (element.attributes().size() > 0) {
                continue;
            }

            Elements tdElems = element.select("td");
            if (tdElems.size() != 6) {
                continue;
            }

            Element titleElem =  tdElems.get(2);
            String title = titleElem.text();
            String itemUrl = titleElem.select("a.news1").attr("href");

            Element creatorElem = tdElems.get(4);
            String creatorName = creatorElem.select("a.news").text();
            String creatorTime = creatorElem.select("span").text();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(creatorTime);
            } catch (ParseException e) {
                log.error("{}", e);
            }

            Item item = new Item();
            item.setTitle(title);
            item.setRefUrl(itemUrl);
            item.setCreatorName(creatorName);
            item.setCreateTime(date);

            items.add(item);
        }

        String nextPageUrl = "";
        String prevPageUrl = "";

        //get the previous, next page url
        elements = document.select("a.news");
        for (Element elem: elements) {
            if (elem.text().equals("下页")) {
                nextPageUrl = elem.attr("href");
            } else if (elem.text().equals("上页")) {
                prevPageUrl = elem.attr("href");
            }
        }

        System.out.println(items);
        System.out.println(prevPageUrl);
        System.out.println(nextPageUrl);
    }

    public static void main(String[] args) throws Exception {
        process(firstUrl);
    }

}
