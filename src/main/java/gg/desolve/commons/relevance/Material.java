package gg.desolve.commons.relevance;

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
}
