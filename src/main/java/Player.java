import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Player implements Comparable<Player> {
    private final UUID id;
    private long balance;
    private BigDecimal winRate;
    private boolean legitimate;
    private Action illegitimateReason;
    private int wins;
    private int totalGames;


    public Player(UUID id, long balance, boolean legitimate) {
        this.id = id;
        this.balance = balance;
        this.winRate = new BigDecimal("0.0");
        this.legitimate = legitimate;
    }

    public UUID getId() {
        return this.id;
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setIllegitimate() {
        this.legitimate = false;
    }

    public Action getIllegitimateReason() {
        return illegitimateReason;
    }

    public boolean isLegitimate() {
        return this.legitimate;
    }

    public void setIllegitimateReason(Action illegitimateReason) {
        this.illegitimateReason = illegitimateReason;
    }

    public void calculateWinRate(Boolean win) {
        if (win) {
            this.wins++;
        }
        this.totalGames++;
        this.winRate = new BigDecimal((double) this.wins / this.totalGames);
    }

    @Override
    public String toString() {
        return this.id + " " + this.balance + " " + this.winRate.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int compareTo(Player p) {
        return this.id.compareTo(p.getId());
    }
}
