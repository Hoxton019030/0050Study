package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        File file = new File("src/main/resources/0050.json");
        String s = readFileAsString(file);

        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject chart = jsonObject.getJSONObject("chart");
        JSONArray result = chart.getJSONArray("result");
        JSONObject jsonObject1 = result.getJSONObject(0);
        JSONArray timestamp = jsonObject1.getJSONArray("timestamp");

        // 日期格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 準備保存結果的 JSON 數組
        JSONArray resultArray = new JSONArray();

        // 取得價格資訊
        JSONObject indicators = jsonObject1.getJSONObject("indicators");
        JSONArray quote = indicators.getJSONArray("quote");
        JSONObject priceInfo = quote.getJSONObject(0);
        JSONArray close = priceInfo.getJSONArray("close");

        // 將日期與價格對應
        for (int i = 0; i < timestamp.size(); i++) {
            long unixTime = timestamp.getLong(i);
            LocalDate date = Instant.ofEpochSecond(unixTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // 獲取對應的價格
            String price = close.getString(i);

            // 建立 JSON 物件並加入結果數組
            JSONObject dataPoint = new JSONObject();
            dataPoint.put("Date", date.format(formatter));
            dataPoint.put("price", price);
            resultArray.add(dataPoint);
        }

        // 將結果寫入 JSON 文件
        saveJsonToFile(resultArray, "output.json");
    }

    private static String readFileAsString(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveJsonToFile(JSONArray jsonArray, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonArray.toJSONString());
            System.out.println("JSON file created: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
