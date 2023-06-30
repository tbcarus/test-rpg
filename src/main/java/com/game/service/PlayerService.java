package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlayerService {
    final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers(String name,
                                   String title,
                                   Race race,
                                   Profession profession,
                                   Long after,
                                   Long before,
                                   Boolean banned,
                                   Integer minExperience,
                                   Integer maxExperience,
                                   Integer minLevel,
                                   Integer maxLevel,
                                   Pageable pageable) {

        return playerRepository.findAll(
                name.equals("%") ? name : "%" + name + "%",
                title.equals("%") ? title : "%" + title + "%",
                race == null ? "%" : race.name(),
                profession == null ? "%" : profession.name(),
                new Date(after),
                new Date(before),
                banned == null || banned, banned != null && banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                pageable).getContent();
    }

    public Player getById(Long id) {
        return playerRepository.findById(id).get();
    }

    public Integer count(String name,
                         String title,
                         Race race,
                         Profession profession,
                         Long after,
                         Long before,
                         Boolean banned,
                         Integer minExperience,
                         Integer maxExperience,
                         Integer minLevel,
                         Integer maxLevel) {
        return playerRepository.findAll(
                name.equals("%") ? name : "%" + name + "%",
                title.equals("%") ? title : "%" + title + "%",
                race == null ? "%" : race.name(),
                profession == null ? "%" : profession.name(),
                new Date(after),
                new Date(before),
                banned == null || banned, banned != null && banned,
                minExperience, maxExperience,
                minLevel, maxLevel).size();
    }

}
