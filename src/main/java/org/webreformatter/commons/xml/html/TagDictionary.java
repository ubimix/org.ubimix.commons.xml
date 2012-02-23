/**
 * 
 */
package org.webreformatter.commons.xml.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kotelnikov
 */
public class TagDictionary {

    public static final String A = "a";

    public static final Set<String> ALL_ATTRIBUTES = new HashSet<String>();

    // The full list of all elements.
    public static final Set<String> ALL_ELEMENTS = new HashSet<String>();

    public static final String ATTR_ALIGN = "align";

    public static final String ATTR_ALT = "alt";

    public static final String ATTR_BACKGROUND = "background";

    public static final String ATTR_BGCOLOR = "bgcolor";

    public static final String ATTR_BORDER = "border";

    public static final String ATTR_CELLPADDING = "cellpadding";

    public static final String ATTR_CELLSPACING = "cellspacing";

    public static final String ATTR_CLASS = "class";

    public static final String ATTR_COLOR = "color";

    public static final String ATTR_FACE = "face";

    public static final String ATTR_HALIGN = "valign";

    public static final String ATTR_HREF = "href";

    public static final String ATTR_ID = "id";

    public static final String ATTR_SIZE = "size";

    public static final String ATTR_SRC = "src";

    public static final String ATTR_STYLE = "style";

    public static final String ATTR_TARGET = "target";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_VALIGN = "valign";

    public static final String B = "b";

    // The full list of elements which can contain other block elements
    public static final Set<String> BLOCK_CONTAINER_ELEMENTS = new LinkedHashSet<String>();

    // The full list of all block elements.
    public static final Set<String> BLOCK_ELEMENTS = new LinkedHashSet<String>();

    public static final String BLOCKQUOTE = "blockquote";

    public static final String BODY = "body";

    public static final String BR = "br";

    public static final String BUTTON = "button";

    public static final String DD = "dd";

    public static final String DIV = "div";

    public static final String DL = "dl";

    public static final String DT = "dt";

    public static final String EM = "em";

    public static final Set<String> EMPTY_ELEMENTS = new HashSet<String>();

    /**
     * @see #LEGEND
     */
    public static final String FIELDSET = "fieldset";

    public static final String FONT = "font";

    public static final String FORM = "form";

    public static final String H1 = "h1";

    public static final String H2 = "h2";

    public static final String H3 = "h3";

    public static final String H4 = "h4";

    public static final String H5 = "h5";

    public static final String H6 = "h6";

    public static final String HEAD = "head";

    public static final List<String> HEADERS = new ArrayList<String>();

    public static final String HR = "hr";

    public static final String I = "i";

    public static final String IFRAME = "iframe";

    public static final String IMG = "img";

    public static final List<String> INLINE_ELEMENTS = new ArrayList<String>();

    public static final String INPUT = "input";

    public static final String LABEL = "label";

    /**
     * @see #FIELDSET
     */
    public static final String LEGEND = "legend";

    public static final String LI = "li";

    public static final Set<String> NON_CONTENT_ELEMENTS = new HashSet<String>();

    public static final String NOSCRIPT = "noscript";

    public static final String OBJECT = "object";

    public static final String OL = "ol";

    public static final String OPTION = "option";

    public static final String P = "p";

    public static final String PRE = "pre";

    public static final String SCRIPT = "script";

    public static final String SELECT = "select";

    public static final String SPAN = "span";

    public static final String STRONG = "strong";

    public static final String STYLE = "style";

    public static final String TABLE = "table";

    public static final String TBODY = "tbody";

    public static final String TD = "td";

    public static final String TEXTAREA = "textarea";

    public static final String TH = "th";

    public static final String THEAD = "thead";

    public static final String TR = "tr";

    public static final String UL = "ul";

