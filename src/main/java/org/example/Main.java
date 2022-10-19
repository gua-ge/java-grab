package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String link = "https://www.qq.com/";
        try {
            URL url = new URL(link);
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
            String line, content;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            content = stringBuilder.toString();
            String matchEl = "<li\\s*?class=\"nav-item\">\\s*?<a\\s*?href=\"(.*?)\"\\s*?[^>]*>(.*?)</a>\\s*?</li>";

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
            File csvFile = new File("E:/qq_nav.csv");
            csvFile.createNewFile();
            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gb2312"), 1024);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}