package hattabi.youness.eventsourcing.command.dto;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}