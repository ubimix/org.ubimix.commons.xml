/**
 * 
 */
package org.webreformatter.rdf.test;

import java.util.List;
import java.util.Set;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;
import org.webreformatter.rdf.RdfIdManager;
import org.webreformatter.rdf.RdfModel;
import org.webreformatter.rdf.RdfResource;
import org.webreformatter.rdf.RdfString;
import org.webreformatter.rdf.RdfValueFactory;

/**
 * @author kotelnikov
 */
public class RDFModelTest extends AbstractTestForRdfModel {

    /**
     * @param name
     */
    public RDFModelTest(String name) {
        super(name);
    }

    public void testIdManager() {
        RdfIdManager manager = newValueFactory();
        IId id = manager.newId();
        assertNotNull(id);

        IName rdf = manager
            .getName("http://www.w3.org/1999/02/22-rdf-syntax-ns#Resource");
        assertNotNull(rdf);
        assertEquals(manager._NS_RDF, rdf.getNamespaceURL());
        assertEquals("Resource", rdf.getLocalName());
        assertEquals("rdf", manager.getPrefix(rdf));
        IName test = manager.getName("rdf:Resource");
        assertEquals(rdf, test);

        test = manager.getName("http://www.foo.bar/a/b/c");
        assertNotNull(test);
        IId namespace = test.getNamespaceURL();
        assertNotNull(namespace);
        assertEquals("http://www.foo.bar/a/b/", namespace.toString());
        assertEquals("c", test.getLocalName());
        assertEquals("http://www.foo.bar/a/b/c", test.getUrl());
        assertNull(manager.getPrefix(test));

        // Fails
        try {
            manager.getName("xx:nn");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testModel() throws Exception {
        RdfValueFactory valueFactory = newValueFactory();
        IId modelId = valueFactory.getName("http://www.foo.bar");
        RdfModel model = new RdfModel(modelId);
        IId id = model.getId();
        assertNotNull(id);
        assertEquals(modelId, id);

        RdfResource modelResource = model.getResource(id, false);
        assertNotNull(modelResource);

        RdfResource resource = model.getModelResource();
        assertNotNull(resource);
        assertEquals(id, resource.getId());
        assertEquals(modelResource, resource);

        Set<RdfResource> resources = model.getAllResources();
        assertNotNull(resources);
        assertTrue(resources.contains(resource));
        // id => namespace + localname
        // value => typed string (with language), resource, number (integer,
        // float, double, short, long, ...),
    }

    public void testResource() {
        RdfValueFactory valueFactory = newValueFactory();
        RdfModel model;
        {
            valueFactory.setNamespacePrefix("http://www.foo.bar/a/b/", "foo");
            IId modelId = valueFactory.getName("http://www.foo.bar");
            model = new RdfModel(modelId);
        }
        RdfResource resource = model.getResource(valueFactory.newId(), true);
        IName title = valueFactory.getName("dc:title");
        resource.setValue(title, model.toRdfValue("Hello, there!"));
        List<IValue> values = resource.getValues(title);
        assertNotNull(values);
        assertEquals(new RdfString("Hello, there!"), values.get(0));

        values = resource.getValues(title);
        List<Object> objValues = model.toJavaValues(values);
        assertNotNull(objValues);
        assertEquals("Hello, there!", objValues.get(0));

        IName totoId = valueFactory.getName("http://www.foo.bar/a/b/toto");
        IName seeAlso = valueFactory.getName("http://www.foo.bar/a/b/seeAlso");
        resource.setValue(seeAlso, totoId);
        RdfResource test = resource.getResource(seeAlso, false);
        assertNull(test);
        test = resource.getResource(seeAlso, true);
        assertNotNull(test);
        assertEquals(totoId, test.getId());

        IName age = valueFactory.getName("foo:age");
        resource.setValue(age, valueFactory.toRdfValue(123));
        assertEquals(123, model.toJavaValue(resource.getValue(age)));

        IName tel = valueFactory.getName("foo:tel");
        resource.addValue(tel, model.toRdfValue("123-34-45"));
        resource.addValue(tel, model.toRdfValue("234-45-56"));
        assertEquals("123-34-45", model.toJavaValue(resource.getValue(tel)));

        List<Object> list = model.toJavaValues(resource.getValues(tel));
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("123-34-45", list.get(0));
        assertEquals("234-45-56", list.get(1));

        values = resource.getValues(tel);
        assertEquals(2, values.size());
        assertEquals(new RdfString("123-34-45"), values.get(0));
        assertEquals(new RdfString("234-45-56"), values.get(1));

        IName greeting = valueFactory.getName("foo:greeting");
        resource.addValue(greeting, model.toRdfValue("Hello", "en"));
        resource.addValue(greeting, model.toRdfValue("Bonjour", "fr"));
        resource.addValue(greeting, model.toRdfValue("Привет", "ru"));
        List<RdfString> strValues = resource.getRdfStringValues(greeting, "ru");
        assertNotNull(strValues);
        assertEquals(1, strValues.size());
        assertEquals(model.toRdfValue("Привет", "ru"), strValues.get(0));
        strValues = resource.getRdfStringValues(greeting, "fr");
        assertNotNull(strValues);
        assertEquals(1, strValues.size());
        assertEquals(model.toRdfValue("Bonjour", "fr"), strValues.get(0));
        strValues = resource.getRdfStringValues(greeting, "en");
        assertNotNull(strValues);
        assertEquals(1, strValues.size());
        assertEquals(model.toRdfValue("Hello", "en"), strValues.get(0));

        strValues = resource.getRdfStringValues(greeting);
        assertNotNull(strValues);
        assertEquals(3, strValues.size());
        assertEquals(model.toRdfValue("Hello", "en"), strValues.get(0));
        assertEquals(model.toRdfValue("Bonjour", "fr"), strValues.get(1));
        assertEquals(model.toRdfValue("Привет", "ru"), strValues.get(2));
    }

    public void testResourceReferenes() {
        RdfValueFactory valueFactory = newValueFactory();
        RdfModel model;
        {
            valueFactory.setNamespacePrefix("http://www.foo.bar/a/b/", "foo");
            IId modelId = valueFactory.getName("http://www.foo.bar");
            model = new RdfModel(modelId);
        }
        RdfResource resource = model.getResource(valueFactory.newId(), true);
        IName propertyId = valueFactory.getName("rdf:seeAlso");

        RdfResource test = resource.getResource(propertyId, false);
        assertNull(test);

        RdfResource target = model.getResource(valueFactory.newId(), true);
        resource.setValue(propertyId, target.getId());

        test = resource.getResource(propertyId, false);
        assertNotNull(test);
        assertEquals(target, test);

    }
}
