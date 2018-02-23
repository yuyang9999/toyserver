package stock.components.utility;

import sun.misc.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyang on 13/2/18.
 */
public class IOUtility {
    static public List<String> readFileContent(String path) throws IOException {
        List<String> ret = new ArrayList<>();

        InputStream is = IOUtils.class.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            ret.add(line);
        }
        br.close();

        return ret;
    }

    static public List<String> readSystemFileContent(String path) throws IOException {
        List<String> ret = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.equals("")) {
                ret.add(line);
            }
        }
        br.close();

        return ret;
    }

    static public void writeContentsToFile(List<String> contents, String writePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(writePath));
        for (String line: contents) {
            br.write(line + "\n");
        }
    }
}
