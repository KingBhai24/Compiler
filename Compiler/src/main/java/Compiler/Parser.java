/*
    Created By: M.Shehriyar
    Roll NO: L17-4358
    Date: October 23, 2021

    Issue: int initialization containing more than one ID or NC.
    example: int a = b + 3 + c;
    This case is not handled.
 */
package Compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Parser {

    int newTempCount = 0;
    int parseTreeTabCount = 0;  //tab count for parse tree
    String currentLine;         //current line in token-lexeme file
    String currentToken;        //current token in token-lexeme file

    Scanner sc;
    int tokenLexemeFileLineNo = 18;         //line no in token-lexeme file used for error detection
    int tacFileLineNO = 1;                  //line no in tac file
    int symbolFileRelativeAddress = 0;      //data types relative address value in symbol file

    String symbolFile = "Symbol_Table.cc";
    String parseTreeFile = "Parse_Tree.cc";
    String tacFile = "Tac.cc";

    public String getToken(String line) {
        StringBuilder sb = new StringBuilder();
        if (!line.isBlank()) {
            for (int i = 1; i < line.length(); i++) {
                if (i > 1 && line.charAt(i) == ',') {
                    break;
                }
                sb.append(line.charAt(i));
            }
        }
        return sb.toString();
    }

    public String getLexeme(String line) {
        StringBuilder sb = new StringBuilder();
        int i;
        if (!line.isBlank()) {
            for (i = 1; i < line.length(); i++) {
                if (i > 1 && line.charAt(i) == ',') {
                    break;
                }
            }
            i++;
            if (line.charAt(i) == '\'' || line.charAt(i) == '"') {
                i++;
            }
            if (i + 2 < line.length()) {
                for (; i < line.length() - 2; i++) {
                    sb.append(line.charAt(i));
                }
            } else {
                sb.append(line.charAt(i));
            }
        }
        return sb.toString();
    }

    public String nextToken() {
        tokenLexemeFileLineNo++;
        String token;
        if (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            token = getToken(currentLine);
        } else {
            token = "";
        }
        return token;
    }

    public void reportError(String s, int n) { // 1 = Expected
        if (n == 1) {
            System.out.println("\nError!  Token NO: " + tokenLexemeFileLineNo + ".  Token: " + currentToken + ".  Expected: '" + s + "'");
        } else if (n == 2) {
            System.out.println("\nError!  Token NO: " + tokenLexemeFileLineNo + ".  Token: " + currentToken + ".  " + s);
        }
    }

    public void writeToSymbolFile(String s, int i) {
        FileWriter fw;
        try {
            fw = new FileWriter(symbolFile, true);
            fw.append(s);
            fw.append(symbolFileRelativeAddress + "\n");
            fw.close();
            symbolFileRelativeAddress = symbolFileRelativeAddress + i;
        } catch (IOException e) {
        }
    }

    public String checkInSymbolFile(String s) {
        FileInputStream fis;
        String l1;
        String[] a;
        try {
            fis = new FileInputStream(symbolFile);
            try (Scanner sc1 = new Scanner(fis)) {
                while (sc1.hasNextLine()) {
                    l1 = sc1.nextLine();
                    if (!l1.isEmpty()) {
                        a = l1.split(" ");
                        if (a[1].contentEquals(s)) {
                            return a[0];
                        }
                    }
                }
            }
            fis.close();
        } catch (IOException e) {
        }
        return "";
    }

    public void printParseTreeTabs(int a, String s) {
        for (int i = 0; i < a; i++) {
            System.out.print("  ");
        }
        System.out.println(s);
    }

    public void writeToTacFile(String s) {
        FileWriter fw;
        try {
            fw = new FileWriter(tacFile, true);
            fw.append(s + "\n");
            fw.close();
            tacFileLineNO++;
        } catch (IOException e) {
        }
    }

    public void backPatch(int a, int b) {
    }
    
    public String newtmp(){
        String tmp = "t";
        tmp = tmp + newTempCount;
        newTempCount++;
        return tmp;
    }

    public void deleteFiles() {
        File f;
        f = new File(symbolFile);
        if(f.exists())
            f.delete();
        f = new File(parseTreeFile);
        if(f.exists())
            f.delete();
        f = new File(tacFile);
        if(f.exists())
            f.delete();
    }
//-----------------------------------------------------------------------------

    public void KWintA() {
        printParseTreeTabs(parseTreeTabCount, "KWintA");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("ID")) {
            if (!checkInSymbolFile(getLexeme(currentLine)).isEmpty()) {
                reportError("'" + getLexeme(currentLine) + "'" + " already defined.", 2);
            } else {
                writeToSymbolFile("int " + getLexeme(currentLine) + ' ', 4);
                KWintB();
            }
        } else {
            reportError("ID", 1);
        }
        parseTreeTabCount--;
    }

    public void KWintB() {
        printParseTreeTabs(parseTreeTabCount, "KWintB");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "," ->
                KWintA();
            case "AsO" -> {
                KWintD();
                KWintC();
            }
            default -> {
            }
        }
        parseTreeTabCount--;
    }

    public void KWintC() {
        printParseTreeTabs(parseTreeTabCount, "KWintC");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals(",")) {
            KWintA();
        } else {
        }
        parseTreeTabCount--;
    }

    public void KWintD() {
        printParseTreeTabs(parseTreeTabCount, "KWintD");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "ID" -> {
            }
            case "NC" -> {
            }
            case "AO" -> {
                String temp = getLexeme(currentLine);
                if (temp.contentEquals("-")) {
                    KWintE();
                } else {
                    reportError("-", 1);
                }
            }
            default ->
                reportError("ID / NC / -", 1);
        }
        parseTreeTabCount--;
    }

    public void KWintE() {
        printParseTreeTabs(parseTreeTabCount, "KWintE");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "ID" -> {
            }
            case "NC" -> {
            }
            default ->
                reportError("ID / NC", 1);
        }
        parseTreeTabCount--;
    }

    public void KWint() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWint");
        parseTreeTabCount++;
        if (currentToken.contentEquals("int")) {
        } else {
            reportError("int", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals(":")) {
            KWintA();
        } else {
            reportError(":", 1);
            return;
        }
        if (currentToken.contentEquals(";")); else {
            reportError(";", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWcharA() {
        printParseTreeTabs(parseTreeTabCount, "KWcharA");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("ID")) {
            if (!checkInSymbolFile(getLexeme(currentLine)).isEmpty()) {
                reportError("'" + getLexeme(currentLine) + "'" + " already defined.", 2);
            } else {
                writeToSymbolFile("char " + getLexeme(currentLine) + ' ', 1);
                KWcharB();
            }
        }
        parseTreeTabCount--;
    }

    public void KWcharB() {
        printParseTreeTabs(parseTreeTabCount, "KWcharB");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "," ->
                KWcharA();
            case "AsO" -> {
                KWcharD();
                KWcharC();
            }
            default -> {
            }
        }
        parseTreeTabCount--;
    }

    public void KWcharC() {
        printParseTreeTabs(parseTreeTabCount, "KWcharC");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals(",")) {
            KWcharA();
        } else {
        }
        parseTreeTabCount--;
    }

    public void KWcharD() {
        printParseTreeTabs(parseTreeTabCount, "KWcharD");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "ID" -> {
            }
            case "'" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("LC")) {
                    currentToken = nextToken();
                    if (currentToken.contentEquals("'")) {
                    } else {
                        reportError("'", 1);
                    }
                } else {
                    reportError("LC", 1);
                }
            }
            default ->
                reportError("ID / 'LC'", 1);
        }
        parseTreeTabCount--;
    }

    public void KWchar() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWchar");
        parseTreeTabCount++;
        if (currentToken.contentEquals("char")) {
            currentToken = nextToken();
            if (currentToken.contentEquals(":")) {
                KWcharA();
                if (currentToken.contentEquals(";")) {
                } else {
                    reportError(";", 1);
                }
            } else {
                reportError(":", 1);
            }
        } else {
            reportError("char", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public String KWintegeridentifierA() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierA");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        switch (currentToken) {
            case "AsO" -> {
                currentToken = nextToken();
                t2 = KWintegeridentifierB();
                t1 = " = " + t2;
            }
            case "Inc/Dec" -> {
                t2 = getLexeme(currentLine);
                currentToken = nextToken();
                t1 = t2;
            }
            default ->
                reportError("AsO / Inc/Dec", 1);
        }
        parseTreeTabCount--;
        return t1;
    }

    public String KWintegeridentifierB() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierB");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        if (currentToken.contentEquals("(")) {
            currentToken = nextToken();
            KWintegeridentifierB();
            if (currentToken.contentEquals(")")) {
                currentToken = nextToken();
                KWintegeridentifierE();
            } else {
                reportError(")", 1);
            }
        } else {
            t2 = KWintegeridentifierC();
            t1 = t2;
        }
        parseTreeTabCount--;
        return t1;
    }

    public String KWintegeridentifierC() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierC");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        String t3 = "";
        t2 = KWintegeridentifierX();
        currentToken = nextToken();
        t3 = KWintegeridentifierD();
        t1 = newtmp();
        writeToTacFile(t1 + " = " + t2 + " " + t3);
        parseTreeTabCount--;
        return t1;
    }

    public String KWintegeridentifierD() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierD");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        String t3 = "";
        if (currentToken.contentEquals("AO")) {
            t2 = getLexeme(currentLine);
            currentToken = nextToken();
            t3 = KWintegeridentifierF();
            t1 = t2 + " " + t3;
        } else {
        }
        parseTreeTabCount--;
        return t1;
    }

    public void KWintegeridentifierE() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierE");
        parseTreeTabCount++;
        if (currentToken.contentEquals("AO")) {
            currentToken = nextToken();
            KWintegeridentifierG();
        } else {
        }
        parseTreeTabCount--;
    }

    public String KWintegeridentifierF() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierF");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        String t3 = "";
        if (currentToken.contentEquals("ID") || currentToken.contentEquals("NC")) {
            t2 = KWintegeridentifierY();
            currentToken = nextToken();
            t3 = KWintegeridentifierD();
            t1 = newtmp();
            writeToTacFile(t1 + " = " + t2 + " " + t3);
        } else {
            t2 = KWintegeridentifierB();
            t1 = t2;
        }
        parseTreeTabCount--;
        return t1;
    }

    public void KWintegeridentifierG() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierG");
        parseTreeTabCount++;
        if (currentToken.contentEquals("ID") || currentToken.contentEquals("NC")) {
            KWintegeridentifierY();
            currentToken = nextToken();
            KWintegeridentifierE();
        } else {
            KWintegeridentifierB();
        }
        parseTreeTabCount--;
    }

    public String KWintegeridentifierX() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierX");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        switch (currentToken) {
            case "ID", "NC" -> {
                t2 = getLexeme(currentLine);
                t1 = t2;
            }
            case "AO" -> {
                String lexeme = getLexeme(currentLine);
                if (lexeme.contentEquals("-")) {
                    currentToken = nextToken();
                    if (currentToken.contentEquals("ID") || currentToken.contentEquals("NC")) {
                        t2 = getLexeme(currentLine);
                        t1 = "-" + t2;
                    } else {
                        reportError("ID / NC", 1);
                    }
                } else {
                    reportError("-", 1);
                }
            }
            default ->
                reportError("ID / NC / -", 1);
        }
        parseTreeTabCount--;
        return t1;
    }

    public String KWintegeridentifierY() {
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifierY");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        if (currentToken.contentEquals("ID") || currentToken.contentEquals("NC")) {
            t2 = getLexeme(currentLine);
            t1 = t2;

        } else {
            reportError("ID / NC", 1);
        }
        parseTreeTabCount--;
        return t1;
    }

    public void KWintegeridentifier() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWintegeridentifier");
        parseTreeTabCount++;
        String t1 = "";
        String t2 = "";
        if (currentToken.contentEquals("ID")) {
            t1 = getLexeme(currentLine);
            currentToken = nextToken();
            t2 = KWintegeridentifierA();
            if (currentToken.contentEquals(";")) {
            } else {
                reportError(";", 1);
            }
        } else {
            reportError("ID", 1);
        }
        writeToTacFile(t1 + t2);
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWcharidentifierA() {
        printParseTreeTabs(parseTreeTabCount, "KWcharidentifierA");
        parseTreeTabCount++;
        switch (currentToken) {
            case "'" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("LC")) {
                    currentToken = nextToken();
                    if (currentToken.contentEquals("'")) {
                    } else {
                        reportError("'", 1);
                    }
                } else {
                    reportError("LC", 1);
                }
            }
            case "ID" -> {
            }
            default ->
                reportError("ID / '", 1);
        }
    }

    public void KWcharidentifier() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWcharidentifier");
        parseTreeTabCount++;
        if (currentToken.contentEquals("ID")) {
            currentToken = nextToken();
            if (currentToken.contentEquals("AsO")) {
                currentToken = nextToken();
                KWcharidentifierA();
                currentToken = nextToken();
                if (currentToken.contentEquals(";")) {
                } else {
                    reportError(";", 1);
                }
            } else {
                reportError("AsO", 1);
            }
        } else {
            reportError("ID", 1);
        }
        parseTreeTabCount--;
    }

