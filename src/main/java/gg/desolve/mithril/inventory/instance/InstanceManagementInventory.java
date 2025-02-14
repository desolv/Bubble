package gg.desolve.mithril.inventory.instance;

import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import gg.desolve.mithril.Mithril;
import org.bukkit.entity.Player;

public class InstanceManagementInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("instanceManagementInventory")
            .provider(new InstanceManagementInventory())
            .size(3, 9)
            .title("Instance Management")
            .manager(Mithril.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        InstanceInventory.sidebars(player, 1, 1, inventoryContents);


    }


    @Override
    public void update(Player player, InventoryContents inventoryContents) {}
}
