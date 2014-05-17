package lexer;

public class Tag {
	
        public final static int
		
                //Palavras reservadas 
                
                START  = 256,
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
        
}
