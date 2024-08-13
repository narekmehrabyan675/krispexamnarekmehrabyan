package com.example.recommendation.functionality;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rss")
public class RssFeedReader {

    @GetMapping("/headlines")
    public ResponseEntity<List<String>> getHeadlines(@RequestParam String url) {
        List<String> headlines = fetchHeadlines(url);
        return ResponseEntity.ok(headlines);
    }

    private List<String> fetchHeadlines(String url) {
        RestTemplate restTemplate = new RestTemplate();
        List<String> headlines = new ArrayList<>();

        try {
            String rssFeed = restTemplate.getForObject(url, String.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(rssFeed));
            Document document = builder.parse(is);

            NodeList nodeList = document.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    headlines.add(title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return headlines;
    }
}

