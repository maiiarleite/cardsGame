package com.klab.cardsGame.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klab.cardsGame.dto.CardsGameRecordDto;
import com.klab.cardsGame.model.CardsGameModel;
import com.klab.cardsGame.service.CardsGameService;
import com.klab.cardsGame.service.DeckOfCardsApi;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cards")
public class CardsGameController {

	private final DeckOfCardsApi deckOfCardsApi;
	private final CardsGameService cardsGameService;
	private final int qtdPlayer = 4;
	private final int qtdCards = 5;

	public CardsGameController(CardsGameService cardsGameService, DeckOfCardsApi deckOfCardsApi) {
		this.cardsGameService = cardsGameService;
		this.deckOfCardsApi = deckOfCardsApi;
	}

	@GetMapping("/start")
	public List<String> startGame() {
		List<String> playerHands = new ArrayList<String>();

		try {
			String deckId = extractId(createNewDeck());
			shuffleDeck(deckId, 1);

			for (int i = 1; i <= qtdPlayer; i++) {
				addPiles(deckId, "player" + i, null);
				String draw = draw(deckId, qtdCards);
				String cards = extractCards(draw, null);
				addPiles(deckId, "player" + i, cards);

				playerHands.add(deckOfCardsApi.listingPiles(deckId, "player" + i));
			}

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return playerHands;
	}

	@GetMapping("/newDeck")
	public String createNewDeck() {
		return deckOfCardsApi.createNewDeck();
	}

	@GetMapping("/shuffle")
	public String shuffleDeck(@RequestBody String deckId, int count) {
		return deckOfCardsApi.shuffleDeck(deckId, count);
	}

	@GetMapping("/addPiles")
	public String addPiles(@RequestBody String deckId, String player, String cards) {
		return deckOfCardsApi.addPiles(deckId, player, cards);
	}

	@GetMapping("/list")
	public String listingPiles(@RequestBody String deckId, String player) {
		return deckOfCardsApi.listingPiles(deckId, player);
	}

	@GetMapping("/draw")
	public String draw(@RequestBody String deckId, int count) {
		return deckOfCardsApi.drawCards(deckId, count);
	}

	@GetMapping("/extractId")
	public String extractId(@RequestBody String deck) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(deck);
		return jsonNode.get("deck_id").asText();
	}

	@GetMapping("/extractCards")
	public String extractCards(@RequestBody String deck, String player) throws JsonMappingException, JsonProcessingException {
		List<String> playerCards = new ArrayList<String>();
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = objectMapper.readTree(deck);
		JsonNode deckNode = jsonNode.get("piles");

		for (int i = 0; i < qtdCards; i++) {
			if (player != null) {
				playerCards.add(deckNode.get(player).get("cards").get(i).get("code").asText());
			} else {
				playerCards.add(jsonNode.get("cards").get(i).get("code").asText());
			}
		}

		return String.join(",", playerCards);
	}

	@PostMapping("/winningPlayer")
	public List<CardsGameRecordDto> winningPlayer(@RequestBody String data) throws JsonMappingException, JsonProcessingException {
		int maxPoint = Integer.MIN_VALUE;
		List<CardsGameRecordDto> winningPlayers = new ArrayList<CardsGameRecordDto>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(data);

		List<String> playerHands = new ArrayList<>();
		JsonNode dataArray = jsonNode.get("data");
		if (dataArray.isArray()) {
			for (JsonNode node : dataArray) {
				playerHands.add(node.asText());
			}
		}

		for (int i = 1; i <= qtdPlayer; i++) {
			jsonNode = objectMapper.readTree(playerHands.get(i-1));
			JsonNode pilesNode = jsonNode.get("piles");
            String playerName = "player" + i;            
			JsonNode playerNode = pilesNode.get(playerName);

			String name = "";
			int valor = 0;
			for (int j = 0; j < qtdCards; j++) {
				String card = playerNode.get("cards").get(j).get("value").asText();
				
				char point = card.charAt(0);

				if (Character.isLetter(point)) {
					valor += cardsGameService.getCardValue(point);
				} else if (Character.isDigit(point)) {
					if ("10".equals(card)){
						valor += 10;
					} else {
						valor += Character.getNumericValue(point);
					}
				} else {
					valor += 0;
				}
			}
			
			if (valor >= maxPoint) {
	            if (valor > maxPoint) {
	                winningPlayers.clear();
	                maxPoint = valor;
	            }
	            CardsGameRecordDto dto = new CardsGameRecordDto(
	            jsonNode.get("deck_id").asText(),
	            playerName,
	            valor,
	            LocalDateTime.now());
	            winningPlayers.add(dto);
	        } 
			
		}

		return winningPlayers;
	}

	@PostMapping("/save")
	public ResponseEntity<CardsGameModel> saveGame(@RequestBody CardsGameRecordDto cardsGameRecordDto) {
		var cardGameModel = new CardsGameModel();
		BeanUtils.copyProperties(cardsGameRecordDto, cardGameModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(cardsGameService.save(cardGameModel));
	}

}
