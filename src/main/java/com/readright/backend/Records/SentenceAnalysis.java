package com.readright.backend.Records;

public record SentenceAnalysis(String sentence,
                               String label,
                               double score) {}
