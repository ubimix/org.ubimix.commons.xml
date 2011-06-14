/**
 * 
 */
package org.webreformatter.rdf.test.ns;

import junit.framework.TestCase;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public class NamespaceManagerTest extends TestCase {

    /**
     * @param name
     */
    public NamespaceManagerTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        NamespaceManager ns = new NamespaceManager();

        ns.setNamespacePrefix("http://www.foo.bar#", "p");
        IId first = ns.getName("p:x");
        IName second = ns.getName("http://www.foo.bar#", "x");

        assertEquals(first, second);
        assertEquals("p:x", ns.getShortForm(first));
        assertEquals("p:x", ns.getShortForm(second));
        assertEquals("http://www.foo.bar#x", first.getUrl());
        assertEquals("http://www.foo.bar#x", second.getUrl());

        // Change the prefix for the same namespace
        ns.setNamespacePrefix("http://www.foo.bar#", "a");
        assertEquals(first, second);
        assertEquals("a:x", ns.getShortForm(first));
        assertEquals("a:x", ns.getShortForm(second));
        assertEquals("http://www.foo.bar#x", first.getUrl());
        assertEquals("http://www.foo.bar#x", second.getUrl());

        // Set the empty prefix for this namespace
        ns.setNamespacePrefix("http://www.foo.bar#", "");
        assertEquals(first, second);
        assertEquals("x", ns.getShortForm(first));
        assertEquals("x", ns.getShortForm(second));
        assertEquals("http://www.foo.bar#x", first.getUrl());
        assertEquals("http://www.foo.bar#x", second.getUrl());

        IId b = ns.getName("http://www.foo.bar#", "x");
        assertEquals(first, b);

        IId a = ns.getName("x");
        assertEquals(first, a);

    }

    public void testNamespaces() {
        NamespaceManager manager = new NamespaceManager();
        IId ns = manager.getId("http://www.foo.bar#");
        manager.setNamespacePrefix(ns, "p");
        assertEquals("p", manager.getNamespacePrefix(
            "http://www.foo.bar#",
            false));
        assertEquals(ns, manager.getNamespaceByPrefix("p"));

        // // The namespace don't have a correct end symbol
        // try {
        // manager.setNamespacePrefix("http://www.foo.bar", "p");
        // fail();
        // } catch (IllegalArgumentException e) {
        // }
        // try {
        // manager.getNamespacePrefix("http://www.foo.bar", true);
        // fail();
        // } catch (IllegalArgumentException e) {
        // }
    }
}
