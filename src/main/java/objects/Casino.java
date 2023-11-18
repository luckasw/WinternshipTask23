package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Casino {
    private long balance;
    private final List<Transaction> transactions;

    public Casino(long balance) {
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public long getBalance() {
        return this.balance;
    }

    public void addBalance(long amount) {
        this.balance += amount;
    }
    public void subtractBalance(long amount) {
        this.balance -= amount;
    }

    public void addChanges(Transaction change) {
        this.transactions.add(change);
    }

    public void rollbackIllegitimateChanges(UUID playerId) {
        for (Transaction change : this.transactions) {
            if (change.getPlayerId().equals(playerId) && !change.isRolledback()) {
                this.balance -= change.getBalanceChange();
                change.setRolledback(true);
            }
        }
    }
}
