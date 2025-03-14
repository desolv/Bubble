package gg.desolve.mithril.relevance;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Material {

    private static final List<String> PRETTY_COLOR_LIST = Arrays.asList(
            "YELLOW",
            "LIME",
            "CYAN",
            "MAGENTA",
            "LIGHT_GRAY",
            "WHITE",
            "PINK",
            "LIGHT_BLUE"
    );

    private static final Map<String, String> COLOR_MAP = Map.ofEntries(
            Map.entry("<dark_red>", "RED"),
            Map.entry("<red>", "RED"),
            Map.entry("<gold>", "YELLOW"),
            Map.entry("<yellow>", "YELLOW"),
            Map.entry("<dark_green>", "GREEN"),
            Map.entry("<green>", "LIME"),
            Map.entry("<aqua>", "CYAN"),
            Map.entry("<dark_aqua>", "CYAN"),
            Map.entry("<blue>", "BLUE"),
            Map.entry("<dark_blue>", "BLUE"),
            Map.entry("<light_purple>", "MAGENTA"),
            Map.entry("<dark_purple>", "PURPLE"),
            Map.entry("<gray>", "LIGHT_GRAY"),
            Map.entry("<light_gray>", "LIGHT_GRAY"),
            Map.entry("<dark_gray>", "GRAY"),
            Map.entry("<black>", "BLACK")
    );

    public static String getRandomWoolColor() {
        Random random = new Random();
        return PRETTY_COLOR_LIST.get(random.nextInt(PRETTY_COLOR_LIST.size()));
    }

    public static String getWoolColor(String color) {
        return COLOR_MAP.getOrDefault(color.toLowerCase(), "WHITE");
    }

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
}
