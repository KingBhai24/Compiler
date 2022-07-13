/*
    Created By: M.Shehriyar
    Roll NO: L17-4358
    Date: October 23, 2021

    This is main file.
    It calls different 
    classes to compile code.
 */
package Compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Compiler {
    
    // Dont Change Order //
    static ArrayList<String> keyWords = new ArrayList<>(Arrays.asList("int", "char", "if", "elif", "else", "while", "input", "print", "println"));
    static ArrayList<Character> separators = new ArrayList<>(Arrays.asList(' ', '\n', ':', ';', ',', '\'', '"', '+', '-', '*', '/', '<', '>', '=', '(', ')', '[', ']', '{', '}'));
    static ArrayList<String> tag = new ArrayList<>(Arrays.asList("^", "ID", "String", "NC", "LC", "IO", "AsO", "AO", "RO", "Inc/Dec", "Err"));

    public static void main(String[] args) {
        //Token - Lexeme Pairs
        Lexical_Analyzer lex = new Lexical_Analyzer();
        //lex.createTokenAndPrintToConsole("Code.cc");
        lex.createTokenAndPrintToFile("Code.cc");
        
        //Parser
        Parser parser = new Parser();
        parser.checkSyntaxAndPrintToFile("Token_Lexeme.cc");
    }
}
