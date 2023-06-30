package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                                                   @RequestParam(value = "after", required = false, defaultValue = "0") Long after,
                                                   @RequestParam(value = "before", required = false, defaultValue = "9223372036854775807") Long before,
                                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                                   @RequestParam(value = "minExperience", required = false, defaultValue = "0") Integer minExperience,
                                                   @RequestParam(value = "maxExperience", required = false, defaultValue = "2147483647") Integer maxExperience,
                                                   @RequestParam(value = "minLevel", required = false, defaultValue = "0") Integer minLevel,
                                                   @RequestParam(value = "maxLevel", required = false, defaultValue = "2147483647") Integer maxLevel,
                                                   @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return new ResponseEntity<>(playerService.getPlayers(
                name,
                title,
                race,
                profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                pageable), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public Player getById(@PathVariable String id) {
        return playerService.getById(Long.parseLong(id));
    }

    @GetMapping("/players/count")
    public Integer getPlayerCount(@RequestParam(value = "name", required = false, defaultValue = "%") String name,
                                  @RequestParam(value = "title", required = false, defaultValue = "%") String title,
                                  @RequestParam(value = "race", required = false) Race race,
                                  @RequestParam(value = "profession", required = false) Profession profession,
                                  @RequestParam(value = "after", required = false, defaultValue = "0") Long after,
                                  @RequestParam(value = "before", required = false, defaultValue = "9223372036854775807") Long before,
                                  @RequestParam(value = "banned", required = false) Boolean banned,
                                  @RequestParam(value = "minExperience", required = false, defaultValue = "0") Integer minExperience,
                                  @RequestParam(value = "maxExperience", required = false, defaultValue = "2147483647") Integer maxExperience,
                                  @RequestParam(value = "minLevel", required = false, defaultValue = "0") Integer minLevel,
                                  @RequestParam(value = "maxLevel", required = false, defaultValue = "2147483647") Integer maxLevel) {
        return playerService.count(
                name,
                title,
                race,
                profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel);
    }
}
