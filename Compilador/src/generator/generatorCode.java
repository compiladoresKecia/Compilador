/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import Environment.Ambiente;
import Environment.id.Identificador;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

/**
 * Gerador de Codigo.
 *
 *
 *
 *
 * Por conveniência, é permitido que os nomes que aparecem no programa fonte
 * apareçam também como endereços no código de três endereços
 *
 * @author wolvery
 */
public class generatorCode {

    /**
     * Codigo completo.
     */
    LinkedList<TreeAdressLine> code;
    /**
     * Operador do codigo de 3 enderecos.
     */
    private int operatorTemporario;
    /**
     * Pilha de operadores.
     */
    private Stack<Integer> operatorStack;
    /**
     * Argumentos.
     */
    private String argumentTemporario;
    /**
     * Pilha argumentos.
     */

    private Stack<String> pilhaArg;
    /**
     * Resultado da linha do codigo.
     */
    private String resultFinal;
    /**
     * Linha do codigo DO-WHILE para carregar.
     */
    private int linhaRetorno;
    /**
     * Lista dos falselist para os IFs.
     */
    private Stack<Integer> pilhaRotulosParaIF;

    /**
     * Lista dos falselist para os ELSEs.
     */
    private Stack<Integer> pilhaRotulosParaELSEs;

    /**
     * Buffer para o codigo vm
     */
    private StringBuffer strBuffer_VM;

    private final int max_Indice = 31;

    private final String temporario[] = {"t1", "t2", "t3", "t4", "t5", "t6",
        "t7", "t8", "t9", "t10", "t11", "t12", "t13", "t14", "t15", "t16",
        "t17", "t18", "t19", "t20", "t21", "t22", "t23", "t24", "t25", "t26",
        "t27", "t28", "t29", "t30", "t31", "t32"};

    private final static int Lado_Esquerdo = 0, Lado_Direito = 1, Lado_Condicao = 2;
    private final String temporario_Condicao[] = {"tl", "tr", "tCo"};

    private int condicionalOperador;

    private int indiceTemporario;

    public generatorCode() {
        code = new LinkedList<>();
        indiceTemporario = 0;
        linhaRetorno = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
        pilhaRotulosParaIF = new Stack<>();
        pilhaRotulosParaELSEs = new Stack<>();
    }

    public void limparPilhas() {
        indiceTemporario = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
    }

    public void adicionarAtrib() {
        if (indiceTemporario > 0) {
            code.add(new TreeAdressLine(Tag.ATRIB, temporario[indiceTemporario - 1], null, resultFinal));
            indiceTemporario = 0;
        } else {
            code.add(new TreeAdressLine(Tag.ATRIB, argumentTemporario, null, resultFinal));
        }
        limparPilhas();
    }

    public void incrementaIndiceTemporario() {
        indiceTemporario++;
        if (indiceTemporario > max_Indice) {
            indiceTemporario = 1;
        }
    }

    /**
     * Obtem os valores da pilha.
     *
     */
    public void remover1lancePilha() {
        this.operatorTemporario = this.obterOperatorDaPilha();
        this.argumentTemporario = this.obterArgumentPilha();
    }

