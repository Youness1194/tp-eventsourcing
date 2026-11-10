package hattabi.youness.eventsourcing.command.dto;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}