# TPO1
## Zadanie: kanały plikowe

Katalog {user.home}/TPO1dir  zawiera pliki tekstowe umieszczone w tym katalogu i jego różnych podkatalogach. Kodowanie plików to Cp1250.
Przeglądając rekursywnie drzewo katalogowe, zaczynające się od {user.home}/TPO1dir,  wczytywać te pliki i dopisywać ich zawartości do pliku o nazwie TPO1res.txt, znadującym się w katalogu projektu. Kodowanie pliku TPO1res.txt winno być UTF-8, a po każdym uruchomieniu programu plik ten powinien zawierać tylko aktualnie przeczytane dane z  plików katalogu/podkatalogow.

Poniższy gotowy fragment winien wykonać całą robotę:
```
      public class Main {
        public static void main(String[] args) {
          String dirName = System.getProperty("user.home")+"/TPO1dir";
          String resultFileName = "TPO1res.txt";
          Futil.processDir(dirName, resultFileName);
        }
      }
```
Uwagi:

•	pliku Main.java nie wolno w żaden sposób modyfikować,
•	trzeba dostarczyć definicji klasy Futil,
•	oczywiście, nazwa katalogu i pliku oraz ich położenie są obowiązkowe,
•	należy zastosować FileVisitor do przeglądania katalogu oraz kanały plikowe (klasa FileChannel) do odczytu/zapisu plików (bez tego rozwiązanie nie uzyska punktów).
•	w wynikach testów mogą być przedstawione dodatkowe zalecenia co do sposobu wykonania zadania (o ile rozwiązanie nie będzie jeszcze ich uwzględniać),.

# TPO2
## Zadanie: klienci usług sieciowych
Napisać aplikację, udostępniającą GUI, w którym po podanu miasta i nazwy kraju pokazywane są:

1.	Informacje o aktualnej pogodzie w tym mieście.
2.	Informacje o kursie wymiany walutu kraju wobec podanej przez uzytkownika waluty.
3.	Informacje o kursie NBP złotego wobec tej waluty podanego kraju.
4.	Strona wiki z opisem miasta.
W p. 1 użyć serwisu api.openweathermap.org, w p. 2 - serwisu exchangeratesapi.io, w p. 3 - informacji ze stron NBP: http://www.nbp.pl/kursy/kursya.html i http://www.nbp.pl/kursy/kursyb.html.
W p. 4 użyć klasy WebEngine z JavaFX dla wbudowania przeglądarki w aplikację Swingową.

Program winien zawierać klasę Service z konstruktorem Service(String kraj) i metodami::

•	String getWeather(String miasto) - zwraca informację o pogodzie w podanym mieście danego kraju w formacie JSON (to ma być pełna informacja uzyskana z serwisu openweather - po prostu tekst w formacie JSON),
•	Double getRateFor(String kod_waluty) - zwraca kurs waluty danego kraju wobec waluty podanej jako argument,
•	Double getNBPRate() - zwraca kurs złotego wobec waluty danego kraju
Następujące przykładowa klasa  pokazuje możliwe użycie tych metod:

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
  }
}
Uwaga 1: zdefiniowanie pokazanych metod w sposób niezalezny od GUI jest obowiązkowe.
Uwaga 2:  W katalogu projektu (np. w podkatalogu lib) nalezy umiescic wykorzystywane JARy (w przeciwnym razie program nie przejdzie kompilacji) i skonfigurowac Build Path tak, by wskazania na te JARy byly w Build Path zawarte.

# TPO3
## PASSTIME

Ćwiczenie stanowi przygotowanie do budowy serwera raportującego upływ czasu.
Jest to praktycznie logika "biznesowa" - będzie przydatna w zadaniu serwerowym.

Zadania

(1)  Parsowanie plików YAML

W klasie Tools dostarczyć metody:
```
    static Options createOptionsFromYaml(String fileName) throws Exception
```
która wczytuje podany plika YAML i na jego podstawie tworzy obiekt klasy Options. Klasa Options jest w projekcie.

