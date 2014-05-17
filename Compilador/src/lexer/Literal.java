/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;


public class Literal extends Token {
	
        public final Literal palavra;
	
        public Literal(Literal p){
		super(Tag.LITERAL);
		palavra = p;
	}
}


