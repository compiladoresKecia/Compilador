/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generator;

import lexer.Token;

/**
 *
 * @author Guilherme
 */
public class TreeAdressLine {
    /**
     * Operador do codigo de 3 enderecos.
     */
    private int operator;
    /**
     * Argumentos.
     */
    private Token argument1,argument2;
    /**
     * Resultado do codigo.
     */
    private String result;
    /**
     * Construtor.
     * @param operator
     * @param argument1
     * @param argument2
     * @param result 
     */
    public TreeAdressLine(int operator, Token argument1, Token argument2, String result) {
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.result = result;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public Token getArgument1() {
        return argument1;
    }

    public void setArgument1(Token argument1) {
        this.argument1 = argument1;
    }

    public Token getArgument2() {
        return argument2;
    }

    public void setArgument2(Token argument2) {
        this.argument2 = argument2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
}
