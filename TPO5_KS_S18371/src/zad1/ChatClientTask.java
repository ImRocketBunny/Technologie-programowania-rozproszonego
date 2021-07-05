/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<ChatClient> {
    public ChatClientTask(Callable<ChatClient> callable) {
        super(callable);
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        Callable<ChatClient> callable = ()-> {
            StringBuffer chatView = new StringBuffer();
            c.login();
            chatView.append("=== "+c.id+" chat view"+"\n");
            new Thread(()->{
                //while (nasluchiwac) {
                    try {
                        c.nasluch(chatView);

                    } catch (IOException e) {

                    }
                // }
            }).start();
            if (wait != 0){
                msgs.forEach(t->
                {
                    try {
                        c.send(t);
                        if (wait != 0){
                            Thread.sleep(wait);
                        }
                    } catch (IOException | InterruptedException e) {
                        //chatView.append("***"+e.toString());
                    }
                });
            }
            c.logout();
            if (wait != 0);
            Thread.sleep(wait);
            c.chatView=chatView.toString();
            return c;
        };
        return new ChatClientTask(callable);
    }

    public ChatClient getClient() {
            try {
                return this.get();
            }catch (ExecutionException e){

            }catch (InterruptedException e){

            }
            return null;
    }
}
