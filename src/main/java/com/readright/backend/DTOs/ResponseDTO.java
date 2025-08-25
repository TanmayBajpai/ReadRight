package com.readright.backend.DTOs;

import com.readright.backend.Records.Analysis;
import com.readright.backend.Records.OutletInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    OutletInfo outletInfo;

    Analysis headlineAnalysis;
    Analysis contentAnalysis;
}
