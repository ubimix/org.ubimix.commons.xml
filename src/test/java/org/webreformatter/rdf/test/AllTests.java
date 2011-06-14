package org.webreformatter.rdf.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.webreformatter.atom.AtomFeedTest;
import org.webreformatter.path.xml.NodeSelectorTest;
import org.webreformatter.rdf.test.ns.NamespaceManagerTest;
import org.webreformatter.rdf.xml.NativeXmlTest;
import org.webreformatter.rdf.xml.XmlTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.webreformatter.rdf.test");
        // $JUnit-BEGIN$
        suite.addTestSuite(RDFModelTest.class);
        suite.addTestSuite(SHA1Test.class);
        suite.addTestSuite(NamespaceManagerTest.class);
        suite.addTestSuite(XmlTest.class);
        suite.addTestSuite(NativeXmlTest.class);
        suite.addTestSuite(NodeSelectorTest.class);
        suite.addTestSuite(AtomFeedTest.class);
        // $JUnit-END$
        return suite;
    }

}