Do parsowania użyć biblioeki SnakeYaml (https://mvnrepository.com/artifact/org.yaml/snakeyaml/1.26).

Przykładowa zawartość pliku YAML o nazwie PassTimeOptions.yaml jest nastepująca (proszę go umiescic w katalogu "user.home"):
```
host: localhost
port: 7777
concurMode: true   # czy klienci działają równolegle?
showSendRes: true  # czy pokazywać zwrócone przez serwer wyniki metody send(...)
clientsMap: # id_klienta -> lista żądań
  Asia:
    - 2016-03-30T12:00 2020-03-30T:10:15
    - 2019-01-10 2020-03-01
    - 2020-03-27T10:00 2020-03-28T10:00
    - 2016-03-30T12:00 2020-03-30T10:15
  Adam:
    - 2018-01-01 2020-03-27
    - 2019-01-01 2020-02-28
    - 2019-01-01 2019-02-29
    - 2020-03-28T10:00 2020-03-29T10:00
```
(2)  Raportowanie upływu czasu.

W klasie Time dostarczyć metody:

public static String passed(String from, String to)

zwracającej tekst opisujący upływ czas od daty from do daty to.

Daty są podawane jako napisy w formacie ISO-8601:
- data bez czasu: YYYY-MM-DD
- data z czasem: YYYY-MM-DDTGG:MM

Przyklady dat zawiera przykładowy plik  YAML.

Opis upływającego czasu ma formę:
```
Od x nazwa_miesiąca_PL (dzień_tygodnia_PL) do y nazwa_miesiąca_PL (dzień_tygodnia_PL)
- mija: d dni, tygodni t
[- godzin: g, minut: m ] 
[- kalendarzowo: [ r (lat|lata|rok}, ] [ m (miesięcy|miesiące|miesiąc ),]  [d  (dzień|dni)]  ]
```
gdzie x, y, d, g, m, r, - liczby całkowite
         t - jest liczbą całkowitą, gdy liczba tygodni jest całkowita, a rzeczywistą (dwa miejsca po kropce) w przeciwnym razie

Nawiasy kwadratowe oznaczają opcjonalność:
a) część opisująca upływ godzin i minut pojawia się tylko wtedy, gdy w formacie daty użyto czasu (np. 2020-10-10T10:00)
b) część "kalendarzowo:" pojawia się tylko wtedy, gdy minął co najmniej jeden dzień
c) w części kalendarzowej opis upływu lat pojawia się tylko wtedy, gdy minął co najmniej jeden rok, opis upływu miesięcy pojawia się tylko wtedy, gdy minął co najmniej jeden miesiąc, a opis upływu dni pojawia się tylko wtedy, gdy liczba dni nie mieści się w pełnych miesiącack.

Nawiasy okrągłe oznaczają alternatywę - dobór właściwej odmiany słowa dla danej liczby.
Gdy w datach podane są godziny należy uwzględnić zmianę czasu (DST) dla strefy czasowej "Europe/Warsaw".
Jeśli podano błędną datę lub datę-czas metoda ma zwrócić opis wyjątku poprzedzony trzema gwiazdkami.

Przykłady:
```
    println passed( "2000-01-01", "2020-04-01")
    println passed( "2018-01-01", "2020-02-02")
    println passed( "2019-01-01", "2020-04-03")
    println passed( "2020-04-01T10:00", "2020-04-01T13:00")
    println passed( "2020-03-27T10:00", "2020-03-28T10:00") // przed zmianą czasu
    println passed( "2020-03-28T10:00", "2020-03-29T10:00") // po zmianie czasu
    println passed( "2020-03-28T10", "2020-03-29T10:00")
    println passed( "2019-02-29", "2020-04-03")
```
Na konsoli:
```
Od 1 stycznia 2000 (sobota) do 1 kwietnia 2020 (środa)
 - mija: 7396 dni, tygodni 1056.57
 - kalendarzowo: 20 lat, 3 miesiące
Od 1 stycznia 2018 (poniedziałek) do 2 lutego 2020 (niedziela)
 - mija: 762 dni, tygodni 108.86
 - kalendarzowo: 2 lata, 1 miesiąc, 1 dzień
Od 1 stycznia 2019 (wtorek) do 3 kwietnia 2020 (piątek)
 - mija: 458 dni, tygodni 65.43
 - kalendarzowo: 1 rok, 3 miesiące, 2 dni
Od 1 kwietnia 2020 (środa) godz. 10:00 do 1 kwietnia 2020 (środa) godz. 13:00
 - mija: 0 dni, tygodni 0
 - godzin: 3, minut: 180
Od 27 marca 2020 (piątek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 24, minut: 1440
 - kalendarzowo: 1 dzień
Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 23, minut: 1380
 - kalendarzowo: 1 dzień
*** java.time.format.DateTimeParseException: Text '2020-03-28T10' could not be parsed at index 13
*** java.time.format.DateTimeParseException: Text '2019-02-29' could not be parsed: Invalid date 'February 29' as '2019' is not a leap year
```


