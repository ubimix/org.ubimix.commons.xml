/**
 * 
 */
package org.webreformatter.commons.xml.html;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.webreformatter.commons.xml.XmlException;
import org.webreformatter.commons.xml.XmlWrapper;
import org.webreformatter.commons.xml.XmlWrapper.XmlContext;
import org.webreformatter.commons.xml.html.HtmlBurner;

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
