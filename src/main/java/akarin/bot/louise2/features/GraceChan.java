package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.Setup;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author akarin
 * @version 1.0
 * @description sound voltex 插件
 * @date 2025/9/1 15:24
 */
@LouiseFeature(code = "grace-chan", name = "Grace 酱", prefix = "!")
public class GraceChan {

    private static final Map<Integer, String> gradeMap = new HashMap<>();

    private static final Map<Integer, String> difficultyMap = new HashMap<>();

    private static final String asphyxiaDBUrl = "/home/akarin/CodingLife/sdvx-tool/sdvx@asphyxia.db";

    private static final String sdvxDBUrl = "/home/akarin/CodingLife/sdvx-tool";

    private static final String musicDB = sdvxDBUrl + "/music_db.json";

    private static final JSONObject sdvxRecords = new JSONObject();

    private static final JSONObject musicDBRecords = new JSONObject();

    public GraceChan() {
        gradeMap.put(1, "D");
        gradeMap.put(2, "C");
        gradeMap.put(3, "B");
        gradeMap.put(4, "A");
        gradeMap.put(5, "A+");
        gradeMap.put(6, "AA");
        gradeMap.put(7, "AA+");
        gradeMap.put(8, "AAA");
        gradeMap.put(9, "AAA+");
        gradeMap.put(10, "S");
        gradeMap.put(11, "S+");
        gradeMap.put(12, "UC");
        gradeMap.put(13, "PUC");
    }

    public String difficultBanner(Integer difficult, Integer infVersion) {
        switch (difficult) {
            case 0:
                return "NDV";
            case 1:
                return "ADV";
            case 2:
                return "EXH";
            case 3: {
                switch (infVersion) {
                    case 2:
                        return "INF";
                    case 3:
                        return "GRV";
                    case 4:
                        return "HVN";
                    case 5:
                        return "VVD";
                    case 6:
                        return "XCD";
                }
            }
        }
        return "UNK";
    }

    @Setup
    public static void main(String args[]) throws Exception {

        GraceChan graceChan = new GraceChan();

        // 曲库
        try (Scanner scanner = new Scanner(new File(musicDB), StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    JSONObject record = JSONObject.parseObject(line);
                    musicDBRecords.put("music", record.getJSONObject("mdb").getJSONArray("music"));
                }
            }
        }

        // 读取 NeDB 文件
        try (Scanner scanner = new Scanner(new File(asphyxiaDBUrl), StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    JSONObject record = JSONObject.parseObject(line);

                    String id = (String) record.get("_id");
                    String collection = record.getString("collection");

                    if (record.getJSONObject("createdAt") != null) {
                        Long createdAt = record.getJSONObject("createdAt").getLong("$$date");
                        record.put("createTime", createdAt);
                    }
                    if (record.getJSONObject("updatedAt") != null) {
                        Long updatedAt = record.getJSONObject("updatedAt").getLong("$$date");
                        record.put("updateTime", updatedAt);
                    }

                    if ("music".equals(collection)) {
                        int index = record.getInteger("mid") + 1;
                        JSONArray musics = musicDBRecords.getJSONArray("music");
                        if (index >= musics.size())
                            record.put("title_name", "未收录");
                        else {
                            JSONObject music = musics.getJSONObject(index);
                            record.put("title_name", music.getJSONObject("info").getString("title_name"));

                            record.put("difficulty",
                                    graceChan.difficultBanner(record.getInteger("type"),
                                            Integer.valueOf(music.getJSONObject("info").getJSONObject("inf_ver").getString("#text"))));
                        }

                    }

                    ((JSONObject) sdvxRecords.computeIfAbsent(collection, k -> new JSONObject())).put(id, record);
                }
            }
        }

        // Asphyxia@SDVX 的所有记录
        JSONObject musicRecords = sdvxRecords.getJSONObject("music");

        String scoreFormatter = "%s %s[%4s](%4s) : %s";
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        List<JSONObject> bestScore = musicRecords.values().stream()
                .sorted((o1, o2) -> ((JSONObject) o2).getIntValue("score") - ((JSONObject) o1).getIntValue("score"))
                .limit(5)
                .map(o -> (JSONObject) o)
                .toList();

        System.out.println("\n前五高分记录: ");
        bestScore.stream().map(e -> String.format(scoreFormatter, df.format(e.getLong("createTime")), e.getInteger(
                "score"), e.getString("difficulty"), gradeMap.get(e.getInteger("grade")), e.getString("title_name"))).forEach(System.out::println);

        List<JSONObject> recent = musicRecords.values().stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getLong("createTime")).reversed())
                .limit(5)
                .map(o -> (JSONObject) o)
                .toList();


        System.out.println("\n最近游玩记录: ");
        recent.stream().map(e -> String.format(scoreFormatter, df.format(e.getLong("createTime")), e.getInteger(
                "score"), e.getString("difficulty"), gradeMap.get(e.getInteger("grade")), e.getString("title_name"))).forEach(System.out::println);
    }

}
