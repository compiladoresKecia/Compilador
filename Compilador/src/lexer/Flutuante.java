package lexer;

public class Flutuante extends Token {
	
        public final float value;
	
        public Flutuante(float v){
		super(Tag.FLUTUANTE);
		value = v;
	}
}

