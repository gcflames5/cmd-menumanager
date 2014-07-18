package net.njay.cmdmanager.annotation;

import net.njay.cmdmanager.menu.Menu;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NestedMenu {

    /**
     * @return Menu Classes that will be registered along with this one
     */
    Class<? extends Menu>[] classes();


}
