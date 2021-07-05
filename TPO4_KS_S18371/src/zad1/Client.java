/**
 *
 *  @author Kruk Seweryn S18371
 *
 */

package zad1;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    private static Charset charset  = Charset.forName("ISO-8859-2");
    String host;
    int port;
    String id;
    SocketChannel socketChannel;
    ByteBuffer inbuf = ByteBuffer.allocateDirect(1024);
    public Client(String host, int port, String id) {
        this.host=host;
        this.port=port;
        this.id=id;
    }

    public void connect() {
        try{
            socketChannel=SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.configureBlocking (false);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String s) {
        s=s+"\n";
        StringBuffer response=new StringBuffer();
        try {
            CharBuffer cbudd= CharBuffer.wrap(s);
            ByteBuffer putB = charset.encode(cbudd);
            socketChannel.write(putB);
            while (true){
                inbuf.clear();
                int readBytes = socketChannel.read(inbuf);
                if (readBytes==0){
                    Thread.sleep(50);
                    continue;
                }else if(readBytes==-1){
                    socketChannel.close();
                    break;
                }else {
                    inbuf.flip();
                    cbudd=charset.decode(inbuf);
                    response.append(cbudd);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return response.toString();
    }
}
