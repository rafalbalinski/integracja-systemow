package com.example.system.integration.reader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;


public class FileReader {
    String file;
    BufferedReader reader = null;
    String[] headers;
    String[][] rows = new String[24][15];

    public FileReader(String[] headers, String file) {
        this.headers = headers;
        this.file = file;
    }

    public String[][] readTxtFile() {
        String line;
        int currentLine = 0;
        rows = new String[24][15];

        try {
            reader = new BufferedReader(new java.io.FileReader(file + ".txt"));
            while ((line = reader.readLine()) != null) {
                rows[currentLine] = line.split(";");
                currentLine++;
            }
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return rows;
    }

    public String[][] readXMLFile() {
        try {
            rows = new String[2][15];
            File inputFile = new File(file + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("laptop");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                Element screenElement = (Element) element.getElementsByTagName("screen").item(0);
                Element processorElement = (Element) element.getElementsByTagName("processor").item(0);
                Element discElement = (Element) element.getElementsByTagName("disc").item(0);
                Element graphicCardElement = (Element) element.getElementsByTagName("graphic_card").item(0);

                rows[i][0] = element.getElementsByTagName("manufacturer").item(0).getTextContent();
                rows[i][1] = element.getElementsByTagName("size").item(0).getTextContent();
                rows[i][2] = element.getElementsByTagName("resolution").item(0).getTextContent();
                rows[i][3] = screenElement.getElementsByTagName("type").item(0).getTextContent();
                rows[i][4] = screenElement.getAttribute("touch");
                rows[i][5] = processorElement.getElementsByTagName("name").item(0).getTextContent();
                rows[i][6] = processorElement.getElementsByTagName("physical_cores").item(0).getTextContent();
                rows[i][7] = processorElement.getElementsByTagName("clock_speed").item(0).getTextContent();
                rows[i][8] = element.getElementsByTagName("ram").item(0).getTextContent();
                rows[i][9] = discElement.getElementsByTagName("storage").item(0).getTextContent();
                rows[i][10] = discElement.getAttribute("type");
                rows[i][11] = graphicCardElement.getElementsByTagName("name").item(0).getTextContent();
                rows[i][12] = graphicCardElement.getElementsByTagName("memory").item(0).getTextContent();
                rows[i][13] = element.getElementsByTagName("os").item(0).getTextContent();
                rows[i][14] = element.getElementsByTagName("disc_reader").item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public void exportDataToTxtFile(DefaultTableModel tableModel) {
        try (PrintWriter writer = new PrintWriter(file + "(exported).txt")) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    writer.print(tableModel.getValueAt(row, col));
                    writer.print(";");
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportDataToXmlFile(DefaultTableModel tableModel) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("laptops");
            rootElement.setAttribute("moddate", "2023-03-15 T 00:00");
            doc.appendChild(rootElement);

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Element laptopElement = doc.createElement("laptop");
                laptopElement.setAttribute("id", Integer.toString(row + 1));
                rootElement.appendChild(laptopElement);

                Element manufacturerElement = doc.createElement("manufacturer");
                manufacturerElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 0), "")));
                laptopElement.appendChild(manufacturerElement);

                Element screenElement = doc.createElement("screen");
                screenElement.setAttribute("touch", Objects.toString(tableModel.getValueAt(row, 4), ""));
                laptopElement.appendChild(screenElement);

                Element sizeElement = doc.createElement("size");
                sizeElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 1), "")));
                screenElement.appendChild(sizeElement);

                Element resolutionElement = doc.createElement("resolution");
                resolutionElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 2), "")));
                screenElement.appendChild(resolutionElement);

                Element typeElement = doc.createElement("type");
                typeElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 3), "")));
                screenElement.appendChild(typeElement);

                Element processorElement = doc.createElement("processor");
                laptopElement.appendChild(processorElement);

                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 5), "")));
                processorElement.appendChild(nameElement);

                Element physicalCoresElement = doc.createElement("physical_cores");
                physicalCoresElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 6), "")));
                processorElement.appendChild(physicalCoresElement);

                Element clockSpeedElement = doc.createElement("clock_speed");
                clockSpeedElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 7), "")));
                processorElement.appendChild(clockSpeedElement);

                Element ramElement = doc.createElement("ram");
                ramElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 8), "")));
                laptopElement.appendChild(ramElement);

                Element discElement = doc.createElement("disc");
                discElement.setAttribute("type", Objects.toString(tableModel.getValueAt(row, 10), ""));
                laptopElement.appendChild(discElement);

                Element storageElement = doc.createElement("storage");
                storageElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 9), "")));
                discElement.appendChild(storageElement);

                Element graphicCardElement = doc.createElement("graphic_card");
                laptopElement.appendChild(graphicCardElement);

                Element graphicCardNameElement = doc.createElement("name");
                graphicCardNameElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 11), "")));
                graphicCardElement.appendChild(graphicCardNameElement);

                Element memoryElement = doc.createElement("memory");
                memoryElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 12), "")));
                graphicCardElement.appendChild(memoryElement);

                Element osElement = doc.createElement("os");
                osElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 13), "")));
                laptopElement.appendChild(osElement);

                Element discReaderElement = doc.createElement("disc_reader");
                discReaderElement.appendChild(doc.createTextNode(Objects.toString(tableModel.getValueAt(row, 14), "")));
                laptopElement.appendChild(discReaderElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(file + "(exported).xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayFileContent() {
        int currentLine = 0;

        System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s |   ", "No");

        for (String header : headers)
            System.out.printf("%-25s |   ", header);
        System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (String[] row : rows) {
            System.out.printf("%-5s |   ", ++currentLine);
            for (String cell : row) {
                System.out.printf("%-25s |   ", cell);
            }
            System.out.println();
        }
    }

    public void displayOccurrencesStatistics(int columnNumber) {
        String[] columns = new String[headers.length];
        HashMap<String, Integer> statsMap = new HashMap<String, Integer>();

        for (int index = 0; index < headers.length; index++)
            columns[index] = rows[index][columnNumber];

        for (String cell : columns) {
            if (statsMap.containsKey(cell)) {
                statsMap.put(cell, statsMap.get(cell) + 1);
            } else {
                statsMap.put(cell, 1);
            }
        }

        System.out.println();
        for (String cell : statsMap.keySet()) {
            System.out.println(cell + " występuje " + statsMap.get(cell) + " razy.");
        }
    }
}
