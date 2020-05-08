package Modelo;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.wait.wait;

public class Navegador {
    
    /* CONFIGURAÇÕES DRIVER */
    private static WebDriver driver = null;
    private static WebElement e = null;
    private static List<WebElement> es = null;
    private static JavascriptExecutor js;
    
    
    /*FUNÇÕES DE CONTROLE*/
    
    
    /*FUNÇÕES DO DRIVER*/
    public static void abre_navegador(){
        try{
            System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-print-preview");
            driver = new ChromeDriver(options);
            /*driver.manage().window().fullscreen();*/
            
            if (driver instanceof JavascriptExecutor) {
                js = (JavascriptExecutor)driver;
            }
        }catch(Exception e){
            System.out.println("Ocorreu um erro: \n " + e);
        }
    }
    public static void fecha_navegador(){
        try{
            if(driver_aberto()){
                driver.quit();
            }
        }catch(Exception e){
            System.out.println("Ocorreu um erro: \n " + e);
        }
    }
    public static boolean driver_aberto(){
        boolean b = false;
        try{
            String name_page = driver.getWindowHandle();
            b = true;
        }catch(Exception e){}
        return b;
    }
    
    
    /*NAVEGAÇÃO*/
    public static String getStringInfoProdutosNFe(String chave){
        String infos_str = "";
        try{
            //entra no site
            driver.get("http://www.nfe.fazenda.gov.br/portal/consultaRecaptcha.aspx?tipoConsulta=completa&tipoConteudo=XbSeqxE8pl8=&nfe=" + chave);
            
            e = wait.element(driver, By.cssSelector("#ctl00_ContentPlaceHolder1_pnlBotoes > div.g-recaptcha > div > div > iframe"));
            if (e != null) {
                new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("#ctl00_ContentPlaceHolder1_pnlBotoes > div.g-recaptcha > div > div > iframe")));
                new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#recaptcha-anchor > div.recaptcha-checkbox-border"))).click();
                if (waitRecaptcha()) {
                    driver.switchTo().defaultContent();
                    e = wait.element(driver, By.cssSelector("#ctl00_ContentPlaceHolder1_btnConsultar"));
                    if (e != null) {
                        e.click();
                        
                        e = wait.element(driver, By.cssSelector("#tab_1 > b"));
                        if (e != null) {
                            e.click();
                            
                            e = wait.element(driver, By.cssSelector("#Emitente > fieldset > table > tbody > tr.col-2 > td:nth-child(1) > span"));
                            if (e != null) {
                                String razao = e.getText();
                                
                                e = wait.element(driver, By.cssSelector("#tab_3 > b"));
                                if (e != null) {
                                    e.click();
                                    
                                    e = wait.element(driver, By.cssSelector("#Prod"));
                                    if (e != null) {
                                        es = driver.findElements(By.cssSelector("#Prod > fieldset > div > table.toggle.box > tbody > tr"));
                                        if (e != null) {
                                            String produtosString = "";
                                            
                                            for (int i = 0; i < es.size(); i++) {
                                                e = es.get(i);
                                                
                                                produtosString += "".equals(produtosString)?"":"§";
                                                produtosString += e.findElement(By.cssSelector(".fixo-prod-serv-descricao > span")).getText();
                                                produtosString += ";" + e.findElement(By.cssSelector(".fixo-prod-serv-vb > span")).getText();
                                            }
                                            
                                            if(!"".equals(produtosString) & !"".equals(razao)){
                                                infos_str = razao + "##";
                                                infos_str += produtosString;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao pegar info produtos nfe: " + e);
            e.printStackTrace();
        }
        
        return infos_str;
    }
    
    private static boolean waitRecaptcha(){
        boolean b = true;
        
        for (int i = 0; i < 300; i++) {
            e = wait.element(driver, By.cssSelector(".recaptcha-checkbox-checked"),1);
            if(e==null){
                if(i >= 300){
                    b = false;
                }else{
                    esperar(1);
                }
            }else{
                break;
            }
        }      
        return b;
    }    
    /*UTILITARIOS*/
    private static void esperar(long segundos){
        wait.java(segundos);
    }
}
