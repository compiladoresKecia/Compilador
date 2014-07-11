package Syntax;

import java.io.IOException;
import lexer.Lexer;
import lexer.SyntaxException;
import lexer.Tag;
import lexer.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author wolvery
 */
public class Syntax {

    Lexer lexer;
    Token token = null;
    private final static String COMENTARIO = "COMENTARIO";
    private final static String LITERAL = "LITERAL";

    public Syntax(Lexer lexer) {
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

    private boolean match(int tag) {
        if (token.getTag() == tag) {
            return true;
        }
        return false;
    }

    private void eatWithMatch(int tag) {
        if (match(tag)) {
            eat(tag);
        } else {
            advance();
            erro();
        }
    }

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

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
    }

    private void erro() {
        try {
            System.out.println("Erro Sintático na linha " + lexer.scan() + " próximo ao Token " + token.toString());
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
        System.exit(0);

    }

    private void eat(int tag) {
        if (token.getTag() == tag) {
            System.out.println("eat " + token);
            advance();
        } else {
            erro();
        }
    }

    private void analisar() {
        program();
    }

    // program -> "program" identifier body
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

    // declList1 -> decl declList
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

    // decl -> type identList1
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

    private void declListAUX() {
        if (token.getTag() == Tag.PVR) {
            eat(Tag.PVR);
            declList();
        }
    }

    // identList1 -> identifier identList
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

    // identList -> , identifier identList | lambda
    private void identListAUX() {
        if (token.getTag() == Tag.VR) {
            eat(Tag.VR);
            identifier();
            identList();
        }
    }

    // type -> "int" | "char"
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

    // identifier -> id
    private void identifier() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                break;

            default:
                erro();
        }
    }

    // stmtList1 -> stmt stmtList
    private void stmtList() {
        switch (token.getTag()) {
            case Tag.ID:
            case Tag.SCAN:
            case Tag.PRINT:
                stmt();
                eatWithMatch(Tag.PVR);
                stmtListAUX();
                break;
            case Tag.IF:
            case Tag.WHILE:
                stmt();
                stmtListAUX();
                break;
            case Tag.EXIT:
                break;
            default:
                erro();
        }
    }

    private void stmtListAUX() {
        stmtList();
    }

    // stmt -> assignStmt | ifStmt | whileStmt | readStmt | writeStmt
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

    // assignStmt -> identifier = simpleExpr
    private void assignStmt() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                eatWithMatch(Tag.EQ);
                simpleExpr1();
                break;

            default:
                erro();
        }
    }

    // ifStmt1 -> if ( condition ) { stmtList1 } ifStmt
    private void ifStmt() {
        switch (token.getTag()) {
            case Tag.IF:
                eat(Tag.IF);
                condition();
                eatWithMatch(Tag.THEN);
                stmtList();
                elseStmt();
                eatWithMatch(Tag.END);
                break;

            default:
                erro();
        }
    }

    // ifStmt -> else { stmtList1 } | lambda
    private void elseStmt() {
        if (token.getTag() == Tag.ELSE) {
            eat(Tag.ELSE);
            stmtList();
        }
    }

    // condition -> expression
    private void condition() {
        expression1();
    }

    // whileStmt -> stmtPrefix { stmtList1 }
    private void whileStmt() {
        switch (token.getTag()) {
            case Tag.DO:
                stmtList();
                stmtSufix();
                break;

            default:
                erro();
        }
    }

    // stmtPrefix -> while ( condition )
    private void stmtSufix() {
        switch (token.getTag()) {
            case Tag.WHILE:
                eat(Tag.WHILE);
                condition();
                eatWithMatch(Tag.END);
                break;

            default:
                erro();
        }
    }

    // readStmt -> read identifier
    private void readStmt() {
        switch (token.getTag()) {
            case Tag.SCAN:
                eat(Tag.SCAN);
                eatWithMatch(Tag.AP);
                identifier();
                eatWithMatch(Tag.FP);
                break;

            default:
                erro();
        }
    }

    // writeStmt -> write writable
    private void writeStmt() {
        switch (token.getTag()) {
            case Tag.PRINT:
                eat(Tag.PRINT);
                eatWithMatch(Tag.AP);
                writable();
                eatWithMatch(Tag.FP);
                break;

            default:
                erro();
        }
    }
    
    // writable -> simpleExpr1 | literal
    private void writable() 
    {
        switch(token.getTag())
        {
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
    
    // expression1 -> simpleExpr1 expression
    private void expression1() 
    {
        switch(token.getTag())
        {
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
    
    // expression -> relop simpleExpr1 | lambda
    private void expression() 
    {
        if(token.getTag() == Tag.EQ || token.getTag() == Tag.GT || token.getTag() == Tag.GE ||
        token.getTag() == Tag.LT || token.getTag() == Tag.LE || token.getTag() == Tag.NEQ)
        {
            relop();
            simpleExpr1();
        }
    }
    
    // simpleExpr1 -> term1 simpleExpr
    private void simpleExpr1() 
    {
        switch(token.getTag())
        {
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
    
    // simpleExpr -> + | - | or | lambda
    private void simpleExpr() 
    {
        if(token.getTag() == Tag.PLUS || token.getTag() == Tag.MINUS || token.getTag() == Tag.OR)
        {
            addop();
            term1();
            simpleExpr();
        }
    }
    
    // term1 -> factorA term
    private void term1() 
    {
        switch(token.getTag())
        {
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
    
    // term -> mulop factorA term | lambda
    private void term() 
    {
        if(token.getTag() == Tag.MULT || token.getTag() == Tag.DIV || token.getTag() == Tag.AND)
        {
            mulop();
            factorA();
            term();
        }
    }
    
    // factorA -> factor | not factor | - factor
    private void factorA() 
    {
        switch(token.getTag())
        {
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
    
    // factor -> identifier | constant | ( expression1 )
    private void factor() 
    {
        switch(token.getTag())
        {
            case Tag.ID:
                eat(Tag.ID);
                break;
            
            case Tag.INTEIRO:
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
    
    private void relop() 
    {
        switch(token.getTag())
        {
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
    
    private void addop() 
    {
        switch(token.getTag())
        {
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
    
    // mulop -> * | / | and
    private void mulop() 
    {
        switch(token.getTag())
        {
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
    
    // constant -> intConst | charConst
    private void constant() 
    {
        switch(token.getTag())
        {       
            case Tag.INTEIRO:
                eat(Tag.INTEIRO);
                break;
                
            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;
                
            default:
                erro();
        }
    }

}
