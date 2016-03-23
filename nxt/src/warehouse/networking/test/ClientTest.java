package warehouse.networking.test;

import warehouse.networking.WarehouseClient;

import java.io.IOException;

/**
 * Created by sxt567 on 23/03/16.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
        WarehouseClient client = new WarehouseClient();
        client.send("Hi there server person");
    }
}
