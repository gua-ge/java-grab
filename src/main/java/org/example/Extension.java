package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extension {
    public static void main(String[] args) {
        String link = null;
        //扩展
        //读取csv文件
        try {
            String line, content;

            File csvFile = new File("E:/qq_nav.csv");
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "gbk"));
            while ((line = csvReader.readLine()) != null) {
                String[] splitArray = line.split(",");
                if ("新闻".equals(splitArray[0])) {
                    //获取新闻频道的链接
                    link = splitArray[1];
                    link = link.replaceFirst("http", "https");
                    break;
                }
            }
            csvReader.close();

            URL newsUrl = new URL(link);

            BufferedReader reader = new BufferedReader(new InputStreamReader(newsUrl.openStream(), "gbk"));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            content = stringBuilder.toString();
            String matchEl = "<a\\s*?[^h]*href=\"(.*?)\"\\s*?[^>]*>(.*?)</a>";
            System.out.println(content);
            JSONArray jsonArray = new JSONArray();
            Pattern pattern = Pattern.compile(matchEl);
            Matcher m = pattern.matcher(content);
            while (m.find()) {
                JSONArray item = new JSONArray();
                item.put(new JSONObject().put("title", m.group(2)));
                item.put(new JSONObject().put("href", m.group(1)));
                jsonArray.put(item);
            }
            System.out.println(jsonArray);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
