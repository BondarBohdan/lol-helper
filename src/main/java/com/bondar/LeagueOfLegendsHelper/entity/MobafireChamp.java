package com.bondar.LeagueOfLegendsHelper.entity;

import lombok.Data;

@Data
public class MobafireChamp {
    private Long id;
    private String champion;
    private String mobafireId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    public String getMobafireId() {
        return mobafireId;
    }

    public void setMobafireId(String mobafireId) {
        this.mobafireId = mobafireId;
    }
}
