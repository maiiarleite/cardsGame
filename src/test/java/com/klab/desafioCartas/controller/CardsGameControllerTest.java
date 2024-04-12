package com.klab.desafioCartas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.klab.cardsGame.controller.CardsGameController;
import com.klab.cardsGame.dto.CardsGameRecordDto;
import com.klab.cardsGame.model.CardsGameModel;
import com.klab.cardsGame.service.CardsGameService;
import com.klab.cardsGame.service.DeckOfCardsApi;

public class CardsGameControllerTest {
	
	private CardsGameService cardsGameService;
    private DeckOfCardsApi deckOfCardsApi;
    private CardsGameController cardsGameController;

    @BeforeEach
    public void setUp() {
        cardsGameService = mock(CardsGameService.class);
        deckOfCardsApi = mock(DeckOfCardsApi.class);
        cardsGameController = new CardsGameController(cardsGameService, deckOfCardsApi);
    }
    
    @Test
    public void testExtractCardsForPlayer() throws JsonProcessingException, JsonMappingException {
        String deck = "{\"piles\":{\"player1\":{\"cards\":[{\"code\":\"card1\"},{\"code\":\"card2\"}]}}}";
        String player = "player1";
        String result = cardsGameController.extractCards(deck, player);
        assertThat(result).isEqualTo("card1,card2");
    }

    @Test
    public void testExtractCardsForNullPlayer() throws JsonProcessingException, JsonMappingException {
        String deck = "{\"cards\":[{\"code\":\"card1\"},{\"code\":\"card2\"}]}";
        String player = null;
        String result = cardsGameController.extractCards(deck, player);
        assertThat(result).isEqualTo("card1,card2");
    }
    
    @Test
    public void testWinningPlayer() throws JsonProcessingException, JsonMappingException {
        String data = "{\"data\":[" +
                          "{\"piles\":{\"player1\":{\"cards\":[{\"value\":\"10\"},{\"value\":\"J\"},{\"value\":\"A\"}]},\"player2\":{\"cards\":[{\"value\":\"8\"},{\"value\":\"7\"},{\"value\":\"K\"}]}}," +
                          "{\"piles\":{\"player1\":{\"cards\":[{\"value\":\"9\"},{\"value\":\"5\"},{\"value\":\"Q\"}]},\"player2\":{\"cards\":[{\"value\":\"6\"},{\"value\":\"4\"},{\"value\":\"2\"}]}}" +
                      "]}";

        List<CardsGameRecordDto> dto = cardsGameController.winningPlayer(data);

        assertThat(dto).hasSize(1);
        assertThat(dto.get(0).name()).isEqualTo("player1");
        assertThat(dto.get(0).point()).isEqualTo(30);
    }
    
    @Test
    public void testSaveGame() {
        // Mocking data
    	CardsGameRecordDto cardsGameRecordDto = new CardsGameRecordDto(
		    "as54s5d4s5sasas5",
		    "Test Game",
		    0,
		    LocalDateTime.now()
		);
    	
        CardsGameModel expectedSavedGame = new CardsGameModel();
        expectedSavedGame.setName("Test Game");
        expectedSavedGame.setPoint(0);
        expectedSavedGame.setDeckId("as54s5d4s5sasas5");
        expectedSavedGame.setDate(LocalDateTime.now());

        when(cardsGameService.save(expectedSavedGame)).thenReturn(expectedSavedGame);

        ResponseEntity<CardsGameModel> response = cardsGameController.saveGame(cardsGameRecordDto);

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().getName().equals("Test Game");
        assert response.getBody().getPoint().equals(0);
        assert response.getBody().getDeckId().equals("as54s5d4s5sasas5");
        assert response.getBody().getDate().equals(LocalDateTime.now());
    }
    
}
