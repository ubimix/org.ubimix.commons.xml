/**
 * 
 */
package org.webreformatter.xml.entities;

/**
 * @author kotelnikov
 */
public class EntityReader {

    public static void main(String[] args) {
        String str = "039";
        System.out.println(Integer.parseInt(str));
    }

    private EntityFactory fFactory;

    private boolean fIgnoreUnknownEntities;

    /**
     * 
     */
    public EntityReader(EntityFactory factory) {
        this(factory, true);
    }

    /**
     * 
     */
    public EntityReader(EntityFactory factory, boolean ignoreUnknownEntities) {
        fFactory = factory;
        fIgnoreUnknownEntities = ignoreUnknownEntities;
    }

    protected Entity getEntityKey(boolean digit, String str) {
        Entity entity;
        int code = -1;
        if (digit) {
            code = Integer.parseInt(str);
            entity = fFactory.getEntityKeyByCode(code);
            str = null;
        } else {
            entity = fFactory.getEntityKeyByName(str);
        }
        if (entity == null && (digit || !fIgnoreUnknownEntities)) {
            entity = new Entity(str, code);
        }
        return entity;
    }

    public Entity readEntity(CharSequence stream, int[] pos) {
        int idx = pos[0];
        int len = stream.length();
        char ch = stream.charAt(idx);
        if (ch != '&') {
            return null;
        }
        Entity entity = null;
        idx++;
        if (idx >= len) {
            return null;
        }
        int begin = idx;
        ch = stream.charAt(idx);
        boolean digit = false;
        if (ch == '#') {
            digit = true;
            idx++;
            if (idx >= len) {
                return null;
            }
            ch = stream.charAt(idx);
            begin = idx;
            while (Character.isDigit(ch)) {
                idx++;
                if (idx >= len) {
                    return null;
                }
                ch = stream.charAt(idx);
            }
        } else {
            while (Character.isLetterOrDigit(ch)) {
                idx++;
                if (idx >= len) {
                    return null;
                }
                ch = stream.charAt(idx);
            }
        }
        int end = idx;
        if (begin == end) {
            return null;
        }
        ch = stream.charAt(idx);
        if (ch != ';') {
            return null;
        }
        idx++;

        CharSequence str = stream.subSequence(begin, end);
        entity = getEntityKey(digit, str.toString());
        if (entity == null) {
            return null;
        }
        if (entity != null) {
            pos[0] = idx;
        }
        return entity;
    }
}
