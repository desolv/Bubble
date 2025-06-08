package gg.desolve.paper;

import gg.desolve.api.Bubble;
import gg.desolve.paper.command.PingCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PaperBubblePlugin extends JavaPlugin {

    private PaperBubbleCommandManager paperBubbleCommandManager;

    @Override
    public void onEnable() {
        new Bubble();

        paperBubbleCommandManager = new PaperBubbleCommandManager(this);
        paperBubbleCommandManager.getInstance().registerCommand(new PingCommand());
    }
}
