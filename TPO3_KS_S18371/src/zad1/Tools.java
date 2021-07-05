/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.yaml.snakeyaml.*;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) throws Exception{
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
        System.out.println();
        int port = Integer.parseInt(ymap.get("port").toString());
        boolean concurMode = Boolean.parseBoolean(ymap.get("concurMode").toString());
        boolean showSendRes = Boolean.parseBoolean((ymap.get("showSendRes").toString()));
        Options options = new Options(host,port,concurMode,showSendRes,clientsMap);
        return options;
    }

}
