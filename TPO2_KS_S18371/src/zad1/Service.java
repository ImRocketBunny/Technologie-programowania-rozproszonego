/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.util.*;



import org.json.JSONException;
import org.json.JSONObject;



import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import java.util.Currency;




public class Service {
    String country;
    String miasto = "";
    String baseMoney ="";
    String currencyInCountry;

    public Service(String country) {
        this.country=country;
    }


    Map<String,String> isoJraje = new HashMap<>();
    {
        for(String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            isoJraje.put(l.getDisplayCountry(Locale.ENGLISH), iso);
        }
    }

    public Double getRateFor(String money) throws IOException, JSONException {
        Locale locale1 = new Locale("EN",country);
        Locale locale=new Locale("en", isoJraje.get(country));
        Currency currency= Currency.getInstance(locale);
        currencyInCountry=String.valueOf(currency);
        String url= "https://api.exchangeratesapi.io/latest?base="+money;
        double kursWaluty=0.0;
        StringBuilder json;
        URL urlConn=new URL(String.format(url));
        HttpURLConnection connection = (HttpURLConnection)urlConn.openConnection();
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader bf=new BufferedReader(reader);
        json = new StringBuilder();
        String tmp="";
        while((tmp=bf.readLine())!=null)
            json.append(tmp).append("\n");
        reader.close();
        String jsonStr = String.valueOf(json);
        JSONObject jsonObj1 = new JSONObject(jsonStr);
        kursWaluty=jsonObj1.getJSONObject("rates").getDouble(String.valueOf(currency));
        baseMoney=money;
        return kursWaluty;
    }

    public String getWeather(String capital) throws JSONException {
        String apiKey = "656d7ae78df0d9a19d0f78af5a3df688";
        String call ="https://api.openweathermap.org/data/2.5/weather?q="+capital+","+isoJraje.get(country)+"&units=metric&appid="+apiKey+"";
        String returnType ="";
        StringBuilder json = new StringBuilder();
        try {
            URL url=new URL(String.format(call));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bf=new BufferedReader(reader);
            json = new StringBuilder();
            String tmp="";
            while((tmp=bf.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        returnType = json.toString();
        miasto=capital;

        return returnType;


    }

    public Double getNBPRate() throws IOException {
        Locale locale=new Locale("en", isoJraje.get(country));
        Currency currency= Currency.getInstance(locale);
        String[] URLs ={"http://www.nbp.pl/kursy/kursya.html", "http://www.nbp.pl/kursy/kursyb.html"};
        Double kursWaluty=null;
        StringBuilder json = new StringBuilder();
        for(int i = 0;i<URLs.length;i++){

            URL url = new URL(String.format(URLs[i]));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bf=new BufferedReader(reader);
            String tmp="";
            while((tmp=bf.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

        }
        String strona =String.valueOf(json);
        //System.out.println(currency);
        String line = ">1 "+currency+"</td>";
        //System.out.println(strona);
        //System.out.println(strona.indexOf(line));
        //System.out.println(strona.indexOf("4,5246"));
        //System.out.println(strona.indexOf("4,5246")-strona.indexOf(line));
        StringBuilder sb2 = new StringBuilder();
        for(int i = (strona.indexOf(String.valueOf(line))+50);i<(strona.indexOf(String.valueOf(currency)))+53;i++){
            sb2.append(strona.charAt(i));
        }
        String kursPLN = String.valueOf(sb2);
        //System.out.println(sb2.toString());

        kursWaluty=Double.parseDouble(kursPLN.replace(',','.'));
        return kursWaluty;
    }

    public String getWiki(){
        if(miasto!=null){
            return "https://en.wikipedia.org/wiki/"+miasto;
        }
        return null;
    }
}  
