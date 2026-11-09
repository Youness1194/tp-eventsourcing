package hattabi.youness.eventsourcing.commonapi.events;

import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;

public record AccountActivatedEvent(String accountId, AccountStatus status) {
}