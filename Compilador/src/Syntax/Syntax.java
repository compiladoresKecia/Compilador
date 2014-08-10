package Syntax;

import Environment.Ambiente;
import java.io.IOException;
import lexer.Lexer;
import Semantics.Semantics;
import java.util.Iterator;
import java.util.Set;
import lexer.SyntaxException;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

/**
 * Syntax Criado.
 *
 *
 * @author Guilherme e Alan Goncalves
 * @version 0.1 Syntax Finalizado.
 * @version 0.1 Semantics Unicidade feito
 *
 */

/*
Gramatica 

1  program ::= start  [decl-list] stmt-list  exit
2  decl-list ::= decl {decl} 
3  decl ::= type ident-list ";" 
4  ident-list ::= identifier {"," identListAUX }
5  identListAUX := identList | λ.
6  type ::= int | float  | string
7  stmt-list ::= stmt { stmtListAUX }
8  stmtListAUX := stmt | λ.
9  stmt ::= assign-stmt ";" | if-stmt | while-stmt 
10 | read-stmt ";" | write-stmt ";" 
11 assign-stmt ::= identifier "=" simple_expr
12 if-stmt ::=  if  condition  then  stmt-list else-stmt
13 else-stmt ::= end | stmt-list  end
14 condition ::= expression
15 while-stmt ::= do stmt-list stmt-sufix
16 stmt-sufix ::= while condition end
17 read-stmt ::= scan  "(" identifier ")"
18 write-stmt ::= print  "(" writable ")"
19 writable ::= simple-expr | literal
20 expression1 ::= simple-expr | expression
21 expression ::= relop simple-expr | λ
22 simple-expr1 ::= term | simple-expr 
23 simple-expr ::= addop term | λ
24 term1 ::= factor-a | term 
25 term ::= mulop factor-a | λ
26 fator-a ::= factor |  not  factor | "-" factor
27 factor ::= identifier | constant | "(" expression ")"
28 relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
29 addop ::= "+" | "-" | or
30 mulop ::= "*" | "/" | and
31 constant ::= integer_const | float_const | literal
32 integer_const   ::= digit {digit}
33 float_const ::= digit{digit} “.”digit{digit}
34 literal ::= " “" {caractere} "”"
35 identifier ::= letter {letter | digit }
36 letter ::= [A-za-z]
37 digit ::= [0-9]
38 caractere ::= um dos caracteres ASCII, exceto “” e quebra de linha
 
 */




public class Syntax {

    /**
     * Lexer para a analise sintatica.
     */
    Lexer lexer;
    
    /**
     * Semantics para a analise semantica.
     */
    Semantics semantic;
    
    /*
     * tipo da variavel para analise semantica    
     */
    private int tipo;
    
    /*
     * Word (lexema) atual para analise semantica
     */    
    Word current;
    
    /**
     * Token carregado pelo lexer.
     */
    Token token = null;
    /**
     * COMENTARIO.
     */
    private final static String COMENTARIO = "COMENTARIO";
    /**
     * LITERAL.
     */
    private final static String LITERAL = "LITERAL";
    /**
     * StringBuffer relativo a gravacao.
     */
    private StringBuffer strBuffer;
    
    private boolean DeuErro=false;
    
    private int linha ;



