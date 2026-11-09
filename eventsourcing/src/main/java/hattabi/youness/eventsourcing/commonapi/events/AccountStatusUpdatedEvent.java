package hattabi.youness.eventsourcing.commonapi.events;

import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;

public record AccountStatusUpdatedEvent(String accountId, AccountStatus status) {
}