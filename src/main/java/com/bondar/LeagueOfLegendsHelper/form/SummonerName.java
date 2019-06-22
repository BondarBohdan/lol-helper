package com.bondar.LeagueOfLegendsHelper.form;

import lombok.Data;

@Data
public class SummonerName {
    private String summonerName;

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }
}
