package lexer;

public class Tag {
	
        public final static int
		
                //Palavras reservadas 
                
                START  = 256,
                EXIT   = 257,                 
                IF     = 258,
                THEN   = 259,
                ELSE   = 260,
                END    = 261,
                DO     = 262,
                WHILE  = 263,
                SCAN   = 264,
                PRINT  = 265,
                NOT    = 266,
                OR     = 267,
                AND    = 268,

                //Operadores e pontuação
                
                EQ    = 272,    //  ==
                GT    = 273,    //  >
                GE    = 274,    //  >=
                LE    = 275,    //  <=
                LT    = 276,    //  <
                NEQ   = 277,    //  <>
                PLUS  = 278,    //  +
                MINUS = 279,    //  -
                MULT  = 280,    //  *
                DIV   = 281,    //  /
                
                
                // Outros Tokens
                INTEIRO   = 285,                
                LITERAL   = 286,
                FLUTUANTE = 287,
		ID        = 288,
		REL       = 289;    // relacional  
                //TRUE   = 290,    
		//FALSE  = 291,    
		
        
}
