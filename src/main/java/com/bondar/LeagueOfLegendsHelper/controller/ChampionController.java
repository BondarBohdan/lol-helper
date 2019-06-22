package com.bondar.LeagueOfLegendsHelper.controller;

import com.bondar.LeagueOfLegendsHelper.form.SummonerName;
import com.bondar.LeagueOfLegendsHelper.service.MobafireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChampionController {

    private MobafireService mobafireService;

    @Autowired
    public ChampionController(MobafireService mobafireService) {
        this.mobafireService = mobafireService;
    }

    @PostMapping("/build")
    public Object getChampionInfo(Model model, @ModelAttribute SummonerName summonerName) throws Exception {
        return mobafireService.getBuild(summonerName.getSummonerName());
    }
}
