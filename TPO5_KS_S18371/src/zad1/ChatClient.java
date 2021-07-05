/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ChatClient {
    private static Charset charset  = Charset.forName("ISO-8859-2");
    String host;
    int port;
    String id, chatView;
    SocketChannel socketChannel;
    ByteBuffer inbuf = ByteBuffer.allocateDirect(1024);
    public ChatClient(String host, int port, String id) {
        this.host=host;
        this.id=id;
        this.port=port;
    }

    public String getChatView() {
        return chatView;
    }
    public void login() throws IOException, InterruptedException {
        try{
            socketChannel=SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.configureBlocking (false);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(75);
        this.send("login "+id);
    }
    public void logout() throws IOException, InterruptedException {
        this.send("logout "+id);
        Thread.sleep(75);
        socketChannel.close();
    }
    public void send(String msg) throws IOException, InterruptedException {
        msg=msg+"\n";
        Thread.sleep(75);
        socketChannel.write(charset.encode(CharBuffer.wrap(msg)));
    }
    public void nasluch(StringBuffer sb) throws IOException{
        CharBuffer cbudd;
        while (true){
            try {
                inbuf.clear();
                int readBytes = socketChannel.read(inbuf);
                if (readBytes == 0) {
                    continue;
                } else if (readBytes == -1) {
                    socketChannel.close();
                    break;
                } else {
                    inbuf.flip();
                    cbudd = charset.decode(inbuf);
                    sb.append(cbudd);
                    continue;
                }
            }catch (ClosedChannelException e){
                break;
            }
        }
    }
}