    /**
     * Construtor do sintatico.
     *
     * @param lexer
     */
    public Syntax(Lexer lexer) {
        this.strBuffer = new StringBuffer();
        this.lexer = lexer;
        try {
            token = lexer.scan();
            current = (Word)token;            
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
    }

    /**
     * Gera o proximo token.
     */
    private void advance() {
        try {
            token = lexer.scan();
            //linha = lexer.getN_linha();
            //System.out.println(linha+token.toString());            
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();

            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao ler o arquivo.");

        }
    }

    /**
     * Gera o erro.
     */
    private void erro() {
        linha = lexer.getN_linha();
        DeuErro=true;
        /*String erro = "Erro Sintático na linha " + lexer.getN_linha()
                + " próximo ao Token " + token.toString() + "\n";*/
        String erro = "Erro Sintático na linha " + linha
                + " próximo ao Token " + token.toString() + "\n";
        System.out.println(erro);
        strBuffer.append(erro);
    }
    /**
     * Informacoes de erro.
     */
    private void erroInfos(int tag){
        erro();
        System.out.println(" Tag a ser comido: " + tag + "\n");
        strBuffer.append("Token esperado: " + tag + " \n");        
    }

    /**
     * Tratar erro pelo modo panico.
     *
     * @param tag
     */
    private void tratarErro(int tag) {
        erroInfos(tag);
        System.out.println("LOOOP: Token esperado: " + tag + " Tokens:");
        do {
            advance();
            System.out.println(token.getTag());
        } while (token.getTag() != tag && lexer.arquivoPronto()
                && token.getTag() != Tag.PVR);
        if (token.getTag() == Tag.PVR) {
            eat(Tag.PVR);
        } else if (token.getTag() == tag) {
            eat(tag);
        }

    }

    /**
     * Elimina o tag e := um novo. Se estiver incorreto gera erro.
     *
     * @param tag
     */
    private void eat(int tag) {
        if (token.getTag() == tag) {
            //System.out.println("eat " + token);
            advance();
        } else {
            tratarErro(tag);
        }
    }

    //Recuperar linha do token
    public int getLinha() {
        return linha;
    }
        
    
    public void analisar() {
        program();
        
        if (!DeuErro){
            System.out.println("\n\n- Nenhum erro foi encontrado.\n" +"Analise Sintatica realizada com sucesso!! -\n\n");
            String msg = "\n\n- Nenhum erro foi encontrado.\n" +"\nAnalise Sintatica realizada com sucesso!! -\n\n";
            strBuffer = new StringBuffer(msg); 
        }
        lexer.gravarSintatico(strBuffer);
        
        /*
         * 
         * Mostrar tabela de Simbolos
         * 
         */
        
        Ambiente.MostrarTabelaSimbolos();
        
    }

    /**
     * 1  program ::= start  [decl-list] stmt-list  exit
     */
    private void program() {
        switch (token.getTag()) {
            case Tag.START:
                eat(Tag.START);
                declList();
                stmtList();
                eat(Tag.EXIT);                
                break;

            default:
                erroInfos(Tag.START);
                declList();
                stmtList();
                eat(Tag.EXIT);
                break;
        }
    }

    /**
     * 2  decl-list ::= decl {decl} 
     * declList := decl declListAUX.
     */
    private void declList() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                decl();
                declListAUX();
                break;
        }
    }

    /**
     * declListAUX := type declList.
     */
    private void declListAUX() {
        eat(Tag.PVR);
        declList();
    }  
    
    
    /**
     * 3  decl ::= type ident-list ";" 
     */
    private void decl() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                tipo = token.getTag();
                type();                
                identList();
                break;

            default:
                erro();
        }
    }

    
    /**
     * 4  ident-list ::= identifier {"," identListAUX }
     */
    private void identList() {
        switch (token.getTag()) {
            case Tag.ID: 
                semantic = new Semantics(current,tipo,token.toString());
                semantic.Type();
                identifier();
                identListAUX();
                break;

            default:
                erro();
        }
    }

    /**
     * 5  identListAUX := identList | λ.
     */
    private void identListAUX() {

        if (token.getTag() == Tag.VR) {
            eat(Tag.VR);
            identList();
        }
    }

    /**
     * type := "int" | "char".
     */
    private void type() {
        switch (token.getTag()) {
            case Tag.INT:
                eat(Tag.INT);
                break;

            case Tag.STRING:
                eat(Tag.STRING);
                break;

            case Tag.FLOAT:
                eat(Tag.FLOAT);
                break;

            default:
                erro();
        }
    }

    /**
     * identifier := id.
     */
    private void identifier() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                break;

            default:
                erro();
        }
    }

    /**
     * stmtList := stmt stmtListAUX.
     */
    private void stmtList() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.SCAN:
            case Tag.PRINT:
                stmt();
                eat(Tag.PVR);
                stmtListAUX();
                break;
            case Tag.IF:
            case Tag.DO:
                stmt();
                stmtListAUX();
                break;
            case Tag.EXIT:
                break;
            default:
                erro();
        }
    }

    /**
     * stmtListAUX := stmt stmtList.
     */
    private void stmtListAUX() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.SCAN:
            case Tag.PRINT:
                stmt();
                eat(Tag.PVR);
                stmtListAUX();
                break;
            case Tag.IF:
            case Tag.DO:
                stmt();
                stmtListAUX();
                break;
            case Tag.EXIT:
                break;
        }
    }

    /**
     * stmt := assignStmt | ifStmt | whileStmt | readStmt | writeStmt.
     */
    private void stmt() {
        switch (token.getTag()) {
            case Tag.ID:
                semantic.Absence();
                assignStmt();
                break;

            case Tag.IF:
                ifStmt();
                break;

            case Tag.DO:
                whileStmt();
                break;

            case Tag.SCAN:
                readStmt();
                break;

            case Tag.PRINT:
                writeStmt();
                break;

            case Tag.EXIT:
                break;

            default:
                erro();
        }
    }

    /**
     * assignStmt := identifier = simpleExpr.
     */
    private void assignStmt() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                eat(Tag.ATRIB);
                simpleExpr1();
                break;

            default:
                erro();
        }
    }

    /**
     * ifStmt := if condition stmtList elseStmt.
     */
    private void ifStmt() {
        switch (token.getTag()) {
            case Tag.IF:
                eat(Tag.IF);
                condition();
                eat(Tag.THEN);
                stmtList();
                elseStmt();
                break;

            default:
                erro();
        }
    }

    /**
     * elseStmt := stmtList | lambda.
     */
    private void elseStmt() {
        if (token.getTag() == Tag.ELSE) {
            eat(Tag.ELSE);
            stmtList();
        }
        eat(Tag.END);
    }

    /**
     * condition := expression.
     */
    private void condition() {
        expression1();
    }

    /**
     * whileStmt := stmtList1 stmtSufix.
     */
    private void whileStmt() {
        switch (token.getTag()) {
            case Tag.DO:
                eat(Tag.DO);
                stmtList();
                stmtSufix();
                break;

            default:
                erro();
        }
    }

    /**
     * stmtSufix := condition.
     */
    private void stmtSufix() {
        switch (token.getTag()) {
            case Tag.WHILE:
                eat(Tag.WHILE);
                condition();
                eat(Tag.END);
                break;

            default:
                erro();
        }
    }

    /**
     * readStmt := identifier.
     */
    private void readStmt() {
        switch (token.getTag()) {
            case Tag.SCAN:
                eat(Tag.SCAN);
                eat(Tag.AP);
                identifier();
                eat(Tag.FP);
                break;

            default:
                erro();
        }
    }

    /**
     * writeStmt := writable.
     */
    private void writeStmt() {
        switch (token.getTag()) {
            case Tag.PRINT:
                eat(Tag.PRINT);
                eat(Tag.AP);
                writable();
                eat(Tag.FP);
                break;

            default:
                erro();
        }
    }

    /**
     * writable := simpleExpr1 | literal.
     */
    private void writable() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
                semantic.Absence();
                simpleExpr1();
                break;

            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;

            default:
                erro();
        }
    }

    /**
     * expression1 := simpleExpr1 expression.
     */
    private void expression1() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.LITERAL:
            case Tag.AP:
            case Tag.NOT:
            case Tag.MINUS:
                simpleExpr1();
                expression();
                break;

            default:
                erro();
        }
    }

    /**
     * expression1 := relop simpleExpr1 | lambda.
     */
    private void expression() {
        if (token.getTag() == Tag.EQ || token.getTag() == Tag.GT || token.getTag() == Tag.GE
                || token.getTag() == Tag.LT || token.getTag() == Tag.LE || token.getTag() == Tag.NEQ) {
            relop();
            simpleExpr1();
        }
    }

    /**
     * simpleExpr1 := term1 simpleExpr.
     */
    private void simpleExpr1() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.LITERAL:
            case Tag.AP:
            case Tag.NOT:
            case Tag.MINUS:
                term1();
                simpleExpr();
                break;

            default:
                erro();
        }
    }

    /**
     * simpleExpr := + | - | or | lambda.
     */
    private void simpleExpr() {
        if (token.getTag() == Tag.PLUS || token.getTag() == Tag.MINUS || token.getTag() == Tag.OR) {
            addop();
            term1();
            simpleExpr();
        }
    }

    /**
     * term1 := factorA term.
     */
    private void term1() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.LITERAL:
            case Tag.AP:
            case Tag.NOT:
            case Tag.MINUS:
                factorA();
                term();
                break;

            default:
                erro();
        }
    }

    /**
     * term := mulop factorA term | lambda.
     */
    private void term() {
        if (token.getTag() == Tag.MULT || token.getTag() == Tag.DIV || token.getTag() == Tag.AND) {
            mulop();
            factorA();
            term();
        }
    }

    /**
     * factorA := factor | not factor | - factor.
     */
    private void factorA() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.LITERAL:
            case Tag.AP:
                factor();
                break;

            case Tag.NOT:
                eat(Tag.NOT);
                factor();
                break;

            case Tag.MINUS:
                eat(Tag.MINUS);
                factor();
                break;

            default:
                erro();
        }
    }

    /**
     * factor := identifier | constant | ( expression1 ).
     */
    private void factor() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                break;

            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.LITERAL:
                constant();
                break;

            case Tag.AP:
                eat(Tag.AP);
                expression1();
                eat(Tag.FP);
                break;

            default:
                erro();
        }
    }

    /**
     * relop := == | < | <= | > | >= | <>.
     */
    private void relop() {
        switch (token.getTag()) {
            case Tag.EQ:
                eat(Tag.EQ);
                break;

            case Tag.GT:
                eat(Tag.GT);
                break;

            case Tag.GE:
                eat(Tag.GE);
                break;

            case Tag.LT:
                eat(Tag.LT);
                break;

            case Tag.LE:
                eat(Tag.LE);
                break;

            case Tag.NEQ:
                eat(Tag.NEQ);
                break;

            default:
                erro();
        }
    }

    /**
     * addop := + | - | or | lambda.
     */
    private void addop() {
        switch (token.getTag()) {
            case Tag.PLUS:
                eat(Tag.PLUS);
                break;

            case Tag.MINUS:
                eat(Tag.MINUS);
                break;

            case Tag.OR:
                eat(Tag.OR);
                break;

            default:
                erro();
        }
    }

    /**
     * mulop := * | / | and.
     */
    private void mulop() {
        switch (token.getTag()) {
            case Tag.MULT:
                eat(Tag.MULT);
                break;

            case Tag.DIV:
                eat(Tag.DIV);
                break;

            case Tag.AND:
                eat(Tag.AND);
                break;

            default:
                erro();
        }
    }

    /**
     * constant := intConst | charConst.
     */
    private void constant() {
        switch (token.getTag()) {
            case Tag.INTEIRO:
                eat(Tag.INTEIRO);
                break;

            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;

            case Tag.FLUTUANTE:
                eat(Tag.FLUTUANTE);
                break;

            default:
                erro();
        }
    }

}