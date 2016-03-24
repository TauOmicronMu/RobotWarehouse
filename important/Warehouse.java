import javax.media.*;
import java.io.*;
 
class Warehouse {

    public static void main(String[] args) {
        try{
            Player player = Manager.createPlayer(new MediaLocator(new File("stillalive1.mp3").toURI().toURL()));
            player.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
