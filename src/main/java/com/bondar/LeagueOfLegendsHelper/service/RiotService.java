package com.bondar.LeagueOfLegendsHelper.service;

import com.bondar.LeagueOfLegendsHelper.constant.Constant;
import com.bondar.LeagueOfLegendsHelper.entity.Champion;
import com.bondar.LeagueOfLegendsHelper.entity.CurrentGameParticipant;
import com.bondar.LeagueOfLegendsHelper.entity.Spectator;
import com.bondar.LeagueOfLegendsHelper.entity.Summoner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@Service
public class RiotService {

    @Value("${riot.api}")
    private String riotKey;

    private final RestTemplate restTemplate;

    @Autowired
    public RiotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Summoner getSummonerInfo(String summonerName) throws Exception {
        try {
            return restTemplate.getForObject(
                    Constant.GET_SUMMONER_BY_NAME + summonerName
                            + "?api_key={riotKey}", Summoner.class, riotKey);
        } catch (HttpClientErrorException e) {
            throw new Exception("Summoner not found");
        }
    }

    private Champion getCurrentChampion(Summoner summoner) throws Exception {
        try {
            List<CurrentGameParticipant> participants = restTemplate.getForObject(
                    Constant.GET_ACTIVE_GAME_BY_SUMMONER +
                            summoner.getId() + "?api_key={riotKey}",
                    Spectator.class, riotKey).getParticipants();

            return getChampionInfo(participants
                    .stream()
                    .filter(p -> p.getSummonerName().equalsIgnoreCase(summoner.getName()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("getCurrentInfo() exception"))
                    .getChampionId());
        } catch (HttpClientErrorException e) {
            throw new Exception("Current summoner is not playing now.");
        }
    }

    private Champion getChampionInfo(Long key) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(Constant.CHAMPIONS_JSON_PATH));
        ObjectMapper mapper = new ObjectMapper();
        List<Champion> champions = mapper.readValue(reader, mapper.getTypeFactory().constructCollectionType(List.class, Champion.class));
        return champions.stream()
                .filter(c -> c.getKey().equals(key))
                .findAny()
                .orElseThrow(() -> new RuntimeException("getChampionInfo() exception"));

    }

    public String getChampionName(String summonerName) throws Exception {
        return getCurrentChampion(getSummonerInfo(summonerName)).getName();
    }

}
