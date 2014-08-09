/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Environment.id.Identificador;
import java.util.Hashtable;
import java.util.Set;
import lexer.Token;
import lexer.Word;

/**
 * Tabela de simbolos.
 *
 * @author Felipe Correa e Guilherme e Alan.
 * @version
 */
public class Ambiente {

    public static Hashtable<Word,Identificador> table;
    protected Ambiente prev;

    /**
     * Construtor.
     *
     * @param prev
     */
    public Ambiente(Ambiente prev) {
        this.table = new Hashtable<Word,Identificador>();
        this.prev = prev;
    }
    
    public Ambiente() {
        this.table = new Hashtable<Word,Identificador>();        
    }

    /**A alterar.
     * Este método insere uma entrada na TS do ambiente A chave da entrada é o
     * Token devolvido pelo analisador léxico Id é uma classe que representa os
     * dados a serem armazenados na TS para identificadores. Comentário
     * retirado do Slide da Prof. Kecia
     * @param id  
     * @param w
     */
    public void put(Word w,Identificador id) {
        table.put(w, id);
        
    }

    /**A alterar.
     * Este método retorna as informações referentes a determinado token. O
     * Token é pesquisado do ambiente atual para os anteriores Comentário
     * retirado do Slide da Prof. Kecia
     *
     * @param w Entrada do token
     * @return Identificador
     */
    public Identificador get(Token w) {
        for (Ambiente e = this; e != null; e = e.prev) {
            Identificador found = (Identificador) e.table.get(w);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
    
    /**
     * Obtem identificador do token nesse Ambiente.
     * @param w
     * @return 
     */

    public Identificador getAmbienteAtual(Token w) {

        Identificador found = (Identificador) this.table.get(w);
        if (found != null) {
            return found;
        }

        return null;
    }
    /**
     * Obtem o token pelo lexema
     * @param lexema
     * @return 
     */
    public static Word getPeloLexema(String lexema){
        for(Word key: table.keySet()){
            if (key.toString().equals(lexema)){
                return key;
            }
        }
        return null;
    }
    /**
     * Obtem um set dos words.
     * @return 
     */
    public static Set<Word> gerarHashMap(){
        return table.keySet();
    }
}
