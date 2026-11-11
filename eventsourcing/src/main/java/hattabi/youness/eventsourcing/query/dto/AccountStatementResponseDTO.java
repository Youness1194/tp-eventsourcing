package hattabi.youness.eventsourcing.query.dto;

import hattabi.youness.eventsourcing.query.entities.Account;
import hattabi.youness.eventsourcing.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(Account account, List<AccountOperation> operations) {
}