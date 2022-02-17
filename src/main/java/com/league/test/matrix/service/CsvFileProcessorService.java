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

    /**
     * Read CSV File
     * @return CSVReader
     * @throws FileNotFoundException
     */
    private CSVReader readFile() throws FileNotFoundException {
        CSVReader reader = new CSVReader(new FileReader(AppConstants.TARGET_FILE_PATH));
        return  reader;
    }

    /**
     * echo matrix
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public String echo() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        String output = "";
        for (List innerList : matrixData) {
            for (int i = 0; i < matrixStatus.getColumnCount(); i++) {
                output += innerList.get(i).toString() + ",";
            }
            output = output.substring(0, output.length() - 1); // remove trailing comma
            output += "\n";
        }

        output = output.substring(0, output.length() - 1); // remove trailing new line character

        return output;
    }

    /**
     * invert matrix
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public String invert() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        String output = "";
        for (int n = 0; n < matrixData.size(); n++) {
            for (List innerList : matrixData) {
                for (int i = n; i < n+1; i++) {
                    output += innerList.get(i).toString() + ",";
                }
            }
            output = output.substring(0, output.length() - 1); // remove trailing comma
            output += "\n";
        }

        output = output.substring(0, output.length() - 1); // remove trailing new line character

        return output;
    }

    /**
     * flatten matrix
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public String flatten() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        String output = "";
        for (List innerList : matrixData) {
            for (int i = 0; i < innerList.size(); i++) {
                output += innerList.get(i).toString() + ",";
            }
        }
        output = output.substring(0, output.length() - 1); // remove trailing comma (,)

        return output;
    }

    /**
     * sum matrix
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public String sum() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        int sum = 0;
        for (List innerList : matrixData) {
            for (int i = 0; i < innerList.size(); i++) {
                sum += Integer.parseInt(innerList.get(i).toString());
            }
        }

        return sum + ""; // convert to string and return
    }

    /**
     * Multiply matrix
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public String multiply() throws IOException, CsvValidationException {
        CSVReader reader = readFile();
        MatrixStatus matrixStatus = validateMatrix(reader);

        List<List<Integer>> matrixData = matrixStatus.getData();
        int multiply = 1;
        for (List innerList : matrixData) {
            for (int i = 0; i < innerList.size(); i++) {
                multiply *= Integer.parseInt(innerList.get(i).toString());
            }
        }
        return multiply + ""; // convert to string and return
    }

    /**
     * Validate and return matrix content as ArrayList<ArrayList<Integer>>
     * @param reader
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    private MatrixStatus validateMatrix(CSVReader reader) throws CsvValidationException, IOException {

        MatrixStatus matrixStatus = new MatrixStatus();
        matrixStatus.setValid(false);

        String[] nextLine;
        int rowCount = 0;
        int columnCount = 0;

        ArrayList outerList = new ArrayList<>();

        while ((nextLine = reader.readNext()) != null) {
            ArrayList innerList = new ArrayList<Integer>();
            ++rowCount;
            if (columnCount == 0) columnCount = nextLine.length; // initialize column size
            else {
                // if columns have different length, invalidate then matrix
                if (columnCount != nextLine.length) throw new InvalidMatrixException("Invalid Matrix, Try again!");
            }

            for (String str: nextLine) {
                // if any of the martix element is non-integer, invalidate the entire matrix
                if (!str.matches(AppConstants.INTEGER_REGEX_PATTERN)) {
                    throw new NumberFormatException("Invalid Matrix, Matrix elements must be integers!");
                }
                innerList.add(Integer.parseInt(str));
            }

            outerList.add(innerList); // add ArrayList<Integer> to outer ArrayList
        }
        if (columnCount != rowCount) throw new InvalidMatrixException("Invalid Matrix, Try again!");

        matrixStatus.setRowCount(rowCount);
        matrixStatus.setColumnCount(columnCount);
        matrixStatus.setValid(true);
        matrixStatus.setData(outerList);
        System.out.println("outerList :: " + Arrays.toString(outerList.toArray()));
        return matrixStatus;
    }
}