    static {
        HEADERS.add(H1);
        HEADERS.add(H2);
        HEADERS.add(H3);
        HEADERS.add(H4);
        HEADERS.add(H5);
        HEADERS.add(H6);

        BLOCK_ELEMENTS.addAll(HEADERS);
        BLOCK_ELEMENTS.addAll(Arrays.asList(
            BLOCKQUOTE,
            DIV,
            DL,
            FIELDSET,
            LEGEND,
            HR,
            IFRAME,
            OL,
            P,
            PRE,
            UL,
            TABLE));

        BLOCK_CONTAINER_ELEMENTS.addAll(Arrays.asList(
            BLOCKQUOTE,
            DIV,
            DD,
            FIELDSET,
            LI,
            PRE,
            TD,
            TH));

        NON_CONTENT_ELEMENTS.addAll(Arrays.asList(
            BUTTON,
            FORM,
            HEAD,
            IFRAME,
            INPUT,
            OPTION,
            SELECT,
            SCRIPT,
            STYLE,
            TEXTAREA));
        EMPTY_ELEMENTS.addAll(Arrays.asList(HR, IMG, BR));

        INLINE_ELEMENTS.addAll(Arrays
            .asList(A, B, BR, EM, STRONG, I, IMG, SPAN));

        ALL_ATTRIBUTES.addAll(Arrays.asList(
            ATTR_ALIGN,
            ATTR_ALT,
            ATTR_BACKGROUND,
            ATTR_BGCOLOR,
            ATTR_BORDER,
            ATTR_CELLPADDING,
            ATTR_CELLSPACING,
            ATTR_CLASS,
            ATTR_COLOR,
            ATTR_FACE,
            ATTR_HALIGN,
            ATTR_HREF,
            ATTR_ID,
            ATTR_SIZE,
            ATTR_SRC,
            ATTR_STYLE,
            ATTR_TARGET,
            ATTR_TITLE,
            ATTR_VALIGN));

        ALL_ELEMENTS.addAll(Arrays.asList(
            A,
            B,
            BLOCKQUOTE,
            BODY,
            BR,
            BUTTON,
            DD,
            DIV,
            DL,
            DT,
            EM,
            FIELDSET,
            FONT,
            FORM,
            H1,
            H2,
            H3,
            H4,
            H5,
            H6,
            HEAD,
            HR,
            I,
            IFRAME,
            IMG,
            INPUT,
            LABEL,
            LEGEND,
            LI,
            NOSCRIPT,
            OBJECT,
            OL,
            OPTION,
            P,
            PRE,
            SCRIPT,
            SELECT,
            SPAN,
            STRONG,
            STYLE,
            TABLE,
            TBODY,
            TD,
            TEXTAREA,
            TH,
            THEAD,
            TR,
            UL));
    }

    public static int getHeaderLevel(String name) {
        return HEADERS.indexOf(name) + 1;
    }

    public static boolean isBlockContainerElement(String name) {
        return BLOCK_CONTAINER_ELEMENTS.contains(name);
    }

    public static boolean isBlockElement(String name) {
        return BLOCK_ELEMENTS.contains(name);
    }

    public static boolean isContentElement(String name) {
        return !NON_CONTENT_ELEMENTS.contains(name);
    }

    public static boolean isDivElement(String name) {
        return DIV.equals(name);
    }

    public static boolean isEmptyElement(String name) {
        return EMPTY_ELEMENTS.contains(name);
    }

    public static boolean isFieldset(String name) {
        return FIELDSET.equals(name);
    }

    public static boolean isFontElement(String name) {
        return FONT.equals(name);
    }

    public static boolean isFormContentElement(String name) {
        return INPUT.equals(name)
            || SELECT.equals(name)
            || OPTION.equals(name)
            || BUTTON.equals(name)
            || TEXTAREA.equals(name);
    }

    public static boolean isFormElement(String name) {
        return FORM.equals(name);
    }

    public static boolean isHeader(String name) {
        return HEADERS.contains(name);
    }

    public static boolean isHtmlAttribute(String name) {
        return ALL_ATTRIBUTES.contains(name);
    }

    public static boolean isHtmlElement(String name) {
        return ALL_ELEMENTS.contains(name);
    }

    public static boolean isImportantAttribute(String name) {
        return ATTR_TITLE.equals(name)
            || ATTR_SRC.equals(name)
            || ATTR_HREF.equals(name);
    }

    public static boolean isInlineElement(String name) {
        return INLINE_ELEMENTS.contains(name);
    }

    public static boolean isLineBreak(String name) {
        return BR.equals(name);
    }

    public static boolean isList(String name) {
        return DL.equals(name) || UL.equals(name) || OL.equals(name);
    }

    public static boolean isListItem(String name) {
        return LI.equals(name) || DT.equals(name) || DD.equals(name);
    }

    public static boolean isParagraph(String name) {
        return P.equals(name);
    }

    public static boolean isScriptElement(String name) {
        return SCRIPT.equals(name);
    }

    public static boolean isSpanElement(String name) {
        return SPAN.equals(name);
    }

    public static boolean isTableBody(String name) {
        return TBODY.equals(name);
    }

    public static boolean isTableCellElement(String name) {
        return TH.equals(name) || TD.equals(name);
    }

    public static boolean isTableElement(String name) {
        return TABLE.equals(name);
    }

    public static boolean isTableHeaderBodyOrRowElement(String name) {
        return THEAD.equals(name) || TBODY.equals(name) || TR.equals(name);
    }

    public static boolean isTableInnerElement(String name) {
        return isTableHeaderBodyOrRowElement(name) || isTableCellElement(name);
    }

    public static boolean isTableRow(String name) {
        return TR.equals(name);
    }

    public static boolean isTextflowContainer(String name) {
        return isHeader(name)
            || isParagraph(name)
            || isTableCellElement(name)
            || isListItem(name)
            || BLOCKQUOTE.equals(name)
            || PRE.equals(name);
    }

    public static boolean keepSpaces(String name) {
        return PRE.equals(name);
    }

}
