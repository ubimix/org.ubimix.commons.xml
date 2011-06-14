package org.webreformatter.atom;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.webreformatter.atom.AtomCategory;
import org.webreformatter.atom.AtomEntry;
import org.webreformatter.atom.AtomFeed;
import org.webreformatter.atom.AtomItem;
import org.webreformatter.atom.AtomPerson;
import org.webreformatter.ns.IId;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentFactory;
import org.webreformatter.xml.basic.XmlDocumentFactory;
import org.webreformatter.xml.sax.SaxDocumentParser;

public class AtomFeedTest extends TestCase {

    protected IId fAtomNamespace;

    protected IXmlDocumentFactory fDocumentFactory;

    protected NamespaceManager fNamespaceManager;

    public AtomFeedTest(String name) {
        super(name);
    }

    private IXmlDocument parseDocument(Reader reader) {
        IXmlDocument doc = fDocumentFactory.newDocument();
        SaxDocumentParser parser = new SaxDocumentParser();
        parser.parse(doc, fAtomNamespace, reader);
        return doc;
    }

    private IXmlDocument parseDocument(String xml) {
        Reader reader = new StringReader(xml);
        return parseDocument(reader);
    }

    @Override
    protected void setUp() throws Exception {
        fNamespaceManager = new NamespaceManager();
        fDocumentFactory = new XmlDocumentFactory(fNamespaceManager);
        fAtomNamespace = fNamespaceManager.getId("http://www.w3.org/2005/Atom");
    }

    public void test() throws Exception {
        String xml = ""
            + "<?xml version='1.0' encoding='utf-8'?>\n"
            + "<feed xmlns='http://www.w3.org/2005/Atom'>\n"
            + " \n"
            + "    <title>Example Feed</title>\n"
            + "    <subtitle>A subtitle.</subtitle>\n"
            + "    <link href='http://example.org/feed/' rel='self' />\n"
            + "    <link href='http://example.org/' />\n"
            + "    <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>\n"
            + "    <updated>2003-12-13T18:30:02Z</updated>\n"
            + "    <author>\n"
            + "        <name>John Doe</name>\n"
            + "        <email>johndoe@example.com</email>\n"
            + "    </author>\n"
            + " \n"
            + "    <entry>\n"
            + "        <title>Atom-Powered Robots Run Amok</title>\n"
            + "        <link href='http://example.org/2003/12/13/atom03' />\n"
            + "        <link rel='alternate' type='text/html' href='http://example.org/2003/12/13/atom03.html'/>\n"
            + "        <link rel='edit' href='http://example.org/2003/12/13/atom03/edit'/>\n"
            + "        <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>\n"
            + "        <updated>2003-12-13T18:30:02Z</updated>\n"
            + "        <summary>Some text.</summary>\n"
            + "        <category term='robots'/>\n"
            + "        <category term='test' label='Test Label' scheme='http://example.org/ns/tags/'/>\n"
            + "        <content type='html'>Robot-generated content.</content>\n"
            + "    </entry>\n"
            + " \n"
            + "</feed>";
        IXmlDocument doc = parseDocument(xml);

        System.out.println(doc);

        AtomFeed feed = new AtomFeed(doc);
        assertEquals("Example Feed", feed.getTitle());
        assertEquals("A subtitle.", feed.getSubtitle());
        assertEquals("http://example.org/feed/", feed.getSelfLink());
        assertEquals("http://example.org/", feed.getLink());
        assertEquals("urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6", feed
            .getId());
        assertEquals(AtomItem.parseDate("2003-12-13T18:30:02Z"), feed
            .getUpdated());

        // Author
        List<AtomPerson> authors = feed.getAuthors();
        assertNotNull(authors);
        assertEquals(1, authors.size());
        AtomPerson author = authors.get(0);
        assertNotNull(author);
        assertEquals("John Doe", author.getName());
        assertEquals("johndoe@example.com", author.getEmail());

        // Entries
        List<AtomEntry> entries = feed.getEntries();
        assertNotNull(entries);
        assertEquals(1, entries.size());
        AtomEntry entry = entries.get(0);
        assertNotNull(entry);
        assertEquals("Atom-Powered Robots Run Amok", entry.getTitle());
        assertEquals("http://example.org/2003/12/13/atom03", entry.getLink());
        assertEquals("http://example.org/2003/12/13/atom03.html", entry
            .getLink("alternate"));
        assertEquals("text/html", entry.getLinkType("alternate"));

        assertEquals("http://example.org/2003/12/13/atom03/edit", entry
            .getEditLink());
        assertEquals("urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a", entry
            .getId());
        assertEquals(AtomItem.parseDate("2003-12-13T18:30:02Z"), entry
            .getUpdated());
        assertEquals("Some text.", entry.getSummary());
        List<AtomCategory> categories = entry.getCategories();
        assertNotNull(categories);
        assertEquals(2, categories.size());
        AtomCategory category = categories.get(0);
        assertNotNull(category);
        assertEquals("robots", category.getTerm());
        assertNull(category.getScheme());
        assertNull(category.getLabel());

        category = categories.get(1);
        assertNotNull(category);
        assertEquals("test", category.getTerm());
        assertEquals("Test Label", category.getLabel());
        assertEquals("http://example.org/ns/tags/", category.getScheme());
    }

    public void testDates() {
        Date date = AtomItem.parseDate("2003-12-13T18:30:02Z");
        assertNotNull(date);
        assertEquals("2003-12-13T18:30:02Z", AtomItem.formatDate(date));
    }

    public void testFile() throws Exception {
        File dir = new File("./tmp");
        File[] list = dir.listFiles();
        int len = list != null ? list.length : 0;
        for (int i = 0; i < len; i++) {
            File file = list[i];
            if (!file.getName().endsWith(".xml"))
                continue;
            System.out
                .println("***************************************************");
            System.out.println(file);
            FileReader reader = new FileReader(file);
            long start = System.currentTimeMillis();
            IXmlDocument doc = parseDocument(reader);
            long stop = System.currentTimeMillis();
            System.out.println("Parse timeout: " + (stop - start) + "ms");
            AtomFeed feed = new AtomFeed(doc);
            List<AtomEntry> entries = feed.getEntries();
            for (AtomEntry entry : entries) {
                String id = entry.getId();
                String title = entry.getTitle();
                List<AtomCategory> categories = entry.getCategories();
                System.out
                    .println("---------------------------------------------------");
                System.out.println("ID:" + id);
                System.out.println("\tTitle: " + title);
                System.out.println("\tPublished: " + entry.getPublished());
                System.out.println("\tUpdated: " + entry.getUpdated());

                List<AtomPerson> authors = entry.getAuthors();
                if (authors != null) {
                    for (AtomPerson author : authors) {
                        System.out.println("\tAuthor: "
                            + author.getName()
                            + " ("
                            + author.getEmail()
                            + ")");
                    }
                }
                for (AtomCategory category : categories) {
                    System.out.println("\tTag: " + category.getTerm());
                }
            }
        }
    }
}
