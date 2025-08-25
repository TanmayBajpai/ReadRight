package com.readright.backend.Services;
import com.readright.backend.POJO.ArticleData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.springframework.stereotype.Service;

@Service
public class ArticleParsingService {
    ExtractorService extractorService;

    public ArticleParsingService(ExtractorService extractorService) {
        this.extractorService = extractorService;
    }

    public ArticleData parseArticle(String link) throws Exception {
        try {
            Document doc = Jsoup.connect(link).timeout(30000).get();

            String html = doc.html();

            String domain = extractorService.extractDomain(link);
            String headline = extractorService.extractHeadline(doc);
            String content = extractorService.extractContent(html);

            return new ArticleData(domain, headline, content);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
