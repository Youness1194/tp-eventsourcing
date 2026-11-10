package hattabi.youness.eventsourcing.query.repositories;

import hattabi.youness.eventsourcing.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}