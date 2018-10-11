package de.flo56958.MineTinker.Commands;

import de.flo56958.MineTinker.Data.Strings;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (sender.hasPermission("minetinker.commands.main")) {
                if (args.length == 0) {
                    ChatWriter.sendMessage(p, ChatColor.RED, "You have entered to few arguments!");
                    ChatWriter.sendMessage(p, ChatColor.WHITE, "Possible arguments are:");
                    onHelp(p);
                }
                if (args.length > 0) {
                    if ((args[0].toLowerCase().equals("help") || args[0].toLowerCase().equals("?")) && p.hasPermission("minetinker.commands.help")) {
                        onHelp(p);
                    } else if ((args[0].toLowerCase().equals("info") || args[0].toLowerCase().equals("i")) && p.hasPermission("minetinker.commands.info")) {
                        info(p);
                    } else if ((args[0].toLowerCase().equals("modifiers") || args[0].toLowerCase().equals("mods")) && p.hasPermission("minetinker.commands.modifiers")) {
                        Mods.modifierList(p);
                    } else if ((args[0].toLowerCase().equals("addexp") || args[0].toLowerCase().equals("ae")) && p.hasPermission("minetinker.commands.addexp")){
                        Functions.addExp(p, args);
                    } else if ((args[0].toLowerCase().equals("name") || args[0].toLowerCase().equals("n")) && p.hasPermission("minetinker.commands.name")){
                        Functions.name(p, args);
                    } else if ((args[0].toLowerCase().equals("addmod") || args[0].toLowerCase().equals("am")) && p.hasPermission("minetinker.commands.addmod")){
                        Functions.addMod(p, args);
                    } else if ((args[0].toLowerCase().equals("removemod") || args[0].toLowerCase().equals("rm")) && p.hasPermission("minetinker.commands.removemod")){
                        Functions.removeMod(p, args);
                    } else if ((args[0].toLowerCase().equals("setdurability") || args[0].toLowerCase().equals("sd")) && p.hasPermission("minetinker.commands.setdurability")){
                        Functions.setDurability(p, args);
                    } else if (((args[0].toLowerCase().equals("give") || args[0].toLowerCase().equals("g")) && p.hasPermission("minetinker.commands.give"))) {
                        Functions.give(p, args);
                    } else {
                        ChatWriter.sendMessage(p, ChatColor.RED, "You have entered a wrong or too many argument(s)! Or you do not have permission to use this command");
                        ChatWriter.sendMessage(p, ChatColor.WHITE, "Possible arguments are:");
                        onHelp(p);
                    }
                }
            }
        } else {
            sender.sendMessage(Strings.CHAT_PREFIX + " This is a player only command");
        }
        return true;
    }

    static void info(Player p) {
        ChatWriter.sendMessage(p, ChatColor.WHITE, "MineTinker is a Plugin made by Flo56958.");
        ChatWriter.sendMessage(p, ChatColor.WHITE, "It is inspired by different mods (e.g. TinkersConstruct)");
    }

    private void onHelp (Player p) {
        int index = 1;
        if (p.hasPermission("minetinker.commands.info")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". Info (i)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.help")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". Help (?)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.modifiers")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". Modifiers (mods)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.addexp")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". AddExp (ae)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.name")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". Name (n)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.addmod")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". AddMod (am)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.removemod")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". RemoveMod (rm)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.setdurability")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". SetDurability (sd)");
            index++;
        }
        if (p.hasPermission("minetinker.commands.give")) {
            ChatWriter.sendMessage(p, ChatColor.WHITE, index + ". Give (g)");
        }
    }
}