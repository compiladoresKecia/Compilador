/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Environment;

import Environment.id.Identificador;
import java.util.Hashtable;
import lexer.Token;

/**
 *Tabela de simbolos.
 * 
 * @author Felipe Correa e Guilherme e Alan.
 * @version 
 */
public class Ambiente {
    private Hashtable table;
    protected Ambiente prev;

    public Ambiente( Ambiente prev) {
        this.table = new Hashtable();
        this.prev = prev;
    }
    public void put(Token w, Identificador i){
        table.put(w,i);
    }
    
    public Identificador get(Token w){
        for (Ambiente e = this; e!=null; e = e.prev){
            Identificador found = (Identificador) e.table.get(w);
            if (found != null)
                return found;     
        }
        return null; 
    }
}
