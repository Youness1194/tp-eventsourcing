package hattabi.youness.eventsourcing.commonapi.events;

import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;

public record AccountCreatedEvent(String accountId, double initialBalance,
        AccountStatus status, String currency) {
}