Zawarte w projekcie klasy Main i Options są niemodyfikowalne.
Klasa Options:
```
package zad1;

import java.util.*;

public class Options {
 
  private String host;
  private int port;
  private boolean concurMode;
  private boolean showSendRes;
  private Map<String, List<String>> clientsMap = new LinkedHashMap<>();
 
  public Options(String host, int port, boolean concurMode, boolean showSendRes,
                 Map<String, List<String>> clientsMap) {
    this.host = host;
    this.port = port;
    this.concurMode = concurMode;
    this.showSendRes = showSendRes;
    this.clientsMap = clientsMap;
  }

  public String getHost() {
    return host;
  }
 
  public int getPort() {
    return port;
  }
 
  public boolean isConcurMode() {
    return concurMode;
  }
 
  public boolean isShowSendRes() {
    return showSendRes;
  }
 
  public Map<String, List<String>> getClientsMap() {
    return clientsMap;
  }
 
  public String toString() {
    String out = "";  // StringBuilder bardziej efektywny, ale za dużo pisania
    out += host + " " + port + " " + concurMode + " " + showSendRes + "\n";
    for (Map.Entry<String, List<String>> e : clientsMap.entrySet()) {
      out += e.getKey() + ": " + e.getValue() + "\n";
    }
    return out;
  }
 
}
```
Klasa Main:
```
package zad1;

public class Main {

  public static void main(String[] args) throws Exception {
    String fileName = System.getProperty("user.home") + "/PassTimeOptions.yaml";
    Options opts = Tools.createOptionsFromYaml(fileName);
    System.out.println(opts);
    opts.getClientsMap().forEach( (id, dates) -> {
      System.out.println(id);
      dates.forEach( dpair -> {
        String[] d = dpair.split(" +");
        String info = Time.passed(d[0], d[1]);
        System.out.println(info);
      });
    });
  }

}
```


Uruchomienie metody main z klasy Main (przy założeniu, że plik PassTimeOptions.yaml jest taki jak podano wyżej) spowoduje wyprowadzenie na knsolę następujących informacji:
```
localhost 7777 true true
Asia: [2016-03-30T12:00 2020-03-30T:10:15, 2019-01-10 2020-03-01, 2020-03-27T10:00 2020-03-28T10:00, 2016-03-30T12:00 2020-03-30T10:15]
Adam: [2018-01-01 2020-03-27, 2019-01-01 2020-02-28, 2019-01-01 2019-02-29, 2020-03-28T10:00 2020-03-29T10:00]

Asia
*** java.time.format.DateTimeParseException: Text '2020-03-30T:10:15' could not be parsed at index 11
Od 10 stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)
 - mija: 416 dni, tygodni 59.43
 - kalendarzowo: 1 rok, 1 miesiąc, 20 dni
Od 27 marca 2020 (piątek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 24, minut: 1440
 - kalendarzowo: 1 dzień
Od 30 marca 2016 (środa) godz. 12:00 do 30 marca 2020 (poniedziałek) godz. 10:15
 - mija: 1461 dni, tygodni 208.71
 - godzin: 35062, minut: 2103735
 - kalendarzowo: 4 lata
Adam
Od 1 stycznia 2018 (poniedziałek) do 27 marca 2020 (piątek)
 - mija: 816 dni, tygodni 116.57
 - kalendarzowo: 2 lata, 2 miesiące, 26 dni
Od 1 stycznia 2019 (wtorek) do 28 lutego 2020 (piątek)
 - mija: 423 dni, tygodni 60.43
 - kalendarzowo: 1 rok, 1 miesiąc, 27 dni
*** java.time.format.DateTimeParseException: Text '2019-02-29' could not be parsed: Invalid date 'February 29' as '2019' is not a leap year
Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 23, minut: 1380
 - kalendarzowo: 1 dzień
```
Format wydruku jest obowiązkowy.

Pomoc:
zastosowac klasy Java Time API (pakiet java.time) m.in.
LocalDate, LocalDateTime, ZonedDateTime, ChronoUnit, Period.

# TPO4
## Zadanie PASSTIME_SERVER

Serwer (klasa Server) podaje informacje o mijającym czasie. Klienci (klasa Client) :
a) łączą się z serwerem
b) wysyłają żądania

Dla każdego klienta serwer prowadzi log jego zapytań i ich wyników oraz ogólny log wszystkich żądań wszystkich klientów. Logi są realizowane w pamięci wewnętrznej serwera (poza systemem plikowym).

Protokół

Żądanie	Odpowiedź	Przykład
```
login id	logged in	login Adam
dataOd dataDo	opis upływu czasu wg secyfikacji z S_PASSTIME
2019-01-20 2020-04-01          
bye	logged out	
bye and log transfer	zawartośc logu klienta	w przykładowym wydruku z działania klasy Main
```

Budowa klasy Server

konstrukto: :
```
public Server(String host, int port)
```
Wymagane metody:

•	 metoda: public void startServer() - uruchamia server w odrębnym wątku,
•	 metoda:  public void stopServer()  - zatrzymuje działanie serwera i wątku w którym działa
•	 metoda: String getServerLog() - zwraca ogólny log serwera
Wymagania konstrukcyjne dla klasy Server

