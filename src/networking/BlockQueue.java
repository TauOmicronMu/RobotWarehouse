package networking;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueue {

	private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

	// Inserts the specified message into this queue.
	public void offer(Message m) {
		queue.offer(m);
	}

	// Retrieves and removes the head of this queue, waiting if
	// necessary until an element becomes available.
	public Message take() {

		while (true) {
			try {
				return (queue.take());
			} catch (InterruptedException e) {
				// This can in principle be triggered by queue.take().
				// But this would only happen if we had interrupt() in our code,
				// which we don't.
				// In any case, if it could happen, we should do nothing here
				// and try again until we succeed without interruption.
			}

		}
	}
}
