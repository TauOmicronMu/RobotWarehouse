import javax.media.*;
import java.io.*;
import java.net.URL;
 
class Warehouse  extends Thread
{
 
private URL url;
private MediaLocator mediaLocator;
private Player playMP3;
 
public mp3(String mp3)
{
try{
   this.url = new URL(mp3);
   }catch(java.net.MalformedURLException e)
      {System.out.println(e.getMessage());}
}
 
public void run()
{

try{
   mediaLocator = new MediaLocator(url);     
   playMP3 = Manager.createPlayer(mediaLocator);
    }catch(java.io.IOException e)
      {System.out.println(e.getMessage());
    }catch(javax.media.NoPlayerException e)
      {System.out.println(e.getMessage());}

playMP3.addControllerListener(new ControllerListener()
  {
  public void controllerUpdate(ControllerEvent e)
     {
     if (e instanceof EndOfMediaEvent)
         {
         playMP3.stop();
         playMP3.close();
         }
     }
  }
 );
 playMP3.realize();
 playMP3.start();
 } 
}

public class playmp3{
  public static void main(String[] args)
  {
  mp3 t = new mp3("file:///C://JavaApplications//cd.mp3");
  t.start();
  }
}

