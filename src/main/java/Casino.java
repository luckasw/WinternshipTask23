import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Casino {
    private long balance;
    private final List<Changes> changes;

    public Casino(long balance) {
        this.balance = balance;
        this.changes = new ArrayList<>();
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

    public void addChanges(Changes change) {
        this.changes.add(change);
    }

    public void rollbackIllegitimateChanges(UUID playerId) {
        for (Changes change : this.changes) {
            if (change.getPlayerId().equals(playerId) && !change.isRolledback()) {
                this.balance -= change.getBalanceChange();
                change.setRolledback(true);
            }
        }
    }
}
