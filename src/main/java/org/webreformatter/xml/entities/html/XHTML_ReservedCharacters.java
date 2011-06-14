package org.webreformatter.xml.entities.html;

import org.webreformatter.xml.entities.Entity;
import org.webreformatter.xml.entities.EntityFactory;

public class XHTML_ReservedCharacters extends XHTMLSymbolsEntities {

    /** "&" - ampersand */
    public final Entity S_amp = newEntityKey("amp", 38);

    /** "'" - (does not work in IE) apostrophe */
    public final Entity S_apos = newEntityKey("apos", 39);

    /** ">" - greater-than */
    public final Entity S_gt = newEntityKey("gt", 62);

    /** '&lt;' - less-than */
    public final Entity S_lt = newEntityKey("lt", 60);

    /** """ - quotation mark */
    public final Entity S_quot = newEntityKey("quot", 34);

    public XHTML_ReservedCharacters(EntityFactory factory) {
        super(factory);
    }

}
