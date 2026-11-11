package hattabi.youness.eventsourcing.query.handlers;

import hattabi.youness.eventsourcing.query.dto.AccountStatementResponseDTO;
import hattabi.youness.eventsourcing.query.entities.Account;
import hattabi.youness.eventsourcing.query.entities.AccountOperation;
import hattabi.youness.eventsourcing.query.queries.GetAccountStatementQuery;
import hattabi.youness.eventsourcing.query.queries.GetAllAccountsQuery;
import hattabi.youness.eventsourcing.query.queries.WatchEventQuery;
import hattabi.youness.eventsourcing.query.repositories.AccountOperationRepository;
import hattabi.youness.eventsourcing.query.repositories.AccountRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {

    private final AccountRepository accountRepository;
    private final AccountOperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository,
            AccountOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.accountId()).orElseThrow();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.accountId());
        return new AccountStatementResponseDTO(account, operations);
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query) {
        return new AccountOperation();
    }
}