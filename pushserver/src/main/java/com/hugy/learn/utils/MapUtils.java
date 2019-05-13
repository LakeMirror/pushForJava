package com.hugy.learn.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapUtils {

    public static Map<String, Object> asMap(Object... keyvalues){
        System.out.println(keyvalues.length);
        try {
            if (keyvalues.length % 2 != 0) {
                throw new Exception("KEY VALUE NUMBER IS NOT DOUBLE");
            }
        } catch (Exception e)  {
            e.printStackTrace();
            return  null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < keyvalues.length / 2; i++) {
            map.put(keyvalues[2 * i].toString(), keyvalues[2 * i + 1]);
        }
        return map;
    }

    public static Map<String, Object> toMap(Object obj) {
        Objects.requireNonNull(obj);
        Map<String, Object> map = new HashMap<>();

        try {
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }
}
