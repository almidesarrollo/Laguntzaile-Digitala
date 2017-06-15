package com.example.dam2_yeray.modulomovil.PostPhp;

import java.util.ArrayList;

public class PostArray {

    private ArrayList<PostValue> valuePairs;

    public PostArray() {
        this.valuePairs = new ArrayList<>();
    }

    public int size() {
        return this.valuePairs.size();
    }

    public void setValue(String key, String value) {
        if (!hasKey(key)) {
            valuePairs.add(new PostValue(key, value));
        } else {
            for (int i=0;i<valuePairs.size();i++) {
                if (valuePairs.get(i).getKey().equals(key))
                    valuePairs.get(i).setValue(key);
            }
        }
    }

    public void setValue(int index, String value) {
        valuePairs.get(index).setValue(value);
    }

    public String getValue(String key) {
        if (!hasKey(key))
            return null;

        for (PostValue p:valuePairs) {
            if (p.getKey().equals(key))
                return p.getValue();
        }

        return null;
    }

    public String getValue(int index) {
        return valuePairs.get(index).getValue();
    }

    public String getKey(int index) {
        return valuePairs.get(index).getKey();
    }

    public void removeValue(String key) {
        for (int i=0;i<valuePairs.size();i++) {
            if (valuePairs.get(i).getKey().equals(key))
                valuePairs.remove(i);
        }
    }

    public boolean hasKey(String key) {
        for (PostValue p:valuePairs) {
            if (p.getKey().equals(key))
                return true;
        }
        return false;
    }

}
