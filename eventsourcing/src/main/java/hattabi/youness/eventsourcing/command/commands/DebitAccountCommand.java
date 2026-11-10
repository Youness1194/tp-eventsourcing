package hattabi.youness.eventsourcing.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DebitAccountCommand(@TargetAggregateIdentifier String id,
        double amount, String currency) {
}