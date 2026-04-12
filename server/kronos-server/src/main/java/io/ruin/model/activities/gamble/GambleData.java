package io.ruin.model.activities.gamble;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;

import java.util.HashMap;

public class GambleData {
	public static HashMap<Integer, GambleData> instance = new HashMap<>();

	public Player playerOne;
	public Player playerTwo;
	public Player host;
	public Player participant;
	public int id;
	public HashMap<Integer, Long> items = new HashMap<>();
	public int playerOneBlackJackValue;
	public int playerTwoBlackJackValue;
	public Player blackJackTurn;
	public String blackJackPlayerOneRolls = "";
	public String blackJackPlayerTwoRolls = "";
	public boolean winnerDetermined = false;
	public boolean inBlackJack = false;
	public Position spawnPosition;


	public GambleData(int id) {
		instance.put(id, this);
		this.id = id;
	}


	public void setPlayers(Player playerOne, Player playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}


}