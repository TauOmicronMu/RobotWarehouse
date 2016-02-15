import java.io.Serializable;
import java.util.Date;

/**
 * Abstract implementation of a Message.
 * @author TauOmicronMu
 */
public class Message implements Serializable {

	/**
	 * Default serial ID.
	 */
	protected static final long serialVersionUID = 1L;
	
	/*
	 * The time at the point of Message creation.
	 */
	protected final Date timestamp;
	
	/*
	 * The nickname of the client that the Message is being
	 * sent from.
	 */
	protected final String sender;
	
	/*
	 * The command detailing what to do with the data -
	 * for example, "EndGame".
	 */
	protected final MessageType messageType;
	
	/*
	 * Holds data specific to the type of message to be sent. 
	 */
	protected final Object data;
	
	public Message(String sender, MessageType messageType, Object data) {
		
		this.sender = sender;
		this.messageType = messageType;
		this.data = data;
		
		this.timestamp = new Date();
	}

	/**
	 * Returns the fromClient attribute of the Message.
	 * @return The nickname of the client that the Message is being sent from.
	 */
	public String getSender() {
		return sender;
	}
    
	/**
	 * Returns the messageCommand attribute of the Message.
	 * @return The command detailing what to do with the data.
	 */
	public MessageType getMessageCommand() {
		return messageType;
	}

	/**
	 * Returns the data attribute of the Message.
	 * @return The data held within the Message.
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Returns the time at the point of Message creation.
	 * @return the timestamp attribute of the Message.
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
