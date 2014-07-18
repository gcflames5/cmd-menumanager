package net.njay.cmdmanager;

import net.njay.cmdmanager.annotation.Command;
import net.njay.cmdmanager.annotation.NestedMenu;
import net.njay.cmdmanager.command.HelpCommand;
import net.njay.cmdmanager.menu.Menu;

import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class MenuManager {

    private Scanner scanner;
    private Map<Class<? extends Menu>, Menu> loadedScreens;
    private Map<Command, Method> globalComamnds;
    private Menu activeMenu;

    /**
     * Constructor.
     *
     * @param scanner Scanner to read from
     * @param classes Classes to register
     */
    public MenuManager(Scanner scanner, Class<? extends Menu>... classes){
        this.scanner = scanner;
        loadedScreens = new HashMap<Class<? extends Menu>, Menu>();
        globalComamnds = new HashMap<Command, Method>();
        loadMenu(HelpCommand.class);
        for (Class clazz : classes)
            loadMenu(clazz);
    }

    /**
     * Begin the loop to check user input
     *
     * @param clazz Starting menu class
     */
    public void loop(Class<? extends Menu> clazz){
        loop(getMenu(clazz));
    }

    /**
     * Begin the loop to check user input
     *
     * @param menu Starting menu
     */
    public void loop(Menu menu){
        this.activeMenu = menu;
        while (true){
            String line = scanner.nextLine();
            String args[] = line.split(" ");
            Map<Command, Method> commands = new HashMap<Command, Method>(globalComamnds);
            commands.putAll(activeMenu.getSubCommands());
            main: for (Map.Entry<Command, Method> entry : commands.entrySet()){
                for (String alias : entry.getKey().aliases()){
                    if (args[0].equalsIgnoreCase(alias)){
                        if ((args.length-1 < entry.getKey().min() && entry.getKey().min() != -1) || (args.length-1 > entry.getKey().max() && entry.getKey().max() != -1)){
                            System.out.println("Incorrect number of arguments! Usage: /" + alias + " " + entry.getKey().usage());
                        }else{
                            invoke(entry.getValue(), getMenu((Class<? extends Menu>) entry.getValue().getDeclaringClass()), args);
                        }
                        break main;
                    }
                }
            }
            System.out.println("Command not found! Possible commands: " + HelpCommand.formatViableCommands(getViableCommands()));
        }
    }

    /**
     * Loads a menu and its commands into memory for later use
     *
     * @param menu Menu instance that you wish to load its subcommands into
     */
    public void loadMenu(Menu menu){
        try {
            Map<Command, Method> subCommands = new HashMap<Command, Method>();
            if (menu.getClass().isAnnotationPresent(NestedMenu.class)){
                NestedMenu nestedMenu = menu.getClass().getAnnotation(NestedMenu.class);
                for (Class<? extends Menu> clazz : nestedMenu.classes()) {
                    loadMenu(clazz);
                }
            }
            for (Method method : menu.getClass().getMethods()) {
                if (method.isAnnotationPresent(Command.class)) {
                    Command command = method.getAnnotation(Command.class);
                    if (command.global())
                        globalComamnds.put(command, method);
                    else
                        subCommands.put(method.getAnnotation(Command.class), method);
                }
            }
            menu.setSubCommands(subCommands);
            loadedScreens.put(menu.getClass(), menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a menu and its commands into memory for later use
     *
     * @param clazz Class of the menu that you which to load
     */
    public void loadMenu(Class<? extends Menu> clazz) {
        try {
            loadMenu(clazz.getConstructor(getClass()).newInstance(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches the loaded screens and returns one with a matching name
     *
     * @param clazz Class of the desired screen
     * @return Found screen
     */
    public Menu getMenu(Class<? extends Menu> clazz) {
        if (loadedScreens.containsKey(clazz))
            return loadedScreens.get(clazz);
        else{
            loadMenu(clazz);
            return getMenu(clazz);
        }
    }

    /**
     * Sets the active screen, all other non-global commands will be ignored
     *
     * @param clazz Class of the Screen to set as the new screen
     */
    public void setActiveMenu(Class<? extends Menu> clazz){
        activeMenu = getMenu(clazz);
    }

    /**
     * Sets the active screen, all other non-global commands will be ignored
     *
     * @param menu Screen to set as the new screen
     */
    public void setActiveMenu(Menu menu, boolean load){
        if (load) loadMenu(menu);
        activeMenu = menu;
    }

    /**
     * @return ArrayList of all the commands that the user can perform at this moment in time
     */
    public List<String> getViableCommands(){
        List<String> list = new ArrayList<String>();
        Map<Command, Method> commands = new HashMap<Command, Method>(globalComamnds);
        commands.putAll(activeMenu.getSubCommands());
        for (Map.Entry<Command, Method> entry : commands.entrySet()){
            list.add(entry.getKey().aliases()[0]);
        }
        return list;
    }

    private void invoke(Method method, Object invoker, String[] args){
        try {
            if (args.length > 2) {
                method.invoke(invoker, new Arguments(Arrays.copyOfRange(args, 1, args.length - 1)));
            }else if (args.length == 2) {
                method.invoke(invoker, new Arguments(args[1]));
            }else {
                method.invoke(invoker, new Arguments(args[0]));
            }
        }catch(Exception e1){
            e1.printStackTrace();
            System.out.println("Failed to invoke method! Make sure methods with the @Command annotation only take 1 Arguments class as a parameter and global commands are public and static!!");
        }
    }
}