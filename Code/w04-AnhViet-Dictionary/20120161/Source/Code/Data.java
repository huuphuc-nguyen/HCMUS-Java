import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Data {

    public static Map<String, String> readXMLFile (String FileName) throws ParserConfigurationException, SAXException, IOException{
        Map <String, String> wordMap = new HashMap<>();

        File myFile = new File(FileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(myFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("record");

        if (nList.getLength() == 0) {
            // XML file contains no <record> elements, so return an empty map
            return wordMap;
        }

        for (int temp = 0; temp < nList.getLength(); temp++){
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;

                String word = eElement.getElementsByTagName("word").item(0).getTextContent();
                String meaning = eElement.getElementsByTagName("meaning").item(0).getTextContent();

                wordMap.put(word,meaning);
            }
        }
        return wordMap;
    }

    public static void writeXMLFile(Map<String, String> wordMap, String DicMode) throws ParserConfigurationException, TransformerException {
        String fileName = "";

        if (DicMode.equals("English to Vietnamese")){
            fileName = "files/Anh_Viet.xml";
        }

        else if (DicMode.equals("Vietnamese to English")){
            fileName = "files/Viet_Anh.xml";
        }

        else if (DicMode.equals("Favorite words")){
            fileName = "files/Fav.xml";
        }
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    
        // Create the root element "dictionary"
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("dictionary");
        doc.appendChild(rootElement);
    
        // Loop through each word in the map and create a "record" element with "word" and "meaning" child elements
        for (Map.Entry<String, String> entry : wordMap.entrySet()) {
            String word = entry.getKey();
            String meaning = entry.getValue();
    
            Element recordElement = doc.createElement("record");
            rootElement.appendChild(recordElement);
    
            Element wordElement = doc.createElement("word");
            wordElement.appendChild(doc.createTextNode(word));
            recordElement.appendChild(wordElement);
    
            Element meaningElement = doc.createElement("meaning");
            meaningElement.appendChild(doc.createTextNode(meaning));
            recordElement.appendChild(meaningElement);
        }
    
        // Write the XML content to the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    public static void writeToTxtFile(String msg, String fileName) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"))) {
            writer.write(msg);
            writer.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public static String[] readTxtFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        String[] result = new String[100];
        int index = 0;
    
        String line;
        while ((line = br.readLine()) != null) {
            result[index] = line;
            index++;
            if (index == result.length) {
                result = Arrays.copyOf(result, result.length * 2); // Double the size of the array if it gets full
            }
        }
        br.close();
    
        return Arrays.copyOf(result, index); // Return only the part of the array that contains the actual data
    }

    public static void clearTxtFile(String fileName){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
