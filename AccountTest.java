package root.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Account account;

    @BeforeEach
    void setupAccount() {
        account = new Account();
    }

    @Test
    void deposit() {
        int balance = account.getBalance();
        int newMoney = 5;
        assertTrue(account.deposit(newMoney),
                "Deposit should return true");
        assertEquals(balance + newMoney, account.getBalance(),
                "New balance should be equal to old plus new money");
    }

    @Test
    void withdraw() {
        int balance = account.getBalance();
        int wantedMoney = 5;
        assertTrue(account.withdraw(wantedMoney),
                "Withdraw should return true");
        assertEquals(balance - wantedMoney, account.getBalance(),
                "New balance should be equal to old minus withdrawn money");
    }

    @Test
    void block() {
        account.block();
        assertTrue(account.isBlocked(),
                "Block method should set blocked field to true");
    }

    @Test
    void accountBlockCheck() {
        assertFalse(account.isBlocked(),
                "Account should not be blocked");
        assertTrue(account.getBalance() >= -account.getMaxCredit(),
                "Account balance should not be less than negative max credit");
    }

    @Test
    void withdrawFromBlockedAccount() {
        account.block();
        int balance = account.getBalance();
        assertFalse(account.withdraw(1),
                "It should hbe impossible to withdraw money from blocked account");
        assertEquals(balance, account.getBalance(),
                "Blocked account balance should not change after withdraw");
    }

    @Test
    void withdrawMoreThanBalanceAndMaxCredit() {
        int balance = account.getBalance();
        assertFalse(account.withdraw(account.getBalance() + account.getMaxCredit()),
                "It should be impossible to withdraw more than the account balance has plus max credit");
        assertEquals(balance, account.getBalance(),
                "After failed withdraw balance should not change");
    }

    @Test
    void setMaxCreditOnUnblockedAccount() {
        int maxCredit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(1),
                "Set max credit should return false if account is not blocked");
        assertEquals(maxCredit, account.getMaxCredit(),
                "After failed setMaxCredit maxCredit should not change");
    }

    @Test
    void setMaxCreditToBoundPlusOne() {
        account.block();
        int maxCredit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(1000001),
                "Set max credit should return false if the value is bigger than bound");
        assertEquals(maxCredit, account.getMaxCredit(),
                "Max credit should not change after failed setMaxCredit call");
    }

    @Test
    void setMaxCreditToNegativeBoundMinusOne() {
        account.block();
        int maxCredit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(-1000001),
                "Set max credit should return false if the absolute value is bigger than bound");
        assertEquals(maxCredit, account.getMaxCredit(),
                "Max credit should not change after failed setMaxCredit call");
    }

    @Test
    void setMaxCreditOnBlockedAccount() {
        account.block();
        int newMaxCredit = 999999;
        assertTrue(account.setMaxCredit(newMaxCredit),
                "Set max credit should return true on blocked account");
        assertEquals(newMaxCredit, account.getMaxCredit(),
                "Max credit should have the negative value of what was given to setMaxCredit method");
    }

    @Test
    void successfulUnblock() {
        account.block();
        assertTrue(account.getBalance() >= -account.getMaxCredit(),
                "Account balance should not be less than negative max credit");
        assertTrue(account.unblock(),
                "Unblock should return true if balance is not less than the max credit");
        assertFalse(account.isBlocked(),
                "Account should be unblocked");
    }

    @Test
    @Disabled("Unable get account balance to be lower than maxCredit in order for unblock to fail")
    void failedUnblock() {
        account.withdraw(account.getBalance() + account.getMaxCredit());
        account.block();
        assertTrue(account.getBalance() < -account.getMaxCredit(),
                "Account balance should be less than negative max credit");
        assertFalse(account.unblock(),
                "Unblock should return false if balance is less than the max credit");
        assertFalse(account.isBlocked(),
                "Account should be blocked");
    }

    @Test
    void depositWithNegativeArgument() {
        int balance = account.getBalance();
        assertFalse(account.deposit(-1),
                "Deposit should return false with negative argument");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void withdrawWithNegativeArgument() {
        int balance = account.getBalance();
        assertFalse(account.withdraw(-1),
                "Withdraw should return false with negative argument");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed withdraw call");
    }

    @Test
    void depositWithTooBigArgument() {
        int balance = account.getBalance();
        assertFalse(account.deposit(1000001),
                "Deposit should return false with argument bigger than the bound");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void withdrawWithTooBigArgument() {
        int balance = account.getBalance();
        assertFalse(account.withdraw(1000001),
                "Withdraw should return false with argument bigger than the bound");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed withdraw call");
    }

    @Test
    void depositWithBlockedAccount() {
        account.block();
        int balance = account.getBalance();
        assertFalse(account.deposit(1),
                "Deposit should return false when account is blocked");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void withdrawWithBlockedAccount() {
        account.block();
        int balance = account.getBalance();
        assertFalse(account.withdraw(1),
                "Withdraw should return false when account is blocked");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed withdraw call");
    }
}