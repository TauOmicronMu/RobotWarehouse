package warehouse.main;

import warehouse.event.BeginAssigningEvent;
import warehouse.management_interface.Main;
import warehouse.util.EventDispatcher;
import warehouse.util.Subscriber;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class WarehouseMain {

    static {
        EventDispatcher.subscribe2(WarehouseMain.class);
    }

    public static void main(String[] args) {
        // Start server
        // Start job input
    }

    @Subscriber
    public void onJobInputFinished(BeginAssigningEvent event) {
        Main warehouseInterface = new Main();
    }

}
