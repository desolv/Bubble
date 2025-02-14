package gg.desolve.commons.inventory.instance;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Converter;
import gg.desolve.commons.relevance.Material;
import gg.desolve.commons.relevance.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InstanceListInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("instanceListInventory")
            .provider(new InstanceListInventory())
            .size(3, 9)
            .title("Instance Management")
            .manager(Commons.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        InstanceInventory.sidebars(player, 0, 1, inventoryContents);

        Pagination pagination = inventoryContents.pagination();
        List<ClickableItem> instances = new ArrayList<>();

        Commons.getInstance().getInstanceManager().retrieve().forEach(instance -> {
            XMaterial material = instance.getId().equals(Commons.getInstance().getInstanceManager().getInstance().getId()) ?
                    XMaterial.FIRE_CHARGE : XMaterial.FERMENTED_SPIDER_EYE;

            ItemStack instanceStack = material.parseItem();
            ItemMeta instanceMeta = instanceStack.getItemMeta();

            instanceMeta.setDisplayName(Message.translate("<gray>@" + instance.getName() + " <dark_gray>#" + instance.getId()));
            instanceMeta.setLore(Stream.of(
                    "<gray>Instance is running on <light_purple>v" + Commons.getInstance().getServer().getBukkitVersion(),
                    "<gray>- <green>Online for " + Converter.time(System.currentTimeMillis() - instance.getBooting()),
                    "<gray>- <red>Heartbeat of " + Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) + " seconds",
                    "<yellow>" + instance.getOnline() + " players since last heartbeat"
            ).map(Message::translate).toList());

            instanceStack.setItemMeta(instanceMeta);
            instances.add(ClickableItem.empty(instanceStack));
        });

        pagination.setItemsPerPage(7);
        pagination.setItems(instances.toArray(new ClickableItem[0]));
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 2));

        if (!pagination.isFirst()) {
            ItemStack previousButton = Material.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
            ItemMeta previousMeta = previousButton.getItemMeta();
            previousMeta.setDisplayName(Message.translate("<yellow>Previous Page"));
            previousMeta.setLore(Stream.of(
                    "<gray>Click to go to the previous page"
            ).map(Message::translate).toList());
            previousButton.setItemMeta(previousMeta);

            inventoryContents.set(0, pagination.isLast() ? 8 : 7, ClickableItem.of(
                    previousButton,
                    e -> INVENTORY.open(player, pagination.previous().getPage())
            ));
        }

        if (!pagination.isLast()) {
            ItemStack nextButton = Material.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
            ItemMeta nextMeta = nextButton.getItemMeta();
            nextMeta.setDisplayName(Message.translate("<yellow>Next Page"));
            nextMeta.setLore(Stream.of(
                    "<gray>Click to go to the next page"
            ).map(Message::translate).toList());
            nextButton.setItemMeta(nextMeta);

            inventoryContents.set(0, 8, ClickableItem.of(
                    nextButton,
                    e -> INVENTORY.open(player, pagination.next().getPage())
            ));
        }



    }


    @Override
    public void update(Player player, InventoryContents inventoryContents) {}
}
