/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cb_kwtadmission;

/**
 *
 * @author Jose Manuel
 */

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource; import javax.xml.transform.stream.StreamResult;

class SendMessage {
    public static final String URL = "https://sgw01.cm.nl/gateway.ashx";

    public static void sendmessage(String message , String sendername,String cellphone) {
        
            UUID productToken = UUID.fromString("62f6f076-eb6e-4395-8f02-83a9505f891b");
            String xml = createXml(productToken, sendername,cellphone,message);
            String response = doHttpPost(URL, xml);
            
            System.out.println("Response: " + response);
            //System.in.read();
       
    }

    private static String createXml(UUID productToken, String sender, String recipient, String message) {
        try {
            ByteArrayOutputStream xml = new ByteArrayOutputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            // Get the DocumentBuilder
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            // Create blank DOM Document
            DOMImplementation impl = docBuilder.getDOMImplementation();
            Document doc = impl.createDocument(null, "MESSAGES", null);

            // create the root element
            Element root = doc.getDocumentElement();
            Element authenticationElement = doc.createElement("AUTHENTICATION");
            Element productTokenElement = doc.createElement("PRODUCTTOKEN");
            authenticationElement.appendChild(productTokenElement);
            Text productTokenValue = doc.createTextNode("" + productToken);
            productTokenElement.appendChild(productTokenValue);
            root.appendChild(authenticationElement);

            Element msgElement = doc.createElement("MSG");
            root.appendChild(msgElement);

            Element fromElement = doc.createElement("FROM");
            Text fromValue = doc.createTextNode(sender);
            fromElement.appendChild(fromValue);
            msgElement.appendChild(fromElement);
            
            Element MINIMUMNUMBEROFMESSAGEPARTSElement = doc.createElement("MINIMUMNUMBEROFMESSAGEPARTS");
            Text MINIMUMNUMBEROFMESSAGEPARTSValue = doc.createTextNode("1");
            MINIMUMNUMBEROFMESSAGEPARTSElement.appendChild(MINIMUMNUMBEROFMESSAGEPARTSValue);
            msgElement.appendChild(MINIMUMNUMBEROFMESSAGEPARTSElement);
            
            Element MAXIMUMNUMBEROFMESSAGEPARTSElement = doc.createElement("MAXIMUMNUMBEROFMESSAGEPARTS");
            Text MAXIMUMNUMBEROFMESSAGEPARTSValue = doc.createTextNode("8");
            MAXIMUMNUMBEROFMESSAGEPARTSElement.appendChild(MAXIMUMNUMBEROFMESSAGEPARTSValue);
            msgElement.appendChild(MAXIMUMNUMBEROFMESSAGEPARTSElement);
            
            Element bodyElement = doc.createElement("BODY");
            Text bodyValue = doc.createTextNode(message);
            bodyElement.appendChild(bodyValue);
            msgElement.appendChild(bodyElement);

             Element CUSTOMGROUPINGElement = doc.createElement("CUSTOMGROUPING");
            Text CUSTOMGROUPINGValue = doc.createTextNode("CB_KWT");
            CUSTOMGROUPINGElement.appendChild(CUSTOMGROUPINGValue);
            msgElement.appendChild(CUSTOMGROUPINGElement);
            
            Element DCSElement = doc.createElement("DCS");
            Text DCSValue = doc.createTextNode("8");
            DCSElement.appendChild(DCSValue);
            msgElement.appendChild(DCSElement);

            Element toElement = doc.createElement("TO");
            Text toValue = doc.createTextNode(recipient);
            toElement.appendChild(toValue);
            msgElement.appendChild(toElement);

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Source src = new DOMSource(doc);
            Result dest = new StreamResult(xml);
            aTransformer.transform(src, dest);

            return xml.toString();

        } catch (TransformerException | ParserConfigurationException ex) {
            System.err.println(ex);
            return ex.toString();
        }
    }

    private static String doHttpPost(String urlString, String requestString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(requestString);
            wr.flush();
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String response = "";
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            wr.close();
            rd.close();

            return response;
        } catch (IOException ex) {
            System.err.println(ex); return ex.toString();
        }
    }
}