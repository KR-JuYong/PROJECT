package project;

//값 입력 및 출력에 대한 import
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//스크린샷 파일 저장에 대한 import
import java.io.File;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.IOException;
import org.apache.commons.io.FileUtils;   
//웹 크롤링 selenium에 대한 import
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class JavaSeleniumTest {
    private WebDriver driver;
  
    //Properties (특성) 입력
    public static final String WEB_DRIVER_ID="webdriver.chrome.dirver";
    //Properties (특성) 입력
    //lib에 설치한 chromedriver의 설치 경로
    public static final String WEB_DRIVER_PATH="D:/workspace/java/base/lib/chromedriver.exe";
    //네이버 증권 홈페이지.
    public String URL="https://finance.naver.com/item/main.naver";

    public JavaSeleniumTest(){
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        //생성자
        ChromeOptions options = new ChromeOptions();
        //홈페이지를 띄우지 않고 프로그램 실행
        //"headless"가 없다면 홈페이지가 출력 됨
        //options.addArguments("headless");
        options.addArguments("--remote-allow-origins=*");
        
        //창 크기 조정
        //options.addArguments("--windows-size=1920,1080");
        
        //W3C오류가 나옴-버전이 업데이트 되면서 selenium 자제체에서 오류가 생긴 것으로 알고 있음.
        //options.setCapability("ignoreProtectedModeSettings", true);
        
        driver = new ChromeDriver(options);
    }

    public static void main(String[] args) throws Exception {
        // 주식 차트 저장할 폴더 생성 - File.mkdirs();
        File Folder1 = new File("D:/workspace/java/base/src/project/주식 차트/TOP 거래상위");
        File Folder2 = new File("D:/workspace/java/base/src/project/주식 차트/TOP 상승");
        File Folder3 = new File("D:/workspace/java/base/src/project/주식 차트/TOP 하락");
        File Folder4 = new File("D:/workspace/java/base/src/project/주식 차트/TOP 시가총액 상위");
        Folder1.mkdirs();
        Folder2.mkdirs();
        Folder3.mkdirs();
        Folder4.mkdirs();
        //시작 전 주식 차트 파일 초기화.
        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 거래상위"));
        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 상승"));
        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 하락"));
        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 시가총액 상위"));

        Scanner scanner =new Scanner(System.in);
        //크롤링 할 종목 선택
        a:while(true){
            System.out.println();
            System.out.println("------------------------------------------------------");
            System.out.println("| 1. 거래상위 | 2. 상승 | 3. 하락 | 4. 시가총액 상위 |");
            System.out.println("------------------------------------------------------");
            System.out.print("크롤링 하고 싶은 TOP 종목을 선택하세요> ");
            int num=scanner.nextInt();

            if(num != 1 && num != 2 && num != 3 && num != 4){
                System.out.println();
                System.out.println("** 잘못 입력 하셨습니다. **");
                continue;
            }
            //종목 선택 후 실행
            JavaSeleniumTest jt= new JavaSeleniumTest();
            if(num==1){
            List<String> resultList2= jt.getItemName2();
            jt.getItemInfo2(resultList2);
        }
            else if(num==2){
            List<String> resultList3=jt.getItemName3();
            jt.getItemInfo3(resultList3);
        
            }else if(num==3){
            List<String> resultList4=jt.getItemName4();
            jt.getItemInfo4(resultList4);
        
            }else if(num==4){
            List<String> resultList=jt.getItemName();
            jt.getItemInfo(resultList);
            }else{
                System.out.println("** 숫자를 잘못 입력 하셨습니다 **");
                continue;
            }
            //종료 프로그램
            scanner.nextLine();
            b:while(true){
                System.out.println();
                System.out.print("종료하시겠습니까? [ Y / N ] > ");
                String finish=scanner.nextLine();
                if(finish.equals("Y")||finish.equals("y")){
                    System.out.println(); 
                    //저장 한 주식 그래프 삭제 여부
                    System.out.print("저장하신 주식 차트를 모두 삭제 하시겠습니까? [ Y / N ] > ");
                    String delete =scanner.nextLine();
                    if(delete.equals("Y")|| delete.equals("y")){
                        //FileUtils.cleanDirectory -> 폴더 안에 있는 파일을 모두 삭제시킴.
                        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 거래상위"));
                        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 상승"));
                        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 하락"));
                        FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 시가총액 상위"));
                        System.out.println();
                        System.out.println("모두 삭제 되었습니다.");
                        System.out.println();
                        System.out.println("프로그램을 종료합니다.");
                        scanner.close();
                        break a;
                        }else if(delete.equals("N")|| delete.equals("n")){
                        System.out.println();
                        System.out.println("프로그램을 종료 합니다.");
                        scanner.close();
                        break a;
                        }
                    }else if(finish.equals("N")|| finish.equals("n")){
                        System.out.println();
                        break b;
                    }else{
                        System.out.println();
                        System.out.println("** 잘못 입력 하셨습니다 **");
                        continue; 
                    }    
        }
    }
}
    //-----------------------------------------------------------------------------------------------------------
    public List<String> getItemName2(){
        List<String> itemNameList2 = new ArrayList<>();
        System.out.println("========= TOP종목 - 거래상위 =========");
    //try을 통한 예외 처리.
    try{
        driver.get(URL);
        //int row =driver.findElements(By.xpath("//*[@id=\"_topItems4\"]/tr")).size();
        //거래 상위 TOP에 있는 회사의 수를 센다.
        int row1 = driver.findElements(By.xpath("//*[@id=\"_topItems1\"]/tr")).size();
        System.out.println("거래 상위 row 수는: "+row1);

        for(int i=0; i<row1; i++){
            //Element(요소)의 속성값을 가져오는 .getAttribute를 사용하여 순위별 값을 가져옴
            //각 사이트 별 주소를 가져온다
            String data= driver.findElement(By.xpath("//*[@id=\"_topItems1\"]/tr["+(i+1)+"]/th/a")).getAttribute("href");
            //각 사이트는 https://finance.naver.com/item/main.naver?code=000000 으로 나오는데
            int index=data.lastIndexOf("=");    //"="을 기준으로 뒤에 있는 숫자 6자리만 가져오도록 설정한다.
            String code=data.substring(index+1).trim(); //substring - 문자열 자르기(=split), .trim() 문자열 앞 뒤의 공백을 제거함.
            itemNameList2.add(code);
        }   
    }catch(Exception e){
        e.printStackTrace();
    }
        return itemNameList2;
    }
    
    public void getItemInfo2(List<String> resultList2){
        int i=0;
        //불러온 웹 크롤링 정보 및 주소 출력하기.    
        for(String code: resultList2){
            System.out.println();
            System.out.println("---실시간 TOP "+(i+=1)+"위---");
            //웹 크롤링 주소 출력
            String param="?";
            param +="code="+ code;
            String newUrl=URL+param;
            System.out.println(newUrl);

            driver.get(newUrl);
            
            //웹 크롤링 정보 출력하기.
            //사이트 기준 우축 투자정보에 대한 데이터를 가져와 출력해준다.
            //String 차트 = driver.findElement(By.xpath("//*[@id=\"chart_area\"]")).getScreenshotAs(OutputType("./png"));
            String 종목이름 =driver.findElement(By.xpath("//*[@id=\"middle\"]/div[1]/div[1]/h2/a")).getText();
            System.out.println("종목 이름: "+종목이름+"("+code+")");

            //투자 정보 첫 칸의 갯수(크기)가 2개, 3개, 4개로 나뉘어져 있음. 그 사이즈를 측정하여 갯수에 따라 크롤링을 달리함.
            int size = driver.findElements(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr")).size();
            
            //주식 차트를 스크린샷 찍어 폴더에 저장.
            try{
                File scrFile = ((TakesScreenshot)driver.findElement(By.xpath("//*[@id=\"chart_area\"]"))).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("D:/workspace/java/base/src/project/주식 차트/TOP 거래상위/"+i+"."+종목이름+".png"));
                }catch(IOException e){
                    e.printStackTrace();
                }
            //투자 정보의 첫 칸의 갯수가 2개인 종목에 대한 출력.
            if(size==2){
            String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
            System.out.println("시가 총액: "+시가총액);
        
            String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
            System.out.println("상장주식수 : "+상장주식수);
    
            String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr/td")).getText();
            System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
            }else if(size==3){        // 투자 정보의 첫 칸의 갯수가 3개인 종목에 대한 출력
                if(종목이름.contains("우드랩")){
                    String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                System.out.println("시가 총액: "+시가총액);
        
                String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                System.out.println("시가 총액순위 : "+시가총액순위);
    
                String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                System.out.println("상장 주식수: "+상장주식수);
    
                String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
    
                String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
    
                String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                System.out.println("외국인 소진율(B/A): "+외국인소진율);
    
                String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
    
                String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                System.out.println("PER|EPS: "+PEREPS);
    
                String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                System.out.println("추정 PER|EPS: "+추정PEREPS);
    
                String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                System.out.println("PBR|BPS: "+PBRBPS);
    
                String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                System.out.println("배당 수익률: "+배당수익률);
    
                String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                System.out.println("동일업종 PER: "+동일업종PER);
    
                String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                System.out.println("동일업종 등락률: "+동일업종등락률);
                }else{
                String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                    System.out.println("시가 총액: "+시가총액);
        
                    String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                    System.out.println("상장주식수 : "+상장주식수);
    
                    String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td")).getText();
                    System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                    String 기초지수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                    System.out.println("기초지수: "+기초지수);
    
                    String 유형 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                    System.out.println("유형: "+유형);
    
                    String 상장일 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                    System.out.println("상장일: "+상장일);
    
                    String 펀드보수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                    System.out.println("펀드보수: "+펀드보수);
    
                    String 자산운용사 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                    System.out.println("자산운용사: "+자산운용사);
    
                    String NAV =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody/tr[1]/td")).getText();
                    System.out.println("NAV: "+NAV);
    
                    String ㅣ1개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                    System.out.println("1개월 수익률: "+ㅣ1개월수익률);
    
                    String ㅣ3개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                    System.out.println("3개월 수익률: "+ㅣ3개월수익률);
    
                    String ㅣ6개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[3]/td")).getText();
                    System.out.println("6개월 수익률: "+ㅣ6개월수익률);
    
                    String ㅣ1년수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[4]/td")).getText();
                    System.out.println("1년 수익률: "+ㅣ1년수익률);
            }
            }else if(size==4){      //투자 정보의 첫 칸의 갯수가 4개인 종목에 대한 출력
                String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                System.out.println("시가 총액: "+시가총액);
        
                String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                System.out.println("시가 총액순위 : "+시가총액순위);
    
                String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                System.out.println("상장 주식수: "+상장주식수);
    
                String 액면가매매단위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[4]/td")).getText();
                System.out.println("액면가 매매단위: "+액면가매매단위);
    
                String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
    
                String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
    
                String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                System.out.println("외국인 소진율(B/A): "+외국인소진율);
    
                String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
    
                String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                System.out.println("PER|EPS: "+PEREPS);
    
                String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                System.out.println("추정 PER|EPS: "+추정PEREPS);
    
                String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                System.out.println("PBR|BPS: "+PBRBPS);
    
                String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                System.out.println("배당 수익률: "+배당수익률);
    
                String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                System.out.println("동일업종 PER: "+동일업종PER);
    
                String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                System.out.println("동일업종 등락률: "+동일업종등락률);
            }
        }
    //클로링하던 웹 페이지를 닫음.
    driver.close();
}
//-----------------------------------------------------------------------------------------------------------
    public List<String> getItemName3(){
        
        List<String> itemNameList3 =new ArrayList<>();
        System.out.println("========= TOP종목 - 상승 =========");
        //try을 통한 예외 처리.
        try{
            driver.get(URL);
            
            //시가 총액 상위 15개의 회사의 갯수를 센다.
            int row =driver.findElements(By.xpath("//*[@id=\"_topItems2\"]/tr")).size();

            System.out.println("상승 row 수는 : "+row);

            for(int i=0; i<row ; i++){
                //Element(요소)의 속성값을 가져오는 .getAttribute를 사용하여 순위별 값을 가져옴
                //각 사이트 별 주소를 가져온다.
                String data= driver.findElement(By.xpath("//*[@id=\"_topItems2\"]/tr["+(i+1)+"]/th/a")).getAttribute("href");
                //각 사이트는 https://finance.naver.com/item/main.naver?code=000000 으로 나오는데
                int index = data.lastIndexOf("=");  //"="을 기준으로 뒤에 있는 숫자 6자리만 가져오도록 설정한다.
                String code=data.substring(index+1).trim(); //substring - 문자열 자르기 (=split) , .trim() 문자열 앞 뒤의 공백을 제거함.
                itemNameList3.add(code);

            }
            }catch(Exception e){
                e.printStackTrace();
            }
                return itemNameList3;
        }

    public void getItemInfo3(List<String> resultList3){
        //불러온 웹 크롤링 정보 및 주소 출력하기.
        int i=0;
        for(String code: resultList3){
            System.out.println();
            System.out.println("---실시간 TOP "+(i+=1)+"위---");

        //웹 크롤링 주소 출력
            String param="?";
            param +="code="+ code;

            String newUrl= URL+param;
            System.out.println(newUrl);

            driver.get(newUrl);
            //웹 크롤링 정보 출력하기.
            //사이트 기준 우측 투자정보에 대한 데이터를 가져와 출력해준다.
            String 종목이름 =driver.findElement(By.xpath("//*[@id=\"middle\"]/div[1]/div[1]/h2/a")).getText();
            System.out.println("종목 이름: "+종목이름+"("+code+")");
            int size = driver.findElements(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr")).size();
            
            try{
                File scrFile = ((TakesScreenshot)driver.findElement(By.xpath("//*[@id=\"chart_area\"]"))).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("D:/workspace/java/base/src/project/주식 차트/TOP 상승/"+i+"."+종목이름+".png"));
                }catch(IOException e){
                    e.printStackTrace();
                }
            if(size==2){
            String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
            System.out.println("시가 총액: "+시가총액);
        
            String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
            System.out.println("상장주식수 : "+상장주식수);
    
            String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr/td")).getText();
            System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
            }else if(size==3){
                if(종목이름.contains("우드랩")){
                    String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                System.out.println("시가 총액: "+시가총액);
        
                String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                System.out.println("시가 총액순위 : "+시가총액순위);
    
                String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                System.out.println("상장 주식수: "+상장주식수);
    
                String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
    
                String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
    
                String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                System.out.println("외국인 소진율(B/A): "+외국인소진율);
    
                String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
    
                String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                System.out.println("PER|EPS: "+PEREPS);
    
                String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                System.out.println("추정 PER|EPS: "+추정PEREPS);
    
                String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                System.out.println("PBR|BPS: "+PBRBPS);
    
                String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                System.out.println("배당 수익률: "+배당수익률);
    
                String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                System.out.println("동일업종 PER: "+동일업종PER);
    
                String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                System.out.println("동일업종 등락률: "+동일업종등락률);
                }else{
                String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                    System.out.println("시가 총액: "+시가총액);
        
                    String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                    System.out.println("상장주식수 : "+상장주식수);
    
                    String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td")).getText();
                    System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                    String 기초지수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                    System.out.println("기초지수: "+기초지수);
    
                    String 유형 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                    System.out.println("유형: "+유형);
    
                    String 상장일 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                    System.out.println("상장일: "+상장일);
    
                    String 펀드보수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                    System.out.println("펀드보수: "+펀드보수);
    
                    String 자산운용사 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                    System.out.println("자산운용사: "+자산운용사);
    
                    String NAV =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody/tr[1]/td")).getText();
                    System.out.println("NAV: "+NAV);
    
                    String ㅣ1개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                    System.out.println("1개월 수익률: "+ㅣ1개월수익률);
    
                    String ㅣ3개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                    System.out.println("3개월 수익률: "+ㅣ3개월수익률);
    
                    String ㅣ6개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[3]/td")).getText();
                    System.out.println("6개월 수익률: "+ㅣ6개월수익률);
    
                    String ㅣ1년수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[4]/td")).getText();
                    System.out.println("1년 수익률: "+ㅣ1년수익률);
                }
            }else if(size==4){
                String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                System.out.println("시가 총액: "+시가총액);
        
                String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                System.out.println("시가 총액순위 : "+시가총액순위);
    
                String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                System.out.println("상장 주식수: "+상장주식수);
    
                String 액면가매매단위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[4]/td")).getText();
                System.out.println("액면가 매매단위: "+액면가매매단위);
    
                String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
    
                String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
    
                String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                System.out.println("외국인 소진율(B/A): "+외국인소진율);
    
                String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
    
                String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
    
                String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                System.out.println("PER|EPS: "+PEREPS);
    
                String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                System.out.println("추정 PER|EPS: "+추정PEREPS);
    
                String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                System.out.println("PBR|BPS: "+PBRBPS);
    
                String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                System.out.println("배당 수익률: "+배당수익률);
    
                String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                System.out.println("동일업종 PER: "+동일업종PER);
    
                String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                System.out.println("동일업종 등락률: "+동일업종등락률);
            }
        }
    driver.close();
    }
//-----------------------------------------------------------------------------------------------------------
    public List<String> getItemName4(){
        List<String> itemNameList4 =new ArrayList<>();
        System.out.println("========= TOP종목 - 하락 =========");
        //try을 통한 예외 처리.
        try{
            driver.get(URL);
                        
            //시가 총액 상위 15개의 회사의 갯수를 센다.
            int row =driver.findElements(By.xpath("//*[@id=\"_topItems3\"]/tr")).size();
            
            System.out.println("하락 row 수는 : "+row);
            
            for(int i=0; i<row ; i++){
                //Element(요소)의 속성값을 가져오는 .getAttribute를 사용하여 순위별 값을 가져옴
                //각 사이트 별 주소를 가져온다.
                String data= driver.findElement(By.xpath("//*[@id=\"_topItems3\"]/tr["+(i+1)+"]/th/a")).getAttribute("href");
                //각 사이트는 https://finance.naver.com/item/main.naver?code=000000 으로 나오는데
                int index = data.lastIndexOf("=");  //"="을 기준으로 뒤에 있는 숫자 6자리만 가져오도록 설정한다.
                String code=data.substring(index+1).trim(); //substring - 문자열 자르기 (=split) , .trim() 문자열 앞 뒤의 공백을 제거함
                itemNameList4.add(code);
            
                }
            }catch(Exception e){
                e.printStackTrace();
            }
                return itemNameList4;
            }
            
            public void getItemInfo4(List<String> resultList4){
                int i=0;
                //불러온 웹 크롤링 정보 및 주소 출력하기.
                for(String code: resultList4){
                    System.out.println();
                    System.out.println("---실시간 TOP "+(i+=1)+"위---");
                    //웹 크롤링 주소 출력
                    String param="?";
                    param +="code="+ code;
            
                    String newUrl= URL+param;
                    System.out.println(newUrl);
            
                    driver.get(newUrl);
                    //웹 크롤링 정보 출력하기.
                    //사이트 기준 우측 투자정보에 대한 데이터를 가져와 출력해준다.
                String 종목이름 =driver.findElement(By.xpath("//*[@id=\"middle\"]/div[1]/div[1]/h2/a")).getText();
                System.out.println("종목 이름: "+종목이름+"("+code+")");

                    int size = driver.findElements(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr")).size();
                    
                    try{
                        File scrFile = ((TakesScreenshot)driver.findElement(By.xpath("//*[@id=\"chart_area\"]"))).getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(scrFile, new File("D:/workspace/java/base/src/project/주식 차트/TOP 하락/"+i+"."+종목이름+".png"));
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    if(size==2){
                    String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                    System.out.println("시가 총액: "+시가총액);
                
                    String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                    System.out.println("상장주식수 : "+상장주식수);
            
                    String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr/td")).getText();
                    System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
                    }else if(size==3){
                        if(종목이름.contains("우드랩")){
                            String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                        System.out.println("시가 총액: "+시가총액);
                
                        String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                        System.out.println("시가 총액순위 : "+시가총액순위);
            
                        String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                        System.out.println("상장 주식수: "+상장주식수);
            
                        String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                        System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
            
                        String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                        System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
            
                        String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                        System.out.println("외국인 소진율(B/A): "+외국인소진율);
            
                        String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                        System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
            
                        String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                        System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
            
                        String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                        System.out.println("PER|EPS: "+PEREPS);
            
                        String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                        System.out.println("추정 PER|EPS: "+추정PEREPS);
            
                        String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                        System.out.println("PBR|BPS: "+PBRBPS);
            
                        String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                        System.out.println("배당 수익률: "+배당수익률);
            
                        String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                        System.out.println("동일업종 PER: "+동일업종PER);
            
                        String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                        System.out.println("동일업종 등락률: "+동일업종등락률);
                        }else{
                        String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                        System.out.println("시가 총액: "+시가총액);
                
                        String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                        System.out.println("상장주식수 : "+상장주식수);
            
                        String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td")).getText();
                        System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
            
                        String 기초지수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                        System.out.println("기초지수: "+기초지수);
            
                        String 유형 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                        System.out.println("유형: "+유형);
            
                        String 상장일 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                        System.out.println("상장일: "+상장일);
            
                        String 펀드보수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                        System.out.println("펀드보수: "+펀드보수);
            
                        String 자산운용사 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                        System.out.println("자산운용사: "+자산운용사);
            
                        String NAV =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody/tr[1]/td")).getText();
                        System.out.println("NAV: "+NAV);
            
                        String ㅣ1개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                        System.out.println("1개월 수익률: "+ㅣ1개월수익률);
            
                        String ㅣ3개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                        System.out.println("3개월 수익률: "+ㅣ3개월수익률);
            
                        String ㅣ6개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[3]/td")).getText();
                        System.out.println("6개월 수익률: "+ㅣ6개월수익률);
            
                        String ㅣ1년수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[4]/td")).getText();
                        System.out.println("1년 수익률: "+ㅣ1년수익률);
                        }
                    }else if(size==4){
                        String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                        System.out.println("시가 총액: "+시가총액);
                
                        String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                        System.out.println("시가 총액순위 : "+시가총액순위);
            
                        String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
                        System.out.println("상장 주식수: "+상장주식수);
            
                        String 액면가매매단위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[4]/td")).getText();
                        System.out.println("액면가 매매단위: "+액면가매매단위);
            
                        String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                        System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);
            
                        String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                        System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);
            
                        String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                        System.out.println("외국인 소진율(B/A): "+외국인소진율);
            
                        String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                        System.out.println("투자의견 | 목표주가: "+투자의견목표주가);
            
                        String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                        System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
            
                        String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
                        System.out.println("PER|EPS: "+PEREPS);
            
                        String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
                        System.out.println("추정 PER|EPS: "+추정PEREPS);
            
                        String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
                        System.out.println("PBR|BPS: "+PBRBPS);
            
                        String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
                        System.out.println("배당 수익률: "+배당수익률);
            
                        String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                        System.out.println("동일업종 PER: "+동일업종PER);
            
                        String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                        System.out.println("동일업종 등락률: "+동일업종등락률);
                    }
            }
    driver.close();
}
//-------------------------------------------------------------------------------------------------------------------------------------------------
    public List<String> getItemName(){
        List<String> itemNameList =new ArrayList<>();
        System.out.println("========= TOP종목 - 시가총액 상위 =========");
        //try을 통한 예외 처리.
        try{
        driver.get(URL);
                                    
            //시가 총액 상위 15개의 회사의 갯수를 센다.
            int row =driver.findElements(By.xpath("//*[@id=\"_topItems4\"]/tr")).size();
                        
            System.out.println("시가총액 상위 row 수는 : "+row);
                        
            for(int i=0; i<row ; i++){
            //Element(요소)의 속성값을 가져오는 .getAttribute를 사용하여 순위별 값을 가져옴
            //각 사이트 별 주소를 가져온다.
            String data= driver.findElement(By.xpath("//*[@id=\"_topItems4\"]/tr["+(i+1)+"]/th/a")).getAttribute("href");
            //각 사이트는 https://finance.naver.com/item/main.naver?code=000000 으로 나오는데
            int index = data.lastIndexOf("=");  //"="을 기준으로 뒤에 있는 숫자 6자리만 가져오도록 설정한다.
            String code=data.substring(index+1).trim(); //substring - 문자열 자르기 (=split) , .trim() 문자열 앞 뒤의 공백을 제거함.                        
            itemNameList.add(code);
            }
        }catch(Exception e){
        e.printStackTrace();
        }
        return itemNameList;
        }
                        
    public void getItemInfo(List<String> resultList){
        int i=0;
        //불러온 웹 크롤링 정보 및 주소 출력하기.
        for(String code: resultList){
            System.out.println();
            System.out.println("---실시간 TOP "+(i+=1)+"위---");
                        
        //웹 크롤링 주소 출력
        String param="?";
        param +="code="+ code;
                        
        String newUrl= URL+param;
        System.out.println(newUrl);
                        
        driver.get(newUrl);
        //웹 크롤링 정보 출력하기.
        //사이트 기준 우측 투자정보에 대한 데이터를 가져와 출력해준다.
        String 종목이름 =driver.findElement(By.xpath("//*[@id=\"middle\"]/div[1]/div[1]/h2/a")).getText();
        System.out.println("종목 이름: "+종목이름+"("+code+")");
                        
        int size = driver.findElements(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr")).size();
        try{
            File scrFile = ((TakesScreenshot)driver.findElement(By.xpath("//*[@id=\"chart_area\"]"))).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("D:/workspace/java/base/src/project/주식 차트/TOP 시가총액 상위/"+i+"."+종목이름+".png"));
            }catch(IOException e){
                e.printStackTrace();
            }
        if(size==2){
        String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
        System.out.println("시가 총액: "+시가총액);
    
        String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
        System.out.println("상장주식수 : "+상장주식수);

        String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr/td")).getText();
        System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);
        }else if(size==3){
            if(종목이름.contains("우드랩")){
                String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
            System.out.println("시가 총액: "+시가총액);
    
            String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
            System.out.println("시가 총액순위 : "+시가총액순위);

            String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
            System.out.println("상장 주식수: "+상장주식수);

            String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
            System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);

            String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
            System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);

            String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
            System.out.println("외국인 소진율(B/A): "+외국인소진율);

            String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
            System.out.println("투자의견 | 목표주가: "+투자의견목표주가);

            String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
            System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);

            String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
            System.out.println("PER|EPS: "+PEREPS);

            String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
            System.out.println("추정 PER|EPS: "+추정PEREPS);

            String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
            System.out.println("PBR|BPS: "+PBRBPS);

            String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
            System.out.println("배당 수익률: "+배당수익률);

            String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
            System.out.println("동일업종 PER: "+동일업종PER);

            String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
            System.out.println("동일업종 등락률: "+동일업종등락률);
            }else{
            String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
                System.out.println("시가 총액: "+시가총액);
    
                String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
                System.out.println("상장주식수 : "+상장주식수);

                String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td")).getText();
                System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);

                String 기초지수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
                System.out.println("기초지수: "+기초지수);

                String 유형 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
                System.out.println("유형: "+유형);

                String 상장일 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
                System.out.println("상장일: "+상장일);

                String 펀드보수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
                System.out.println("펀드보수: "+펀드보수);

                String 자산운용사 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
                System.out.println("자산운용사: "+자산운용사);

                String NAV =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody/tr[1]/td")).getText();
                System.out.println("NAV: "+NAV);

                String ㅣ1개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
                System.out.println("1개월 수익률: "+ㅣ1개월수익률);

                String ㅣ3개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
                System.out.println("3개월 수익률: "+ㅣ3개월수익률);

                String ㅣ6개월수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[3]/td")).getText();
                System.out.println("6개월 수익률: "+ㅣ6개월수익률);

                String ㅣ1년수익률=driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[4]/td")).getText();
                System.out.println("1년 수익률: "+ㅣ1년수익률);
            }
        }else if(size==4){
            String 시가총액 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[1]/td")).getText();
            System.out.println("시가 총액: "+시가총액);
    
            String 시가총액순위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[2]/td")).getText();
            System.out.println("시가 총액순위 : "+시가총액순위);

            String 상장주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[3]/td/em")).getText();
            System.out.println("상장 주식수: "+상장주식수);

            String 액면가매매단위 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[1]/table/tbody/tr[4]/td")).getText();
            System.out.println("액면가 매매단위: "+액면가매매단위);

            String 외국인한도주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[1]/td")).getText();
            System.out.println("외국인 한도 주식 수(A): "+외국인한도주식수);

            String 외국인보유주식수 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[2]/td")).getText();
            System.out.println("외국인 보유 주식 수(B): "+외국인보유주식수);

            String 외국인소진율 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[2]/table/tbody/tr[3]/td")).getText();
            System.out.println("외국인 소진율(B/A): "+외국인소진율);

            String 투자의견목표주가 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[1]/td")).getText();
            System.out.println("투자의견 | 목표주가: "+투자의견목표주가);

            String ㅣ52주최고52주최저 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[3]/table/tbody/tr[2]/td")).getText();
            System.out.println("52주최고 | 52주최저: "+ㅣ52주최고52주최저);

            String PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[1]/tr/td")).getText();
            System.out.println("PER|EPS: "+PEREPS);

            String 추정PEREPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[1]/td")).getText();
            System.out.println("추정 PER|EPS: "+추정PEREPS);

            String PBRBPS =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[2]/td")).getText();
            System.out.println("PBR|BPS: "+PBRBPS);

            String 배당수익률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[4]/table/tbody[2]/tr[3]/td")).getText();
            System.out.println("배당 수익률: "+배당수익률);

            String 동일업종PER =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[1]/td")).getText();
            System.out.println("동일업종 PER: "+동일업종PER);

            String 동일업종등락률 =driver.findElement(By.xpath("//*[@id=\"tab_con1\"]/div[5]/table/tbody/tr[2]/td")).getText();
            System.out.println("동일업종 등락률: "+동일업종등락률);
        }
}
    driver.close();      
    }
}


    
    

