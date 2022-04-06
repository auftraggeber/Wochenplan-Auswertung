package me.langner.jonas.wpapp.xml;

import me.langner.jonas.wpapp.objects.ui.frames.ErrorUI;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * XML-Dateien werden hier interpretiert.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Parser {

    private File file;
    private Document document;

    /**
     * Interpretiert eine XML-Datei.
     * @param file Die Datei.
     */
    public Parser(File file) {
        this.file = file;

        readDocument();
    }

    /**
     * Interpretiert eine XML-Datei
     * @param path Der Pfad zur Datei.
     */
    public Parser(String path) {
        this(new File(path));
    }

    /**
     * Liest das Dokument aus.
     */
    private void readDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder =  documentBuilderFactory.newDocumentBuilder();

            this.document = documentBuilder.parse(this.file);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Document getDocument() {
        return document;
    }

    public File getFile() {
        return file;
    }

}
