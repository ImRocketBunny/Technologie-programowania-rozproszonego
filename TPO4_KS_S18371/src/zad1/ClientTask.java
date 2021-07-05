/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class ClientTask extends FutureTask<String> {
    private ClientTask(Callable<String> callable) {
        super(callable);
    }

    public static ClientTask create(Client c, List<String> reqList, boolean showRes) {
        Callable<String> callable = ()->{
            StringBuffer log = new StringBuffer();
            c.connect();
            c.send("login "+c.id);
            //log.append("=== "+c.id+" log start ===\n" +
             //       "logged in"+"\n");
            for(String req : reqList){
                String res = c.send(req);
                //log.append("Request: " + req+"\n"+"Result:"+"\n"+res+"\n");
                if(showRes) {
                    System.out.println(res);
                }
            }
            String l =(c.send("bye and log transfer"));
            log.append(l);

            return log.toString();
        };
        return new ClientTask(callable);
    }
}
