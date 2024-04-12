package com.klab.cardsGame.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.klab.cardsGame.model.CardsGameModel;
import com.klab.cardsGame.repository.CardsGameRepository;

import jakarta.transaction.Transactional;

@Service
public class CardsGameService {

    private final CardsGameRepository cardsGameRepository;
    private final Map<Character, Integer> cardValues = new HashMap<>();

    public CardsGameService(CardsGameRepository cardsGameRepository) {
        this.cardsGameRepository = cardsGameRepository;
        initializeCardValues();
    }

    private void initializeCardValues() {
        cardValues.put('A', 1);
        cardValues.put('J', 11);
        cardValues.put('Q', 12);
        cardValues.put('K', 13);
    }
    @Transactional
    public CardsGameModel save(CardsGameModel cardsGameModel) {
        return cardsGameRepository.save(cardsGameModel);
    }

	public int getCardValue(Character cardCode) {
    	if (cardValues.containsKey(cardCode)) {
            return cardValues.get(cardCode);
        } else {
            if (Character.isDigit(cardCode)) {
                return Character.getNumericValue(cardCode);
            } else {
                return 0;
            }
        }
    }
    
}
