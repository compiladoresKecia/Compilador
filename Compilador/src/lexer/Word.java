package lexer;

public class Word extends Token{
	
    public final String lexeme = "";
    
    

    
    public static final Word and = new Word ("&&", Tag.AND);    
    public static final Word or = new Word ("||", Tag.OR);
    
    public static final Word eq = new Word ("==", Tag.EQ);
    public static final Word gt = new Word (">", Tag.GE);
    public static final Word ge = new Word (">=", Tag.GE);
    public static final Word le = new Word ("<=", Tag.LE);    
    public static final Word lt = new Word ("<", Tag.LT);    
    public static final Word ne = new Word ("<>", Tag.NEQ);
    public static final Word plus = new Word ("+", Tag.PLUS);
    public static final Word minus = new Word ("-", Tag.MINUS);
    public static final Word mult = new Word ("*", Tag.MULT);
    public static final Word div = new Word ("-", Tag.DIV);
       
       
    public static final Word True = new Word ("true", Tag.TRUE);
    public static final Word False = new Word ("false", Tag.FALSE);
    
        public Word(String s, int tag ){
		super(tag);
		//lexeme = new String(s);
	}
        
        public String toString(){
            return "" + lexeme;
        }       
}
/*
 *  START  = 256,
                EXIT   = 257, 
                INT    = 258,
                FLOAT  = 259,
                STRING = 260,
                IF     = 261,
                THEN   = 262,
                ELSE   = 263,
                END    = 264,
                DO     = 265,
                WHILE  = 266,
                SCAN   = 267,
                PRINT  = 268,
                NOT    = 269,
                OR     = 270,
                AND    = 271,
                //Operadores e pontuação
                EQ    = 274,    //  ==
                GT    = 275,    //  >
                GE    = 276,    //  >=
                LE    = 277,    //  <=
                LT    = 278,    //  <
                NEQ   = 279,    //  <>
                PLUS  = 280,    //  +
                MINUS = 281,    //  -
                MULT  = 282,    //  *
                DIV   = 283,    //  /
                
                
                // Outros Tokens 
                NUM   = 286,
		ID    = 287,
		TRUE  = 288,
		FALSE = 289,
		REL   = 290;
 
 
 */