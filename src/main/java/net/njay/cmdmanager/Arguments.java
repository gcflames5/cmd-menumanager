package net.njay.cmdmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 7/9/2014.
 */
public class Arguments {

    private List<String> args;

    public Arguments(String... arguments){
        args = new ArrayList<String>();
        Collections.addAll(args, arguments);
    }

    public List<String> getArgs(){
        return this.args;
    }

    public int getInteger(int id){
        return Integer.valueOf(getArgs().get(id));
    }

    public double getDouble(int id){
        return Double.valueOf(getArgs().get(id));
    }

    public float getFloat(int id){
        return Float.valueOf(getArgs().get(id));
    }

    public long getLong(int id){
        return Long.valueOf(getArgs().get(id));
    }

    public <T> T get(int id){
        return (T) getArgs().get(id);
    }

}
