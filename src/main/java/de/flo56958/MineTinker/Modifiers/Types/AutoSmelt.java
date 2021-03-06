package de.flo56958.MineTinker.Modifiers.Types;

import de.flo56958.MineTinker.Data.ModifierFailCause;
import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Events.MTBlockBreakEvent;
import de.flo56958.MineTinker.Events.ModifierFailEvent;
import de.flo56958.MineTinker.Main;
import de.flo56958.MineTinker.Modifiers.Craftable;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import de.flo56958.MineTinker.Utilities.ConfigurationManager;
import de.flo56958.MineTinker.Utilities.ItemGenerator;
import de.flo56958.MineTinker.Utilities.Modifiers_Config;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AutoSmelt extends Modifier implements Craftable, Listener {

    private int percentagePerLevel;
    private boolean hasSound;
    private boolean hasParticles;
    private boolean worksUnderWater;
    private boolean smeltStone;
    private boolean burnCoal;

    public AutoSmelt() {
        super(ModifierType.AUTO_SMELT,
                new ArrayList<>(Arrays.asList(ToolType.AXE, ToolType.PICKAXE, ToolType.SHOVEL)),
                Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @Override
    public void reload() {
    	FileConfiguration config = getConfig();
    	config.options().copyDefaults(true);
    	
    	String key = "Auto-Smelt";
    	config.addDefault(key + ".allowed", true);
    	config.addDefault(key + ".name", key);
    	config.addDefault(key + ".name_modifier", "Enhanced Furnace");
    	config.addDefault(key + ".description", "Chance to smelt ore when mined!");
        config.addDefault(key + ".description_modifier", "%WHITE%Modifier-Item for the Auto-Smelt-Modifier");
    	config.addDefault(key + ".Color", "%YELLOW%");
    	config.addDefault(key + ".MaxLevel", 5);
    	config.addDefault(key + ".PercentagePerLevel", 20);
    	config.addDefault(key + ".Sound", true); //Auto-Smelt makes a sound
    	config.addDefault(key + ".Particles", true); //Auto-Smelt will create a particle effect when triggered
    	config.addDefault(key + ".smelt_stone", false);
    	config.addDefault(key + ".burn_coal", true);
    	config.addDefault(key + ".works_under_water", true);
    	config.addDefault(key + ".Recipe.Enabled", true);
    	config.addDefault(key + ".Recipe.Top", "CCC");
    	config.addDefault(key + ".Recipe.Middle", "CFC");
    	config.addDefault(key + ".Recipe.Bottom", "CCC");
    	config.addDefault(key + ".Recipe.Materials.C", "FURNACE");
    	config.addDefault(key + ".Recipe.Materials.F", "BLAZE_ROD");
    	
    	ConfigurationManager.saveConfig(config);
    	
    	init(config.getString(key + ".name"),
                "[" + config.getString(key + ".name_modifier") + "] " + config.getString(key + ".description"),
                ChatWriter.getColor(config.getString(key + ".Color")),
                config.getInt(key + ".MaxLevel"),
                modManager.createModifierItem(Material.FURNACE, ChatWriter.getColor(config.getString(key + ".Color")) + config.getString(key + ".name_modifier"), ChatWriter.addColors(config.getString(key + ".description_modifier")), this));
        
        this.percentagePerLevel = config.getInt(key + ".PercentagePerLevel");
        this.hasSound = config.getBoolean(key + ".Sound");
        this.hasParticles = config.getBoolean(key + ".Particles");
        this.worksUnderWater = config.getBoolean(key + ".works_under_water");
        this.smeltStone = config.getBoolean(key + ".smelt_stone");
        this.burnCoal = config.getBoolean(key + ".burn_coal");
    }
    
    @Override
    public ItemStack applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (modManager.get(ModifierType.SILK_TOUCH) != null) {
            if (modManager.hasMod(tool, modManager.get(ModifierType.SILK_TOUCH))) {
                pluginManager.callEvent(new ModifierFailEvent(p, tool, this, ModifierFailCause.INCOMPATIBLE_MODIFIERS, isCommand));
                return null;
            }
        }

        return Modifier.checkAndAdd(p, tool, this, "autosmelt", isCommand);
    }

    @Override
    public void removeMod(ItemStack tool) { }

    /**
     * The Effect for the BlockBreak-Listener
     * @param event the Event
     */
    @EventHandler
    public void effect(MTBlockBreakEvent event) {
        if (event.isCancelled() || !this.isAllowed()) { return; }
        Player p = event.getPlayer();
        ItemStack tool = event.getTool();
        Block b = event.getBlock();
        BlockBreakEvent e = event.getEvent();

    	FileConfiguration config = getConfig();
    	
        if (!p.hasPermission("minetinker.modifiers.autosmelt.use")) { return; }//TODO: Think about more blocks for Auto-Smelt
        if (!modManager.hasMod(tool, this)) { return; }

        if (!worksUnderWater) {
            if (p.isSwimming() || p.getWorld().getBlockAt(p.getLocation()).getType().equals(Material.WATER)) { return; }
        }

        boolean allowLuck = false;
        int amount = 1;
        Material loot;
        switch (b.getType()) {
            case STONE:
                if (!smeltStone) { return; }
            case COBBLESTONE:
                loot = Material.STONE;
                break;

            case SAND:
                loot = Material.GLASS;
                break;

            case RED_SAND:
                loot = Material.RED_STAINED_GLASS;
                break;

            case ACACIA_LOG:
            case BIRCH_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case OAK_LOG:
            case SPRUCE_LOG:

            case STRIPPED_ACACIA_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_OAK_LOG:
            case STRIPPED_SPRUCE_LOG:

            case ACACIA_WOOD:
            case BIRCH_WOOD:
            case DARK_OAK_WOOD:
            case JUNGLE_WOOD:
            case OAK_WOOD:
            case SPRUCE_WOOD:

            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_WOOD:
                allowLuck = true;
                loot = Material.CHARCOAL;
                break;

            case IRON_ORE:
                allowLuck = true;
                loot = Material.IRON_INGOT;
                break;

            case GOLD_ORE:
                allowLuck = true;
                loot = Material.GOLD_INGOT;
                break;

            case NETHERRACK:
                allowLuck = true;
                loot = Material.NETHER_BRICK;
                break;

            case KELP_PLANT:
                loot = Material.DRIED_KELP;
                break;

            case WET_SPONGE:
                loot = Material.SPONGE;
                break;

            case COAL_ORE:
            case COAL_BLOCK:
                if (!burnCoal) { return; }
                loot = Material.AIR;
                break;

            case CLAY:
                loot = Material.BRICK;
                amount = 4;
                break;

            default:
                return;
        }

        Random rand = new Random();
        int n = rand.nextInt(100);
        if (n <= this.percentagePerLevel * modManager.getModLevel(tool, this)) {
            if (allowLuck && modManager.get(ModifierType.LUCK) != null) {
                int level = modManager.getModLevel(tool, modManager.get(ModifierType.LUCK));
                if (level > 0) {
                    amount = amount + rand.nextInt(level) * amount; //Times amount is for clay as it drops 4 per block
                }
            }

            if (!loot.equals(Material.AIR)) {
                ItemStack items = new ItemStack(loot, amount);
                b.getLocation().getWorld().dropItemNaturally(b.getLocation(), items);
            }
            e.setDropItems(false);

            if (this.hasParticles) { b.getLocation().getWorld().spawnParticle(Particle.FLAME, b.getLocation(), 5); }
            if (this.hasSound) { b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_GENERIC_BURN, 0.2F, 0.5F); }
            ChatWriter.log(false, p.getDisplayName() + " triggered Auto-Smelt on " + ItemGenerator.getDisplayName(tool) + ChatColor.GRAY + " (" + tool.getType().toString() + ") while mining " + e.getBlock().getType().toString() + "!");
        }
    }

    @Override
    public void registerCraftingRecipe() {
        _registerCraftingRecipe(getConfig(), this, "Auto-Smelt", "Modifier_Autosmelt");
    }
    
    private static FileConfiguration getConfig() {
		return ConfigurationManager.getConfig(Modifiers_Config.Auto_Smelt);
    }

    @Override
    public boolean isAllowed() {
    	return getConfig().getBoolean("Auto-Smelt.allowed");
    }
}