•	multipleksowania kanałów gniazd (użycie selektora).
•	serwer może obsługiwać równolegle wielu klientów, ale obsługa żądań klientów odbywa się w jednym wątku
Budowa klasy Client

konstruktor:
```
public Client(String host, int port, String id), gdzie id - identyfikator klienta
```
Wymagane metody:

•	metoda: public void connect() - łączy z serwerem
•	metoda: public String send(String req) - wysyła żądanie req i zwraca odpowiedź serwera
Wymagania konstrukcyjne dla klasy Client

•	nieblokujące wejście - wyjście

Dodatkowo stworzyć klasę ClientTask, umożliwiającą uruchamianie klientów w odrębnych wątkach poprzez ExecutorService.
Obiekty tej klasy tworzy statyczna metoda:
```
     public static ClientTask create(Client c, List<String> reqs, boolean showSendRes)
```
gdzie:
c - klient (obiekt klasy Client)
reqs - lista zapytań o uplyw czasu

Kod działający w wątku ma wykonywać następując działania:

•	łączy się z serwerem,
•	wysyła żądanie "login" z identyfikatorem klienta
•	wysyła kolejne żądania z listy reqs
•	wysyła "bye and log transfer" i odbiera od serwera log zapytań i ich wyników dla danego klienta
Jeżeki parametr showSendRes jest true to po każdym send odpowiedź serwera jest wypisywana na konsoli. Niezależnie od wartości parametru należy zapewnić, by log klienta był dostępny jak tylko klient zakończy działanie.

Dodatkowo dostarczyć klasy Time (logika obliczania czasu) oraz Tools (wczytywanie opcji i żadań klientów, potrzebnych do dzialania klasy Main).

Przygotowana klasa Main ilustruje przypadki interakcji klient-serwer:
```
package zad1;

import java.util.*;
import java.util.concurrent.*;

public class Main {

  public static void main(String[] args) throws Exception {
    String fileName = System.getProperty("user.home") + "/PassTimeServerOptions.yaml";
    Options opts = Tools.createOptionsFromYaml(fileName);
    String host = opts.getHost();
    int port = opts.getPort();
    boolean concur =  opts.isConcurMode();
    boolean showRes = opts.isShowSendRes();
    Map<String, List<String>> clRequests = opts.getClientsMap();
    ExecutorService es = Executors.newCachedThreadPool();
    List<ClientTask> ctasks = new ArrayList<>();
    List<String> clogs = new ArrayList<>();
   
    Server s = new Server(host, port);
    s.startServer();
   
    // start clients
    clRequests.forEach( (id, reqList) -> {
      Client c = new Client(host, port, id);
      if (concur) {
        ClientTask ctask = ClientTask.create(c, reqList, showRes);
        ctasks.add(ctask);
        es.execute(ctask);
      } else {
        c.connect();
        c.send("login " + id);
        for(String req : reqList) {
          String res = c.send(req);
          if (showRes) System.out.println(res);
        }
        String clog = c.send("bye and log transfer");
        System.out.println(clog);
      }
    });
   
    if (concur) {
      ctasks.forEach( task -> {
        try {
          String log = task.get();
          clogs.add(log);
        } catch (InterruptedException | ExecutionException exc) {
          System.out.println(exc);
        }
      });
      clogs.forEach( System.out::println);
      es.shutdown();
    }
    s.stopServer();
    System.out.println("\n=== Server log ===");
    System.out.println(s.getServerLog());
  }

}
```
Wyniki na konsoli
A. Zawartośc pliku PassTimeServerOptions.yaml
```
host: localhost
port: 7777
concurMode: false   # czy klienci działają równolegle?
showSendRes: false  # czy pokazywać zwrócone przez serwer wyniki metody send(...)
clientsMap: # id_klienta -> lista żądań
  Asia:
    - 2019-01-10 2020-03-01
    - 2020-03-27T10:00 2020-03-28T10:00
  Adam:
    - 2018-01-01 2020-03-27
    - 2020-03-28T10:00 2020-03-29T10:00
```
A. Wynik:
```
=== Asia log start ===
logged in
Request: 2019-01-10 2020-03-01
Result:
Od 10 stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)
 - mija: 416 dni, tygodni 59.43
 - kalendarzowo: 1 rok, 1 miesiąc, 20 dni
Request: 2020-03-27T10:00 2020-03-28T10:00
Result:
Od 27 marca 2020 (piątek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 24, minut: 1440
 - kalendarzowo: 1 dzień
logged out
=== Asia log end ===

=== Adam log start ===
logged in
Request: 2018-01-01 2020-03-27
Result:
Od 1 stycznia 2018 (poniedziałek) do 27 marca 2020 (piątek)
 - mija: 816 dni, tygodni 116.57
 - kalendarzowo: 2 lata, 2 miesiące, 26 dni
Request: 2020-03-28T10:00 2020-03-29T10:00
Result:
Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 23, minut: 1380
 - kalendarzowo: 1 dzień
logged out
=== Adam log end ===


=== Server log ===
Asia logged in at 16:54:09.507
Asia request at 16:54:09.554: "2019-01-10 2020-03-01"
Asia request at 16:54:10.193: "2020-03-27T10:00 2020-03-28T10:00"
Asia logged out at 16:54:10.256
Adam logged in at 16:54:10.318
Adam request at 16:54:10.382: "2018-01-01 2020-03-27"
Adam request at 16:54:10.444: "2020-03-28T10:00 2020-03-29T10:00"
Adam logged out at 16:54:10.506
```

