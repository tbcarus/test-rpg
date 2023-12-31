package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.error.BadRequestException;
import com.game.error.NotFoundException;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers(Specification<Player> specification, Pageable pageable) {

        return playerRepository.findAll(specification, pageable).getContent();
    }

    public Player getById(Long id) {
        return playerRepository.findById(id).get();
    }

    public Integer count(Specification<Player> specification) {
        return playerRepository.findAll(specification).size();
    }

    public Player createPlayer(Player player) {
        if (player.getName() == null || player.getTitle() == null || player.getRace() == null || player.getProfession() == null
                || player.getBirthday() == null || player.getExperience() == null) {
            throw new BadRequestException();
        }
        checkName(player.getName());
        checkTitle(player.getTitle());
        checkBirthday(player.getBirthday().getTime());
        checkExperience(player.getExperience());
        if (player.getBanned() == null) {
            player.setBanned(false);
        }
        int level = calcLvl(player.getExperience());
        int expToNextLvl = calcToNextLvl(level, player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(expToNextLvl);

        return playerRepository.save(player);
    }

    public Player getPlayer(String idStr) {
        long id = checkId(idStr);
        Optional<Player> opt = playerRepository.findById(id);
        if (!opt.isPresent()) {
            throw new NotFoundException();
        }
        return opt.get();
    }

    public void delete(String idStr) {
        long id = checkId(idStr);
        Optional<Player> opt = playerRepository.findById(id);
        if (!opt.isPresent()) {
            throw new NotFoundException();
        }
        playerRepository.deleteById(id);
    }

    public Player update(String idStr, Player upd) {
        long id = checkId(idStr);
        if (!playerRepository.existsById(id)) {
            throw new NotFoundException();
        }
        Player player = playerRepository.findById(id).get();

        if (upd.getName() != null) {
            checkName(upd.getName());
            player.setName(upd.getName());
        }
        if (upd.getTitle() != null) {
            checkTitle(upd.getTitle());
            player.setTitle(upd.getTitle());
        }
        if (upd.getRace() != null) {
            player.setRace(upd.getRace());
        }
        if (upd.getProfession() != null) {
            player.setProfession(upd.getProfession());
        }
        if (upd.getBirthday() != null) {
            checkBirthday(upd.getBirthday().getTime());
            player.setBirthday(upd.getBirthday());
        }
        if (upd.getBanned() != null) {
            player.setBanned(upd.getBanned());
        }
        if (upd.getExperience() != null) {
            checkExperience(upd.getExperience());
            int level = calcLvl(upd.getExperience());
            int expToNextLvl = calcToNextLvl(level, upd.getExperience());
            player.setLevel(level);
            player.setUntilNextLevel(expToNextLvl);
            player.setExperience(upd.getExperience());
        }
        return playerRepository.save(player);
    }

    private Long checkId(String idStr) {
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (Exception exc) {
            throw new BadRequestException();
        }
        if (id <= 0) {
            throw new BadRequestException();
        }
        return id;
    }

    private int calcLvl(int exp) {
        return (int) (Math.sqrt(2500 + 200 * exp) - 50) / 100;
    }

    private int calcToNextLvl(int level, int exp) {
        return 50 * (level + 1) * (level + 2) - exp;
    }

    private void checkName(String name) {
        if (name.length() > 12 || name.isEmpty()) {
            throw new BadRequestException();
        }
    }

    private void checkTitle(String title) {
        if (title.length() > 30) {
            throw new BadRequestException();
        }
    }

    private void checkExperience(Integer exp) {
        if (exp < 0 || exp > 10000000) {
            throw new BadRequestException();
        }
    }

    private void checkBirthday(Long bd) {
        if (bd < 0) {
            throw new BadRequestException();
        }
        int year = new Date(bd).getYear() + 1900;
        if (year < 2000 || year > 3000) {
            throw new BadRequestException();
        }
    }

    public Specification<Player> filterByName(String name) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }

    public Specification<Player> filterByTitle(String title) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("title"), "%" + title + "%");
            }
        };
    }

    public Specification<Player> filterByRace(Race race) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (race == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("race"), race);
            }
        };
    }

    public Specification<Player> filterByProfession(Profession profession) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (profession == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("profession"), profession);
            }
        };
    }

    public Specification<Player> filterByDate(Long after, Long before) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Date tempAfter = after == null ? new Date(0) : new Date(after);
                Date tempBefore = before == null ? new Date() : new Date(before);
                return criteriaBuilder.between(root.get("birthday"), tempAfter, tempBefore);
            }
        };
    }

    public Specification<Player> filterBanned(Boolean banned) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (banned == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("banned"), banned);
            }
        };
    }

    public Specification<Player> filterByRange(String field, Integer min, Integer max) {
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Integer tempMin = min == null ? 0 : min;
                Integer tempMax = max == null ? Integer.MAX_VALUE : max;
                return criteriaBuilder.between(root.get(field), tempMin, tempMax);
            }
        };
    }

}
