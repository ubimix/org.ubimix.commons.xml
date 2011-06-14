package org.webreformatter.xml.entities.html;

import org.webreformatter.xml.entities.EntityFactory;

/**
 * @author kotelnikov
 */
public class XHTML_AllEntities extends XHTMLSymbolsEntities {

    public final XHTML_ISO_8859_1_Characters ISO_8859_1_Characters;

    public final XHTML_ISO_8859_1_Symbols ISO_8859_1_Symbols;

    public final XHTML_ReservedCharacters RESERVED_CHARACTERS;

    public final XHTMLSpecialEntities SPECIAL_ENTITIES;

    public final XHTMLSymbolsEntities SYMBOL_ENTITIES;

    public XHTML_AllEntities(EntityFactory factory) {
        super(factory);
        ISO_8859_1_Characters = new XHTML_ISO_8859_1_Characters(factory);
        ISO_8859_1_Symbols = new XHTML_ISO_8859_1_Symbols(factory);
        RESERVED_CHARACTERS = new XHTML_ReservedCharacters(factory);
        SPECIAL_ENTITIES = new XHTMLSpecialEntities(factory);
        SYMBOL_ENTITIES = new XHTMLSymbolsEntities(factory);
    }

}
