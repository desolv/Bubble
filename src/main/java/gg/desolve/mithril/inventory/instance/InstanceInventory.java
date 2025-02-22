package gg.desolve.mithril.inventory.instance;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.instance.Instance;
import gg.desolve.mithril.relevance.Converter;
import gg.desolve.mithril.relevance.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.stream.Stream;

public class InstanceInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("instanceInventory")
            .provider(new InstanceInventory())
            .size(3, 9)
            .title("Instance Management")
            .manager(Mithril.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        sidebars(player, -1, -1, contents);
        Instance instance = Mithril.getInstance().getInstanceManager().getInstance();

        // Instance Information
        ItemStack instanceStack = XMaterial.FIRE_CHARGE.parseItem();
        ItemMeta instanceMeta = instanceStack.getItemMeta();
        instanceMeta.setDisplayName(Message.translate("<gray>@" + instance.getName() + " <dark_gray>#" + instance.getId()));
        instanceStack.setItemMeta(instanceMeta);
        contents.set(1, 4, ClickableItem.empty(instanceStack));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        Instance instance = Mithril.getInstance().getInstanceManager().getInstance();

        ItemStack instanceStack = Objects.requireNonNull(contents.get(1, 4).orElse(null)).getItem();
        ItemMeta instanceMeta = instanceStack.getItemMeta();

        instanceMeta.setLore(Stream.of(
                "<gray>Instance is running on <light_purple>v" + Mithril.getInstance().getServer().getBukkitVersion(),
                "<gray>- <green>Online for " + Converter.time(System.currentTimeMillis() - instance.getBooting()),
                "<gray>- <red>Heartbeat of " + Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) + " seconds",
                "<yellow>" + instance.getOnline() + " players since last heartbeat"
        ).map(Message::translate).toList());

        instanceStack.setItemMeta(instanceMeta);
        contents.set(1, 4, ClickableItem.empty(instanceStack));
    }

    public static void sidebars(Player player, int greenRow, int greenColumn, InventoryContents contents) {
        // Aesthetic Gray Pane
        ItemStack glassStack = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        ItemMeta glassMeta = glassStack.getItemMeta();
        glassMeta.setDisplayName(" ");
        glassStack.setItemMeta(glassMeta);
        contents.fillColumn(1, ClickableItem.empty(glassStack));

        // Aesthetic Green Pane
        if (greenRow >= 0) {
            ItemStack greenStack = XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
            ItemMeta greenMeta = greenStack.getItemMeta();
            greenMeta.setDisplayName(" ");
            greenStack.setItemMeta(greenMeta);
            contents.set(greenRow, greenColumn, ClickableItem.empty(greenStack));
        }

        // Instance Showcase
        ItemStack anvilStack = XMaterial.ANVIL.parseItem();
        ItemMeta anvilMeta = anvilStack.getItemMeta();
        anvilMeta.setDisplayName(Message.translate("<green>Showcase"));
        anvilMeta.setLore(Stream.of("<gray>View concurrent instances running",
                "<gray>around the redis instance").map(Message::translate).toList());
        anvilStack.setItemMeta(anvilMeta);

        contents.set(0, 0, ClickableItem.of(anvilStack, e -> {
            if (Mithril.getInstance().getInstanceManager().retrieve().size() <= 1) {
                ItemStack redStack = XMaterial.RED_STAINED_GLASS_PANE.parseItem();
                ItemMeta redMeta = redStack.getItemMeta();
                redMeta.setDisplayName(Message.translate("<Red>Missing"));
                redMeta.setLore(Stream.of("<gray>Needs more than 1 instance")
                        .map(Message::translate).toList());
                redStack.setItemMeta(redMeta);

                contents.set(0, 1, ClickableItem.empty(redStack));
                Bukkit.getScheduler().runTaskLater(Mithril.getInstance(), () ->
                        contents.set(0, 1, ClickableItem.empty(glassStack)), 60L);
            } else {
                if (Mithril.getInstance().getInventoryManager().getInventory(player).orElse(null) == InstanceListInventory.INVENTORY)
                    InstanceInventory.INVENTORY.open(player);
                else InstanceListInventory.INVENTORY.open(player);
            }
        }));

        // Instance Management
        ItemStack blazeStack = XMaterial.BLAZE_POWDER.parseItem();
        ItemMeta blazeMeta = blazeStack.getItemMeta();
        blazeMeta.setDisplayName(Message.translate("<red>Management"));
        blazeMeta.setLore(Stream.of("<gray>Modify instances such as executing",
                "<gray>reboots or commands across instances").map(Message::translate).toList());
        blazeStack.setItemMeta(blazeMeta);

        contents.set(1, 0, ClickableItem.of(blazeStack, e -> {
            if (Mithril.getInstance().getInventoryManager().getInventory(player).orElse(null) == InstanceManagementInventory.INVENTORY)
                InstanceInventory.INVENTORY.open(player);
            else InstanceManagementInventory.INVENTORY.open(player);
        }));
    }
}
