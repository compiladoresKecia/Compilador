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
                INT    = 269,
                FLOAT  = 270,
                STRING = 271,

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
                ATRIB = 282,    //  =
                PT    = 283,    //  .
                VR    = 284,    //  ,
                PVR   = 285,    //  ;
                AP    = 286,    //  (
                FP    = 287,    //  )
                AS    = 288,    //  " \" " ASPAS
                
                // Outros Tokens
                INTEIRO   = 290,                
                LITERAL   = 291,    // literal, string, texto
                FLUTUANTE = 292,
		//STRING    = 293,    
		ID        = 294,    // identificador
                REL       = 295;    // relacional  
                //TRUE   = 296,    
		//FALSE  = 297,    
		
        
}
