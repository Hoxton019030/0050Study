package org.example;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        File file = new File("src/main/resources/0050.json");
        String s = Method.readFileAsString(file);
//        System.out.println(s);

        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject chart = jsonObject.getJSONObject("chart");
        JSONArray result = chart.getJSONArray("result");
        JSONObject jsonObject1 = result.getJSONObject(0);
        JSONArray timestamp = jsonObject1.getJSONArray("timestamp");
        // 日期格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Object o : timestamp) {
            long unixTime = Long.parseLong(o.toString());
            LocalDate date = Instant.ofEpochSecond(unixTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            System.out.println(date.format(formatter));
        }

//        String currency = meta.getString("currency");
//        String result = chart.getString("error");
//        System.out.println(result);
    }
}