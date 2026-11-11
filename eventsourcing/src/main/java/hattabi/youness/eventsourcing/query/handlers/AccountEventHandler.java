package hattabi.youness.eventsourcing.query.handlers;

import hattabi.youness.eventsourcing.commonapi.enums.OperationType;
import hattabi.youness.eventsourcing.commonapi.events.*;
import hattabi.youness.eventsourcing.query.entities.Account;
import hattabi.youness.eventsourcing.query.entities.AccountOperation;
import hattabi.youness.eventsourcing.query.repositories.AccountOperationRepository;
import hattabi.youness.eventsourcing.query.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AccountEventHandler {

    private final AccountRepository accountRepository;
    private final AccountOperationRepository operationRepository;

    public AccountEventHandler(AccountRepository accountRepository,
            AccountOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, @Timestamp Instant instant) {
        log.info("QUERY SIDE: AccountCreatedEvent received");
        accountRepository.save(Account.builder()
                .id(event.accountId())
                .balance(event.initialBalance())
                .status(event.status())
                .currency(event.currency())
                .createdAt(instant)
                .build());
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("QUERY SIDE: AccountActivatedEvent received");
        Account account = accountRepository.findById(event.accountId()).orElseThrow();
        account.setStatus(event.status());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountStatusUpdatedEvent event) {
        log.info("QUERY SIDE: AccountStatusUpdatedEvent received");
        Account account = accountRepository.findById(event.accountId()).orElseThrow();
        account.setStatus(event.status());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event, @Timestamp Instant instant) {
        log.info("QUERY SIDE: AccountDebitedEvent received");
        Account account = accountRepository.findById(event.accountId()).orElseThrow();
        AccountOperation operation = operationRepository.save(AccountOperation.builder()
                .amount(event.amount())
                .operationDate(instant)
                .type(OperationType.DEBIT)
                .currency(event.currency())
                .account(account)
                .build());
        account.setBalance(account.getBalance() - operation.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event, @Timestamp Instant instant) {
        log.info("QUERY SIDE: AccountCreditedEvent received");
        Account account = accountRepository.findById(event.accountId()).orElseThrow();
        AccountOperation operation = operationRepository.save(AccountOperation.builder()
                .amount(event.amount())
                .operationDate(instant)
                .type(OperationType.CREDIT)
                .currency(event.currency())
                .account(account)
                .build());
        account.setBalance(account.getBalance() + operation.getAmount());
        accountRepository.save(account);
    }
}