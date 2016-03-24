import javax.media.*;
import java.io.*;
 
class Warehouse {

    public static void main(String[] args) {
        Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
        Format input2 = new AudioFormat(AudioFormat.MPEG);
        Format output = new AudioFormat(AudioFormat.LINEAR);
        PlugInManager.addPlugIn(
                "com.sun.media.codec.audio.mp3.JavaDecoder",
                new Format[]{input1, input2},
                new Format[]{output},
                PlugInManager.CODEC
        );
        try{
            Player player = Manager.createPlayer(new MediaLocator(new File("stillalive1.mp3").toURI().toURL()));
            player.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
