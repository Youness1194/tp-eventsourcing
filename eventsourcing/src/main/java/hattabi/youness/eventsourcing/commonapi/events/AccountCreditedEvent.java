// commonapi/events/AccountCreditedEvent.java
package hattabi.youness.eventsourcing.commonapi.events;

public record AccountCreditedEvent(String accountId, double amount, String currency) {
}