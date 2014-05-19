package lexer;

public class Word extends Token{
	
    public String lexeme = "";
            
    //public static final Word and = new Word ("&&", Tag.AND);    
    //public static final Word or = new Word ("||", Tag.OR);
    
    public static final Word eq = new Word ("==", Tag.EQ);
    public static final Word gt = new Word (">", Tag.GT);
    public static final Word ge = new Word (">=", Tag.GE);
    public static final Word le = new Word ("<=", Tag.LE);    
    public static final Word lt = new Word ("<", Tag.LT);    
    public static final Word neq = new Word ("<>", Tag.NEQ);
    public static final Word plus = new Word ("+", Tag.PLUS);
    public static final Word minus = new Word ("-", Tag.MINUS);
    public static final Word mult = new Word ("*", Tag.MULT);
    public static final Word div = new Word ("/", Tag.DIV);
    public static final Word atrib = new Word ("=", Tag.ATRIB);
    public static final Word pt = new Word (".", Tag.PT);
    public static final Word vr = new Word (",", Tag.VR);
    public static final Word pvr = new Word (";", Tag.PVR);
    public static final Word ap = new Word ("(", Tag.AP);
    public static final Word fp = new Word (")", Tag.FP);
    public static final Word as = new Word ("\"", Tag.AS);
       
       
    //public static final Word True = new Word ("true", Tag.TRUE);
    //public static final Word False = new Word ("false", Tag.FALSE);
    /**
     * Construtor do token.
     * @param s
     * @param tag 
     */
        public Word(String s, int tag ){
		super(tag);
		lexeme = new String(s);
	}
        /**
         * Obtem o lexema.
         * @return 
         */
        public String toString(){
            return "" + lexeme;
        }       
}