//-----------------------------------------------------------------------------
    public void KWinputA() {
        printParseTreeTabs(parseTreeTabCount, "KWinputA");
        parseTreeTabCount++;
        if (currentToken.contentEquals("IO")) {
            currentToken = nextToken();
            if (currentToken.contentEquals("ID")) {
                writeToTacFile("in " + getLexeme(currentLine));
                KWinputB();
            } else {
                reportError("ID", 1);
            }
        } else {
            reportError("IO", 1);
        }
        parseTreeTabCount--;
    }

    public void KWinputB() {
        printParseTreeTabs(parseTreeTabCount, "KWinputB");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("IO")) {
            KWinputA();
        } else {
        }
        parseTreeTabCount--;
    }

    public void KWinput() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWinput");
        parseTreeTabCount++;
        if (currentToken.contentEquals("input")) {
            currentToken = nextToken();
            KWinputA();
            if (currentToken.contentEquals(";")) {
            } else {
                reportError(";", 1);
            }
        } else {
            reportError("input", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public int[] KWwhileA() {
        int[] temp = new int[2];
        printParseTreeTabs(parseTreeTabCount, "KWwhileA");
        parseTreeTabCount++;
        currentToken = nextToken();
        String a = getLexeme(currentLine);
        switch (currentToken) {
            case "ID" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    String b = getLexeme(currentLine);
                    currentToken = nextToken();
                    switch (currentToken) {
                        case "NC" -> {
                            temp[0] = tacFileLineNO;
                            writeToTacFile("if " + a + " " + b + " " + getLexeme(currentLine) + " goto " + (tacFileLineNO + 2));
                            temp[1] = tacFileLineNO;
                            writeToTacFile("goto");
                        }
                        case "ID" -> {
                            temp[0] = tacFileLineNO;
                            writeToTacFile("if " + a + " " + b + " " + getLexeme(currentLine) + " goto " + (tacFileLineNO + 2));
                            temp[1] = tacFileLineNO;
                            writeToTacFile("goto");
                        }
                        default ->
                            reportError("NC / ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            case "NC" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    String b = getLexeme(currentLine);
                    currentToken = nextToken();
                    if (currentToken.contentEquals("ID")) {
                        temp[0] = tacFileLineNO;
                        writeToTacFile("if " + a + " " + b + " " + getLexeme(currentLine) + " goto " + (tacFileLineNO + 2));
                        temp[1] = tacFileLineNO;
                        writeToTacFile("goto");
                    } else {
                        reportError("ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            default ->
                reportError("ID / NC", 1);
        }
        parseTreeTabCount--;
        return temp;
    }

    public void KWwhile() {
        int[] temp;
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWwhile");
        parseTreeTabCount++;
        if (currentToken.contentEquals("while")) {
            temp = KWwhileA();
        } else {
            reportError("while", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals(":")) {
        } else {
            reportError(":", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals("{")) {
            AKW();
            writeToTacFile("goto " + temp[0]);
        } else {
            reportError("{", 1);
            return;
        }
        if (currentToken.contentEquals("}")) {
            backPatch(temp[1], tacFileLineNO);
        } else {
            reportError("}", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWifA() {
        printParseTreeTabs(parseTreeTabCount, "KWifA");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "ID" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    currentToken = nextToken();
                    switch (currentToken) {
                        case "NC" -> {
                        }
                        case "ID" -> {
                        }
                        default ->
                            reportError("NC / ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            case "NC" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    currentToken = nextToken();
                    if (currentToken.contentEquals("ID")) {
                    } else {
                        reportError("ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            default ->
                reportError("ID / NC", 1);
        }
        parseTreeTabCount--;
    }

    public void KWifB() {
        printParseTreeTabs(parseTreeTabCount, "KWifB");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "elif" -> {
                KWelif();
                KWifB();
            }
            case "else" ->
                KWelse();
            default -> {
            }
        }
        parseTreeTabCount--;
    }

    public void KWif() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWif");
        parseTreeTabCount++;
        if (currentToken.contentEquals("if")) {
            KWifA();
        } else {
            reportError("if", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals(":")) {
        } else {
            reportError(":", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals("{")) {
            AKW();
        } else {
            reportError("{", 1);
            return;
        }
        if (currentToken.contentEquals("}")) {
            KWifB();
        } else {
            reportError("}", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWelifA() {
        printParseTreeTabs(parseTreeTabCount, "KWelifA");
        parseTreeTabCount++;
        currentToken = nextToken();
        switch (currentToken) {
            case "ID" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    currentToken = nextToken();
                    switch (currentToken) {
                        case "NC" -> {
                        }
                        case "ID" -> {
                        }
                        default ->
                            reportError("NC / ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            case "NC" -> {
                currentToken = nextToken();
                if (currentToken.contentEquals("RO")) {
                    currentToken = nextToken();
                    if (currentToken.contentEquals("ID")) {
                    } else {
                        reportError("ID", 1);
                    }
                } else {
                    reportError("RO", 1);
                }
            }
            default ->
                reportError("ID / NC", 1);
        }
        parseTreeTabCount--;
    }

    public void KWelif() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWelif");
        parseTreeTabCount++;
        if (currentToken.contentEquals("elif")) {
            KWelifA();
        } else {
            reportError("elif", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals(":")) {
        } else {
            reportError(":", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals("{")) {
            AKW();
        } else {
            reportError("{", 1);
            return;
        }
        if (currentToken.contentEquals("}")) {
        } else {
            reportError("}", 1);
        }
        parseTreeTabCount--;
    }

//-----------------------------------------------------------------------------
    public void KWelse() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "KWelse");
        parseTreeTabCount++;
        if (currentToken.contentEquals("else")) {
        } else {
            reportError("else", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals(":")) {
        } else {
            reportError(":", 1);
            return;
        }
        currentToken = nextToken();
        if (currentToken.contentEquals("{")) {
            AKW();
        } else {
            reportError("{", 1);
            return;
        }
        if (currentToken.contentEquals("}")) {
        } else {
            reportError("}", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWprintA() {
        printParseTreeTabs(parseTreeTabCount, "KWprintA");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.charAt(0) == '"') {
            currentToken = nextToken();
            if (currentToken.contains("String")) {
                currentToken = nextToken();
                if (currentToken.charAt(0) == '"') {
                    KWprintB();
                }
            }
        } else if (currentToken.contentEquals("ID")) {
            KWprintB();
        }

        parseTreeTabCount--;
    }

    public void KWprintB() {
        printParseTreeTabs(parseTreeTabCount, "KWprintB");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("AO")) {
            if (getLexeme(currentLine).contentEquals("+")) {
                KWprintA();
            } else {
                reportError("+", 1);
            }
        } else {
        }
        parseTreeTabCount--;
    }

    public void KWprint() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "print");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("(")) {
            KWprintA();
            if (currentToken.contentEquals(")")) {
                currentToken = nextToken();
                if (currentToken.contentEquals(";")) {
                } else {
                    reportError(";", 1);
                }
            } else {
                reportError(")", 1);
            }
        } else {
            reportError("(", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void KWprintlnA() {
        printParseTreeTabs(parseTreeTabCount, "KWprintlnA");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.charAt(0) == '"') {
            currentToken = nextToken();
            if (currentToken.contains("String")) {
                currentToken = nextToken();
                if (currentToken.charAt(0) == '"') {
                    KWprintlnB();
                }
            }
        } else if (currentToken.contentEquals("ID")) {
            KWprintlnB();
        }
        parseTreeTabCount--;
    }

    public void KWprintlnB() {
        printParseTreeTabs(parseTreeTabCount, "KWprintlnB");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("AO")) {
            if (getLexeme(currentLine).contentEquals("+")) {
                KWprintlnA();
            } else {
                reportError("+", 1);
            }
        } else {
        }
        parseTreeTabCount--;
    }

    public void KWprintln() {
        System.out.println();
        printParseTreeTabs(parseTreeTabCount, "println");
        parseTreeTabCount++;
        currentToken = nextToken();
        if (currentToken.contentEquals("(")) {
            KWprintlnA();
            if (currentToken.contentEquals(")")) {
                currentToken = nextToken();
                if (currentToken.contentEquals(";")) {
                } else {
                    reportError(";", 1);
                }
            } else {
                reportError(")", 1);
            }
        } else {
            reportError("(", 1);
        }
        parseTreeTabCount--;
    }
//-----------------------------------------------------------------------------

    public void AKW() {
        currentToken = nextToken();
        switch (currentToken) {
            case "int" -> {
                KWint();
                AKW();
            }
            case "char" -> {
                KWchar();
                AKW();
            }
            case "if" -> {
                KWif();
                AKW();
            }
            case "elif" -> {
                reportError("\"elif\" without \"if\".", 2);
                KWelif();
                AKW();
            }
            case "else" -> {
                reportError("\"else\" without \"if\".", 2);
                KWelse();
                AKW();
            }
            case "while" -> {
                KWwhile();
                AKW();
            }
            case "input" -> {
                KWinput();
                AKW();
            }
            case "ID" -> {
                switch (checkInSymbolFile(getLexeme(currentLine))) {
                    case "int" ->
                        KWintegeridentifier();
                    case "char" -> {
                        KWcharidentifier();
                    }
                    default ->
                        reportError("'" + getLexeme(currentLine) + "'" + " is not defined.", 2);
                }
                AKW();
            }
            case "print" -> {
                KWprint();
                AKW();
            }
            case "println" -> {
                KWprintln();
                AKW();
            }
            default -> {
            }
        }
    }
//-----------------------------------------------------------------------------

    public void checkSyntax(String readFile) {
        try (FileInputStream fis = new FileInputStream(readFile)) {
            sc = new Scanner(fis);
            for (int i = 0; i < 18; i++) { //Skips initial lines
                sc.nextLine();
            }
            while (sc.hasNextLine()) {
                currentToken = nextToken();
                parseTreeTabCount = 0;
                switch (currentToken) {
                    case "int" -> {
                        KWint();
                    }
                    case "char" -> {
                        KWchar();
                    }
                    case "if" -> {
                        KWif();
                    }
                    case "elif" -> {
                        reportError("\"elif\" without \"if\".", 2);
                        KWelif();
                    }
                    case "else" -> {
                        reportError("\"else\" without \"if\".", 2);
                        KWelse();
                    }
                    case "while" -> {
                        KWwhile();
                    }
                    case "input" -> {
                        KWinput();
                    }
                    case "print" -> {
                        KWprint();
                    }
                    case "println" -> {
                        KWprintln();
                    }
                    case "ID" -> {
                        switch (checkInSymbolFile(getLexeme(currentLine))) {
                            case "int" ->
                                KWintegeridentifier();
                            case "char" -> {
                                KWcharidentifier();
                            }
                            default ->
                                reportError("'" + getLexeme(currentLine) + "'" + " is not defined.", 2);
                        }
                    }
                    default -> {
                        reportError("", 2);
                    }
                }
            }
            sc.close();
            fis.close();
        } catch (IOException e) {
        }
    }
//-----------------------------------------------------------------------------

    public void checkSyntaxAndPrintToFile(String readFile) {
        deleteFiles();
        try (PrintStream file = new PrintStream(new File(parseTreeFile))) {
            System.setOut(file);
            checkSyntax(readFile);
            file.close();
        } catch (IOException e) {
        }
    }
}
