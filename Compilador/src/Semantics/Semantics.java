/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantics;

import Environment.Ambiente;
import Environment.id.Identificador;
import lexer.*;

/**
 *
 * @author Alan e Guilherme Gonçalves
 */
public class Semantics {

    private Word word,word1;
    private int tipo;
    private String _tipo,lexema;
    private static Identificador idVazio;
    
    public Semantics(Word word, int tipo, String lexema) {
        
        this.word = word;
        this.tipo = tipo;
        this.lexema = lexema; 
        this.idVazio = new Identificador(0);
    
    }
    
    
    /*
     * Classe responsavel por fazer a verificacao da consistencia semantica do programa fonte 
     * 
     * Verificar:
     * 
     * Tipo
     * Classe (variavel,procedimento,funcao)
     * Unicidade
     * 
     */
    
    //Verifica Tipo
    public void Type(){
        //Verifica se na tabela o identificador já possui tipo
        //Se ainda nao, coloca o tipo do identificador
        
        word1 = (Word)Ambiente.getPeloLexema(lexema);
        
        if (word.getType() == null){
            
            switch(tipo){
                case 290: _tipo = "INT"; break;
                case 291: _tipo = "STRING"; break;
                case 292: _tipo = "FLOAT"; break;
                default: System.out.println("Erro! Tipo nao encontrado");
            }           
            
            word.setType(_tipo);
            Ambiente.table.remove(word1);
            Ambiente.table.put(word, idVazio);            
            
        }  
        else{
            System.out.println("Tipo jah cadastrado!"+"Token: "+word1.toString());
        }
    
    }
    
    //Verifica a classe
    public void Class(){
        
    }
    
    //Verifica unicidade
    public void Unity(){
        
        //Se o lexema do indentificador existir a variavel jah 
        //foi declarada
        if (Ambiente.table.containsValue(lexema)){
            System.out.println("Erro semantico! Variavel [ "+lexema+" ]ja foi declarada.");
        } 
           
    }
    
    
    
    
    
    
    
}
