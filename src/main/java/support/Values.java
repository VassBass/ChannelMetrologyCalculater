package support;

import exceptions.ValueClassException;

import java.io.Serializable;
import java.util.ArrayList;

public class Values implements Serializable {
    private final ArrayList<String> keys = new ArrayList<>();
    private final ArrayList<Object> values = new ArrayList<>();

    public int valueIndex(String key){
        for (int x=0;x<keys.size();x++){
            if (keys.get(x).equals(key)){
                return x;
            }
        }
        return -1;
    }

    public void putValue(String key, Object value){
        int index = this.valueIndex(key);
        if (index == -1) {
            this.keys.add(key);
            this.values.add(value);
        }else {
            this.values.set(index, value);
        }
    }

    public String getStringValue(String key) throws ValueClassException {
        int index = this.valueIndex(key);
        if (index == -1){
            return null;
        }else {
            if (this.values.get(index) instanceof String){
                return (String) this.values.get(index);
            }else {
                throw new ValueClassException("");
            }
        }
    }

    public Double getDoubleValue(String key) throws ValueClassException {
        int index = this.valueIndex(key);
        if (index == -1){
            return null;
        }else {
            if (this.values.get(index) instanceof Double){
                return (double) this.values.get(index);
            }else {
                throw new ValueClassException(0D);
            }
        }
    }

    public Integer getIntValue(String key) throws ValueClassException {
        int index = this.valueIndex(key);
        if (index == -1){
            return null;
        }else {
            if (this.values.get(index) instanceof Integer){
                return (int) this.values.get(index);
            }else {
                throw new ValueClassException(0);
            }
        }
    }

    public Boolean getBooleanValue(String key) throws ValueClassException {
        int index = this.valueIndex(key);
        if (index == -1){
            return null;
        }else {
            if (this.values.get(index) instanceof Boolean){
                return (boolean) this.values.get(index);
            }else {
                throw new ValueClassException(Boolean.TRUE);
            }
        }
    }

    public Object getValue(String key) {
        int index = this.valueIndex(key);
        if (index == -1){
            return null;
        }else {
            return this.values.get(index);
        }
    }

    public void removeValue(String key){
        int index = this.valueIndex(key);
        if (index != -1){
            this.keys.remove(index);
            this.values.remove(index);
        }
    }

    public void printAllValues(){
        for (int x=0;x<this.keys.size();x++){
            System.out.println("key: \"" + this.keys.get(x) + "\" value: \"" + String.valueOf(this.values.get(x)) + "\"");
        }
    }

    public void printValue(String key){
        int index = this.valueIndex(key);
        if (index == -1){
            System.out.println("Value not found");
        }else {
            System.out.println("key: \"" + this.keys.get(index) + "\" value: \"" + String.valueOf(this.values.get(index)) + "\"");
        }
    }
}
