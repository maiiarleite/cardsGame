package com.klab.desafioCartas.controller;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardsGameRecordDto(@NotBlank String deckId, 
		@NotBlank String name, 
		@NotNull Integer point,
		@NotNull LocalDateTime date) {
}