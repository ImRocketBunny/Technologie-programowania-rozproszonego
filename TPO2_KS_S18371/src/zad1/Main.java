/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONException;
import org.json.JSONObject;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;


public class Main extends Application {
  static String weatherJson;
  static Service kraj;
  static Double rate1;
  static Double rate2;
  public Scene scenaMain;
  static String wikiAdr;
  public static void main(String[] args) throws IOException, JSONException {

    kraj = new Service("Hungary");
    weatherJson = kraj.getWeather("Budapest");
    rate1 = kraj.getRateFor("EUR");
    //rate2 = kraj.getNBPRate();
    wikiAdr=kraj.getWiki();
    // ...
    // część uruchamiająca GUI
    launch(args);
  }
  public String pogodaToString(String tekst) throws JSONException {
    String tekstPoFormacie = "";
        JSONObject jsonObj=new JSONObject(tekst);
        if(!jsonObj.isNull("main")){
            tekstPoFormacie += "Temperatura: " + jsonObj.getJSONObject("main").get("temp") + "\n";
            tekstPoFormacie += "Temperatura odczuwalna: "+jsonObj.getJSONObject("main").get("feels_like")+"\n";
            tekstPoFormacie += "Temperatura minimalna: " + jsonObj.getJSONObject("main").get("temp_min") + "\n";
            tekstPoFormacie += "Temperatura maksymalna: " + jsonObj.getJSONObject("main").get("temp_max") + "\n";
            tekstPoFormacie += "Wilgotnosc: " + jsonObj.getJSONObject("main").get("humidity") +'%'+ "\n";
            tekstPoFormacie += "Cisnienie: " + jsonObj.getJSONObject("main").get("pressure");
        }
        return tekstPoFormacie;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    WebView przegladarka;
    WebEngine engineWeb;
    BorderPane mainPanel, browserPanel;
    primaryStage.setTitle("Informacje o miescie");
    TabPane zakladkiPanel = new TabPane();;
    Tab zakladka0, zakladka1;
    zakladka0=new Tab("Informacje");
    zakladka1=new Tab("Wikipedia");
    mainPanel = new BorderPane();
    mainPanel.setPadding(new Insets(15, 15, 15, 15));
    javafx.scene.text.Text text1 = new javafx.scene.text.Text("Obecna Pogoda w "+kraj.miasto+"\n"+(pogodaToString(weatherJson))+"\n"
    +"Kurs waluty "+kraj.currencyInCountry+ " wzgledem 1 "+kraj.baseMoney+" to\n"
            +rate1+"\n"+
            "KursNBP waluty "+kraj.currencyInCountry+" to\n"+
            rate2);
    text1.setStyle("-fx-font: 48 arial;");
    mainPanel.setTop(text1);
    zakladka0.setContent(mainPanel);
    browserPanel=new BorderPane();
    przegladarka = new WebView();
    engineWeb=przegladarka.getEngine();
    engineWeb.load(wikiAdr);
    browserPanel.setCenter(przegladarka);
    zakladka1.setContent(browserPanel);
    zakladkiPanel.getTabs().addAll(zakladka0, zakladka1);
    zakladkiPanel.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    scenaMain = new Scene(zakladkiPanel, 1280, 1024);
    primaryStage.setScene(scenaMain);
    primaryStage.setWidth(1280);
    primaryStage.setHeight(1024);
    primaryStage.show();
  }
}
