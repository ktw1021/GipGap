package apartment;

import java.io.IOException;
import java.util.Scanner;

public class Main_App {
	
	private static String ym;
	
	public static String getYm() {
		return ym;
	}
	public static void setYm(String newYm) {
		ym = newYm;
	}

	public static void main(String[] args) {
    	
    	System.out.println("주소: 검색을 원하는 시군구를 입력하세요.");
    	Scanner sc = new Scanner(System.in);
    	String 주소 = sc.next();
    	
    	Api_Caller_Code apiService = new Api_Caller_Code();
        try {
            String response = apiService.fetchData(주소, 1, 3, "xml");
            Xml_Parser_Code parser = new Xml_Parser_Code();
            String xmlData = response;
            parser.parseXml(xmlData);
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
        System.out.println("조회할 연월을 입력하세요.");
        System.out.println("Ex) 2024년 4월의 경우, 202404");
        setYm(sc.next());
        
        
        Api_Caller apiCaller = new Api_Caller();
        // API 호출하기
        String xmlResponse = apiCaller.callApi(1, 500);  // 페이지 번호 1, 한 페이지당 결과 수 10으로 설정
        if (xmlResponse != null) {
            System.out.println("API 호출 성공, XML 응답 데이터:");
//            System.out.println(xmlResponse);

            // XML 파싱하고 결과 출력하기
            String parseResult = apiCaller.parseXml(xmlResponse);
            System.out.println(parseResult);
        } else {
            System.out.println("API 호출 실패 또는 응답이 비었습니다.");
        }
        
        sc.close();
    }
}
