package zad1;


import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileVisitorImpl implements FileVisitor<Path>{
    FileChannel outputFC;

    Charset inCode = Charset.forName("Cp1250");
    Charset outCode = Charset.forName("UTF-8");

    public FileVisitorImpl(Path outputPath) throws IOException {
        this.outputFC=FileChannel.open(outputPath, CREATE,StandardOpenOption.WRITE,StandardOpenOption.READ);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try{
           new Recode(inCode,outCode, FileChannel.open(file),outputFC,attrs.size());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return CONTINUE;
    }
}
