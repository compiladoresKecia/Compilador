package Execution;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Desktop;
import java.util.Scanner;


//Abrir arquivo
import java.io.*;
import java.io.File;  
import java.util.List;
import javax.swing.JFileChooser;


import java.util.Map;
import lexer.Lexer;
import lexer.SyntaxException;


/**
 *
 * @author wolvery
 */

public class compilador {
   
    public static void main(String[] args) throws IOException, SyntaxException {  
    
        Scanner in = new Scanner(System.in);
        
        String arquivo = "";
                
        Lexer lerArquivo = new Lexer();
        
        arquivo = lerArquivo.abrirArquivo();
        
        Lexer analisadorLexico = new Lexer(arquivo);
        
        analisadorLexico.analiseLexica();
                
        System.out.println("\n *** Compilação realizada com sucesso! *** \n");  
        
        
    }

   

 
}
