package br.com.fullcycle.infrastructure.dtos;

public record NewEventDTO(
    String date,
    String name,
    Integer totalSpots,
    String partnerId
) {

}