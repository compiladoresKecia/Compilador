/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantics;

import Environment.Ambiente;
import Environment.id.Identificador;
import java.util.Iterator;
import java.util.Set;
import lexer.*;
import Syntax.Syntax;

/**
 *
 * @author Alan e Guilherme Gonçalves
 */
public class Semantics {

    private Word word,word1;
    private int tipo;
    private String _tipo,lexema;
    private static Identificador idVazio;
    private int linha;
    
    public Semantics(Word word, int tipo, String lexema) {
        
        this.word = word;
        this.tipo = tipo;
        this.lexema = lexema; 
        this.idVazio = new Identificador(0);
       // Ambiente ambiente = new Ambiente();
    
    }
    
    
    /**
     * Classe responsavel por fazer a verificacao da consistencia semantica do programa fonte 
     * 
     * Verificar:
     * 
     * Tipo
     * Classe (variavel,procedimento,funcao)
     * Unicidade / ausencia
     * 
     */
    
    /**
     * Verifica Tipo
     */
    public void Type(){
        
        //Verifica se na tabela o identificador já possui tipo
        //Se ainda nao, coloca o tipo do identificador
        
        word1 = (Word)Ambiente.getPeloLexema(lexema);
        
        if (word1.getType() == null){            
            switch(tipo){
                case 269: _tipo = "INT"; break;
                case 270: _tipo = "FLOAT"; break;
                case 271: _tipo = "STRING"; break;
                default: System.out.println("Erro! Tipo nao encontrado");
            }           
        }  
        else{
            
            System.out.println("-> Tipo ja cadastrado!"+"\n** Identificador: "+word1.toString());
            System.out.println("**  Tipo = "+word1.getType()+"\n");
            System.exit(0);
            
        }
        
        word1.setType(_tipo);
        Ambiente.table.remove(word);
        Ambiente.table.put(word1, idVazio);
        //System.out.println("\n\nTIPO CADASTRADO DA VARIAVEL ["+lexema+"] == "+ Ambiente.getPeloLexema(lexema).getType()+"\n\n");  //Debug
         
    }
    
    /**
     *  Verifica a classe
     */    
    public void Class(){
        
    }
    
    
    /**
     * Verifica a ausencia
     */
    public void Absence(){
        
        //Testa se a variavel foi usada sem ser declarada    
        
        linha = Lexer.getLinhaID();
        Set<Word> words = Ambiente.gerarHashMap();            
        Iterator<Word> iterator = words.iterator();        
        while (iterator.hasNext()) {            
            Word palavra = iterator.next();
            if ((palavra.getType() == null)&&(palavra.getTag() == Tag.ID)) {
                System.out.println("\n\nErro semantico! ");
                System.out.println("\nVariavel [" +palavra.lexeme+"] foi utilizada mas NAO foi declarada."+"\nLinha: "+linha+"\n\n");
                System.exit(0);
            }
        }
    }
        
    
    //Verifica unicidade
    public void Unity(){
        
        //Se o lexema do indentificador existir a variavel jah 
        //foi declarada
        
        linha = Lexer.getLinhaID();
        
        if (Ambiente.table.containsValue(lexema)){
            System.out.println("Erro semantico! Variavel [ "+lexema+" ]ja foi declarada."+"\nLinha: "+linha);
            System.exit(0);
        }        
        
    } 
    
    
}
