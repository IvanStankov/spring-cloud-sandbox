package com.ivan.scs.contract.cp;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/accounts")
public class ContractProducerController {

    @GetMapping
    public List<UserAccount> getAllAccounts() {
        final Random random = new Random();
        return List.of(
                this.createJohnDoe((long) random.nextInt(1000)),
                this.createJaneDoe((long) random.nextInt(1000))
        );
    }

    @GetMapping("/{id}")
    public UserAccount getAccount(@PathVariable final Long id) {
        return this.createJohnDoe(id);
    }

    @PostMapping
    public UserAccount createAccount(@RequestBody final UserAccount userAccount) {
        userAccount.setId((long) new Random().nextInt(1000));
        return userAccount;
    }

    @PutMapping("/{id}")
    public UserAccount updateAccount(@RequestBody final UserAccount userAccount) {
        return userAccount;
    }

    private UserAccount createJohnDoe(final Long id) {
        return new UserAccount(id, "John", "Doe", LocalDate.of(1983, Month.AUGUST, 21));
    }

    private UserAccount createJaneDoe(final Long id) {
        return new UserAccount(id, "Jane", "Doe", LocalDate.of(1984, Month.FEBRUARY, 29));
    }

}
