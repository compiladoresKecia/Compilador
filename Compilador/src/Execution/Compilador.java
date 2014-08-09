package Execution;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Syntax.Syntax;
import carregarArquivo.BaseTXT;
import java.io.IOException;
import lexer.Lexer;
import lexer.SyntaxException;


/**
 *
 * @author wolvery
 */

public class Compilador {
   /**
    * Execucao principal.
    * @param args
    * @throws IOException
    * @throws SyntaxException 
    */
    public static void main(String[] args) throws IOException, SyntaxException {  
        BaseTXT baseTXT =  new BaseTXT();
        Syntax syntax;
        if (baseTXT.arquivoLidoPronto()){
            Lexer lexer = new Lexer(baseTXT);            
            //lexer.analiseLexica();
            //Reload arquivo e abre um novo lexer.
            baseTXT.reload();
            lexer = new Lexer(baseTXT);
            syntax = new Syntax(lexer);
            syntax.analisar();
            System.out.println(
                    "\n *** Compilação realizada com sucesso! *** \n");  
        }else{
            System.out.println("Execucao não é possivel.");
        }
        
       
        
        
    }

   

 
}
