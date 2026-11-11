package hattabi.youness.eventsourcing.command.aggregates;

import hattabi.youness.eventsourcing.command.commands.*;
import hattabi.youness.eventsourcing.commonapi.enums.AccountStatus;
import hattabi.youness.eventsourcing.commonapi.events.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;

    protected AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("AddAccountCommand received");
        if (command.initialBalance() <= 0) {
            throw new IllegalArgumentException("The initial balance must be positive");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.id(), command.initialBalance(), AccountStatus.CREATED, command.currency()));

        AggregateLifecycle.apply(new AccountActivatedEvent(command.id(), AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("AccountCreatedEvent occurred");
        this.accountId = event.accountId();
        this.balance = event.initialBalance();
        this.status = event.status();
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("AccountActivatedEvent occurred");
        this.status = event.status();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("CreditAccountCommand received");
        if (status != AccountStatus.ACTIVATED) {
            throw new RuntimeException("Account " + command.id() + " is not activated");
        }
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("The amount must be positive");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.id(), command.amount(), command.currency()));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("AccountCreditedEvent occurred");
        this.balance += event.amount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("DebitAccountCommand received");
        if (status != AccountStatus.ACTIVATED) {
            throw new RuntimeException("Account " + command.id() + " is not activated");
        }
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("The amount must be positive");
        }
        if (balance < command.amount()) {
            throw new RuntimeException("Insufficient balance: " + balance);
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.id(), command.amount(), command.currency()));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("AccountDebitedEvent occurred");
        this.balance -= event.amount();
    }

    @CommandHandler
    public void handle(UpdateAccountStatusCommand command) {
        log.info("UpdateAccountStatusCommand received");
        if (this.status == command.status()) {
            throw new RuntimeException("Account is already in status " + command.status());
        }
        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(command.id(), command.status()));
    }

    @EventSourcingHandler
    public void on(AccountStatusUpdatedEvent event) {
        log.info("AccountStatusUpdatedEvent occurred");
        this.status = event.status();
    }
}