package hattabi.youness.eventsourcing.query.controllers;

import hattabi.youness.eventsourcing.query.dto.AccountStatementResponseDTO;
import hattabi.youness.eventsourcing.query.entities.Account;
import hattabi.youness.eventsourcing.query.entities.AccountOperation;
import hattabi.youness.eventsourcing.query.queries.GetAccountStatementQuery;
import hattabi.youness.eventsourcing.query.queries.GetAllAccountsQuery;
import hattabi.youness.eventsourcing.query.queries.WatchEventQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {

    private final QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts() {
        return queryGateway.query(new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
    }

    @GetMapping("/statement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(
            @PathVariable String accountId) {
        return queryGateway.query(new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperation> watch(@PathVariable String accountId) {
        SubscriptionQueryResult<AccountOperation, AccountOperation> result = queryGateway.subscriptionQuery(
                new WatchEventQuery(accountId),
                ResponseTypes.instanceOf(AccountOperation.class),
                ResponseTypes.instanceOf(AccountOperation.class));
        return result.initialResult()
                .filter(op -> op.getId() != null)
                .concatWith(result.updates())
                .doFinally(signal -> result.close());
    }
}