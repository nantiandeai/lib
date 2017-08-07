package com.lianyitech.modules.common;

import com.lianyitech.modules.catalog.entity.BookDirectory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookDirectoryXmlParser extends DefaultHandler {
    private static Logger logger = LoggerFactory.getLogger(BookDirectoryXmlParser.class);
    private BookDirectory book = null;
    private String attname = null;
    private final StringBuilder buff = new StringBuilder();
    private final List<String> tags = new ArrayList<>();
    /**
     * @return the book
     */
    BookDirectory getBook() {
        return book;
    }
    BookDirectoryXmlParser(InputStream is) {
        try {
            SAXParserFactory spfactory = SAXParserFactory.newInstance();
            spfactory.setValidating(false);
            SAXParser saxParser = spfactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(new InputSource(is));
        } catch (Exception e) {
            logger.error("操作失败",e);
            System.exit(1);
        }
    }
    public void startElement(String uri, String localName, String name,
                             Attributes atts) throws SAXException {
        if (name.equalsIgnoreCase("entry")) {
            book = new BookDirectory();
        } else if (name.equalsIgnoreCase("db:attribute")) {
            attname = atts.getValue("name");
        } else if (name.equalsIgnoreCase("db:tag")) {
            tags.add(atts.getValue("name"));
        } else if (name.equalsIgnoreCase("link")) {
            if ("image".equalsIgnoreCase(atts.getValue("rel"))) {
                //book.setImagePath(atts.getValue("href"));
            }
        }
        buff.setLength(0);
    }

    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if ("entry".equalsIgnoreCase(name)) {
            StringBuilder str = new StringBuilder();
            for (String t : tags) {
                str.append(t).append("/");
            }
            //book.setTags(str.toString());
        } else if (name.equalsIgnoreCase("db:attribute")) {
            String value = buff.toString().trim();
            if ("isbn10".equalsIgnoreCase(attname)) {
                book.setIsbn(value.replace("-",""));
            } else if ("isbn13".equalsIgnoreCase(attname)) {
                book.setIsbn(value.replace("","-"));
            } else if ("title".equalsIgnoreCase(attname)) {
                book.setTitle(value);
            } else if ("pages".equalsIgnoreCase(attname)) {
                book.setPageNo(value);
            } else if ("author".equalsIgnoreCase(attname)) {
                book.setAuthor(value);
            } else if ("price".equalsIgnoreCase(attname)) {
                try {
                    book.setPrice(Double.parseDouble(value.replace("元", "")));
                }catch (Exception e){
                    System.out.println("价格转换异常");
                }
            } else if ("publisher".equalsIgnoreCase(attname)) {
                book.setPublishingName(value);
            } else if ("binding".equalsIgnoreCase(attname)) {
                book.setBindingForm(value);
            } else if ("pubdate".equalsIgnoreCase(attname)) {
                book.setPublishingTime(value);
            }
        } else if ("summary".equalsIgnoreCase(name)) {
           // book.setSummary(buff.toString());
        }
        buff.setLength(0);
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        buff.append(ch, start, length);
    }
    public static BookDirectory parse_book(String isbn) throws IOException {
        BookDirectory book = null;
        String url = "http://api.douban.com/book/subject/isbn/"+isbn;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            if(entity!=null) {
                InputStream is = entity.getContent();
                book = new BookDirectoryXmlParser(is).getBook();
            }
        }
        return book;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(parse_book("9787550293151").getTitle());
    }
}
