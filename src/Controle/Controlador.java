package Controle;

import Modelo.NFe;
import Modelo.Navegador;
import Visao.carregando;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.Arquivo;

public class Controlador {
    public static String mensagem_final  = "";
    
    public static String link_arquivo = "";
    private static File pastaFile = null;
    private static String link_pasta_arquivo= "";
   
    
    
    public static void iniciar(){
        if (Selector.Arquivo.verifica(link_arquivo,"csv")) {
            link_pasta_arquivo = new File(link_arquivo).getParent();
            pastaFile = new File(link_pasta_arquivo);
            
            try{
                //exibe carregamento
                JFrame carregamento = new carregando();
                carregamento.setVisible(true);
                
                Executar_Funcoes();

                //esconde carregamento
                carregamento.setVisible(false);
        
                if(!"".equals(mensagem_final)){
                    JOptionPane.showMessageDialog( null ,
                        mensagem_final,
                        " Programa Finalizado!" , JOptionPane.INFORMATION_MESSAGE );
                }
                //System.exit(0);
            }catch(Exception e){
                JOptionPane.showMessageDialog( null ,
                    " OCORREU UM ERRO NÃO ESPERADO NO JAVA! ERRO: \n" + e,
                    " ERRO NO JAVA!" , JOptionPane.WARNING_MESSAGE );
                e.printStackTrace();
            }
        }
    }
    
    /*FUNÇÕES*/
    private static void Executar_Funcoes(){
        try{
            //Abrir Navegador
            //Navegador.abre_navegador();
            
            //Percorrer arquivo
            String texto_arquivo = Arquivo.ler(link_arquivo);
            String[] linhas_arquivo = texto_arquivo.split("\n");
            
            for (String linha : linhas_arquivo) {
                String[] colunas = linha.split(";");
                //Verificar Arquivo
                if(colunas.length >= 10){
                    NFe nfe = new NFe(colunas);
                    
                    if(nfe.isValid()){
                        //fazer coisas malucas
                        if(nfe.used(pastaFile) == false){
                            String infos_chave_nfe = Navegador.getStringInfoProdutosNFe(nfe.getChave());
                            if(!"".equals(infos_chave_nfe)){
                                String razao = infos_chave_nfe.split("##")[0];
                                
                                String produtos_str  = infos_chave_nfe.split("##")[1];
                                String[] produtos = produtos_str.split("§");
                                
                                for (String produto : produtos) {
                                    String prod_desc = produto.split(";")[0];
                                    String prod_valor = produto.split(";")[1];
                                    
                                    nfe.insereProduto(link_pasta_arquivo, razao, prod_desc, prod_valor);
                                }
                            }
                        }
                    }
                }
            }
            
            mensagem_final = "Programa finalizado!";
            
            //Navegador.fecha_navegador();
            
        }catch(Exception e){
            Erro("Erro ao executar as funções: " +  e);
            e.printStackTrace();
        }
    }
    
    /* UTILITÁRIOS */
    private static void Erro(String erro){
        System.out.println(erro);
        JOptionPane.showMessageDialog( null ,
                    erro,
                    " ERRO!" , JOptionPane.ERROR_MESSAGE);
    }
    public static String ultimo_dia_mes(int mes, int ano){
        return LocalDate.of(ano,mes,1).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }
    public static String primeiro_dia_mes(int mes, int ano){
        return LocalDate.of(ano,mes,1).toString();
    }
    public static boolean isNumeric (String s) {
        try {
            Long.parseLong (s); 
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    public static boolean isDate(String date){
        return isDate(date,"dd/MM/yyyy");
    }
    public static boolean isDate(String date, String format_date){
        DateFormat format = new SimpleDateFormat(format_date);
        format.setLenient(false);
        try {
            format.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Double getDouble(String s){
        Double b;
        String return_Double = "";
        try{
            String[] numbers = new String[]{"0","1","2","3","4","5","6","7","8","9",".",","};
            s = s.replaceAll(" ", "");
            
            String[] split_s = s.split("");
            for (String charr : split_s) {
                for (String num : numbers) {
                    if(charr.equals(num)){
                        return_Double += charr;
                        break;
                    }
                }
            }
            
            int pontos = ocorrencias(return_Double, "\\.");
            int virgulas = ocorrencias(return_Double, ",");
            
            if(pontos > 1 | virgulas > 1){
                //verifica ocorrencias de casda um para tirar ponto milhar
                return_Double = "";
            }else if(pontos == 1 & virgulas == 1){
                return_Double = return_Double.replaceAll("\\.", "");
            }

        }catch(Exception e){
            return_Double = "";
        }
        
        if(!"".equals(return_Double)){
            try{
                b = Double.valueOf(return_Double.replaceAll(",","."));
            }catch(Exception e){
                b = 0.0;
            }
        }else{b = 0.0;}
         
        return b;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static int ocorrencias(String s,String expressao){
        return (s.length() - s.replaceAll(expressao, "").length()) / expressao.length();
    }
}
