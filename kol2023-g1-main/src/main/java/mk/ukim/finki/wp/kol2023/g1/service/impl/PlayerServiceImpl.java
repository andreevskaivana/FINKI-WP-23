package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Player> listAllPlayers() {
        return this.playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
//        Player player=findById(id);
//        return player;

        //ako ima player go vraka inaku null vraka
        return this.playerRepository.findById(id).get();
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        //team znaci deka treba da se dodade team zavisnost i preku id da go najdeme timot
        Team t = teamRepository.findById(team).get();
        Player player = new Player(name, bio, pointsPerGame, position, t);
        return this.playerRepository.save(player);

    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player player = this.findById(id);
        Team t = teamRepository.findById(team).get();
        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(t);
        return this.playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = this.findById(id);
        this.playerRepository.delete(player);
        return player;
    }

    @Override
    public Player vote(Long id) {
        return playerRepository.findById(id).get();
    }


    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {

        List<Player> newPlayers = new ArrayList<>();

        if (pointsPerGame == null && position == null) {
            newPlayers = this.playerRepository.findAll();
        }
        if (pointsPerGame != null && position != null) {
            newPlayers = this.playerRepository.findByPointsPerGameLessThanAndPosition(pointsPerGame, position);
        }
        if (pointsPerGame != null && position == null) {
            newPlayers = this.playerRepository.findByPointsPerGameLessThan(pointsPerGame);
        }
        if (pointsPerGame == null && position != null) {
            newPlayers = this.playerRepository.findByPosition(position);
        }
        return newPlayers;
    }

}
