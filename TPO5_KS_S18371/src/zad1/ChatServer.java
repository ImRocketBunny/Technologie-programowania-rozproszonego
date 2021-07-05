/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.Key;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ChatServer {
    private StringBuffer remsg;
    private static Pattern reqPatt = Pattern.compile(" +", 3);
    //private StringBuffer clientMessageStrinfg = new StringBuffer();
    private StringBuffer reqString = new StringBuffer();
    private static Charset charset  = Charset.forName("ISO-8859-2");
    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);
    private static final int BSIZE = 1024;
    private ServerSocketChannel ssc = null;
    private Selector selector = null;
    ExecutorService eServer = Executors.newCachedThreadPool();
    boolean serverIsRunning = false;
    StringBuilder serverLog = new StringBuilder();
    List<String> loginy =new ArrayList<>();
    public HashMap<SocketChannel,String> socketLogin = new HashMap<>();
    public HashMap<String,SocketChannel> loginSocket= new HashMap<>();
    public ChatServer(String host, int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    private void writeResp(SocketChannel sc, String addMsg) throws IOException {
        remsg= new StringBuffer();
        remsg.append(addMsg);
        ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
        sc.write(buf);
    }

    public void startServer() {
        eServer.execute(()->{
            serverService();
        });
        System.out.println("Server started"+"\n");
    }

    public void stopServer() {
        serverIsRunning=false;
        eServer.shutdownNow();
        System.out.println("Server stopped");

    }

    public String getServerLog() {
        return serverLog.toString();
    }
    public void serverService(){
        serverIsRunning = true;
        while(serverIsRunning == true) {
            try {
                selector.select();

                Set keys = selector.selectedKeys();

                Iterator iter = keys.iterator();
                while (iter.hasNext()) {

                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        SocketChannel cc = ssc.accept();
                        cc.configureBlocking(false);
                        cc.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel cc = (SocketChannel) key.channel();
                        if (!cc.isOpen())
                            return;
                        reqString.setLength(0);
                        bbuf.clear();
                        try {
                            readLoop:
                            while (true) {// kontynujemy je dopóki
                                int n = cc.read(bbuf);     // nie natrafimy na koniec wiersza
                                if (n > 0) {
                                    bbuf.flip();
                                    CharBuffer cbuf = charset.decode(bbuf);
                                    while (cbuf.hasRemaining()) {
                                        char c = cbuf.get();
                                        if (c == '\r' || c == '\n') break readLoop;
                                        reqString.append(c);
                                    }
                                }
                            }
                            String[] req = reqPatt.split(reqString, 2);
                            String cmd = req[0];
                            if(!(cmd.equals("logout")||cmd.equals("login"))) {
                                serverLog.append(LocalTime.now()+" "+socketLogin.get(cc)+": "+req[0]+" "+req[1]+"\n");
                                for (Map.Entry<String, SocketChannel> entry : loginSocket.entrySet()
                                ) {
                                    writeResp(entry.getValue(),socketLogin.get(cc)+": "+req[0]+" "+req[1]+"\n");
                                }
                            }
                            else if (cmd.equals("logout")) {
                                serverLog.append(LocalTime.now()+" "+socketLogin.get(cc)+" logged out"+"\n");
                                for (Map.Entry<String, SocketChannel> entry : loginSocket.entrySet()
                                ) {
                                    writeResp(entry.getValue(),socketLogin.get(cc)+" logged out"+"\n");
                                }
                                loginSocket.remove(socketLogin.get(cc));
                                socketLogin.remove(cc);
                                cc.close();
                                cc.socket().close();
                            }
                            else if (cmd.equals("login")) {
                                loginy.add(req[1]);
                                socketLogin.put(cc,req[1]);
                                loginSocket.put(req[1],cc);
                                serverLog.append(LocalTime.now()+" "+req[1]+" logged in"+"\n");
                                for (Map.Entry<String, SocketChannel> entry : loginSocket.entrySet()
                                     ) {
                                    writeResp(entry.getValue(),req[1]+" logged in"+"\n");
                                }

                            }
                            else {
                                for (Map.Entry<String, SocketChannel> entry : loginSocket.entrySet()
                                     ) {
                                    writeResp(entry.getValue(),null);
                                }
                            }
                        } catch (Exception exc) {

                        }
                        continue;
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                continue;
            }
        }
    }
}