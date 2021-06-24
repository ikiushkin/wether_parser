import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String[] args) throws Exception {
        Document page = getPage();
        //css query lang
        Element tableWth= page.select("table[class=wt]").first();

        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");

        int index = 0;

        for (Element name : names) {
            String dataString = name.select("th[id=dt]").text();
            String date = getDateFromString(dataString);

            //System.out.println(date + "   Явления    Температура    Давление    Влажность    Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }

    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from String!");
    }

    public static Document getPage() throws IOException {
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);

        return page;
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 20 - values.size();

        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + " | ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------------");
        }
        return iterationCount;
    }
}
