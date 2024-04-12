package com.klab.cardsGame.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="TBL_GAME")
public class CardsGameModel implements Serializable
{	
	private static final long serialVersionUID = 8015014589228105617L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID Id;
	private String deckId;
	private String name;
	private Integer point;
	private LocalDateTime date;
		
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	public String getDeckId() {
		return deckId;
	}
	public void setDeckId(String deckId) {
		this.deckId = deckId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
