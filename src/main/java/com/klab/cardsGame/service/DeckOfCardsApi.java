package com.klab.cardsGame.service;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeckOfCardsApi {
	    
    private static final String URL_BASE = "https://deckofcardsapi.com/api/deck/";

    private final RestTemplate restTemplate;

    public DeckOfCardsApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createNewDeck() {
        String url = URL_BASE + "new";
        return restTemplate.getForObject(url, String.class);
    }

    public String drawCards(String deckId, int count) {
        String url = URL_BASE + deckId + "/draw/?count=" + count;
        return restTemplate.getForObject(url, String.class);
    }

    public String shuffleDeck(String deckId, int count) {
        String url = URL_BASE + deckId + "/shuffle/?deck_count=" + count;
        return restTemplate.getForObject(url, String.class);
    }
    
    public String addPiles(String deckId, String name, String cards) {
        String url = URL_BASE + deckId + "/pile/" + name + "/add/?cards=" + cards;
        return restTemplate.getForObject(url, String.class);
    }
   
    public String listingPiles(String deckId, String name) {
        String url = URL_BASE + deckId + "/pile/" + name + "/list/";
        return restTemplate.getForObject(url, String.class);
    }
    
}