B, W pliku PassTimeServerOptions.yaml ustawiono:
concurMode: true   # czy klienci działają równolegle?
showSendRes: false  # czy pokazywać zwrócone przez serwer wyniki metody send(...)

B. Wynik - zmienia się log serwera:
```
=== Server log ===
Asia logged in at 16:59:23.494
Adam logged in at 16:59:23.494
Asia request at 16:59:23.541: "2019-01-10 2020-03-01"
Adam request at 16:59:24.071: "2018-01-01 2020-03-27"
Asia request at 16:59:24.102: "2020-03-27T10:00 2020-03-28T10:00"
Adam request at 16:59:24.118: "2020-03-28T10:00 2020-03-29T10:00"
Asia logged out at 16:59:24.165
Adam logged out at 16:59:24.165
```

C, W pliku PassTimeServerOptions.yaml ustawiono:
concurMode: false  # czy klienci działają równolegle?
showSendRes: true  # czy pokazywać zwrócone przez serwer wyniki metody send(...)

C. Wyniki:
```
Od 10 stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)
 - mija: 416 dni, tygodni 59.43
 - kalendarzowo: 1 rok, 1 miesiąc, 20 dni
Od 27 marca 2020 (piątek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 24, minut: 1440
 - kalendarzowo: 1 dzień
=== Asia log start ===
logged in
Request: 2019-01-10 2020-03-01
Result:
Od 10 stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)
 - mija: 416 dni, tygodni 59.43
 - kalendarzowo: 1 rok, 1 miesiąc, 20 dni
Request: 2020-03-27T10:00 2020-03-28T10:00
Result:
Od 27 marca 2020 (piątek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 24, minut: 1440
 - kalendarzowo: 1 dzień
logged out
=== Asia log end ===

Od 1 stycznia 2018 (poniedziałek) do 27 marca 2020 (piątek)
 - mija: 816 dni, tygodni 116.57
 - kalendarzowo: 2 lata, 2 miesiące, 26 dni
Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 23, minut: 1380
 - kalendarzowo: 1 dzień
=== Adam log start ===
logged in
Request: 2018-01-01 2020-03-27
Result:
Od 1 stycznia 2018 (poniedziałek) do 27 marca 2020 (piątek)
 - mija: 816 dni, tygodni 116.57
 - kalendarzowo: 2 lata, 2 miesiące, 26 dni
Request: 2020-03-28T10:00 2020-03-29T10:00
Result:
Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
 - mija: 1 dzień, tygodni 0.14
 - godzin: 23, minut: 1380
 - kalendarzowo: 1 dzień
logged out
=== Adam log end ===


=== Server log ===
Asia logged in at 17:02:34.537
Asia request at 17:02:34.583: "2019-01-10 2020-03-01"
Asia request at 17:02:35.145: "2020-03-27T10:00 2020-03-28T10:00"
Asia logged out at 17:02:35.207
Adam logged in at 17:02:35.207
Adam request at 17:02:35.207: "2018-01-01 2020-03-27"
Adam request at 17:02:35.270: "2020-03-28T10:00 2020-03-29T10:00"
Adam logged out at 17:02:35.335

```
Podsumowanie - klasy w projekcie i co należy zrobić.

Klasa	Uwagi
Main	jest w projekcie - niemodyfikowalna
Options	jest w projekcie - niemodyfikowalna
Time	do zrobienia wg specyfikacji S_PASSTIME   ew. już gotowa z zad. 3  

Tools	do zrobienia wg specyfikacji S_PASSTIME   ew. już gotowa z zad. 3  

Server	do zrobienia
Client	do zrobienia
ClientTask	do zrobienia


Uwaga: dobrą praktyką byłoby rozdzielenie klas na różne pakiety (server, client, tools itp.), ale NIE ROBIMY TEGO,  by  nie zwiększac już i tak trochę skomplikowanej struktury zadania. Wszystkie klasy są więc w pakiecie zad1.


