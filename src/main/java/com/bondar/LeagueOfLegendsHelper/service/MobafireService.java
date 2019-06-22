package com.bondar.LeagueOfLegendsHelper.service;

import com.bondar.LeagueOfLegendsHelper.constant.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.bondar.LeagueOfLegendsHelper.constant.Constant.HREF_ATTRIBUTE;

@Service
public class MobafireService {

    private final RiotService riotService;

    @Autowired
    public MobafireService(RiotService riotService) {
        this.riotService = riotService;
    }

    public String findBuildLink(String champion) throws IOException {
        Document document = Jsoup.connect(Constant.MOBAFIRE_CHAMPIONS_URL).followRedirects(false).get();
        Elements championLinks = document.getElementsByAttribute(HREF_ATTRIBUTE);
        String relativeLink = championLinks
                .stream()
                .map(e -> e.attr(HREF_ATTRIBUTE))
                .filter(s -> s.contains(champion.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("findBuildLink() exception"));

        return htmlModifier(elementsRemover(relativeLink));
    }

    private Document elementsRemover(String relativeLink) throws IOException {
        Document document = Jsoup.connect(Constant.MOBAFIRE + relativeLink + "/stats").followRedirects(true).get();
        String link = Constant.MOBAFIRE + relativeLink + "/stats";
        document.getElementsByTag("script").remove();
        document.getElementById("collapsing-header").remove();
        document.getElementById("headerHolder").remove();
        document.getElementById("build-view").remove();
        document.getElementById("footer").remove();
        document.getElementsByClass("header-wrap").remove();
        document.getElementsByClass("champ-build__section__header__meta").remove();
        document.getElementsByClass("col-right").remove();
        document.getElementsByClass("champ-tabs__search").remove();

        return document;
    }

    private String htmlModifier(Document document) {
        String html = document.toString();
        html = html.substring(0, html.indexOf("<style>"));
        html = html.replaceAll("href=\"/", "href=\"https://mobafire.com/");
        html = html.replaceAll("src=\"/", "src=\"https://mobafire.com/");

        return html;
    }

    public String getBuild(String summonerName) throws Exception {
        return findBuildLink(riotService.getChampionName(summonerName));
    }
}
