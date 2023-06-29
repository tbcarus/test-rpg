package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    @Query("select p from Player p where " +
            "p.name like ?1 " +
            "and p.title like ?2 " +
            "and p.race like ?3 " +
            "and p.profession like ?4")
    Page<Player> findAll(String name,
                         String title,
                         String race,
                         String profession,
                         Long after,
                         Long before,
                         Boolean banned,
                         Integer minExperience,
                         Integer maxExperience,
                         Integer minLevel,
                         Integer maxLevel,
                         Pageable pageable);
}
