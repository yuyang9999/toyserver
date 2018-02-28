package stock.components.data_fetcher;

import lombok.extern.slf4j.Slf4j;
import stock.components.utility.IOUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 28/2/18.
 */
@Slf4j
public class SymbolQuery {
    static private List<String> symbols;
    static {
        try {
            symbols = IOUtility.readFileContent("/symbols.txt");
        } catch (IOException e) {
            log.error("{}", e);
            symbols = new ArrayList<>();
        }
    }

    static public List<String> getSuggestSymbols(String query) {
        List<String> ret = new ArrayList<>();
        if (query == null || query.equals("")) {
            return ret;
        }

        for (String s: symbols) {
            if (s.startsWith(query)) {
                ret.add(s);
            }
        }
        return ret;
    }

}
