package com.readright.backend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {
    public String domain;
    public String headline;
    public String content;
}
