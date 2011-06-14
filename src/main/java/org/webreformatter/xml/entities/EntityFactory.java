/**
 * 
 */
package org.webreformatter.xml.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class EntityFactory {

    private Map<Integer, Entity> fCodeToEntity = new HashMap<Integer, Entity>();

    private Map<String, Entity> fNameToEntity = new HashMap<String, Entity>();

    /**
     * 
     */
    public EntityFactory() {
    }

    public Entity getEntityKeyByCode(int code) {
        return fCodeToEntity.get(code);
    }

    public Entity getEntityKeyByName(String name) {
        return fNameToEntity.get(name);
    }

    public int getSize() {
        return fCodeToEntity.size();
    }

    public Entity newEntityKey(String name, int code) {
        Entity oldKey = fCodeToEntity.get(code);
        if (oldKey != null) {
            if (!name.equals(oldKey.getName())) {
                throw new IllegalStateException(
                    "An entity with this code is already registered, "
                        + "but it has an another name.");
            }
            return oldKey;
        }
        oldKey = fNameToEntity.get(name);
        if (oldKey != null) {
            if (code != oldKey.getCode()) {
                throw new IllegalStateException(
                    "An entity with this name is already registered, "
                        + "but it has an another code.");
            }
            return oldKey;
        }
        Entity key = new Entity(name, code);
        fCodeToEntity.put(code, key);
        fNameToEntity.put(name, key);
        return key;
    }
}
