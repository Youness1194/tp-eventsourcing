package hattabi.youness.eventsourcing.command.dto;

import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus status) {
}