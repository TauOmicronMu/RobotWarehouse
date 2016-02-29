import java.io.IOException;
import java.io.ObjectInputStream;

public class ServerReceiver extends Thread{
	
	private ClientTable table;
	private ObjectInputStream fromClient;
	private String robotName;
	public ServerReceiver(ObjectInputStream fromClient, ClientTable table, String name)
	{
		this.fromClient = fromClient;
		this.table = table;
		this.robotName= name;
	}
	
	public void run() {
	    
	      while (true) {
	    	  
	        try {
	        	//reading object from the client and then respond accordingly
	        	//should add the message to the 
	        	
	        	//Should work something like that
				Object obj = fromClient.readObject();
				/*table.getQueue(robotName);
				
				 Message msg = new Message(messageType, obj);
		         BlockQueue recipientsQueue = table.getQueue(robotName);
		          if (recipientsQueue != null)
		            recipientsQueue.offer(msg);*/
			} catch (ClassNotFoundException | IOException e) {
				
				e.printStackTrace();
			}
	        
//	 
	    }
	}

}
