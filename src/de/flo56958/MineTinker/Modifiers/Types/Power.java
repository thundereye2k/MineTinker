package de.flo56958.MineTinker.Modifiers.Types;

import de.flo56958.MineTinker.Data.ModifierFailCause;
import de.flo56958.MineTinker.Data.PlayerData;
import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Events.ModifierFailEvent;
import de.flo56958.MineTinker.Main;
import de.flo56958.MineTinker.Modifiers.Craftable;
import de.flo56958.MineTinker.Modifiers.Enchantable;
import de.flo56958.MineTinker.Modifiers.ModManager;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import de.flo56958.MineTinker.Utilities.ItemGenerator;
import de.flo56958.MineTinker.Utilities.PlayerInfo;
import net.minecraft.server.v1_13_R2.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Power extends Modifier implements Enchantable, Craftable {
    private static final ModManager modManager = Main.getModManager();
    private static final PluginManager pluginManager = Bukkit.getPluginManager();
    private static final FileConfiguration config = Main.getMain().getConfigurations().getConfig("Power.yml");

    public static final HashMap<Player, Boolean> HASPOWER = new HashMap<>();

    private boolean lv1_vertical;

    public Power() {
        super(config.getString("Power.name"),
                "[Enchanted Emerald] Tool can destroy more blocks per swing!",
                ModifierType.POWER,
                ChatColor.GREEN,
                config.getInt("Power.MaxLevel"),
                ItemGenerator.itemEnchanter(Material.EMERALD, ChatColor.GREEN + config.getString("Power.name_modifier"), 1, Enchantment.ARROW_DAMAGE, 1),
                new ArrayList<>(Arrays.asList(ToolType.AXE, ToolType.HOE, ToolType.PICKAXE, ToolType.SHOVEL)),
                Main.getPlugin());
        this.lv1_vertical = config.getBoolean("Power.lv1_vertical");
    }

    @Override
    public ItemStack applyMod(Player p, ItemStack tool, boolean isCommand) {
        if (modManager.get(ModifierType.TIMBER) != null) {
            if (modManager.hasMod(tool, modManager.get(ModifierType.TIMBER))) {
                pluginManager.callEvent(new ModifierFailEvent(p, tool, this, ModifierFailCause.INCOMPATIBLE_MODIFIERS, isCommand));
                return null;
            }
        }

        return Modifier.checkAndAdd(p, tool, this, "power", isCommand);
    }

    private boolean checkPower(Player p, ItemStack tool) {
        if (!p.hasPermission("minetinker.modifiers.power.use")) { return false; }
        if (HASPOWER.get(p)) { return false; }
        if (p.isSneaking()) { return false; }

        return modManager.hasMod(tool, this);
    }

    public void effect(Player p, ItemStack tool, Block b) {
        if (!checkPower(p, tool)) { return; }

        ChatWriter.log(false, p.getDisplayName() + " triggered Power on " + ItemGenerator.getDisplayName(tool) + ChatColor.GRAY + " (" + tool.getType().toString() + ")!");

        HASPOWER.replace(p, true);

        int level = modManager.getModLevel(tool, this);

        if (level == 1) {
            if (lv1_vertical) {
                if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.DOWN) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.UP)) {
                    if (PlayerInfo.getFacingDirection(p).equals("N") || PlayerInfo.getFacingDirection(p).equals("S")) {
                        Block b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, 1));
                        Block b2 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, -1));
                        powerBlockBreak(b1, (CraftPlayer) p);
                        powerBlockBreak(b2, (CraftPlayer) p);
                    } else if (PlayerInfo.getFacingDirection(p).equals("W") || PlayerInfo.getFacingDirection(p).equals("E")) {
                        Block b1 = b.getWorld().getBlockAt(b.getLocation().add(1, 0, 0));
                        Block b2 = b.getWorld().getBlockAt(b.getLocation().add(-1, 0, 0));
                        powerBlockBreak(b1, (CraftPlayer) p);
                        powerBlockBreak(b2, (CraftPlayer) p);
                    }
                } else {
                    Block b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 1, 0));
                    Block b2 = b.getWorld().getBlockAt(b.getLocation().add(0, -1, 0));
                    powerBlockBreak(b1, (CraftPlayer) p);
                    powerBlockBreak(b2, (CraftPlayer) p);
                }
            } else if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.DOWN) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.UP)) {
                if (PlayerInfo.getFacingDirection(p).equals("N") || PlayerInfo.getFacingDirection(p).equals("S")) {
                    Block b1 = b.getWorld().getBlockAt(b.getLocation().add(1, 0, 0));
                    Block b2 = b.getWorld().getBlockAt(b.getLocation().add(-1, 0, 0));
                    powerBlockBreak(b1, (CraftPlayer) p);
                    powerBlockBreak(b2, (CraftPlayer) p);
                } else if (PlayerInfo.getFacingDirection(p).equals("W") || PlayerInfo.getFacingDirection(p).equals("E")) {
                    Block b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, 1));
                    Block b2 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, -1));
                    powerBlockBreak(b1, (CraftPlayer) p);
                    powerBlockBreak(b2, (CraftPlayer) p);
                }
            } else if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.NORTH) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.SOUTH)) {
                Block b1 = b.getWorld().getBlockAt(b.getLocation().add(1, 0, 0));
                Block b2 = b.getWorld().getBlockAt(b.getLocation().add(-1, 0, 0));
                powerBlockBreak(b1, (CraftPlayer) p);
                powerBlockBreak(b2, (CraftPlayer) p);
            } else if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.WEST) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.EAST)) {
                Block b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, 1));
                Block b2 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, -1));
                powerBlockBreak(b1, (CraftPlayer) p);
                powerBlockBreak(b2, (CraftPlayer) p);
            }
        } else {
            HASPOWER.replace(p, true);
            if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.DOWN) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.UP)) {
                for (int x = -(level - 1); x <= (level - 1); x++) {
                    for (int z = -(level - 1); z <= (level - 1); z++) {
                        if (!(x == 0 && z == 0)) {
                            Block b1 = b.getWorld().getBlockAt(b.getLocation().add(x, 0, z));
                            powerBlockBreak(b1, (CraftPlayer) p);
                        }
                    }
                }
            } else if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.NORTH) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.SOUTH)) {
                for (int x = -(level - 1); x <= (level - 1); x++) {
                    for (int y = -(level - 1); y <= (level - 1); y++) {
                        if (!(x == 0 && y == 0)) {
                            Block b1 = b.getWorld().getBlockAt(b.getLocation().add(x, y, 0));
                            powerBlockBreak(b1, (CraftPlayer) p);
                        }
                    }
                }
            } else if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.EAST) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.WEST)) {
                for (int z = -(level - 1); z <= (level - 1); z++) {
                    for (int y = -(level - 1); y <= (level - 1); y++) {
                        if (!(z == 0 && y == 0)) {
                            Block b1 = b.getWorld().getBlockAt(b.getLocation().add(0, y, z));
                            powerBlockBreak(b1, (CraftPlayer) p);
                        }
                    }
                }
            }
        }

        HASPOWER.replace(p, false);
    }

    public void effect(Player p, ItemStack tool, PlayerInteractEvent e) {
        if (!checkPower(p, tool)) { return; }

        ChatWriter.log(false, p.getDisplayName() + " triggered Power on " + ItemGenerator.getDisplayName(tool) + ChatColor.GRAY + " (" + tool.getType().toString() + ")!");

        HASPOWER.replace(p, true);

        int level = modManager.getModLevel(tool, this);

        Block b = e.getClickedBlock();

        if (level == 1) {
            if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.DOWN) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.UP)) {
                Block b1;
                Block b2;
                if (PlayerInfo.getFacingDirection(p).equals("N") || PlayerInfo.getFacingDirection(p).equals("S")) {
                    if (config.getBoolean("Power.lv1_vertical")) {
                        b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, 1));
                        b2 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, -1));
                    } else {
                        b1 = b.getWorld().getBlockAt(b.getLocation().add(1, 0, 0));
                        b2 = b.getWorld().getBlockAt(b.getLocation().add(-1, 0, 0));
                    }
                } else if (PlayerInfo.getFacingDirection(p).equals("W") || PlayerInfo.getFacingDirection(p).equals("E")) {
                    if (config.getBoolean("Power.lv1_vertical")) {
                        b1 = b.getWorld().getBlockAt(b.getLocation().add(1, 0, 0));
                        b2 = b.getWorld().getBlockAt(b.getLocation().add(-1, 0, 0));
                    } else {
                        b1 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, 1));
                        b2 = b.getWorld().getBlockAt(b.getLocation().add(0, 0, -1));
                    }
                } else {
                    b1 = b;
                    b2 = b;
                }
                powerCreateFarmland(p, tool, b1);
                powerCreateFarmland(p, tool, b2);
            }
        } else {
            if (PlayerData.BLOCKFACE.get(p).equals(BlockFace.DOWN) || PlayerData.BLOCKFACE.get(p).equals(BlockFace.UP)) {
                for (int x = -(level - 1); x <= (level - 1); x++) {
                    for (int z = -(level - 1); z <= (level - 1); z++) {
                        if (!(x == 0 && z == 0)) {
                            Block b_ = p.getWorld().getBlockAt(b.getLocation().add(x, 0, z));
                            powerCreateFarmland(p, tool, b_);
                        }
                    }
                }
            }
        }

        HASPOWER.replace(p, false);
    }

    private void powerBlockBreak(Block b, CraftPlayer p) {
        if (!b.getType().equals(Material.AIR) && !b.getType().equals(Material.CAVE_AIR) && !b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.WATER) && !b.getType().equals(Material.BUBBLE_COLUMN) && !b.getType().equals(Material.LAVA)) {
            p.getHandle().playerInteractManager.breakBlock(new BlockPosition(b.getX(), b.getY(), b.getZ()));
        }
    }

    private static void powerCreateFarmland(Player p, ItemStack tool, Block b) {
        if (b.getType().equals(Material.GRASS_BLOCK) || b.getType().equals(Material.DIRT)) {
            if (b.getWorld().getBlockAt(b.getLocation().add(0, 1, 0)).getType().equals(Material.AIR)) {
                tool.setDurability((short) (tool.getDurability() + 1));
                Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(p, Action.RIGHT_CLICK_BLOCK, tool, b, BlockFace.UP));
                b.setType(Material.FARMLAND); //Event only does Plugin event (no vanilla conversion to Farmland and Tool-Damage)
            }
        }
    }

    @Override
    public void enchantItem(Player p, ItemStack item) {
        if (!p.hasPermission("minetinker.modifiers.power.craft")) { return; }
        ItemGenerator.createModifierItem(p, this, "Power");
    }

    @Override
    public void registerCraftingRecipe() {
        _registerCraftingRecipe(config, modManager, ModifierType.POWER, "Power", "Modifier_Power");
    }
}
