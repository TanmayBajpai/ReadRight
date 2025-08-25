package com.readright.backend.Controllers;

import com.readright.backend.Records.Analysis;
import com.readright.backend.DTOs.ResponseDTO;
import com.readright.backend.POJO.ArticleData;
import com.readright.backend.Records.OutletInfo;
import com.readright.backend.Services.ArticleParsingService;
import com.readright.backend.Services.GetAnalysisService;
import com.readright.backend.Services.LoadOutletInfo;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping(("/api"))
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {

    Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(30, Refill.greedy(30, Duration.ofMinutes(1))))
            .build();

    ArticleParsingService articleParsingService;
    LoadOutletInfo loadOutletInfo;
    GetAnalysisService getAnalysisService;

    public MainController(ArticleParsingService articleParsingService, LoadOutletInfo loadOutletInfo, GetAnalysisService getAnalysisService) {
        this.articleParsingService = articleParsingService;
        this.loadOutletInfo = loadOutletInfo;
        this.getAnalysisService = getAnalysisService;
    }

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forward() {
        return "forward:/index.html";
    }


    @GetMapping("/get-bias")
    public ResponseEntity<ResponseDTO> getBias(@RequestParam String link) {
        if (bucket.tryConsume(1)) {
            ArticleData articleData;

            try {
                articleData = articleParsingService.parseArticle(link);
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO());
            }

            Optional<OutletInfo> optionalOutletInfo = loadOutletInfo.findByDomain(articleData.getDomain());
            OutletInfo outletInfo;

            outletInfo = optionalOutletInfo.orElse(null);

            Analysis headlineAnalysis = getAnalysisService.getAnalysis(articleData.getHeadline());

            Analysis contentAnalysis = getAnalysisService.getAnalysis(articleData.getContent());

            return ResponseEntity.ok(new ResponseDTO(outletInfo, headlineAnalysis, contentAnalysis));
        } else {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(null);
        }
    }
}
