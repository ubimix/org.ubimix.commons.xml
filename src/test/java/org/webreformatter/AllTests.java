package org.webreformatter;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.webreformatter.atom.AtomFeedTest;
import org.webreformatter.path.xml.NodeSelectorTest;
import org.webreformatter.path.xml.RdfSelectorTest;
import org.webreformatter.rdf.parser.NTripleParserTest;
import org.webreformatter.rdf.parser.NTripleParserTest1;
import org.webreformatter.rdf.test.RDFModelTest;
import org.webreformatter.rdf.test.SHA1Test;
import org.webreformatter.rdf.test.ns.NamespaceManagerTest;
import org.webreformatter.rdf.xml.NativeXmlTest;
import org.webreformatter.rdf.xml.XmlTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(AtomFeedTest.class);
        suite.addTestSuite(NodeSelectorTest.class);
        suite.addTestSuite(RdfSelectorTest.class);
        suite.addTestSuite(NTripleParserTest.class);
        suite.addTestSuite(NTripleParserTest1.class);
        suite.addTestSuite(RDFModelTest.class);
        suite.addTestSuite(SHA1Test.class);
        suite.addTestSuite(NamespaceManagerTest.class);
        suite.addTestSuite(NativeXmlTest.class);
        suite.addTestSuite(XmlTest.class);
        // $JUnit-END$
        return suite;
    }

}
