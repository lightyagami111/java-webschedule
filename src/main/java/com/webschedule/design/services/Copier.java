/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.services;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author ivaylo
 */
public final class Copier {
    
    static Logger log = Logger.getLogger(Copier.class.getName());

    public static void copy(final Object from, final Object to) {
        Map<String, Field> fromFields = analyze(from);
        Map<String, Field> toFields = analyze(to);
        fromFields.keySet().retainAll(toFields.keySet());
        for (Entry<String, Field> fromFieldEntry : fromFields.entrySet()) {
            final String name = fromFieldEntry.getKey();
            final Field sourceField = fromFieldEntry.getValue();
            final Field targetField = toFields.get(name);
            if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                sourceField.setAccessible(true);
                if (Modifier.isFinal(targetField.getModifiers())) continue;
                targetField.setAccessible(true);
                try {
                    targetField.set(to, sourceField.get(from));
                } catch (IllegalAccessException e) {
                    log.error("Can't access field!", e);
                    throw new IllegalStateException("Can't access field!");
                }
            }
        }
    }

    private static Map<String, Field> analyze(Object object) {
        if (object == null) throw new NullPointerException();

        Map<String, Field> map = new TreeMap<String, Field>();

        Class<?> current = object.getClass();
        while (current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
            current = current.getSuperclass();
        }
        return map;
    }
}