# TPO5
## Zadanie SIMPLECHAT_NIO

Napisać serwer czatu, który:
obsługuje logowanie klientów (tylko id, bez hasła),
przyjmuje od klientów wiadomości i rozsyła je do zalogowanych klientów,
obsługuje wylogowanie klientów,
gromadzi wszystkie odpowiedzi na żądania klientów w logu, realizowanym w pamięci wewnętrznej (poza systemem plikowym).

Zadania te wykonuje klasa ChatServer, która ma:
konstruktor: public ChatServer(String host, int port)
metodę: public void startServer(), która uruchamia serwer w odrębnym wątku,
metodę: public void stopServer(), która zatrzymuje serwer i wątek, w którym działa,
 metodę String getServerLog() - zwraca  log serwera (wymagany format logu będzie widoczny w dalszych przykładach).

Wymagania konstrukcyjne dla klasy ChatServer:
multipleksowania kanałów gniazd (użycie selektora),
serwer może obsługiwać równolegle wielu klientów, ale obsługa żądań klientów odbywa się w jednym wątku,

Dostarczyć także klasy ChatClient z konstruktorem:
```
 public ChatClient(String host, int port, String id), gdzie id - id klienta
```
i następującymi metodami:
public void login() - loguje klienta na serwer
public void logout() - wylogowuje klienta,
public void send(String req)  - wysyła do serwera żądanie req
public String getChatView() - zwraca dotychczasowy widok czatu z pozycji danego klienta (czyli wszystkie infomacje, jakie dostaje on po kolei od serwera)
Dla metody send żądaniem może być posłanie tekstu wiadomości, zalogowanie, wylogowanie, a protokół komunikacji z  serwerem można po swojemu wymyślić.

Wymagania konstrukcyjne dla klasy ChatClient
nieblokujące wejście - wyjście

Dodatkowo stworzyć klasę ChatClientTask, umożliwiającą uruchamianie klientów w odrębnych wątkach poprzez ExecutorService.
Obiekty tej klasy tworzy statyczna metoda:
```
     public static ChatClientTask create(Client c, List<String> msgs, int wait)
```
gdzie:
c - klient (obiekt klasy Client)
msgs - lista wiadomości do wysłania przez klienta c
wait - czas wstrzymania pomiędzy wysyłaniem żądań.

