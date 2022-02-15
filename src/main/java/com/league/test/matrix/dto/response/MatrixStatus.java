package com.league.test.matrix.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MatrixStatus {
    int rowCount;
    int columnCount;
    boolean isValid;
    List<List<Integer>> data;
}
