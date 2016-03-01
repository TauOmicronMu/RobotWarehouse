package networking;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientTable {

  private ConcurrentMap<String,BlockQueue> queueTable  = new ConcurrentHashMap<String,BlockQueue>();

  // The following overrides any previously existing nickname, and
  // hence the last client to use this nickname will get the messages
  // for that nickname, and the previously exisiting clients with that
  // nickname won't be able to get messages. Obviously, this is not a
  // good design of a messaging system. So I don't get full marks:

  public void add(String nickname) {
    queueTable.put(nickname, new BlockQueue());
  }

  // Returns null if the nickname is not in the table:
  public BlockQueue getQueue(String nickname) {
    return queueTable.get(nickname);
  }

}