Kod działający w wątku ma wykonywać następując działania:
łączy się z serwerem i loguje się (c.login()
wysyła kolejne wiadomości z listy msgs (c.send(...))
wylogowuje klienta (c.logout())

Parametr wait w sygnaturze metodu create oznacza czas w milisekundach, na jaki wątek danego klienta jest wstrzymywany po każdym żądaniu. Jeśli wait jest 0, wątek klienta nie jest wstrzymywany,

Oto pseudokod fragmentu odpowiedzialnego za posylanie żądań:
```
      c.login();
      if (wait != 0) uśpienie_watku_na wait ms;
      // ....
        dla_każdej_wiadomości_z_listy msgs {
          // ...
          c.send( wiadomość );
          if (wait != 0) uśpienie_watku_na wait ms;
        }
      c.logout();
      if (wait != 0) uśpienie_watku_na wait ms;
```
W projekcie znajduje się klasa Main (plik niemodyfikowalny), w której z pliku testowego wprowadzane są informacje nt. konfiguracji serwera (host, port) oraz  klientów (id, czas wstrzymania wątku po każdym żądaniu, zestaw wiadomości do wysłania).

Format pliku testowego:
pierwszy wiersz: nazwa_hosta
drugi wiersz: nr portu
kolejne wiersze:
```
id_klienta<TAB>parametr wait w ms<TAB>msg1<TAB>msg2<TAB> ....  <TAB>msgN
```

Klasa Main:
```
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
 
  public static void main(String[] args) throws Exception {

    String testFileName = System.getProperty("user.home") + "/ChatTest.txt";
    List<String> test = Files.readAllLines(Paths.get(testFileName));
    String host = test.remove(0);
    int port = Integer.valueOf(test.remove(0));
    ChatServer s = new ChatServer(host, port);
    s.startServer();
   
    ExecutorService es = Executors.newCachedThreadPool();
    List<ChatClientTask> ctasks = new ArrayList<>();
   
    for (String line : test) {
      String[] elts = line.split("\t");
      String id = elts[0];
      int wait = Integer.valueOf(elts[1]);
      List<String> msgs = new ArrayList<>();
      for (int i = 2; i < elts.length; i++) msgs.add(elts[i] + ", mówię ja, " +id);
      ChatClient c = new ChatClient(host, port, id);
      ChatClientTask ctask = ChatClientTask.create(c, msgs, wait);
      ctasks.add(ctask);
      es.execute(ctask);
    }
    ctasks.forEach( task -> {
      try {
        task.get();
      } catch (InterruptedException | ExecutionException exc) {
        System.out.println("*** " + exc);
      }
    });
    es.shutdown();
    s.stopServer();
   
    System.out.println("\n=== Server log ===");
    System.out.println(s.getServerLog());

    ctasks.forEach(t -> System.out.println(t.getClient().getChatView())); 
  }
}
```

Dla pliku testowego o treści:
```
localhost
9999
Asia    50    Dzień dobry    aaaa    bbbb    Do widzenia
Adam    50    Dzień dobry    aaaa    bbbb    Do widzenia
Sara    50    Dzień dobry    aaaa    bbbb    Do widzenia
```


program ten może wyprowadzić:
```
Server started

Server stopped

=== Server log ===
00:25:08.698 Asia logged in
00:25:08.698 Sara logged in
00:25:08.745 Adam logged in
00:25:08.745 Sara: Dzień dobry, mówię ja, Sara
00:25:08.745 Asia: Dzień dobry, mówię ja, Asia
00:25:08.807 Sara: aaaa, mówię ja, Sara
00:25:08.807 Adam: Dzień dobry, mówię ja, Adam
00:25:08.807 Asia: aaaa, mówię ja, Asia
00:25:08.869 Adam: aaaa, mówię ja, Adam
00:25:08.869 Sara: bbbb, mówię ja, Sara
00:25:08.869 Asia: bbbb, mówię ja, Asia
00:25:08.932 Adam: bbbb, mówię ja, Adam
00:25:08.932 Sara: Do widzenia, mówię ja, Sara
00:25:08.932 Asia: Do widzenia, mówię ja, Asia
00:25:08.994 Sara logged out
00:25:08.994 Adam: Do widzenia, mówię ja, Adam
00:25:08.994 Asia logged out
00:25:09.057 Adam logged out

=== Asia chat view
Asia logged in
Sara logged in
Adam logged in
Sara: Dzień dobry, mówię ja, Sara
Asia: Dzień dobry, mówię ja, Asia
Sara: aaaa, mówię ja, Sara
Adam: Dzień dobry, mówię ja, Adam
Asia: aaaa, mówię ja, Asia
Adam: aaaa, mówię ja, Adam
Sara: bbbb, mówię ja, Sara
Asia: bbbb, mówię ja, Asia
Adam: bbbb, mówię ja, Adam
Sara: Do widzenia, mówię ja, Sara
Asia: Do widzenia, mówię ja, Asia
Sara logged out
Adam: Do widzenia, mówię ja, Adam
Asia logged out

=== Adam chat view
Adam logged in
Sara: Dzień dobry, mówię ja, Sara
Asia: Dzień dobry, mówię ja, Asia
Sara: aaaa, mówię ja, Sara
Adam: Dzień dobry, mówię ja, Adam
Asia: aaaa, mówię ja, Asia
Adam: aaaa, mówię ja, Adam
Sara: bbbb, mówię ja, Sara
Asia: bbbb, mówię ja, Asia
Adam: bbbb, mówię ja, Adam
Sara: Do widzenia, mówię ja, Sara
Asia: Do widzenia, mówię ja, Asia
Sara logged out
Adam: Do widzenia, mówię ja, Adam
Asia logged out
Adam logged out
```

Przyklad pokazuje wymaganą formę wydruku (chatView klienta, wejścia w logu serwera). W szczególności:
start serwera powoduje wypisanie na konsoli: Server started
logowanie klienta id skutkuje rozesłaniem wiadomości: id logged in
wylogowanie klienta id skutkuje rozesłaniem wiadomości: id logged out
otrzymanie wiadomości msg od klienta id skutkuje rozeslaniem wiadomości id: msg
widok czatu zwracany przez client.getChatView() dla klienta id jest poprzedzony nagłówkiem:  === id chat view
log serwera jest poprzedzony nagłówkiem === Server log ===  i zawiera kolejno wszystkie odpowiedzi serwera z podaniem czasu w formacie HH:MM:SS.nnn, gdzie nnn - milisekundy (czas wg zegara systemowego),
zatrzymanie serwera wypisuje na konsoli: Server stopped
wszelkie błędy w interakcji klienta z serwerem (wyjątki exc, np. IOException) powinny być dodawane do chatView klienta  jako exc.toString() poprzedzone trzema gwiazdakami
Forma wydruku jest obowiązkowa, a jej niedotrzymanie powoduje utratę punktów.
Konkretna zawartość wydruków chatview i logu seerwera (kolejność wierszy, podane  czasy itp.) mogą być w każdym przebiegu inne, ważne jednak, aby widac było równoległą obsługę klientów oraz zachowaną logikę: klienci otrzymują, w odpowiedniej kolejności, tylko te wiadomości, które się pojawiły  od momentu ich logowania do momentu wylogowania.

Podusumowanie:
trzeba stworzyć klasy ChatServer, ChatClient, ChatClientTask w taki sposób, aby zapewnić właściwe wykonanie  kodu metody main z klasy Main

Ale
trzeba przygotować kod  na rozliczne konfiguracje podawane w pliku ChatTest.txt.
Przyklady wyników Main.main():
Dla:
```
localhost
33333
Asia    20    Dzień dobry    beee    Do widzenia
Sara    20    Dzień dobry    muuu    Do widzenia
```
Wynik:
```
Server started

Server stopped

=== Server log ===
01:18:43.723 Asia logged in
01:18:43.723 Sara logged in
01:18:43.738 Asia: Dzień dobry, mówię ja, Asia
01:18:43.738 Sara: Dzień dobry, mówię ja, Sara
01:18:43.769 Sara: muuu, mówię ja, Sara
01:18:43.769 Asia: beee, mówię ja, Asia
01:18:43.801 Asia: Do widzenia, mówię ja, Asia
01:18:43.801 Sara: Do widzenia, mówię ja, Sara
01:18:43.832 Asia logged out
01:18:43.832 Sara logged out

=== Asia chat view
Asia logged in
Sara logged in
Asia: Dzień dobry, mówię ja, Asia
Sara: Dzień dobry, mówię ja, Sara
Sara: muuu, mówię ja, Sara
Asia: beee, mówię ja, Asia
Asia: Do widzenia, mówię ja, Asia
Sara: Do widzenia, mówię ja, Sara
Asia logged out

=== Sara chat view
Sara logged in
Asia: Dzień dobry, mówię ja, Asia
Sara: Dzień dobry, mówię ja, Sara
Sara: muuu, mówię ja, Sara
Asia: beee, mówię ja, Asia
Asia: Do widzenia, mówię ja, Asia
Sara: Do widzenia, mówię ja, Sara
Asia logged out
Sara logged out


Dla:
localhost
55557
Asia    10    Dzień dobry    beee    Do widzenia
Sara    20    Dzień dobry    muuu    Do widzenia

Server started

Server stopped

=== Server log ===
01:25:33.293 Asia logged in
01:25:33.293 Asia: Dzień dobry, mówię ja, Asia
01:25:33.308 Asia: beee, mówię ja, Asia
01:25:33.324 Sara logged in
01:25:33.324 Asia: Do widzenia, mówię ja, Asia
01:25:33.339 Asia logged out
01:25:33.355 Sara: Dzień dobry, mówię ja, Sara
01:25:33.386 Sara: muuu, mówię ja, Sara
01:25:33.417 Sara: Do widzenia, mówię ja, Sara
01:25:33.449 Sara logged out

=== Asia chat view
Asia logged in
Asia: Dzień dobry, mówię ja, Asia
Asia: beee, mówię ja, Asia
Sara logged in
Asia: Do widzenia, mówię ja, Asia
Asia logged out

=== Sara chat view
Sara logged in
Asia: Do widzenia, mówię ja, Asia
Asia logged out
Sara: Dzień dobry, mówię ja, Sara
Sara: muuu, mówię ja, Sara
Sara: Do widzenia, mówię ja, Sara
Sara logged out
```
A dla takiej konfiguracji (zagłodzenie wątku):
```
localhost
55557
Asia    0    Dzień dobry    beee    Do widzenia
Sara    0    Dzień dobry    muuu    Do widzenia
```
Wynik:
```
Server started

Server stopped

=== Server log ===
01:50:13.603 Asia logged in
01:50:13.603 Asia: Dzień dobry, mówię ja, Asia
01:50:13.603 Asia: beee, mówię ja, Asia
01:50:13.603 Asia: Do widzenia, mówię ja, Asia
01:50:13.603 Asia logged out
01:50:13.634 Sara logged in
01:50:13.634 Sara: Dzień dobry, mówię ja, Sara
01:50:13.634 Sara: muuu, mówię ja, Sara
01:50:13.634 Sara: Do widzenia, mówię ja, Sara
01:50:13.634 Sara logged out

=== Asia chat view
Asia logged in
Asia: Dzień dobry, mówię ja, Asia
Asia: beee, mówię ja, Asia
Asia: Do widzenia, mówię ja, Asia
Asia logged out

=== Sara chat view
Sara logged in
Sara: Dzień dobry, mówię ja, Sara
Sara: muuu, mówię ja, Sara
Sara: Do widzenia, mówię ja, Sara
Sara logged out
```


Uwaga: plik Main.java jest niemodyfikowalny.
