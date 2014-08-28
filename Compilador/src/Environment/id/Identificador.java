/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Environment.id;

/**
 * Classe de identificador.
 * @author Felipe Correa e Guilherme e Alan.
 */
public class Identificador {
    //Identificacao do Identificador
    private int offset;
    /**
     * Construtor.
     * @param identificacao 
     */
    public Identificador(int identificacao) {
        this.offset = identificacao;
    }
    /**
     * Get Offset.
     * @return 
     */
    public int getOffset() {
        return offset;
    }
    
}
