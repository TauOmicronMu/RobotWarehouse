package warehouse.networking.test;

import warehouse.event.Event;
import warehouse.networking.WarehouseServer;
import warehouse.util.MultiSubscriber;

import java.io.IOException;

/**
 * Created by sxt567 on 23/03/16.
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        WarehouseServer server = new WarehouseServer();
        server.broadcast("Hi there bob");
    }

    @MultiSubscriber
    public static void onPacket(Event event) {
        System.out.println(String.format("Msg: %s%n", event.toString()));
    }

}
