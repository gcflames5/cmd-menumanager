package net.njay.cmdmanager.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * @return All possible aliases for the command
     */
    String[] aliases();

    /**
     * @return Minimum number of arguments
     */
    int min() default 0;

    /**
     * @return Maximum number of arguments
     */
    int max() default 0;

    /**
     * @return Displays when command argument lengths is outside the max/min
     */
    String usage() default "";

    /**
     * @return If true, command will run everywhere, if false it will only run when its screen is active, method must be static or an error will be thrown
     */
    boolean global() default false;

}
