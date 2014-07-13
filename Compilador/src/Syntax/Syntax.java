package Syntax;

import java.io.IOException;
import lexer.Lexer;
import lexer.SyntaxException;
import lexer.Tag;
import lexer.Token;

/**
 * Syntax Criado.
 * 
 * 
 * @author Guilherme e Alan Goncalves
 * @version 0.1 Syntax Finalizado.
 *
 */
public class Syntax {

    /**
     * Lexer para a analise sintatica.
     */
    Lexer lexer;
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
        String erro = "Erro Sintático na linha " + lexer.getN_linha()
                + " próximo ao Token " + token.toString() + "\n";
        System.out.println(erro);
        strBuffer.append(erro);
    }

    /**
     * Tratar erro pelo modo panico.
     *
     * @param tag
     */
    private void tratarErro(int tag) {
        strBuffer.append("Token esperado: " + tag + " \n");      
        System.out.println("LOOOP: Token esperado: " + tag +" Tokens:");
        do{
            
            advance();
            System.out.println(token.getTag());
        }while(token.getTag()!=tag && lexer.arquivoPronto()  &&
                token.getTag()!=Tag.PVR);
        if(token.getTag()==Tag.PVR){
           eat(Tag.PVR); 
        } else if(token.getTag()==tag){
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
            System.out.println("eat " + token);
            advance();
        } else {
            erro(); 
            System.out.println(" Tag a ser comido: "+ tag +"\n");
            tratarErro(tag);
                     
        }
    }

    public void analisar() {
        program();
        lexer.gravarSintatico(strBuffer);
    }

    /**
     * program := START declist body END.
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
                erro();
        }
    }

    /**
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
     * decl := type identList.
     */
    private void decl() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                type();
                identList();
                break;

            default:
                erro();
        }
    }

    /**
     * declListAUX := type declList.
     */
    private void declListAUX() {
        if (token.getTag() == Tag.PVR) {
            eat(Tag.PVR);
            declList();
        }
    }

    /**
     * identList := identifier identListAUX.
     */
    private void identList() {
        switch (token.getTag()) {
            case Tag.ID:
                identifier();
                identListAUX();
                break;

            default:
                erro();
        }
    }

    /**
     * identList := identList | lambda.
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
        stmtList();
    }

    /**
     * stmt := assignStmt | ifStmt | whileStmt | readStmt | writeStmt.
     */
    private void stmt() {
        switch (token.getTag()) {
            case Tag.ID:
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
                eat(Tag.END);
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
            case Tag.FLOAT:
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
            case Tag.FLOAT:
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
            case Tag.FLOAT:
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
            case Tag.FLOAT:
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
            case Tag.FLOAT:
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
            case Tag.FLOAT:
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

            case Tag.FLOAT:
                eat(Tag.FLOAT);
                break;

            default:
                erro();
        }
    }

}
