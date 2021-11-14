package com.example.MyServer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DisclosureRequestService {

    public static final String URL_DISCLOUSRE_FILE = "https://opendart.fss.or.kr/api/document.xml";

    public static final String KEY_API_KEY = "crtfc_key";
    public static final String API_KEY = "b1117710ff85f5aec0183f030d7a71f5132b4370";
    public static final String KEY_RECEPT_NUM = "rcept_no";

    public String getRate(String receptNum) {
        String requestUrl = URL_DISCLOUSRE_FILE + "?" + KEY_API_KEY + "=" + API_KEY + "&" + KEY_RECEPT_NUM + "=" + receptNum;
        String html = getHtml(requestUrl);
        String rate = getRate1(html);
        return rate;
    }

    private String getHtml(String requestUrl) {
        String html = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                ZipEntry zipEntry;
                while((zipEntry = zipInputStream.getNextEntry()) != null) {
//                    String name = zipEntry.getName();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream, "euc-kr"));
                    while(true) {
                        String line = br.readLine();
                        if(line == null) {
                            break;
                        }
                        sb.append(line);
                    }
                    html = sb.toString();
                    br.close();
                }
                zipInputStream.close();
            }

        } catch(Exception e) { }

        return html;
    }

    private String getRate1(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("td");
        String rate = null;

        try {
            for (Element element : elements) {
                String tdName = element.text();
                if (tdName.equals("매출액")) {
                    Element rateElement = element.nextElementSibling()
                            .nextElementSibling()
                            .nextElementSibling()
                            .nextElementSibling()
                            .nextElementSibling()
                            .nextElementSibling();

                    rate = rateElement.text();
                    rate = rate.split("%")[0];
                    String[] rateInfo = rate.split("\\(");
                    if(rateInfo.length > 1) {
                        rate = rateInfo[1].trim();
                    }
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rate;
    }


}
