package net.njay.cmdmanager.command;

import net.njay.cmdmanager.Arguments;
import net.njay.cmdmanager.MenuManager;
import net.njay.cmdmanager.annotation.Command;
import net.njay.cmdmanager.menu.Menu;

import java.util.List;

public class HelpCommand extends Menu {

    public HelpCommand(MenuManager manager) {
        super(manager);
    }

    @Command(aliases = {"help", "h"}, min = -1, max = -1, usage = "", global = true)
    public void help(Arguments args){
        System.out.println("Possible commands: " + formatViableCommands(screenManager.getViableCommands()));
    }

    public static String formatViableCommands(List<String> commands){
        StringBuilder builder = new StringBuilder();
        for (String s : commands){
            builder.append(s + ", ");
        }
        return builder.toString().substring(0, builder.length()-3);
    }
}
