package gg.desolve.mithril.relevance;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Material {

    public static ItemStack getSkull(String skull) {
        if (skull.length() > 36)
            return XSkull.createItem()
                    .profile(Profileable.detect(skull))
                    .fallback(Profileable.username("Steve"))
                    .apply();

        return XSkull.createItem()
                .profile(Profileable.of(UUID.fromString(skull)))
                .fallback(Profileable.username("Steve"))
                .apply();
    }

    public static String getWool(String color) {
        return switch (color.toLowerCase()) {
            case "<dark_red>" -> "RED";
            case "<red>" -> "RED";
            case "<gold>", "yellow" -> "YELLOW";
            case "<dark_green>" -> "GREEN";
            case "<green>" -> "LIME";
            case "<aqua>", "dark_aqua" -> "CYAN";
            case "<blue>" -> "BLUE";
            case "<dark_blue>" -> "BLUE";
            case "<light_purple>" -> "MAGENTA";
            case "<dark_purple>" -> "PURPLE";
            case "<white>" -> "WHITE";
            case "<gray>" -> "LIGHT_GRAY";
            case "<dark_gray>" -> "GRAY";
            case "<black>" -> "BLACK";
            default -> "WHITE";
        };
    }
}