    public void adicionarAssigmentExpression(String arg) {
        if (indiceTemporario == 0) {
            //Importante para retirar o ultimo lance caso exista apenas 1 na pilha
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(),
                    arg, this.argumentTemporario, temporario[indiceTemporario]));
        } else {
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(),
                    this.argumentTemporario, temporario[indiceTemporario - 1], temporario[indiceTemporario]));
        }

        incrementaIndiceTemporario();
    }

    public void adicionarNot() {
        code.add(new TreeAdressLine(Tag.NOT, argumentTemporario, null, temporario[indiceTemporario]));
        this.argumentTemporario = temporario[indiceTemporario];
        incrementaIndiceTemporario();
    }

    public void adicionarMinus() {
        code.add(new TreeAdressLine(Tag.MINUS, argumentTemporario, null, temporario[indiceTemporario]));
        this.argumentTemporario = temporario[indiceTemporario];
        incrementaIndiceTemporario();
    }

    public void adicionarIFStatement() {
        code.add(new TreeAdressLine(Tag.IF, temporario[indiceTemporario - 1], null, "goto " + code.size() + 2));
        code.add(new TreeAdressLine(0, null, null, "goto LABEL_PILHA_IF"));
    }

    public void adicionarElsetatement() {
        code.add(new TreeAdressLine(Tag.ELSE, temporario[indiceTemporario - 1], null, "goto " + code.size() + 1));
        code.add(new TreeAdressLine(0, null, null, "goto LABEL_PILHA_ELSE"));
    }

    public void adicionarLadoEsquerdoCondicao() {
        code.add(new TreeAdressLine(Tag.ATRIB, temporario[indiceTemporario - 1], null, temporario_Condicao[Lado_Esquerdo]));
        limparPilhas();
    }

    public void adicionarCondicao() {
        code.add(new TreeAdressLine(Tag.ATRIB, temporario[indiceTemporario - 1], null, temporario_Condicao[Lado_Direito]));
        code.add(new TreeAdressLine(operatorTemporario,
                temporario_Condicao[Lado_Esquerdo],
                temporario_Condicao[Lado_Direito],
                temporario_Condicao[Lado_Condicao]));
        limparPilhas();

    }

    public void adicionarCodigo(int tag) {
        code.add(new TreeAdressLine(tag, this.argumentTemporario, null, null));
        limparPilhas();
    }

    public void adicionarWhile() {
        code.add(new TreeAdressLine(Tag.IF, temporario_Condicao[Lado_Condicao], null, "goto " + linhaRetorno));
        limparPilhas();
    }

    public LinkedList<TreeAdressLine> getCode() {
        return code;
    }

    public void setCode(LinkedList<TreeAdressLine> code) {
        this.code = code;
    }

    public int obterOperatorDaPilha() {
        return operatorStack.pop();
    }

    public int getOperatorTemporario() {
        return operatorTemporario;
    }

    public void setOperatorTemporario(int operatorTemporario) {
        this.operatorTemporario = operatorTemporario;
        operatorStack.push(operatorTemporario);
    }

    public String obterArgumentPilha() {
        return pilhaArg.pop();
    }

    public String getArgumenTemporario() {
        return argumentTemporario;
    }

    public void setArgumentTemporario(String argument1Temporario) {
        this.argumentTemporario = argument1Temporario;
        pilhaArg.push(argument1Temporario);
    }

    public String getResultFinal() {
        return resultFinal;
    }

    public void setResultFinal(String resultFinal) {
        this.resultFinal = resultFinal;
    }

    public int getIndiceTemporario() {
        return indiceTemporario;
    }

    public void setIndiceTemporario(int indiceTemporario) {
        this.indiceTemporario = indiceTemporario;
    }

    public int getCondicionalOperador() {
        return condicionalOperador;
    }

    public void setCondicionalOperador(int condicionalOperador) {
        this.condicionalOperador = condicionalOperador;
    }

    public int getLinhaRetorno() {
        return linhaRetorno;
    }

    public void setLinhaRetorno() {
        linhaRetorno = code.size();
    }

    public void inserirLinhaRotuloIF() {
        pilhaRotulosParaIF.push(code.size());
    }

    public void inserirLinhaRotuloELSE() {
        pilhaRotulosParaELSEs.push(code.size());
    }

    public void gerarCodigo() {
        TreeAdressLine codigo_Aux;
        Word wordAux, wordResult;
        String aux, rsult;
        String arg1, arg2;
        int offset_Aux = Ambiente.gerarIdentificador().getOffset();
        while (!code.isEmpty()) {
            aux = "PUSHN " + offset_Aux;
            //adiciona a linha de comando aux
            strBuffer_VM.append(aux);
            codigo_Aux = code.pop();
            rsult = codigo_Aux.getResult();
            wordResult = Ambiente.getPeloLexema(rsult);
            if (wordResult != null) {
                if (codigo_Aux.getOperator() == Tag.ATRIB) {
                    wordAux = Ambiente.getPeloLexema(codigo_Aux.getArgument1());
                    // caso o primeiro argumento nao for id wordaux eh nullo
                    if (wordAux == null) {
                        wordAux = Ambiente.getPeloLexema(codigo_Aux.getResult());
                        aux = atribuicaoTipo(wordAux.getType()) + codigo_Aux.getArgument1();
                        strBuffer_VM.append(aux);
                        aux = "STOREG " + Ambiente.getOffsetPeloLexema(wordAux.lexeme);
                        strBuffer_VM.append(aux);
                    }//caso wordaux eh id faca
                    else {
                        offset_Aux = Ambiente.table.get(wordAux).getOffset();
                        aux = "PUSHG " + offset_Aux;
                        strBuffer_VM.append(aux);
                        aux = "STOREG " + Ambiente.getOffsetPeloLexema(codigo_Aux.getResult());
                        strBuffer_VM.append(aux);
                    }
                }
                if (    codigo_Aux.getOperator() == Tag.PLUS
                        || codigo_Aux.getOperator() == Tag.MINUS
                        || codigo_Aux.getOperator() == Tag.MULT
                        || codigo_Aux.getOperator() == Tag.DIV
                        || codigo_Aux.getOperator() == Tag.GE
                        || codigo_Aux.getOperator() == Tag.EQ
                        || codigo_Aux.getOperator() == Tag.GT
                        || codigo_Aux.getOperator() == Tag.LE
                        || codigo_Aux.getOperator() == Tag.LT
                        || codigo_Aux.getOperator() == Tag.NEQ) {

                    arg1 = codigo_Aux.getArgument1();
                    arg2 = codigo_Aux.getArgument2();

                }

            }
        }
    }

    public String atribuicaoTipo(String tipoE) {
        String tipo = null;
        switch (tipoE) {
            case "INT":
                return "PUSHI ";
            case "FLOAT":
                return "PUSHF ";
            case "STRING":
                return "PUSHS ";

        }
        return tipo;
    }

    public String operacaoInteiro(int operacao) {
        switch (operacao) {
            case Tag.PLUS:
                return "ADD";

            case Tag.MINUS:
                return "SUB";

            case Tag.DIV:
                return "DIV";

            case Tag.MULT:
                return "MUL";

            case Tag.GE:
                return "SUPEQ";

            case Tag.GT:
                return "SUP";

            case Tag.EQ:
                return "EQUAL";

            case Tag.LE:
                return "INFEQ";

            case Tag.LT:
                return "INF";

            case Tag.NEQ:
                return "NOT";

        }
        return null;
    }

    public String operacaoFlutuante(int operacao) {
        switch (operacao) {
            case Tag.PLUS:
                return "FADD";

            case Tag.MINUS:
                return "FSUB";

            case Tag.DIV:
                return "FDIV";

            case Tag.MULT:
                return "FMUL";

            case Tag.GE:
                return "FSUPEQ";

            case Tag.GT:
                return "FSUP";

            case Tag.EQ:
                return "EQUAL";

            case Tag.LE:
                return "FINFEQ";

            case Tag.LT:
                return "FINF";

        }
        return null;
    }

    public String operacaoString(int operacao) {
        switch (operacao) {
            case Tag.PLUS:
                return "CONCAT";
        }
        return null;
    }

}
