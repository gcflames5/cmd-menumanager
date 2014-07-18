CmdMenu
=========

A small library that handles a command hierarchy using the command line

[JavaDocs](http://docs.njay.net/cmdMenus/)

Tutorial
========

**For regular (non-global) commands:** The command must only take 1 argument: net.njay.cmd.Menu.Arguments

**For global commands:** The command must be public and static, and carries the same argument requirement

Creating a Local Command:
------

Use the **@Command** annotation:

**aliases**: A String[] of acceptable Strings that will trigger this command

**min**: minimum number of arguments after command (use -1 for no requirement)

**max**: maximum number of arguments after commands (use -1 for no requirement)

**usage**: if the user inputs an unacceptable amount of arguments, the usage variable will be shown to them

**global**: whether or not the command is global (default=false)

```java
@Command(aliases = {"alias1", "alias2"}, min = 1, max = 3, usage = "<number> <opt:anotherNumber> <opt:anotherNumber>")
public void ping(Arguments args){
    System.out.println("PONG! First Argument:" + args.getArgs().get(0));
}
```

Creating a Global Command:
--------
Same as creating a local command, however, you must set the global variable to true and the **method must be static**
```java
@Command(aliases = {"changeMenu"}, min = 1, max = 1, usage = "<number>", global = true)
public static void change(Arguments args) throws ClassNotFoundException, NoSuchMenuException {
    manager.setActiveMenu((Class<? extends Menu>) Class.forName(args.getArgs().get(0)));
}
```

Creating a new Menu (with Commands):
------

```java
public class TestMenu extends Menu {

    public TestMenu(MenuManager manager) {
        super(manager);
    }

    @Command(aliases = {"ping", "ping1"}, min = 1, max = 1, usage = "<number>")
    public void ping(Arguments args){
        System.out.println("PONG! First Argument:" + args.getArgs().get(0));
    }

    @Command(aliases = {"pong", "pong2"}, min = -1, max = -1, usage = "<number>") //use -1 to signify a lack of requirement
    public void pong(Arguments args){
        System.out.println("PING! First Argument" + args.getArgs().get(0));
    }

    @Command(aliases = {"changeMenu"}, min = 1, max = 1, usage = "<number>", global = true)
    public static void change(Arguments args) throws ClassNotFoundException, NoSuchMenuException {
        manager.setActiveMenu((Class<? extends Menu>) Class.forName(args.getArgs().get(0)));
    }
}
```

Use **@NestedMenu** to automatically register other menus before this is registered:
```java
@NestedMenu(classes = {TestSubMenu.class})
public class TestMenu extends Menu {}
```

Initializing and Running the MenuManager:
-------
```java
MenuManager manager = new MenuManager(new Scanner(System.in), TestMenu.class); //Create a new MenuManager with the default scanner and load the TestMenu.class Menu
manager.loop(TestMenu.class); //start the loop with TestMenu as the intial Menu
```
