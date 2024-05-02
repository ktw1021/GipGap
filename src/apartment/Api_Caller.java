package apartment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Api_Caller {
	
	private Api_Caller_Code code;
	
	public Api_Caller(Api_Caller_Code code) {
		this.code=code;
	}
	public Api_Caller() {
		
	}
	
    private String serviceKey = "aFaYNwN4cILVTvYDfYL3Cq37TtGoDHXLhbbk2qfEwXuVVhMMUtTdaJCoFAP/0/22YRvXvWs9OKOQB036Tj31Rg==";
    private String lawdCd;
    private String dealYmd = "202404";

    public String callApi(int pageNo, int numOfRows) {
    	
    	Api_Caller_Code code = new Api_Caller_Code();
    	
    	dealYmd = Main_App.getYm();
    	
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(Xml_Parser_Code.getAddress(), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(dealYmd, "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300 ?
                    conn.getInputStream() : conn.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseXml(String xmlData) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");
            StringBuilder results = new StringBuilder();
            results.append("주요 거래 정보:\n");

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String 거래년 = element.getElementsByTagName("년").item(0).getTextContent().trim();
                    String 거래월 = element.getElementsByTagName("월").item(0).getTextContent().trim();
                    String 거래일 = element.getElementsByTagName("일").item(0).getTextContent().trim();
                    String 거래금액 = element.getElementsByTagName("거래금액").item(0).getTextContent().trim();
                    String 아파트 = element.getElementsByTagName("아파트").item(0).getTextContent().trim();
                    String 도로명 = element.getElementsByTagName("도로명").item(0).getTextContent().trim();
                    String 전용면적 = element.getElementsByTagName("전용면적").item(0).getTextContent().trim();
                    String 층 = element.getElementsByTagName("층").item(0).getTextContent().trim();
                    String 법정동 = element.getElementsByTagName("법정동").item(0).getTextContent().trim();
                    
                    거래금액 = 거래금액.replace(",","");
                    long parse;
                    parse = (long) (Long.parseLong(거래금액));
                    double amount = parse/10000.0;
                    
                    results.append(String.format("%s년 %s월 %s일 | 법정동: %s | 도로명: %s | 아파트: %s | 거래금액: %.1f억 | 전용면적: %s㎡ | %s층\n",
                            거래년, 거래월, 거래일, 법정동, 도로명, 아파트, amount, 전용면적, 층));
                }
            }
            return results.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "XML 파싱 중 오류 발생";
        }
    }
}
