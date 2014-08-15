package Syntax;

import Environment.Ambiente;
import Semantics.Semantics;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import lexer.Lexer;
import lexer.SyntaxException;
import lexer.Token;
import lexer.Word;

public class Syntax {

    Lexer lexer;
    Semantics semantic;
    private int tipo;
    Word current;
    Token token = null;
    private static final String COMENTARIO = "COMENTARIO";
    private static final String LITERAL = "LITERAL";
    private StringBuffer strBuffer;
    private StringBuffer strBufferSemantico;

    private LinkedHashSet<String> stringTemporario = new LinkedHashSet<String>();
    private boolean DeuErro = false;
    private int linha;

    public Syntax(Lexer lexer) {
        this.strBuffer = new StringBuffer();
        this.strBufferSemantico = new StringBuffer();
        this.lexer = lexer;
        try {
            this.token = lexer.scan();
            this.current = ((Word) this.token);
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals("COMENTARIO")) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals("LITERAL")) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao ler o arquivo. ");
        }
    }

    private void advance() {
        try {
            this.token = this.lexer.scan();

            this.current = new Word(this.token, this.token.toString());
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals("COMENTARIO")) {
                this.lexer.erroComentario();
            } else if (ex.getMessage().equals("LITERAL")) {
                this.lexer.erroLiteral();
            }
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao ler o arquivo.");
        }
    }

    private void erro() {
        this.linha = this.lexer.getN_linha();
        this.DeuErro = true;

        String erro = "Erro Sintático na linha " + this.linha + " próximo ao Token " + this.token.toString() + "\n";

        System.out.println(erro);
        this.strBuffer.append(erro);
    }

    private void erroInfos(int tag) {
        erro();
        System.out.println(" Tag a ser comido: " + tag + "\n");
        this.strBuffer.append("Token esperado: " + tag + " \n");
    }

    private void tratarErro(int tag) {
        erroInfos(tag);
        System.out.println("LOOOP: Token esperado: " + tag + " Tokens:");
        do {
            advance();
            System.out.println(this.token.getTag());
        } while ((this.token.getTag() != tag) && (this.lexer.arquivoPronto()) && (this.token.getTag() != 285));
        if (this.token.getTag() == 285) {
            eat(285);
        } else if (this.token.getTag() == tag) {
            eat(tag);
        }
    }

    private void eat(int tag) {
        if (this.token.getTag() == tag) {
            advance();
        } else {
            tratarErro(tag);
        }
    }

    public int getLinha() {
        return this.linha;
    }

    public void analisar() {
        program();
        if (!this.DeuErro) {
            System.out.println("\n\n- Nenhum erro foi encontrado.\nAnalise Sintatica realizada com sucesso!! -\n\n");
            String msg = "\n\n- Nenhum erro foi encontrado.\n\nAnalise Sintatica realizada com sucesso!! -\n\n";
            this.strBuffer = new StringBuffer(msg);
        }
        this.lexer.gravarSintatico(this.strBuffer);
        gravarBufferSemantico();
        if (strBufferSemantico.length()==0){
            strBufferSemantico.append("Nenhum erro foi encontrado!!!\n\nAnalise semantica realizada com sucesso");
        }
        this.lexer.gravarSemantico(this.strBufferSemantico);

        Ambiente.MostrarTabelaSimbolos();
    }

    private void program() {
        switch (this.token.getTag()) {
            case 256:
                eat(256);
                declList();
                stmtList();
                eat(257);
                break;
            default:
                erroInfos(256);
                declList();
                stmtList();
                eat(257);
        }
    }

    private void declList() {
        switch (this.token.getTag()) {
            case 269:
            case 270:
            case 271:
                decl();
                declListAUX();
        }
    }

    private void declListAUX() {
        eat(285);
        declList();
    }

    private void decl() {
        switch (this.token.getTag()) {
            case 269:
            case 270:
            case 271:
                this.tipo = this.token.getTag();
                type();
                identList();
                break;
            default:
                erro();
        }
    }

    private void identList() {
        switch (this.token.getTag()) {
            case 294:
                this.semantic = new Semantics(this.current, this.tipo, this.token.toString());
                adicionarElemento(this.semantic.Type());
                identifier();
                identListAUX();
                break;
            default:
                erro();
        }
    }

    private void identListAUX() {
        if (this.token.getTag() == 284) {
            eat(284);
            identList();
        }
    }

    private void type() {
        switch (this.token.getTag()) {
            case 269:
                eat(269);
                break;
            case 271:
                eat(271);
                break;
            case 270:
                eat(270);
                break;
            default:
                erro();
        }
    }

    private void identifier() {
        switch (this.token.getTag()) {
            case 294:
                eat(294);
                break;
            default:
                erro();
        }
    }

    private void stmtList() {
        switch (this.token.getTag()) {
            case 264:
            case 265:
            case 294:
                stmt();
                eat(285);
                stmtListAUX();
                break;
            case 258:
            case 262:
                stmt();
                stmtListAUX();
                break;
            case 257:
                break;
            default:
                erro();
        }
    }

    private void stmtListAUX() {
        switch (this.token.getTag()) {
            case 264:
            case 265:
            case 294:
                stmt();
                eat(285);
                stmtListAUX();
                break;
            case 258:
            case 262:
                stmt();
                stmtListAUX();
                break;
        }
    }

    private void stmt() {
        switch (this.token.getTag()) {
            case 294:
                this.semantic = new Semantics(this.current, 294, this.token.toString());
                adicionarElemento(this.semantic.Absence());
                assignStmt();
                break;
            case 258:
                ifStmt();
                break;
            case 262:
                whileStmt();
                break;
            case 264:
                readStmt();
                break;
            case 265:
                writeStmt();
                break;
            case 257:
                break;
            default:
                erro();
        }
    }

    private void assignStmt() {
        switch (this.token.getTag()) {
            case 294:
                eat(294);
                eat(282);
                simpleExpr1();
                break;
            default:
                erro();
        }
    }

    private void ifStmt() {
        switch (this.token.getTag()) {
            case 258:
                eat(258);
                condition();
                eat(259);
                stmtList();
                elseStmt();
                break;
            default:
                erro();
        }
    }

    private void elseStmt() {
        if (this.token.getTag() == 260) {
            eat(260);
            stmtList();
        }
        eat(261);
    }

    private void condition() {
        expression1();
    }

    private void whileStmt() {
        switch (this.token.getTag()) {
            case 262:
                eat(262);
                stmtList();
                stmtSufix();
                break;
            default:
                erro();
        }
    }

    private void stmtSufix() {
        switch (this.token.getTag()) {
            case 263:
                eat(263);
                condition();
                eat(261);
                break;
            default:
                erro();
        }
    }

    private void readStmt() {
        switch (this.token.getTag()) {
            case 264:
                eat(264);
                eat(286);
                identifier();
                eat(287);
                break;
            default:
                erro();
        }
    }

    private void writeStmt() {
        switch (this.token.getTag()) {
            case 265:
                eat(265);
                eat(286);
                writable();
                eat(287);
                break;
            default:
                erro();
        }
    }

    private void writable() {
        switch (this.token.getTag()) {
            case 290:
            case 292:
            case 294:
                adicionarElemento( this.semantic.Absence());
                simpleExpr1();
                break;
            case 291:
                eat(291);
                break;
            case 293:
            default:
                erro();
        }
    }

    private void expression1() {
        switch (this.token.getTag()) {
            case 266:
            case 279:
            case 286:
            case 290:
            case 291:
            case 292:
            case 294:
                simpleExpr1();
                expression();
                break;
            default:
                erro();
        }
    }

    private void expression() {
        if ((this.token.getTag() == 272) || (this.token.getTag() == 273) || (this.token.getTag() == 274) || (this.token.getTag() == 276) || (this.token.getTag() == 275) || (this.token.getTag() == 277)) {
            relop();
            simpleExpr1();
        }
    }

    private void simpleExpr1() {
        switch (this.token.getTag()) {
            case 294:
                this.semantic = new Semantics(this.current, 294, this.token.toString());
                adicionarElemento(this.semantic.Absence());
                term1();
                simpleExpr();
                break;
            case 290:
            case 291:
            case 292:
                this.semantic = new Semantics(this.current, this.token.getTag(), this.token.toString());
            case 266:
            case 279:
            case 286:
                term1();
                simpleExpr();
                break;
            default:
                erro();
        }
    }

    private void simpleExpr() {
        if ((this.token.getTag() == 278) || (this.token.getTag() == 279) || (this.token.getTag() == 267)) {
            addop();
            term1();
            simpleExpr();
        }
    }

    private void term1() {
        switch (this.token.getTag()) {
            case 266:
            case 279:
            case 286:
            case 290:
            case 291:
            case 292:
            case 294:
                factorA();
                term();
                break;
            default:
                erro();
        }
    }

    private void term() {
        if ((this.token.getTag() == 280) || (this.token.getTag() == 281) || (this.token.getTag() == 268)) {
            mulop();
            factorA();
            term();
        }
    }

    private void factorA() {
        switch (this.token.getTag()) {
            case 286:
            case 290:
            case 291:
            case 292:
            case 294:
                factor();
                break;
            case 266:
                eat(266);
                factor();
                break;
            case 279:
                eat(279);
                factor();
                break;
            default:
                erro();
        }
    }

    private void factor() {
        switch (this.token.getTag()) {
            case 294:
                adicionarElemento(this.semantic.TypeAssignment(this.token.toString(), 0));
                eat(294);
                break;
            case 290:
            case 291:
            case 292:
                constant();
                break;
            case 286:
                eat(286);
                expression1();
                eat(287);
                break;
            case 287:
            case 288:
            case 289:
            case 293:
            default:
                erro();
        }
    }

    private void relop() {
        switch (this.token.getTag()) {
            case 272:
                eat(272);
                break;
            case 273:
                eat(273);
                break;
            case 274:
                eat(274);
                break;
            case 276:
                eat(276);
                break;
            case 275:
                eat(275);
                break;
            case 277:
                eat(277);
                break;
            default:
                erro();
        }
    }

    private void addop() {
        switch (this.token.getTag()) {
            case 278:
                eat(278);
                break;
            case 279:
                eat(279);
                break;
            case 267:
                eat(267);
                break;
            default:
                erro();
        }
    }

    private void mulop() {
        switch (this.token.getTag()) {
            case 280:
                eat(280);
                break;
            case 281:
                eat(281);
                break;
            case 268:
                eat(268);
                break;
            default:
                erro();
        }
    }

    private void constant() {
        switch (this.token.getTag()) {
            case 290:
                adicionarElemento(this.semantic.TypeAssignment(null, 290));
                
                eat(290);
                break;
            case 291:
                adicionarElemento(this.semantic.TypeAssignment(null, 291));
                
                eat(291);
                break;
            case 292:
                adicionarElemento(this.semantic.TypeAssignment(null, 292));                
                eat(292);
                break;
            default:
                erro();
        }
    }

    public void adicionarElemento(String entrada) {
        if (entrada != null) {
            this.stringTemporario.add(entrada);
        }
    }

    public void gravarBufferSemantico() {
        Iterator<String> iterator = stringTemporario.iterator();
        while (iterator.hasNext()) {
            strBufferSemantico.append(iterator.next());
        }

    }

}
