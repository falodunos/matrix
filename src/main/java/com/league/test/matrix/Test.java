package com.league.test.matrix;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "\\uploads\\matrix.csv";
        path = path.replace("\\", "/");
        InputStream in = new FileInputStream(path);
        System.out.println("path " + in.read());
    }
}
