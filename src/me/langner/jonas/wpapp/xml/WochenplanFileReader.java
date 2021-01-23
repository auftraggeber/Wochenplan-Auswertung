package me.langner.jonas.wpapp.xml;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Liest die Waochenplan-Datei nach der Syntax und erstellt Java-Objekte.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class WochenplanFileReader {

    private Parser parser;

    /**
     * Sucht automatisch nach der Datei.
     */
    public WochenplanFileReader() {
        this(new FileChooser().getFile());
    }

    /**
     * Interpretiert die WPAPP-Datei.
     * @param file Die zu interpretierende Datei.
     */
    public WochenplanFileReader(File file) {
        if (file != null) {
            this.parser = new Parser(file);

            start();
        }

    }

    /**
     * Interpretiert die WPAPP-Datei.
     * @param path Die zu interpretierende Datei.
     */
    public WochenplanFileReader(String path) {
        this(new File(path));
    }

    /**
     * Startet die Interpretation.
     */
    private void start() {

        /* überprüfen, ob Datei gefunden */
        if (parser != null && parser.getFile() != null) {
            // Datei gefunden
            Document xml = parser.getDocument();

            /* ab hier interpretieren */
            readPeriod(xml);
            readMachines(xml);
            readEntries(xml);
        }
    }

    /**
     * Liest die Zeitspanne aus.
     * @param xml Das XML-Document.
     */
    private void readPeriod(Document xml) {
        NodeList periodList = xml.getElementsByTagName("period");

        /* überprüfen, ob auch nur eine Zeitspanne gegeben ist */
        if (periodList.getLength() == 1) {
            // nur eine -> Start und ende auslesen
            NodeList dates = periodList.item(0).getChildNodes();

            Node start = null;
            Node end = null;

            /* iterieren und Start/ende bekommen */
            for (int i = 0; i < dates.getLength(); i++) {

                /* ermitteln, ob Start */
                if (dates.item(i).getNodeName().equalsIgnoreCase("start")) {
                    start = dates.item(i);
                }
                /* ermitteln, ob Ende */
                else if (dates.item(i).getNodeName().equalsIgnoreCase("end")) {
                    end = dates.item(i);
                }
            }

            /* zu Datum umwandeln */
            if (start != null && end != null) {

                try {

                    Date startDate = WPAPP.SQL_FORMAT.parse(start.getTextContent());
                    Date endDate = WPAPP.SQL_FORMAT.parse(end.getTextContent());

                    /* überprüfen, ob nicht schon ein anderes Datum */
                    if (WPAPP.getWochenplan().getPeriod() != null && WPAPP.getWochenplan().getPeriod().getStart().before(startDate))
                        startDate = WPAPP.getWochenplan().getPeriod().getStart();

                    /* gleiche für Ende */
                    if (WPAPP.getWochenplan().getPeriod() != null && WPAPP.getWochenplan().getPeriod().getEnd().after(endDate))
                        endDate = WPAPP.getWochenplan().getPeriod().getEnd();

                    /* ZeitPeriode speichern */
                    Period period = new Period(startDate, endDate);
                    WPAPP.getWochenplan().setPeriod(period);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    /**
     * Liest die Maschinen aus.
     * @param xml Das XML-Document.
     */
    private void readMachines(Document xml) {

        /* Maschinenliste finden */
        NodeList machineLists = xml.getElementsByTagName("machinelist");

        /* ermitteln, ob es eine Maschinenliste gibt */
        if (machineLists.getLength() > 0 && machineLists.item(0).getChildNodes().getLength() > 0) {
            // gibt Maschinenliste - erste ist die mit Stammdaten -> lesen und Objekte erstellen

            for (int i = 0; i < machineLists.item(0).getChildNodes().getLength(); i++) {
                /* Maschine auslesen */
                Node machineNode = machineLists.item(0).getChildNodes().item(i);

                /* wenn es wirklich eine Maschine ist -> dann Daten lesen */
                if (machineNode.getNodeName().equalsIgnoreCase("machine")) {
                    // wirklich Maschine
                    Machine machine = createMachine(machineNode);

                    /* Maschine und dessen Werkzeuge hinzufügen */
                    WPAPP.getWochenplan().addMachine(machine);
                }

            }
        }
    }

    /**
     * Liest alle Einträge aus und speichert sie in Objekten.
     * @param xml Das XML-Dokument.
     */
    private void readEntries(Document xml) {

        /* Eintrags-liste finden */
        NodeList entryList = xml.getElementsByTagName("entrylist");

        /* ermitteln, ob es Eintragsliste gibt */
        if (entryList.getLength() > 0 && entryList.item(0).getChildNodes().getLength() > 0) {
            // Liste existiert -> alle Einträge auswerten

            for (int i = 0; i < entryList.item(0).getChildNodes().getLength(); i++) {
                Node child = entryList.item(0).getChildNodes().item(i);

                /* ermitteln, ob eintrag */
                if (child.getNodeName().equalsIgnoreCase("entry")) {
                    // eintrag -> analysieren

                    Machine machine = null;
                    Tool tool = null;
                    Date date = null;
                    int shift = -1;
                    float value = -1F;
                    boolean preparation = false;


                    for (int j = 0; j < child.getChildNodes().getLength(); j++) {
                        Node infoNode = child.getChildNodes().item(j);

                        /* ermitteln, welche Daten angegeben wurden */
                        switch (infoNode.getNodeName().toLowerCase()) {
                            case "machineid":
                                // Maschinen-id wurde angegeben
                                try {
                                    machine = WPAPP.getWochenplan().getMachineById(Integer.parseInt(infoNode.getTextContent()));
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "toolid":
                                // Werkzeug-id wurde angegeben
                                try {
                                    tool = WPAPP.getWochenplan().getToolById(Integer.parseInt(infoNode.getTextContent()));
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "date":
                                // Datum
                                try {
                                    date = WPAPP.SQL_FORMAT.parse(infoNode.getTextContent());
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "shift":
                                // Schicht
                                try {
                                    shift = Integer.parseInt(infoNode.getTextContent());
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "value":
                                // Wert
                                try {
                                    value = Float.parseFloat(infoNode.getTextContent());
                                }
                                catch (Exception ex) {

                                    try {
                                        if (infoNode.getTextContent().endsWith(".")) {
                                            // fehlerhafter punkt am ende -> entfernen
                                            char chars[] = infoNode.getTextContent().toCharArray();
                                            String stringValue = "";

                                            // String neu bauen
                                            for (int c = 0; c < chars.length; c++) {

                                                if (c+1 < chars.length) {
                                                    stringValue += c;
                                                }
                                            }

                                            /* neu probieren */
                                            value = Float.parseFloat(stringValue);
                                        }
                                    }
                                    catch (Exception ex2) {
                                        ex2.printStackTrace();
                                    }
                                }
                                break;
                            case "preparation":
                                // Rüstung
                                try {
                                    preparation = Boolean.parseBoolean(infoNode.getTextContent());
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            default:
                                System.err.println("Fehler: Kann Node nicht interpretieren: " + infoNode.getNodeName());
                                break;

                        }
                    }

                    /* überprüfen, ob alle notwendigen Elemente angegeben sind */
                    if (machine != null && tool != null && date != null && shift >= 0 && value >= 0) {
                        // alle angegeben -> erstellen
                        new StaffEntry(machine, tool, date, shift, value, preparation);
                    }
                }
            }
        }
    }

    /**
     * Liest Daten und erstellt eine Maschine.
     * @param machineNode Die XML-Node mit den Daten der Maschine.
     * @return Die erstellte Maschine.
     */
    private Machine createMachine(Node machineNode) {

        int machineID = -1;
        String machineName = null;
        List<Tool> toolList = new ArrayList<>();

        /* jedes Kind ermitteln */
        if (machineNode.getChildNodes().getLength() > 0) {
            // es gibt Kinder -> nach art überprüfen

            for (int j = 0; j < machineNode.getChildNodes().getLength(); j++) {
                Node child = machineNode.getChildNodes().item(j);

                /* Art des Tags ermitteln */
                switch (child.getNodeName().toLowerCase()) {
                    case "id":
                        // es handelt sich um die ID der Maschine -> speichern
                        try {
                            machineID = Integer.parseInt(child.getTextContent());
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "name":
                        // es handelt sich um den Namen der Maschine
                        machineName = child.getTextContent();
                        break;
                    case "tool":
                        // es handelt sich um ein verknüpftes werkzeug -> erstellen
                        Tool tool = createTool(child);

                        /* zu liste hinzufügen, wenn nicht null und nicht schon teil */
                        if (tool != null && !toolList.contains(tool))
                            toolList.add(tool);

                        break;
                    default:
                        System.err.println("Fehler unbekannter Tag (keine Interpretation: " + child.getNodeName() + ")!");
                        break;

                }
            }
        }

        /* überprüfen, ob Machine erstellt werden kann */
        if (machineID > 0 && machineName != null) {
            // kann erstellt werden -> erstellen & ausgeben
            Machine machine = new Machine(machineID, machineName);

            /* noch alle Tools hinzufügen */
            toolList.forEach(machine::addTool);

            return machine;
        }

        return null;
    }

    /**
     * Liest Informationen eines Tool und erstellt ein Objekt.
     * @param toolNode Die XML-Node mit den Infos des Werkzeugs.
     * @return Das erstellte Werkzeug.
     */
    private Tool createTool(Node toolNode) {

        int toolID = -1;
        String toolName = null;
        float preparationTime = -1F;

        /* jedes Kind ermitteln */
        if (toolNode.getChildNodes().getLength() > 0) {
            // es gibt Kinder -> nach art überprüfen

            for (int j = 0; j < toolNode.getChildNodes().getLength(); j++) {
                Node child = toolNode.getChildNodes().item(j);

                /* Art des Tags ermitteln */
                switch (child.getNodeName().toLowerCase()) {
                    case "id":
                        // es handelt sich um die ID des Werkzeugs -> speichern
                        try {
                            toolID = Integer.parseInt(child.getTextContent());
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "name":
                        // es handelt sich um den Namen des Werkzeugs
                        toolName = child.getTextContent();
                        break;
                    case "preparation-time":
                        // es handelt sich um die Rüstzeit -> speichern
                        try {
                            preparationTime = Float.parseFloat(child.getTextContent());
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        System.err.println("Fehler unbekannter Tag (keine Interpretation: " + child.getNodeName() + ")!");
                        break;

                }
            }
        }

        /* überprüfen, ob Machine erstellt werden kann */
        if (toolID > 0 && toolName != null && preparationTime > 0) {
            // kann erstellt werden -> erstellen & ausgeben
            return new Tool(toolID, toolName, preparationTime);
        }

        return null;
    }

}
