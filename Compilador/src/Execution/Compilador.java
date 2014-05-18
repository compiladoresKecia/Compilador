package Execution;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import carregarArquivo.BaseTXT;
import java.io.IOException;
import lexer.Lexer;
import lexer.SyntaxException;


/**
 *
 * @author wolvery
 */

public class Compilador {
   
    public static void main(String[] args) throws IOException, SyntaxException {  
        BaseTXT baseTXT =  new BaseTXT();
        if (baseTXT.arquivoLidoPronto()){
            Lexer lexer = new Lexer(baseTXT);
            lexer.analiseLexica();
            System.out.println(
                    "\n *** Compilação realizada com sucesso! *** \n");  
        }else{
            System.out.println("Execucao não é possivel.");
        }
        
       
        
        
    }

   

 
}
