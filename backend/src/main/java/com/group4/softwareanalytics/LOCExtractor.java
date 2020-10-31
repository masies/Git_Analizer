package com.group4.softwareanalytics;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LOCExtractor {

    public static List<String> extractLines(String src)
    {
        List<String> lineNb = new ArrayList<String>();

        Document doc = getXml(src);

        NodeList nodeList = doc.getFirstChild().getChildNodes();


        List<String> finalLineNb = new ArrayList<String>(
                new HashSet<String>(cleanXml(nodeList,lineNb)));

        return finalLineNb;
    }


    public static List<String> cleanXml(NodeList nodeList, List<String> lineNb)
    {

        List<String> intruders = new ArrayList<String>(
                Arrays.asList("import","comment","{","}",";",""));


        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(intruders.contains(node.getNodeName()))
            {
                //System.out.println(node);
                node.getParentNode().removeChild(node);
            }
            else
            {
                if(node.getNodeName() != "block")
                {
                    try{
                        lineNb.add(node.getAttributes().getNamedItem("pos:start").getNodeValue().split(":")[0]);
                    }catch (Exception e)
                    { }
                }
                if(node.getChildNodes().getLength() > 0)
                {
                    cleanXml(node.getChildNodes(),lineNb);
                }
            }
        }
        return lineNb;
    }

    public static Document getXml(String src)
    {

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("bash", "-c", "srcml --position " + src);

        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                Document doc = convertStringToXMLDocument(output.toString());
                return doc;
            } else {
                //abnormal...
            }


        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        return null;
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


        DocumentBuilder builder = null;
        try
        {

            builder = factory.newDocumentBuilder();


            Document doc = (Document) builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
