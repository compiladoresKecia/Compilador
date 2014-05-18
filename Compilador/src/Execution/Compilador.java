package Execution;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lexer.Lexer;
import lexer.SyntaxException;

/**
 *
 * @author wolvery
 */

public class Compilador {
   
    public static void main(String[] args) throws IOException, SyntaxException {  
    
             
        String arquivo = "";
                
        Lexer lerArquivo = new Lexer();
        
       
        
        FileReader arquivoASerCompilado = 
        
        Lexer analisadorLexico = new Lexer(arquivo);
        
        analisadorLexico.analiseLexica();
                
        System.out.println("\n *** Compilação realizada com sucesso! *** \n");  
        
        
    }

   

 
}
