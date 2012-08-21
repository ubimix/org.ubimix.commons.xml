package org.webreformatter.commons.xml.html;

/**
 * @author kotelnikov
 */
public class TagStat {

    private int fBlockElements;

    private int fInlineElements;

    private int fLineBreakElements;

    private int fOtherXmlNodes;

    private int fSpaceBlocks;

    private int fTextBlocks;

    private int fXmlElements;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TagStat)) {
            return false;
        }
        TagStat o = (TagStat) obj;
        int[] array1 = toArray();
        int[] array2 = o.toArray();
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public int getAllElements() {
        return fBlockElements + fInlineElements + fXmlElements;
    }

    public int getAllMeaningfulNodes() {
        return getAllElements() + fOtherXmlNodes + fTextBlocks;
    }

    public int getAllTextBlocks() {
        return fTextBlocks + fSpaceBlocks;
    }

    protected int getBlockElements() {
        return fBlockElements;
    }

    protected int getInlineElements() {
        return fInlineElements;
    }

    protected int getLineBreakElements() {
        return fLineBreakElements;
    }

    protected int getOtherXmlNodes() {
        return fOtherXmlNodes;
    }

    protected int getSpaceBlocks() {
        return fSpaceBlocks;
    }

    protected int getTextBlocks() {
        return fTextBlocks;
    }

    protected int getXmlElements() {
        return fXmlElements;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        int[] array = toArray();
        for (int value : array) {
            hashCode = 31 * hashCode + value;
        }
        return hashCode;
    }

    public void incBlockElements() {
        fBlockElements++;
    }

    public void incInlineElements() {
        fInlineElements++;
    }

    public void incLineBreakElements() {
        fLineBreakElements++;
    }

    public void incOtherXmlNodes() {
        fOtherXmlNodes++;
    }

    public void incSpaceBlocks() {
        fSpaceBlocks++;
    }

    public void incTextBlocks() {
        fTextBlocks++;
    }

    public void incXmlElements() {
        fXmlElements++;
    }

    private int[] toArray() {
        int[] array = {
            fBlockElements,
            fInlineElements,
            fLineBreakElements,
            fOtherXmlNodes,
            fSpaceBlocks,
            fTextBlocks,
            fXmlElements };
        return array;
    }

    @Override
    public String toString() {
        return "{\n"
            + "  \"blockElements\":"
            + fBlockElements
            + ",\n"
            + "  \"inlineElements\":"
            + fInlineElements
            + ",\n"
            + "  \"lineBreakElements\":"
            + fLineBreakElements
            + ",\n"
            + "  \"otherXmlNodes\":"
            + fOtherXmlNodes
            + ",\n"
            + "  \"spaceBlocks\":"
            + fSpaceBlocks
            + ",\n"
            + "  \"textBlocks\":"
            + fTextBlocks
            + ",\n"
            + "  \"xmlElements\":"
            + fXmlElements
            + "\n"
            + "}";
    }


}