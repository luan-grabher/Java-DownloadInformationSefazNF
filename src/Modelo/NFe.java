package Modelo;

import Selector.Pasta;
import java.io.File;
import main.Arquivo;

public class NFe {
    private long empresa; //0
    private int filial; //1
    private long nro_NF; //2
    private String especie; //3
    private int serie; //4
    private String data; //5
    private double valor; //6
    private int mdf; //7
    private String chave; //8
    private long natureza; //9
    
    private boolean valida = true;
    
    public NFe(String[] colunas){
        try{
            empresa = Long.valueOf(colunas[0]);
            filial = Integer.valueOf(colunas[1]);
            nro_NF = Long.valueOf(colunas[2]);
            especie = colunas[3];
            serie = Integer.valueOf(colunas[4]);
            data = colunas[5];
            valor = Double.valueOf(colunas[6].replaceAll("\\.", "").replaceAll(",","\\."));
            mdf = Integer.valueOf(colunas[7]);
            chave = colunas[8].replaceAll(" ", "");
            natureza = Long.valueOf(colunas[9].replaceAll("\r", ""));
            
            validarDataEChave();
            
            
        }catch(Exception e){
            valida = false;
        }
    }
    //Getters
    public String getChave(){
        return chave;
    }
    
    //Setters
    public void insereProduto(String pasta_arquivo_nfs, String razao, String prodDesc, String prodValor){
        String local_arquivo = pasta_arquivo_nfs + "\\Dados Nfe Produtos.csv";
        
        String texto_atual = Arquivo.ler(local_arquivo);
        texto_atual = "ERRO".equals(texto_atual.substring(0,4))?"":texto_atual;
        
        texto_atual += "".equals(texto_atual)?"":"\n";
        texto_atual += empresa + ";";
        texto_atual += filial + ";";
        texto_atual += nro_NF + ";";
        texto_atual += especie + ";";
        texto_atual += serie + ";";
        texto_atual += data + ";";
        texto_atual += prodValor + ";";
        texto_atual += mdf + ";";
        texto_atual += chave + ";";
        texto_atual += natureza + ";";
        texto_atual += razao + ";";
        texto_atual += prodDesc + ";";
        
        Arquivo.salvar(local_arquivo, texto_atual);
    }
    
    
    //Validações
    private void validarDataEChave(){
        if(Controle.Controlador.isDate(data)){
            if(chave.length() == 44){
                for (int i = 0; i < chave.length(); i++) {
                    String nro = chave.substring(i,i+1);
                    if(Controle.Controlador.isNumeric(nro) == false){
                        valida =  false;
                        break;
                    }
                }
            }else{
                valida = false;
            }
        }else{
            valida = false;
        }
    }
    public boolean used(File pasta_arquivo_nfes){
        boolean b = false;
        
        File arquivo_usados = Pasta.procura_arquivo(pasta_arquivo_nfes, "Dados;NFe;Produtos;.csv");
        if(arquivo_usados != null){
            String texto_arquivo = Arquivo.ler(arquivo_usados.getAbsolutePath());
            b = texto_arquivo.contains(chave);
        }
        
        return b;
    }
    public boolean isValid(){
        return valida;
    }    
}
