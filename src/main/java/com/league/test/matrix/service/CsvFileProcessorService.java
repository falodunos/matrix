package com.league.test.matrix.service;
import com.league.test.matrix.dto.response.MatrixStatus;
import com.league.test.matrix.exceptions.InvalidMatrixException;
import com.league.test.matrix.service.util.AppConstants;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvFileProcessorService {

    private CSVReader readFile() throws FileNotFoundException {
        CSVReader reader = new CSVReader(new FileReader(AppConstants.TARGET_FILE_PATH));
        return  reader;
    }

    public String echo() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        String output = "";
        for (List innerList : matrixData) {
            for (int i = 0; i < matrixStatus.getColumnCount(); i++) {
                output += innerList.get(i).toString() + ",";
            }
            output = output.substring(0, output.length() - 1);
            output += "\n";
        }

        return output;
    }

    private MatrixStatus validateMatrix(CSVReader reader) throws CsvValidationException, IOException {

        MatrixStatus matrixStatus = new MatrixStatus();
        matrixStatus.setValid(false);

        String[] nextLine;
        int rowCount = 0;
        int columnCount = 0;

        ArrayList outterList = new ArrayList<>();

        while ((nextLine = reader.readNext()) != null) {
            ArrayList innerList = new ArrayList<Integer>();
            ++rowCount;
            if (columnCount == 0) columnCount = nextLine.length;
            else {
                if (columnCount != nextLine.length) throw new InvalidMatrixException("Invalid Matrix, Try again!");
            }

            for (String str: nextLine) {
                if (!str.matches(AppConstants.INTEGER_REGEX_PATTERN)) {
                    throw new NumberFormatException("Invalid Matrix, Matrix elements must be integers!");
                }
                innerList.add(Integer.parseInt(str));
            }

            outterList.add(innerList);
        }
        if (columnCount != rowCount) throw new InvalidMatrixException("Invalid Matrix, Try again!");

        matrixStatus.setRowCount(rowCount);
        matrixStatus.setColumnCount(columnCount);
        matrixStatus.setValid(true);
        matrixStatus.setData(outterList);
        System.out.println("outterList :: " + Arrays.toString(outterList.toArray()));
        return matrixStatus;
    }
}
