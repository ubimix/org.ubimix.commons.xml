package org.webreformatter.xml.entities.html;

import org.webreformatter.xml.entities.Entity;
import org.webreformatter.xml.entities.EntityFactory;

public class XHTML_ISO_8859_1_Symbols extends XHTMLSymbolsEntities {

    /** "´" - spacing acute */
    public final Entity S_acute = newEntityKey("acute", 180);

    /** "¦" - broken vertical bar */
    public final Entity S_brvbar = newEntityKey("brvbar", 166);

    /** "¸" - spacing cedilla */
    public final Entity S_cedil = newEntityKey("cedil", 184);

    /** "¢" - cent */
    public final Entity S_cent = newEntityKey("cent", 162);

    /** "©" - copyright */
    public final Entity S_copy = newEntityKey("copy", 169);

    /** "¤" - currency */
    public final Entity S_curren = newEntityKey("curren", 164);

    /** "°" - degree */
    public final Entity S_deg = newEntityKey("deg", 176);

    /** "÷" - division */
    public final Entity S_divide = newEntityKey("divide", 247);

    /** "½" - fraction 1/2 */
    public final Entity S_frac12 = newEntityKey("frac12", 189);

    /** "¼" - fraction 1/4 */
    public final Entity S_frac14 = newEntityKey("frac14", 188);

    /** "¾" - fraction 3/4 */
    public final Entity S_frac34 = newEntityKey("frac34", 190);

    /** "¡" - inverted exclamation mark */
    public final Entity S_iexcl = newEntityKey("iexcl", 161);

    /** "¿" - inverted question mark */
    public final Entity S_iquest = newEntityKey("iquest", 191);

    /** "«" - angle quotation mark (left) */
    public final Entity S_laquo = newEntityKey("laquo", 171);

    /** "¯" - spacing macron */
    public final Entity S_macr = newEntityKey("macr", 175);

    /** "µ" - micro */
    public final Entity S_micro = newEntityKey("micro", 181);

    /** "·" - middle dot */
    public final Entity S_middot = newEntityKey("middot", 183);

    /** " " - non-breaking space */
    public final Entity S_nbsp = newEntityKey("nbsp", 160);

    /** "¬" - negation */
    public final Entity S_not = newEntityKey("not", 172);

    /** "ª" - feminine ordinal indicator */
    public final Entity S_ordf = newEntityKey("ordf", 170);

    /** "º" - masculine ordinal indicator */
    public final Entity S_ordm = newEntityKey("ordm", 186);

    /** "¶" - paragraph */
    public final Entity S_para = newEntityKey("para", 182);

    /** "±" - plus-or-minus */
    public final Entity S_plusmn = newEntityKey("plusmn", 177);

    /** "£" - pound */
    public final Entity S_pound = newEntityKey("pound", 163);

    /** "»" - angle quotation mark (right) */
    public final Entity S_raquo = newEntityKey("raquo", 187);

    /** "®" - registered trademark */
    public final Entity S_reg = newEntityKey("reg", 174);

    /** "§" - section */
    public final Entity S_sect = newEntityKey("sect", 167);

    /** " " - soft hyphen */
    public final Entity S_shy = newEntityKey("shy", 173);

    /** "¹" - superscript 1 */
    public final Entity S_sup1 = newEntityKey("sup1", 185);

    /** "²" - superscript 2 */
    public final Entity S_sup2 = newEntityKey("sup2", 178);

    /** "³" - superscript 3 */
    public final Entity S_sup3 = newEntityKey("sup3", 179);

    /** "×" - multiplication */
    public final Entity S_times = newEntityKey("times", 215);

    /** "¨" - spacing diaeresis */
    public final Entity S_uml = newEntityKey("uml", 168);

    /** "¥" - yen */
    public final Entity S_yen = newEntityKey("yen", 165);

    public XHTML_ISO_8859_1_Symbols(EntityFactory factory) {
        super(factory);
    }

}
