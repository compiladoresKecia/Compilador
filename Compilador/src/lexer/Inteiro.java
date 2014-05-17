/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;



/**
 *
 * @author Alan
 */
public class Inteiro extends Token {
    
    public final int valor;
    
    public Inteiro(int v){
        super(Tag.INTEIRO);
        valor = v;
    }
    
    public String toString(){
        return "" + valor;
    }

}
