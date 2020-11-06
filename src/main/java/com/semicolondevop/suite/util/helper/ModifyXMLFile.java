package com.semicolondevop.suite.util.helper;



import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;


/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 7:16 PM
 * @project com.semicolondevop.suite.util.helper in ds-suite
 */


@Slf4j
public final class ModifyXMLFile {

    public  void updateCredentialsAndRepoLink(String credentialsIdValue, String descriptionValue, String urlValue)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("jenkins/jobs/jenkins-pipeline.xml").getFile());
            log.info("THE LOGGER FILE {}", file);
            Document doc = documentBuilder.parse(file);
            Node description = doc.getElementsByTagName("description").item(0);
            Node credentialsId = doc.getElementsByTagName("credentialsId").item(0);
            Node url = doc.getElementsByTagName("url").item(0);
            log.info("THE CREDENTIAL ID IS {}", credentialsId);
            log.info("THE URL ID IS {}", url);
            credentialsId.setTextContent(credentialsIdValue);
            url.setTextContent(urlValue);
            description.setTextContent(descriptionValue);
            log.info("The doc description {}", description.getTextContent());

//             write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }
        catch (ParserConfigurationException | IOException | SAXException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}
