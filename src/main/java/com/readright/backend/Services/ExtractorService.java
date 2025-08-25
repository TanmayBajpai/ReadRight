package com.readright.backend.Services;

import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ExtractorService {
    public String extractHeadline(Document doc) {
        Element ogTitle = doc.select("meta[property=og:title]").first();
        if (ogTitle != null) {
            return ogTitle.attr("content");
        }

        Element h1 = doc.select("h1").first();
        if (h1 != null) {
            return h1.text();
        }

        return doc.title();
    }

    public String extractContent(String html) {
        try {
            return ArticleExtractor.INSTANCE.getText(new StringReader(html));
        } catch (Exception e) {
            return null;
        }
    }

    public String extractDomain(String link) {
        try {
            URI uri = new URI(link);

            String host = uri.getHost();

            if (host == null) {
                return null;
            }

            if (host.startsWith("www.")) {
                return host.substring(4);
            }

            return host;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
