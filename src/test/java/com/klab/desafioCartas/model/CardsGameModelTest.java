package com.klab.desafioCartas.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

import com.klab.cardsGame.model.CardsGameModel;

public class CardsGameModelTest {
    
	@Test
    public void testGettersAndSetters() {
        CardsGameModel gameModel = new CardsGameModel();
        
        UUID Id = UUID.randomUUID();
        String deckId = "21sd654s89d";
        String name = "Test Game";
        Integer point = 0;
        LocalDateTime date = LocalDateTime.now();

        gameModel.setId(Id);
        gameModel.setDeckId(deckId);
        gameModel.setName(name);
        gameModel.setPoint(point);
        gameModel.setDate(date);

        assertEquals(Id, gameModel.getId());
        assertEquals(deckId, gameModel.getDeckId());
        assertEquals(name, gameModel.getName());
        assertEquals(point, gameModel.getPoint());
        assertEquals(date, gameModel.getDate());
    }
}
