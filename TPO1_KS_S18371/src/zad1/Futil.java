package zad1;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        try {
            Path indir = Paths.get(dirName);
            Path out = Paths.get(resultFileName);
            FileVisitorImpl visitor = new FileVisitorImpl(out);
            Files.walkFileTree(indir,visitor);
        }catch (IOException e){
            System.out.println(e);
        }


    }

}
