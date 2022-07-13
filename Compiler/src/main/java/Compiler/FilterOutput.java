/*
    Created By: M.Shehriyar
    Roll NO: L17-4358
    Date: October 23, 2021

    This program takes a file as
    a command line argument and
    extracts tokens from it and
    stores them in a file
 */
package Compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class FilterOutput {

    public void extractTokens(String readFile) {
        try {
            String line;
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(readFile);
            try (Scanner sc = new Scanner(fis)) {
                for (int i = 0; i < 18; i++) { //Skips initial lines
                    sc.nextLine();
                }
                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    if (!line.isBlank()) {
                        for (int i = 1; i < line.length(); i++) {     //ignores (
                            if (i > 1 && line.charAt(i) == ',') {
                                break;
                            }
                            sb.append(line.charAt(i));
                        }
                    }
                    System.out.println(sb.toString());
                    sb.setLength(0);
                }
            }
        } catch (IOException e) {
        }
    }

    public void extractAndPrintTokens(String readFile, String writeFile) {
        try (PrintStream file = new PrintStream(new File(writeFile))) {
            FilterOutput FO = new FilterOutput();
            System.setOut(file);
            FO.extractTokens(readFile);
        } catch (IOException e) {
        }
    }
}
