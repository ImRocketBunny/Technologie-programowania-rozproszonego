/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
    public static String passed(String s, String s1) {
        Locale pl = new Locale("pl");
        Locale none = new Locale("xx");
        if(s.length()<=10){
            try {
                String kalendarzowo = "";
                String dpatt = "d MMMM yyyy (EEEE)";
                LocalDate lt = LocalDate.parse(s);
                LocalDate lt2 = LocalDate.parse(s1);
                Period pero = Period.between(lt, lt2);
                if(pero.getYears()>0 && pero.getYears()<2){
                    kalendarzowo+=pero.getYears()+" rok";
                }else if(pero.getYears()>=2 && pero.getYears()<5){
                    kalendarzowo+=pero.getYears()+" lata";
                }else if(pero.getYears()>=5){
                    kalendarzowo+=pero.getYears()+" lat";
                }
                if(pero.getMonths()>0&&pero.getMonths()<2) {
                    kalendarzowo +=", "+pero.getMonths() + " miesiąc, ";
                }else if(pero.getMonths()>=2 && pero.getMonths()<5){
                    kalendarzowo += ", "+pero.getMonths() + " miesiące, ";
                }else if(pero.getMonths()>=5){
                    kalendarzowo += ", "+pero.getMonths() + " miesięcy, ";
                }
                if(pero.getDays()>0&&pero.getDays()<2){
                    kalendarzowo+= pero.getDays()+" dzien";
                }else if(pero.getDays()>=2){
                    kalendarzowo+= pero.getDays()+" dni";
                }
                NumberFormat nf = NumberFormat.getNumberInstance(none);
                DecimalFormat cf = (DecimalFormat)nf;
                cf.applyPattern("###.##");
                double liczba = (ChronoUnit.DAYS.between(lt, lt2)/7.0);
                String dzienDni = ChronoUnit.DAYS.between(lt, lt2)>1 ? " dni":" dzień";
                return "Od "+lt.format( DateTimeFormatter.ofPattern(dpatt) )+ " do "+lt2.format( DateTimeFormatter.ofPattern(dpatt) )+"\n"+
                        " - mija: "+ ChronoUnit.DAYS.between(lt, lt2) + dzienDni+", tygodni "+cf.format(liczba)+"\n"+
                        " - kalendarzowo: "+kalendarzowo;

            }catch (DateTimeException e){
                return "*** "+e.toString();
            }
        } else {
            try {
                String kalendarzowo = "";
                String tpatt = "d MMMM yyyy (EEEE) 'godz.' HH:mm";
                LocalDateTime lt = LocalDateTime.parse(s);
                LocalDateTime lt2 = LocalDateTime.parse(s1);
                ZonedDateTime zdt1 = ZonedDateTime.of(lt, ZoneId.of("Europe/Warsaw"));
                ZonedDateTime zdt2 = ZonedDateTime.of(lt2, ZoneId.of("Europe/Warsaw"));
                LocalDate ltt = lt.toLocalDate();
                LocalDate ltt2 = lt2.toLocalDate();
                Period pero = Period.between(ltt,ltt2);
                if(pero.getYears()>0 && pero.getYears()<2){
                    kalendarzowo+=pero.getYears()+" rok";
                }else if(pero.getYears()>=2 && pero.getYears()<5){
                    kalendarzowo+=pero.getYears()+" lata";
                }else if(pero.getYears()>=5){
                    kalendarzowo+=pero.getYears()+" lat";
                }
                if(pero.getMonths()>0&&pero.getMonths()<2) {
                    kalendarzowo +=", "+pero.getMonths() + " miesiąc, ";
                }else if(pero.getMonths()>=2 && pero.getMonths()<5){
                    kalendarzowo += ", "+pero.getMonths() + " miesiące, ";
                }else if(pero.getMonths()>=5){
                    kalendarzowo += ", "+pero.getMonths() + " miesięcy, ";
                }
                if(pero.getDays()>0&&pero.getDays()<2){
                    kalendarzowo+= pero.getDays()+" dzień";
                }else if(pero.getDays()>=2){
                    kalendarzowo+= pero.getDays()+" dni";
                }
                String dzienDni = ChronoUnit.DAYS.between(lt.toLocalDate(), lt2.toLocalDate())>1 ? " dni":" dzień";
                NumberFormat nf = NumberFormat.getNumberInstance(none);
                DecimalFormat cf = (DecimalFormat)nf;
                cf.applyPattern("###.##");
                double liczba = (ChronoUnit.DAYS.between(lt, lt2)/7.0);
                return "Od "+lt.format( DateTimeFormatter.ofPattern(tpatt))+" do "+lt2.format( DateTimeFormatter.ofPattern(tpatt))+"\n"+ //stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)\n" +
                        " - mija: "+ ChronoUnit.DAYS.between(lt.toLocalDate(), lt2.toLocalDate()) + dzienDni+", tygodni "+cf.format(liczba)+"\n"+
                        " - godzin: "+ChronoUnit.HOURS.between(zdt1, zdt2)+", minut: "+ChronoUnit.MINUTES.between(zdt1, zdt2)+"\n"+
                        " - kalendarzowo: "+kalendarzowo;

            }catch (DateTimeException e){

                return "*** "+e.toString();
            }
        }
        //return "";
    }
}
