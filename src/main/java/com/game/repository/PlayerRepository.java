package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    String query = "select p from Player p where " +
            "p.name like ?1 " +
            "and p.title like ?2 " +
            "and LOWER(p.race) like LOWER(?3) " +
            "and LOWER(p.profession) like LOWER(?4) " +
            "and LOWER(p.birthday) between ?5 and ?6 " +
            "and p.banned in (?7, ?8) " +
            "and p.experience between ?9 and ?10 " +
            "and p.level between ?11 and ?12";

    @Query(query)
    Page<Player> findAll(String name,                   // 1
                         String title,                  // 2
                         String race,                   // 3
                         String profession,             // 4
                         Date after,                    // 5
                         Date before,                   // 6
                         boolean banned1,                // 7
                         boolean banned2,                // 8
                         Integer minExperience,         // 9
                         Integer maxExperience,         // 10
                         Integer minLevel,              // 11
                         Integer maxLevel,              // 12
                         Pageable pageable);            // 13

    @Query(query)
    List<Player> findAll(String name,                   // 1
                         String title,                  // 2
                         String race,                   // 3
                         String profession,             // 4
                         Date after,                    // 5
                         Date before,                   // 6
                         boolean banned1,                // 7
                         boolean banned2,                // 8
                         Integer minExperience,         // 9
                         Integer maxExperience,         // 10
                         Integer minLevel,              // 11
                         Integer maxLevel);              // 12
}
