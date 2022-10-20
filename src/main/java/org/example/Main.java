package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
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
            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 1024);

            for (int i=0; i < jsonArray.length(); i++) {
                JSONArray item = new JSONArray(jsonArray.get(i).toString());
                JSONObject object;
                Iterator keyIterator;
                if (i ==0 ) {
                    for (int j=0; j < item.length(); j++) {
                        object = new JSONObject(item.get(j).toString());
                        keyIterator = object.keys();
                        while (keyIterator.hasNext()) {
                            var name = keyIterator.next().toString();
                            csvWriter.write(name);
                            if (j < item.length()-1) csvWriter.write(",");
                            if (j == item.length()-1) csvWriter.write("\n");

                        }
                    }
                }

                for (int j=0; j < item.length(); j++) {
                    object = new JSONObject(item.get(j).toString());
                    keyIterator = object.keys();
                    while (keyIterator.hasNext()) {
                        var name = keyIterator.next().toString();
                        csvWriter.write(object.get(name).toString());
                        if (j < item.length()-1) csvWriter.write(",");
                        if (j == item.length()-1) csvWriter.write("\n");
                    }
                }
            }
            csvWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}