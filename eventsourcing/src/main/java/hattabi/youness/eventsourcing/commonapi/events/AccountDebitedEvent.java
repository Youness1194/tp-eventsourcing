package hattabi.youness.eventsourcing.commonapi.events;

public record AccountDebitedEvent(String accountId, double amount, String currency) {
}