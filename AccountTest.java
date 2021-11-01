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
    void withdrawMoreThanBalanceAndMaxCreditAndOne() {
        int balance = account.getBalance();
        assertFalse(account.withdraw(account.getBalance() + account.getMaxCredit() + 1),
                "It should be impossible to withdraw more than the account balance has plus max credit");
        assertEquals(balance, account.getBalance(),
                "After failed withdraw balance should not change");
    }

    @Test
    void withdrawMoreThanBalanceAndMaxCredit() {
        int balance = account.getBalance();
        assertTrue(account.withdraw(account.getBalance() + account.getMaxCredit()),
                "It should be impossible to withdraw more than the account balance has plus max credit");
        assertEquals(balance - account.getMaxCredit(), account.getBalance(),
                "After failed withdraw balance should not change");
    }

    @Test
    void withdrawZeroMoney() {
        int balance = account.getBalance();
        assertTrue(account.withdraw(account.getBalance()),
                "It should be possible to withdraw zero money");
        assertEquals(balance, account.getBalance(),
                "After withdraw balance should not change");
    }

    @Test
    void withdrawBoundMoney() {
        account.deposit(1000000);
        int balance = account.getBalance();
        assertTrue(account.withdraw(1000000),
                "It should be possible to withdraw zero money");
        assertEquals(balance - 1000000, account.getBalance(),
                "After withdraw balance should not change");
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
    void setMaxCreditEqualsToBound() {
        account.block();
        int newMaxCredit = 1000000;
        assertTrue(account.setMaxCredit(newMaxCredit),
                "Set max credit should return true on blocked account");
        assertEquals(newMaxCredit, account.getMaxCredit(),
                "Max credit should have the value of what was given to setMaxCredit method");
    }

    @Test
    void setMaxCreditEqualsToMinusBound() {
        account.block();
        int newMaxCredit = -1000000;
        assertTrue(account.setMaxCredit(newMaxCredit),
                "Set max credit should return true on blocked account");
        assertEquals(newMaxCredit, account.getMaxCredit(),
                "Max credit should have the value of what was given to setMaxCredit method");
    }

    @Test
    void unblockSuccessful() {
        account.block();
        assertTrue(account.getBalance() >= -account.getMaxCredit(),
                "Account balance should not be less than negative max credit");
        assertTrue(account.unblock(),
                "Unblock should return true if balance is not less than the max credit");
        assertFalse(account.isBlocked(),
                "Account should be unblocked");
    }

    @Test
    void unblockSuccessfulWithMaxCreditMoney() {
        account.withdraw(account.getMaxCredit());
        account.block();
        assertTrue(account.getBalance() >= -account.getMaxCredit(),
                "Account balance should not be less than negative max credit");
        assertTrue(account.unblock(),
                "Unblock should return true if balance is not less than the max credit");
        assertFalse(account.isBlocked(),
                "Account should be unblocked");
    }

    @Test
    void unblockWithBalanceLessThanMaxCreditMoney() {
        account.setMaxCredit(100);
        account.withdraw(50);
        account.block();
        account.setMaxCredit(1);
        assertTrue(account.getBalance() < -account.getMaxCredit(),
                "Account balance should not be more than negative max credit");
        assertFalse(account.unblock(),
                "Unblock should return false if balance is not less than the max credit");
        assertTrue(account.isBlocked(),
                "Account should be blocked");
    }

    @Test
    @Disabled("Unable get account balance to be lower than maxCredit in order for unblock to fail")
    void unblockFail() {
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
    void depositTooMuchMoney() {
        int balance = account.getBalance();
        assertFalse(account.deposit(1000001),
                "Deposit should return false with argument bigger than the bound");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void depositZeroMoney() {
        int balance = account.getBalance();
        assertTrue(account.deposit(0),
                "Deposit should return true with argument equals to zero");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void depositNegativeMoney() {
        int balance = account.getBalance();
        assertFalse(account.deposit(-100),
                "Deposit should return false with argument equals to zero");
        assertEquals(balance, account.getBalance(),
                "Balance should not change after failed deposit call");
    }

    @Test
    void depositBoundMoney() {
        int balance = account.getBalance();
        assertTrue(account.deposit(1000000),
                "Deposit should return true with argument equals to the bound");
        assertEquals(balance + 1000000, account.getBalance(),
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