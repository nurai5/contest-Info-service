package kg.mkk.gov.contestinfoservice.contestParser;

import kg.mkk.gov.contestinfoservice.telegram.TelegramBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContestParsingService {

    private final ContestRepository contestRepository;
    private final ContestService contestService;
    private final TelegramBot telegramBot;


    @Autowired
    public ContestParsingService(ContestRepository contestRepository,
                                 ContestService contestService,
                                 TelegramBot telegramBot) {
        this.contestRepository = contestRepository;
        this.contestService = contestService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0 9,18 * * ?")
    public void fetchAndParseHtml() throws URISyntaxException {
        String url = "https://mkk.gov.kg/ru/category/%d0%ba%d0%be%d0%bd%d0%ba%d1%83%d1%80%d1%81%d1%8b-%d0%bf%d0%be-%d1%86%d0%b5%d0%bd%d1%82%d1%80%d0%b0%d0%bb%d1%8c%d0%bd%d1%8b%d0%bc-%d0%b3%d0%be%d1%81%d1%83%d0%b4%d0%b0%d1%80%d1%81%d1%82%d0%b2%d0%b5/";
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.set("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.set("Connection", "keep-alive");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, String.class);
        String htmlContent = response.getBody();
        System.out.println(htmlContent);

        parseAndSaveData(htmlContent);
    }

    public static LocalDate extractDate(String url) {
        String regex = "ru/(\\d{4})/(\\d{2})/(\\d{2})/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String year = matcher.group(1);
            String month = matcher.group(2);
            String day = matcher.group(3);

            return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        } else {
            return LocalDate.now();
        }
    }

    public void parseAndSaveData(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements elements = doc.select("article.d-md-flex.mg-posts-sec-post.align-items-center");

        for (Element element : elements) {
            String title = element.select("h4.entry-title.title a").text();
            String description = element.select("div.mg-content p").text();
            String url = element.select("h4.entry-title.title a").attr("href");
            LocalDate date = extractDate(url);

            Elements tagElements = element.select("div.mg-blog-category a");
            Map<ETag, String> tags = new HashMap<>();

            for (Element tagElement : tagElements) {
                String tagName = tagElement.text().toUpperCase();
                try {
                    ETag tag = ETag.fromDescription(tagName);
                    tags.put(tag, tag.getDescription());
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown tag: " + tagName);
                }
            }

            Optional<Contest> existingContest = contestRepository.findByUrl(url);
            if (existingContest.isPresent()) {
                continue;
            }

            Contest contest = new Contest();
            contest.setTitle(title);
            contest.setDescription(description);
            contest.setDate(date);
            contest.setUrl(url);
            contest.setTags(tags);
            contestService.createContest(contest);

            this.sendTelegramMessage(contest);
        }
    }

    public void sendTelegramMessage(Contest contest) {
        telegramBot.sendMessageToChannel(contest);
    }
}
