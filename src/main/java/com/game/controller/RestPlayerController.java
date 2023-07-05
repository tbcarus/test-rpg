package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class RestPlayerController {

    final PlayerService playerService;

    public RestPlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(value = "name", required = false, defaultValue = "%") String name,
                                                   @RequestParam(value = "title", required = false, defaultValue = "%") String title,
                                                   @RequestParam(value = "race", required = false) Race race,
                                                   @RequestParam(value = "profession", required = false) Profession profession,
                                                   @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "before", required = false) Long before,
                                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                   @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        Specification<Player> specification = Specification.where(playerService.filterByName(name)
                .and(playerService.filterByTitle(title))
                .and(playerService.filterByRace(race))
                .and(playerService.filterByProfession(profession))
                .and(playerService.filterByDate(after, before))
                .and(playerService.filterBanned(banned))
                .and(playerService.filterByRange("experience", minExperience, maxExperience))
                .and(playerService.filterByRange("level", minLevel, maxLevel)));
        return new ResponseEntity<>(playerService.getPlayers(specification, pageable), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public Player getById(@PathVariable String id) {
        return playerService.getById(Long.parseLong(id));
    }

    @GetMapping("/players/count")
    public Integer getPlayerCount(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "race", required = false) Race race,
                                  @RequestParam(value = "profession", required = false) Profession profession,
                                  @RequestParam(value = "after", required = false) Long after,
                                  @RequestParam(value = "before", required = false) Long before,
                                  @RequestParam(value = "banned", required = false) Boolean banned,
                                  @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                  @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                  @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                  @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        return playerService.count();
    }
}
