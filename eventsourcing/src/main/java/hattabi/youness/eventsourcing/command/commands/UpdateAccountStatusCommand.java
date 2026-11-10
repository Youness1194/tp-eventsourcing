package hattabi.youness.eventsourcing.command.commands;

import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UpdateAccountStatusCommand(@TargetAggregateIdentifier String id,
        AccountStatus status) {
}