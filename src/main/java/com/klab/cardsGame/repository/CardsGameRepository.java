package com.klab.cardsGame.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klab.cardsGame.model.CardsGameModel;

public interface CardsGameRepository extends JpaRepository<CardsGameModel, UUID>{
}
