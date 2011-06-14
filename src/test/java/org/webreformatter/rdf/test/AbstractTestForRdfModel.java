/**
 * 
 */
package org.webreformatter.rdf.test;

import junit.framework.TestCase;

import org.webreformatter.rdf.RdfValueFactory;

/**
 * @author kotelnikov
 */
public class AbstractTestForRdfModel extends TestCase {

    public AbstractTestForRdfModel(String name) {
        super(name);
    }

    protected RdfValueFactory newValueFactory() {
        RdfValueFactory factory = new RdfValueFactory();
        factory.setNamespacePrefix(
            "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            "rdf");
        factory.setNamespacePrefix(
            "http://www.w3.org/2000/01/rdf-schema#",
            "rdfs");
        factory.setNamespacePrefix("http://purl.org/dc/elements/1.1/", "dc");
        factory.setNamespacePrefix("http://www.w3.org/2002/07/owl#", "owl");
        return factory;
    }

}
