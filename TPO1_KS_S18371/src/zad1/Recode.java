package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Recode {
    ByteBuffer buffer;
    public Recode(Charset inCode,Charset outCode,FileChannel inputFC,FileChannel outputFC,long buff_size){
        try{
            buffer = ByteBuffer.allocate((int)buff_size +1);
            buffer.clear();
            inputFC.read(buffer);
            buffer.flip();
            CharBuffer charbuff = inCode.decode(buffer);
            ByteBuffer buff=outCode.encode(charbuff);
            while (buff.hasRemaining()){
                outputFC.write(buff);
            }
        } catch (IOException e) {
            System.out.println("Recode error");
        }
    }
}
