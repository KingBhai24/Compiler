/*
    Created By: M.Shehriyar
    Roll NO: L17-4358
    Date: October 23, 2021

    This program takes a file and
    make token lexeme pairs and
    stores them in a file

    Issue: File shouldnot contain tabs.
 */
package Compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Lexical_Analyzer {
    
    String tokenLexemeFile = "Token_Lexeme.cc";

    public boolean checkIsSmallAlphabet(char a) {
        return a >= 'a' && a <= 'z';
    }

    public boolean checkIsCapitalAlphabet(char a) {
        return a >= 'A' && a <= 'Z';
    }

    public boolean checkIsAlphabet(char a) {
        return (checkIsCapitalAlphabet(a) || checkIsSmallAlphabet(a));
    }

    public boolean checkIsDigit(char a) {
        return a >= '0' && a <= '9';
    }

    public boolean checkIsUnderScore(char a) {
        return a == '_';
    }

    public boolean checkIsSeparator(char a) {
        boolean flag = false;
        for (int i = 0; i < Compiler.separators.size(); i++) {
            if (a == Compiler.separators.get(i)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean checkIsKeyWord(String a) {
        boolean flag = false;
        for (String i : Compiler.keyWords) {
            if (i.compareTo(a) == 0) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean checkIsNumericConstant(String a) {
        boolean flag = true;
        for (int i = 0; i < a.length(); i++) {
            if (!checkIsDigit(a.charAt(i))) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean checkIsIdentifier(String a) {
        int flag = 0;
        if (checkIsAlphabet(a.charAt(0))) {
            flag = 1;
        }
        for (int i = 1; i < a.length() && flag == 1; i++) {
            if (checkIsAlphabet(a.charAt(i)) || checkIsDigit(a.charAt(i)) || checkIsUnderScore(a.charAt(i))) {
                flag = 1;
            } else {
                flag = 2;
            }
        }
        return flag == 1;
    }

    public void printIsSeparator(String a) {
        switch (a) {
            case " ", "\n" -> {
            }
            case ",", "'", "\"", ":", ";", "(", ")", "[", "]", "{", "}" ->
                System.out.println("(" + a + "," + Compiler.tag.get(0) + ")");
            case "->" ->
                System.out.println("(" + Compiler.tag.get(5) + ",\"" + a + "\")");
            case "=" ->
                System.out.println("(" + Compiler.tag.get(6) + ",\'" + a + "\')");
            case "+", "-", "*", "/" ->
                System.out.println("(" + Compiler.tag.get(7) + ",\'" + a + "\')");
            case "<", ">", "<=", ">=", "==", "!=" ->
                System.out.println("(" + Compiler.tag.get(8) + "," + "\"" + a + "\")");
            case "++", "--" ->
                System.out.println("(" + Compiler.tag.get(9) + ",\"" + a + "\")");
            default -> {
                System.out.println("Error! printIsSeparator(String a). Error char input: " + a);
            }
        }
    }

    public void printIsKeyWord(String a) {
        System.out.println("(" + a + "," + Compiler.tag.get(0) + ")");
    }

    public void printIsIdentifier(String a) {
        System.out.println("(" + Compiler.tag.get(1) + ",\"" + a + "\")");
    }

    public void printIsString(String a) {
        System.out.println("(" + Compiler.tag.get(2) + ",\"" + a + "\")");
    }

    public void printIsNumericConstant(String a) {
        System.out.println("(" + Compiler.tag.get(3) + ",\"" + a + "\")");
    }

    public void printIsLiteralConstant(char a) {
        System.out.println("(" + Compiler.tag.get(4) + ",\'" + a + "\')");
    }

    public void printIsUnknown(String a) {
        System.out.println("(" + Compiler.tag.get(10) + ",\"" + a + "\")");
    }
    
    public void deleteFiles(){
        File f;
        f = new File(tokenLexemeFile);
        if(f.exists())
            f.delete();
    }

    public void createTokenAndPrintToConsole(String readFile) {
        printTokenLexemeInfo();
        createAndPrintTokenLexeme(readFile);
    }

    public void createTokenAndPrintToFile(String readFile) {
        deleteFiles();
        try (PrintStream file = new PrintStream(new File(tokenLexemeFile))) {
            System.setOut(file);
            printTokenLexemeInfo();
            createAndPrintTokenLexeme(readFile);
            file.close();
        } catch (IOException e) {
        }
    }

    public void printTokenLexemeInfo() {
        System.out.println("Tokens Info:");
        System.out.print("  Keywords -> ");
        printIsKeyWord("int");
        System.out.print("  Identiifers -> ");
        printIsIdentifier("my_char");
        System.out.print("  Separators -> ");
        printIsSeparator(";");
        System.out.print("  Assignment Operator -> ");
        printIsSeparator("=");
        System.out.print("  Input Operator -> ");
        printIsSeparator("->");
        System.out.print("  Arithematic Operators -> ");
        printIsSeparator("+");
        System.out.print("  Relational Operators -> ");
        printIsSeparator("!=");
        System.out.print("  Increment/Decrement Operators -> ");
        printIsSeparator("++");
        System.out.print("  Strings -> ");
        printIsString("Hello World");
        System.out.print("  Numeric Constants -> ");
        printIsNumericConstant("100");
        System.out.print("  Literal Constants -> ");
        printIsLiteralConstant('a');
        System.out.print("  Error Tokens -> ");
        printIsUnknown("int@abc");
        System.out.println("  It Ignores Spaces and NewLines");
        System.out.println("  It Ignores Single Line and Multi Line Comments");
        System.out.println("  It Ignores \"//\", \"/*\" ");
        System.out.println();
    }

    public void createAndPrintTokenLexeme(String readFile) {
        System.out.println("Token-Lexeme:");
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(readFile);
            try (Scanner sc = new Scanner(fis)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine() + "\n";
                    OUTER:
                    for (int lineIndex = 0; lineIndex < line.length(); lineIndex++) {
                        if (checkIsAlphabet(line.charAt(lineIndex))) {
                            sb.append(line.charAt(lineIndex));
                        } else if (checkIsDigit(line.charAt(lineIndex))) {
                            sb.append(line.charAt(lineIndex));
                        } else if (checkIsUnderScore(line.charAt(lineIndex))) {
                            sb.append(line.charAt(lineIndex));
                        } else if (checkIsSeparator(line.charAt(lineIndex))) {
                            if (!sb.isEmpty()) {
                                if (checkIsKeyWord(sb.toString())) {
                                    printIsKeyWord(sb.toString());
                                    sb.setLength(0);
                                } else if (checkIsIdentifier(sb.toString())) {
                                    printIsIdentifier(sb.toString());
                                    sb.setLength(0);
                                } else if (checkIsNumericConstant(sb.toString())) {
                                    printIsNumericConstant(sb.toString());
                                    sb.setLength(0);
                                } else {
                                    printIsUnknown(sb.toString());
                                    sb.setLength(0);
                                }
                            }
                            sb.append(line.charAt(lineIndex));
                            if (lineIndex + 1 >= line.length()) {           //handling single saparator remaining at the end
                                printIsSeparator(sb.toString());
                                sb.setLength(0);
                            } else if (lineIndex + 1 < line.length()) {     //handling combinational separators
                                switch (line.charAt(lineIndex)) {
                                    case ' ', '\n', '*', ':', ';', ',', '(', ')', '[', ']', '{', '}' -> {     //handling single separators
                                        printIsSeparator(sb.toString());
                                        sb.setLength(0);
                                    }
                                    case '<' -> {
                                        if (line.charAt(lineIndex + 1) == '=') {
                                            sb.append('=');
                                            lineIndex++;
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        } else {
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '>' -> {
                                        if (line.charAt(lineIndex + 1) == '=') {
                                            sb.append('=');
                                            lineIndex++;
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        } else {
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '=' -> {
                                        if (line.charAt(lineIndex + 1) == '=') {
                                            sb.append('=');
                                            lineIndex++;
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        } else {
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '!' -> {
                                        if (line.charAt(lineIndex + 1) == '=') {
                                            sb.append('=');
                                            lineIndex++;
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        } else {
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '+' -> {
                                        if (line.charAt(lineIndex + 1) == '+') {
                                            lineIndex++;
                                            sb.append('+');
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        } else {
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '-' -> {
                                        switch (line.charAt(lineIndex + 1)) {
                                            case '-' -> {
                                                sb.append('-');
                                                lineIndex++;
                                                printIsSeparator(sb.toString());
                                                sb.setLength(0);
                                            }
                                            case '>' -> {
                                                sb.append('>');
                                                lineIndex++;
                                                printIsSeparator(sb.toString());
                                                sb.setLength(0);
                                            }
                                            default -> {
                                                printIsSeparator(sb.toString());
                                                sb.setLength(0);
                                            }
                                        }
                                    }
                                    case '/' -> {
                                        switch (line.charAt(lineIndex + 1)) {
                                            case '/' -> {
                                                sb.setLength(0);
                                                break OUTER;
                                            }
                                            case '*' -> {
                                                sb.setLength(0);
                                                lineIndex++;
                                                boolean flag = false;
                                                while (!flag) {
                                                    if (lineIndex + 2 < line.length()) {
                                                        lineIndex++;
                                                    } else if (lineIndex + 2 >= line.length()) {
                                                        if (sc.hasNextLine()) {
                                                            line = sc.nextLine();
                                                            lineIndex = 0;
                                                        } else {
                                                            break OUTER;
                                                        }
                                                    }
                                                    if (!line.isEmpty()) {
                                                        if (line.charAt(lineIndex) == '*') {
                                                            if (lineIndex + 1 < line.length()) {
                                                                if (line.charAt(lineIndex + 1) == '/') {
                                                                    lineIndex++;
                                                                    flag = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            default -> {
                                                printIsSeparator(sb.toString());
                                                sb.setLength(0);
                                            }
                                        }
                                    }
                                    case '"' -> {
                                        printIsSeparator(sb.toString());
                                        sb.setLength(0);
                                        lineIndex++;
                                        while (line.charAt(lineIndex) != '"' && lineIndex + 1 < line.length()) {
                                            sb.append(line.charAt(lineIndex));
                                            lineIndex++;
                                        }
                                        printIsString(sb.toString());
                                        sb.setLength(0);
                                        if (line.charAt(lineIndex) == '"') {
                                            sb.append('"');
                                            printIsSeparator(sb.toString());
                                            sb.setLength(0);
                                        }
                                    }
                                    case '\'' -> {
                                        printIsSeparator(sb.toString());
                                        sb.setLength(0);
                                        if (lineIndex + 1 < line.length()) {
                                            lineIndex++;
                                            sb.append(line.charAt(lineIndex));
                                            printIsLiteralConstant(sb.charAt(0));
                                            sb.setLength(0);
                                        }
                                        if (lineIndex + 1 < line.length()) {
                                            lineIndex++;
                                            sb.append(line.charAt(lineIndex));
                                            if (sb.charAt(0) == '\'') {
                                                printIsSeparator(sb.toString());
                                                sb.setLength(0);
                                            } else {
                                                lineIndex--;
                                                sb.setLength(0);
                                            }
                                        }
                                    }
                                    default -> {
                                        System.out.println("Error! createTokens(String a). Unchecked char: " + line.charAt(lineIndex));
                                    }
                                }
                            }
                        } else {
                            sb.append(line.charAt(lineIndex));
                        }
                    }
                }
                sc.close();
                fis.close();
            }
        } catch (IOException e) {
        }
    }
}
