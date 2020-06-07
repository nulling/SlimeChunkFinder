package io.github.nulling;

import org.bukkit.plugin.java.JavaPlugin;

public class SlimeChunkFinder extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("SlimeChunkFinder initialized!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SlimeChunkFinder disabled!");
    }
}
