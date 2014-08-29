/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

/**
 *
 *
 * EXEMPLO: VERIFICA T1 = 1 + 2 PUSHI 1 PUSHI 2 ADD T2 = 2 + TI PUSHI 2 ADD T3 =
 * 4 * T2 PUSHI 4 MULT A = T3 STOREG OFFSET_A
 * 
*/
import Environment.Ambiente;
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
    private Token argumentTemporario;
    /**
     * Pilha argumentos.
     */

    private Stack<Token> pilhaArg;
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
    private Stack<String> pilhaRotulosParaIF;
    private int indiceRotulos;
    /**
     * Lista dos falselist para os IFs.
     */
    private Stack<String> pilhaRotulosParaWHILES;

    /**
     * Lista dos falselist para os ELSEs.
     */
    private Stack<String> pilhaRotulosParaELSEs;
    private int indiceRotulosElses;

    /**
     * Buffer para o codigo vm
     */
    private StringBuffer strBuffer_VM;

    private final int max_Indice = 31;

    private final Token temporario = new Token(100);

    private final static int Lado_Esquerdo = 0, Lado_Direito = 1, Lado_Condicao = 2;
    private final Token temporario_Condicao[] = {new Token(101), new Token(102), new Token(103)};

    private int condicionalOperador;

    private int indiceTemporario;

    private LinkedList<Integer> tipoCondicao;

    public generatorCode() {
        indiceRotulos = 0;
        indiceRotulosElses = 0;
        code = new LinkedList<>();
        indiceTemporario = 0;
        linhaRetorno = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
        pilhaRotulosParaIF = new Stack<>();
        pilhaRotulosParaELSEs = new Stack<>();
        tipoCondicao = new LinkedList();
        pilhaRotulosParaWHILES = new Stack();
        strBuffer_VM = new StringBuffer();
    }

    public void limparPilhas() {
        indiceTemporario = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
    }

    public void adicionarAtrib() {
        if (indiceTemporario > 0) {
            code.add(new TreeAdressLine(Tag.ATRIB, temporario, null, resultFinal));
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

    public void adicionarAssigmentExpression(Token arg) {
        if (indiceTemporario == 0) {
            //Importante para retirar o ultimo lance caso exista apenas 1 na pilha
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(),
                    arg, this.argumentTemporario, temporario.toString()));
        } else {
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(),
                    this.argumentTemporario, temporario, temporario.toString()));
        }

        incrementaIndiceTemporario();
    }

    public void adicionarNot() {
        code.add(new TreeAdressLine(Tag.NOT, argumentTemporario, null, temporario.toString()));
        this.argumentTemporario = temporario;
        incrementaIndiceTemporario();
    }

    public void adicionarMinus() {
        code.add(new TreeAdressLine(Tag.MINUS, argumentTemporario, null, temporario.toString()));
        this.argumentTemporario = temporario;
        incrementaIndiceTemporario();
    }

    public void adicionarIFStatement() {
        code.add(new TreeAdressLine(Tag.IF, temporario, null, "IF"));
    }

    public void adicionarENDStatement() {
        code.add(new TreeAdressLine(Tag.END, temporario, null, "IF"));
        indiceRotulos++;
    }

    public void adicionarElsetatement() {
        code.add(new TreeAdressLine(Tag.ELSE, temporario, null, "ELSE"));

    }

    public void adicionarENDELSEStatement() {
        code.add(new TreeAdressLine(Tag.END, temporario, null, "ELSE"));
        indiceRotulosElses++;
    }

    public void adicionarLadoEsquerdoCondicao() {
        code.add(new TreeAdressLine(Tag.ATRIB, temporario, null, temporario_Condicao[Lado_Esquerdo].toString()));
        limparPilhas();
    }

    public void adicionarCondicao() {
        code.add(new TreeAdressLine(Tag.ATRIB, temporario, null, temporario_Condicao[Lado_Direito].toString()));
        code.add(new TreeAdressLine(operatorTemporario,
                temporario_Condicao[Lado_Esquerdo],
                temporario_Condicao[Lado_Direito],
                temporario_Condicao[Lado_Condicao].toString()));
        limparPilhas();

    }

    public void adicionarCodigo(int tag) {
        code.add(new TreeAdressLine(tag, this.argumentTemporario, null, null));
        limparPilhas();
    }

    public void adicionarWhile() {
        code.add(new TreeAdressLine(Tag.IF, temporario_Condicao[Lado_Condicao], null, "while"));
        limparPilhas();
    }

    public void adicionarDo() {
        code.add(new TreeAdressLine(Tag.DO, null, null, null));
        limparPilhas();
    }

    public void adicionarRead() {
        code.add(new TreeAdressLine(Tag.SCAN, argumentTemporario, null, null));

    }

    public void adicionarWrite() {
        code.add(new TreeAdressLine(Tag.PRINT, argumentTemporario, null, null));
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

    public Token obterArgumentPilha() {
        return pilhaArg.pop();
    }

    public Token getArgumenTemporario() {
        return argumentTemporario;
    }

    public void setArgumentTemporario(Token argument1Temporario) {
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

    public void inserirTipoCondicao(int tipo) {
        tipoCondicao.push(tipo);
    }

    public void gerarCodigo() {
        TreeAdressLine codigo_Aux;
        int addr = 0;
        Word wordAux, wordArg1, wordArg2, wordResult;
        String aux, rsult;
        String arg1, arg2;
        int offset_Max = Ambiente.gerarIdentificador().getOffset() + 3;
        int offset_Aux;
        aux = "START" + "\n";
        strBuffer_VM.append(aux);
        //+3 = LADO ESQUERDO E DIREITO e CONDICAO
        aux = "PUSHN " + offset_Max + "\n";
        //adiciona a linha de comando aux
        strBuffer_VM.append(aux);
        System.out.println("-------------------------------------");
        while (!code.isEmpty()) {

            codigo_Aux = code.pop();
            rsult = codigo_Aux.getResult();
            wordResult = Ambiente.getPeloLexema(rsult);
            controle(codigo_Aux);
            if (wordResult != null) {

                if (codigo_Aux.getOperator() == Tag.ATRIB) {
                    wordAux = Ambiente.getPeloLexema(codigo_Aux.getArgument1().toString());
                    // caso o primeiro argumento nao for id wordaux eh nullo
                    if (wordAux != null) {
                        // Se nao for um terminal carregue seu valor com o PUSH
                        if (!containsTemporarioQualquer(codigo_Aux.getArgument1().toString())) {
                            aux = atribuicaoTipo(wordResult.getType()) + codigo_Aux.getArgument1() + "\n";
                            strBuffer_VM.append(aux);
                        }
                        //CARREGA O TOPO NA PILHA
                        aux = "STOREG " + Ambiente.getOffsetPeloLexema(wordResult.lexeme) + "\n";
                        strBuffer_VM.append(aux);

                    }
                }

            } else {
                if (codigo_Aux.getOperator() == Tag.PLUS
                        || codigo_Aux.getOperator() == Tag.MINUS
                        || codigo_Aux.getOperator() == Tag.MULT
                        || codigo_Aux.getOperator() == Tag.DIV
                        || codigo_Aux.getOperator() == Tag.GE
                        || codigo_Aux.getOperator() == Tag.EQ
                        || codigo_Aux.getOperator() == Tag.GT
                        || codigo_Aux.getOperator() == Tag.LE
                        || codigo_Aux.getOperator() == Tag.LT
                        || codigo_Aux.getOperator() == Tag.NEQ) {
                    arg1 = codigo_Aux.getArgument1().toString();
                    if (codigo_Aux.getArgument2() != null) {
                        arg2 = codigo_Aux.getArgument2().toString();
                        //TEMPORARIO = CONSTANT OPERACAO CONSTANT
                        if (!containsTemporarioQualquer(arg1) && !containsTemporarioQualquer(arg2)) {
                            wordArg1 = Ambiente.getPeloLexema(arg1);
                            wordArg2 = Ambiente.getPeloLexema(arg2);
                            //TEMPORARIO = CONSTANT OPERACAO CONSTANT
                            if (wordArg1 == null && wordArg2 == null) {
                                aux = atribuicaoConstanteTipo(codigo_Aux.getArgument1().getTag()) + codigo_Aux.getArgument1().toString() + "\n";
                                strBuffer_VM.append(aux);
                                aux = atribuicaoConstanteTipo(codigo_Aux.getArgument2().getTag()) + codigo_Aux.getArgument2().toString() + "\n";
                                strBuffer_VM.append(aux);
                                aux = getOperacaoTipo(codigo_Aux.getArgument1().getTag(), codigo_Aux.getOperator());
                                strBuffer_VM.append(aux);
                            }//TEMPORARIO = ID OPERACAO CONSTANT 
                            else if (wordArg1 != null && wordArg2 == null) {
                                aux = "PUSHG " + Ambiente.getOffsetPeloLexema(wordArg1.lexeme) + "\n";
                                strBuffer_VM.append(aux);
                                aux = atribuicaoConstanteTipo(codigo_Aux.getArgument2().getTag()) + codigo_Aux.getArgument2().toString() + "\n";
                                strBuffer_VM.append(aux);
                                aux = getOperacaoTipo(codigo_Aux.getArgument1().getTag(), codigo_Aux.getOperator());
                                strBuffer_VM.append(aux);
                            } else if (wordArg1 != null && wordArg2 != null) {
                                aux = "PUSHG " + Ambiente.getOffsetPeloLexema(wordArg1.lexeme) + "\n";
                                strBuffer_VM.append(aux);
                                aux = "PUSHG " + Ambiente.getOffsetPeloLexema(wordArg2.lexeme) + "\n";
                                strBuffer_VM.append(aux);
                                aux = getOperacaoTipoID(wordArg1.getType(), codigo_Aux.getOperator());
                                strBuffer_VM.append(aux);
                            }

                        }
                        //TEMPORARIO = CONSTANT OPERACAO TEMPORARIO
                        if (!containsTemporarioQualquer(arg1) && containsTemporario(arg2)) {
                            aux = atribuicaoConstanteTipo(codigo_Aux.getArgument1().getTag()) + codigo_Aux.getArgument1().toString() + "\n";
                            strBuffer_VM.append(aux);
                            aux = getOperacaoTipo(codigo_Aux.getArgument1().getTag(), codigo_Aux.getOperator());
                            strBuffer_VM.append(aux);
                        } //TEMPORARIO = TEMPORARIO OPERACAO TEMPORARIO
                        else if (temporario_Condicao[Lado_Condicao].toString().equals(codigo_Aux.getResult())) {
                            // LADO condicao
                            int tipo_condicao = tipoCondicao.pop();
                            offset_Aux = (offset_Max + 1);
                            aux = "PUSHG " + offset_Aux + "\n";
                            strBuffer_VM.append(aux);
                            offset_Aux = (offset_Max + 2);
                            aux = "PUSHG " + offset_Aux + "\n";
                            strBuffer_VM.append(aux);
                            aux = getOperacaoTipo(tipo_condicao, codigo_Aux.getOperator());
                            strBuffer_VM.append(aux);
                            offset_Aux = (offset_Max + 3);
                            aux = "STOREG " + offset_Aux + "\n";
                            strBuffer_VM.append(aux);

                        }

                    }

                } else if (temporario_Condicao[Lado_Esquerdo].toString().equals(codigo_Aux.getResult())) {
                    // LADO ESQUERDO CONDICIONAL
                    offset_Aux = (offset_Max + 1);
                    aux = "STOREG " + offset_Aux + "\n";
                    strBuffer_VM.append(aux);
                } else if (temporario_Condicao[Lado_Direito].toString().equals(codigo_Aux.getResult())) {
                    // LADO DIREITO CONDICIONAL
                    offset_Aux = (offset_Max + 2);
                    aux = "STOREG " + offset_Aux + "\n";
                    strBuffer_VM.append(aux);
                } /**
                 * -- SE T FOR FALSO GOTO ENDIF ( SE TIVER UM ELSE )->ADICIONA
                 * JUMP ELSEEND -- ENDIF (SETIVER UM IF )-> ENDIF <= ELSE
                 * ---ELSEEND
                 */
                else if (codigo_Aux.getOperator() == Tag.IF) {
                    offset_Aux = (offset_Max + 3);
                    aux = "PUSHG " + offset_Aux + "\n";
                    strBuffer_VM.append(aux);

                    if (codigo_Aux.getResult().equals("while")) {
                        aux = "JZ " + pilhaRotulosParaWHILES.pop() + "\n";
                        strBuffer_VM.append(aux);
                    } else {
                        aux = "JZ " + "B" + indiceRotulos + ":";
                        pilhaRotulosParaIF.add("B" + indiceRotulos + ":" + "\n");
                        indiceRotulos--;
                        strBuffer_VM.append(aux);

                    }
                } else if (codigo_Aux.getOperator() == Tag.ELSE) {
                    aux = pilhaRotulosParaIF.pop() + "\n";
                    strBuffer_VM.append(aux);
                } else if (codigo_Aux.getOperator() == Tag.DO) {
                    addr++;
                    aux = "A" + addr + ":" + "\n";
                    strBuffer_VM.append(aux);
                    pilhaRotulosParaWHILES.add(aux);
                } else if (codigo_Aux.getOperator() == Tag.END && "IF".equals(codigo_Aux.getResult())) {
                    if (!"ELSE".equals(code.peekFirst().getResult())) {
                        aux = pilhaRotulosParaIF.pop() + "\n";
                        strBuffer_VM.append(aux);
                    } else {
                        aux = "JUMP " + "B" + indiceRotulosElses + ":" + "\n";
                        pilhaRotulosParaELSEs.add("B" + indiceRotulosElses + ":");
                        indiceRotulosElses--;
                        strBuffer_VM.append(aux);
                    }

                } else if (codigo_Aux.getOperator() == Tag.END
                        && "ELSE".equals(codigo_Aux.getResult())) {
                    aux = pilhaRotulosParaELSEs.pop() + "\n";
                    strBuffer_VM.append(aux);

                } else if (codigo_Aux.getOperator() == Tag.SCAN) {
                    wordAux = Ambiente.getPeloLexema(codigo_Aux.getArgument1().toString());
                    strBuffer_VM.append("READ \n");
                    aux = "STOREG " + Ambiente.table.get(wordAux).getOffset() + "\n";
                    strBuffer_VM.append(aux);
                } else if (codigo_Aux.getOperator() == Tag.PRINT) {
                    wordAux = Ambiente.getPeloLexema(codigo_Aux.getArgument1().toString());
                    if (wordAux != null) {
                        aux = "PUSHG " + Ambiente.table.get(wordAux).getOffset() + "\n";
                        strBuffer_VM.append(aux);
                        aux = operacaoSaidaEscritaID(wordAux.getType())+ "\n" ;
                        strBuffer_VM.append(aux);

                    } else {
                        int tipo = codigo_Aux.getArgument1().getTag();
                        aux = atribuicaoConstanteTipo(tipo) + codigo_Aux.getArgument1().toString() + "\n";
                        strBuffer_VM.append(aux);
                        aux = operacaoSaidaEscritaConstante(tipo) + "\n";
                        strBuffer_VM.append(aux);
                    }

                }

            }
        }
        aux = "STOP" + "\n";
        strBuffer_VM.append(aux);
        System.out.println("-------------------------------------");
    }

    public String atribuicaoConstanteTipo(int tag) {
        switch (tag) {
            case Tag.INTEIRO:
                return "PUSHI ";
            case Tag.FLUTUANTE:
                return "PUSHF ";
            case Tag.LITERAL:
                return "PUSHS ";
            default:
                return null;

        }
    }

    public String getOperacaoTipoID(String tag, int operacao) {
        switch (tag) {
            case "INT":
                return operacaoInteiro(operacao);
            case "FLOAT":
                return operacaoFlutuante(operacao);
            case "STRING":
                return operacaoString(operacao);
            default:
                return null;

        }
    }

    public String getOperacaoTipo(int tag, int operacao) {
        switch (tag) {
            case Tag.INTEIRO:
                return operacaoInteiro(operacao);
            case Tag.FLUTUANTE:
                return operacaoFlutuante(operacao);
            case Tag.LITERAL:
                return operacaoString(operacao);
            default:
                return null;

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
                return "ADD \n";

            case Tag.MINUS:
                return "SUB \n";

            case Tag.DIV:
                return "DIV \n";

            case Tag.MULT:
                return "MUL \n";

            case Tag.GE:
                return "SUPEQ \n";

            case Tag.GT:
                return "SUP \n";

            case Tag.EQ:
                return "EQUAL \n";

            case Tag.LE:
                return "INFEQ \n";

            case Tag.LT:
                return "INF \n";

            case Tag.NEQ:
                return "NOT \n EQUAL \n";

        }
        return null;
    }

    public String operacaoFlutuante(int operacao) {
        switch (operacao) {
            case Tag.PLUS:
                return "FADD \n";

            case Tag.MINUS:
                return "FSUB \n";

            case Tag.DIV:
                return "FDIV \n";

            case Tag.MULT:
                return "FMUL \n";

            case Tag.GE:
                return "FSUPEQ \n";

            case Tag.GT:
                return "FSUP \n";

            case Tag.EQ:
                return "EQUAL \n";

            case Tag.LE:
                return "FINFEQ \n";

            case Tag.LT:
                return "FINF \n";

        }
        return null;
    }

    public String operacaoString(int operacao) {
        switch (operacao) {
            case Tag.PLUS:
                return "CONCAT \n";
        }
        return null;
    }

    public String operacaoSaidaEscritaConstante(int operacao) {
        switch (operacao) {
            case Tag.INTEIRO:
                return "WRITEI ";
            case Tag.FLUTUANTE:
                return "WRITEF ";
            case Tag.LITERAL:
                return "WRITES ";
        }
        return null;
    }

    public String operacaoSaidaEscritaID(String operacao) {
        switch (operacao) {
            case "INT":
                return "WRITEI ";
            case "FLOAT":
                return "WRITEF ";
            case "STRING":
                return "WRITES ";
        }
        return null;
    }

    public String operacaoEntrada(int operacao) {
        switch (operacao) {
            case Tag.SCAN:
                return "READ \n";
        }
        return null;
    }

    public StringBuffer getStrBuffer_VM() {
        gerarCodigo();
        return strBuffer_VM;
    }

    public boolean containsCondicao(String aux) {

        for (int i = 0; i <= 2; i++) {
            if (aux.equals(temporario_Condicao[i].toString())) {
                return true;
            }

        }
        return false;
    }

    public boolean containsTemporario(String aux) {
        if (aux.equals(temporario.toString())) {
            return true;
        }
        return false;
    }

    public boolean containsTemporarioQualquer(String aux) {
        return containsCondicao(aux) || containsTemporario(aux);
    }

    public void controle(TreeAdressLine codigo_Aux) {
        System.out.println("---");
        System.out.println("Operator: " + codigo_Aux.getOperator());

        if (codigo_Aux.getArgument1() != null) {
            System.out.println("Arg1: " + codigo_Aux.getArgument1().toString() + " " + codigo_Aux.getArgument1().getTag());
        }
        if (codigo_Aux.getArgument2() != null) {
            System.out.println("Arg2: " + codigo_Aux.getArgument2().toString() + " " + codigo_Aux.getArgument2().getTag());
        }
        if (codigo_Aux.getResult() != null) {
            System.out.println("Result: " + codigo_Aux.getResult());
        }
        System.out.println("---");
    }

}
/**
 *  * --- c = b + a * (1 + a*c); A=[b] O=[+] A=[a b] O=[* +] A=[1 a b] O=[+ * +]
 * A=[a 1 a b] O=[* + * +] A=[c a 1 a b] O=[* + * +] arg = a argumentTemp = c -
 * t1 =a*c I=1 ---- A=[1 a b] O=[ + * +] arg = a argumentTemp = 1 -t2=1+t1 ----
 * A=[a b] O=[* +] argumentTemp = a -t3= a* t2 ---- A=[b] O=[+] -t4= a+ t3 ----
 */
