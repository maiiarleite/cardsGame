package com.klab.desafioCartas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.klab.cardsGame.model.CardsGameModel;
import com.klab.cardsGame.repository.CardsGameRepository;
import com.klab.cardsGame.service.CardsGameService;

public class CardsGameServiceTest {

    private CardsGameRepository cardsGameRepository;
    private CardsGameService cardsGameService;

    @BeforeEach
    public void setUp() {
        cardsGameRepository = mock(CardsGameRepository.class);
        cardsGameService = new CardsGameService(cardsGameRepository);
    }

    @Test
    public void testSave() {
        CardsGameModel modelToSave = new CardsGameModel();
        when(cardsGameRepository.save(modelToSave)).thenReturn(modelToSave);
        CardsGameModel savedModel = cardsGameService.save(modelToSave);
        assertEquals(modelToSave, savedModel);
    }

    @Test
    public void testGetCardValue() {
        assertEquals(1, cardsGameService.getCardValue('A'));
        assertEquals(11, cardsGameService.getCardValue('J'));
        assertEquals(12, cardsGameService.getCardValue('Q'));
        assertEquals(13, cardsGameService.getCardValue('K'));
        
        assertEquals(7, cardsGameService.getCardValue('7'));
    }
}