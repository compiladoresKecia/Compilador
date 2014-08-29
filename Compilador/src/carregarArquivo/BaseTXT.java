package carregarArquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class BaseTXT
{
  JFileChooser chooser = new JFileChooser(".");
  private FileReader arquivoLido;
  private FileWriter arquivoGravar;
  private FileWriter arquivoGravarSintatico;
  private FileWriter arquivoGravarSemantico;
  private FileWriter arquivoCodigo;
  
  public BaseTXT()
  {
    this.arquivoLido = abrirArquivo();
    try
    {
      this.arquivoGravar = new FileWriter("LOGLEXICO.txt", false);
      this.arquivoGravarSintatico = new FileWriter("LOGSINTATICO.txt", false);
      this.arquivoGravarSemantico = new FileWriter("LOGSEMANTICO.txt", false);
      this.arquivoCodigo = new FileWriter("codigo.vm",false);
    }
    catch (IOException ex)
    {
      Logger.getLogger(BaseTXT.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private FileReader abrirArquivo()
  {
    int flagRead;
    do
    {
      flagRead = this.chooser.showOpenDialog(null);
    } while (flagRead != 0);
    try
    {
      this.arquivoLido = new FileReader(this.chooser.getSelectedFile());
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Arquivo não encontrado");
    }
    return this.arquivoLido;
  }
  
  public void reload()
    throws IOException
  {
    try
    {
      this.arquivoLido = new FileReader(this.chooser.getSelectedFile());
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Arquivo não encontrado");
    }
  }
  
  public char readch()
    throws IOException
  {
    return (char)this.arquivoLido.read();
  }
  
  public FileReader getArquivoEscrita()
  {
    return this.arquivoLido;
  }
  
  public void escreverArquivo(String texto, boolean acabou)
  {
    PrintWriter escreve_linha = new PrintWriter(this.arquivoGravar, true);
    
    escreve_linha.println(texto);
    if (acabou) {
      escreve_linha.close();
    }
  }
  
  public void escreverArquivoSintatico(StringBuffer stBuffer)
  {
    PrintWriter escreve_linha = new PrintWriter(this.arquivoGravarSintatico, true);
    
    escreve_linha.println(stBuffer.toString());
    escreve_linha.close();
  }
  
  public void escreverArquivoSemantico(StringBuffer stBuffer)
  {
    PrintWriter escreve_linha = new PrintWriter(this.arquivoGravarSemantico, true);
    escreve_linha.println(stBuffer.toString());
    escreve_linha.close();
  }
   public void escreverArquivoCodigo(StringBuffer stBuffer)
  {
    PrintWriter escreve_linha = new PrintWriter(this.arquivoCodigo, true);
    escreve_linha.println(stBuffer.toString());
    escreve_linha.close();
  }
  
  public boolean arquivoLidoPronto()
    throws IOException
  {
    return this.arquivoLido.ready();
  }
}