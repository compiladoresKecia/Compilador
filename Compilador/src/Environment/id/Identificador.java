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
    private final int identificacao;
    /**
     * Construtor.
     * @param identificacao 
     */
    public Identificador(int identificacao) {
        this.identificacao = identificacao;
    }
    /**
     * Get Identificacao.
     * @return 
     */
    public int getIdentificacao() {
        return identificacao;
    }
    
}
