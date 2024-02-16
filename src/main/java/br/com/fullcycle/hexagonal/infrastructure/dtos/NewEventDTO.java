package br.com.fullcycle.hexagonal.infrastructure.dtos;

public record NewEventDTO(
    String date,
    String name,
    Integer totalSpots,
    String partnerId
) {

}