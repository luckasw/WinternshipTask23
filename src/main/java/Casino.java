import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Casino {
    private long balance;
    private List<Changes> changes;

    public Casino(long balance) {
        this.balance = balance;
        this.changes = new ArrayList<>();
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void addBalance(long amount) {
        this.balance += amount;
    }

    public void addChanges(Changes change) {
        this.changes.add(change);
    }

    public List<Changes> getChanges() {
        return this.changes;
    }

    public void rollbackIllegitimateChanges(UUID playerId) {
        for (Changes change : this.changes) {
            if (change.getPlayerId().equals(playerId) && !change.isRollbacked()) {
                this.balance -= change.getBalanceChange();
            }
        }
    }
}
