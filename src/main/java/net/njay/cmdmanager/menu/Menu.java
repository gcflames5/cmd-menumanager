package net.njay.cmdmanager.menu;

import net.njay.cmdmanager.MenuManager;
import net.njay.cmdmanager.annotation.Command;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class Menu {

    protected MenuManager screenManager;
    private Map<Command, Method> subCommands;

    public Menu(MenuManager manager){
        this.screenManager = manager;
    }

    public void setSubCommands(Map<Command, Method> subCommands){
        this.subCommands = subCommands;
    }

    public Map<Command, Method> getSubCommands(){
        return this.subCommands;
    }

}
