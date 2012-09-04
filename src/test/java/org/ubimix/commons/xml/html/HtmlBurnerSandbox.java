/**
 * 
 */
package org.ubimix.commons.xml.html;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.ubimix.commons.xml.XmlException;
import org.ubimix.commons.xml.XmlWrapper;
import org.ubimix.commons.xml.XmlWrapper.XmlContext;
import org.ubimix.commons.xml.html.HtmlBurner;

/**
 * @author kotelnikov
 */
public class HtmlBurnerSandbox {

    public static void main(String[] args) throws IOException, XmlException {
        FileReader reader = new FileReader("./tmp/test.xml");
        XmlContext context = XmlContext.build();
        XmlWrapper doc = context.readXML(reader);
        HtmlBurner burner = new HtmlBurner();
        burner.burnHtml(doc.getRootElement());
        FileWriter writer = new FileWriter("./tmp/test-out.xml");
        writer.write(doc.toString());
        writer.close();
    }

}
