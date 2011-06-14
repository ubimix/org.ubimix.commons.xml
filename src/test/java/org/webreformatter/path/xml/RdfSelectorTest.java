/**
 * 
 */
package org.webreformatter.path.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;
import org.webreformatter.path.IPathNodeCollector;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.path.rdf.RdfNodeProvider;
import org.webreformatter.path.rdf.RdfSelectorBuilder;
import org.webreformatter.path.rdf.RdfStatement;
import org.webreformatter.rdf.RdfModel;
import org.webreformatter.rdf.RdfResource;
import org.webreformatter.rdf.RdfValueFactory;
import org.webreformatter.rdf.parser.NTriplesSerializer;
import org.webreformatter.rdf.parser.RdfModelVisitor;

/**
 * @author kotelnikov
 */
public class RdfSelectorTest extends TestCase {

    protected RdfSelectorBuilder fBuilder;

    private RdfModel fModel;

    protected RdfValueFactory fNamespaceManager;

    /**
     * @param name
     */
    public RdfSelectorTest(String name) {
        super(name);
    }

    protected IName getName(String name) {
        return fNamespaceManager.getName(name);
    }

    protected RdfResource getResource(String url) {
        IName uri = fNamespaceManager.getName(url);
        RdfResource resource = fModel.getResource(uri, true);
        return resource;
    }

    @Override
    protected void setUp() throws Exception {
        fNamespaceManager = new RdfValueFactory();
        fNamespaceManager.setNamespacePrefix("http://www.foo.bar/ns#", "test");
        fBuilder = new RdfSelectorBuilder(fNamespaceManager);
        IId id = fNamespaceManager.newId();
        fModel = new RdfModel(id);
    }

    protected void setValue(RdfResource resource, String name, Object value) {
        IName propertyUrl = getName(name);
        IValue val = fNamespaceManager.toRdfValue(value);
        resource.setValue(propertyUrl, val);
    }

    protected void setValue(String resourceUrl, String name, Object value) {
        RdfResource resource = getResource(resourceUrl);
        setValue(resource, name, value);
    }

    public void test() {
        RdfResource user1 = getResource("test:user1");
        RdfResource address = getResource("test:addr1");
        setValue(user1, "test:firstName", "John");
        setValue(user1, "test:lastName", "Smith");
        setValue(user1, "test:age", 38);
        setValue(user1, "test:address", address);

        RdfResource user2 = getResource("test:user2");
        setValue(user2, "test:firstName", "Bill");
        setValue(user2, "test:lastName", "Simpson");

        setValue(user2, "test:knows", user1);

        setValue(address, "test:city", "Paris");
        setValue(address, "test:building", 123);
        setValue(address, "test:street", "Rue Rivoli");

        RdfModelVisitor visitor = new RdfModelVisitor();
        NTriplesSerializer serializer = new NTriplesSerializer();
        visitor.visit(fModel, serializer);
        System.out.println(serializer);

        RdfSelectorBuilder b = new RdfSelectorBuilder();
        IName city = fNamespaceManager.getName("test:city");
        b.add(city, "Paris");

        RdfNodeProvider provider = new RdfNodeProvider(fModel);
        PathProcessor selector = b.buildPath(provider);
        final List<RdfStatement> result = new ArrayList<RdfStatement>();
        IPathNodeCollector collector = new IPathNodeCollector() {
            public boolean setResult(Object node) {
                result.add((RdfStatement) node);
                return true;
            }
        };

        for (Iterator<?> iterator = provider.getChildren(address); iterator
            .hasNext();) {
            Object node = iterator.next();
            selector.select(node, collector);
        }
        for (RdfStatement s : result) {
            System.out.println(s);
        }
    }
}
