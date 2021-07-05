/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) throws IOException {
        FileReader reader = new FileReader((fileName));
        BufferedReader br = new BufferedReader(reader);
        String line ="";
        String yamlFile = "";
        while((line=br.readLine())!=null){
            yamlFile+=(line)+"\n";
        }

        Yaml yaml = new Yaml();
        Map<String, Object> ymap = yaml.load(yamlFile);
        Map<String, List<String>> clientsMap = (Map<String, List<String>>) ymap.get("clientsMap");
        String host = (String) ymap.get("host");
        int port = Integer.parseInt(ymap.get("port").toString());
        boolean concurMode = Boolean.parseBoolean(ymap.get("concurMode").toString());
        boolean showSendRes = Boolean.parseBoolean((ymap.get("showSendRes").toString()));
        Options options = new Options(host,port,concurMode,showSendRes,clientsMap);
        return options;
    }
}
