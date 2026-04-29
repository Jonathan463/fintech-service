package technologyforall.com.chargeservice.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum ChargeTier {

    MICRO(
            new BigDecimal("5000"),
            new BigDecimal("1.5"),
            new BigDecimal("10"),
            new BigDecimal("75")
    ),
    LOW(
            new BigDecimal("50000"),
            new BigDecimal("1.0"),
            new BigDecimal("25"),
            new BigDecimal("500")
    ),
    MID(
            new BigDecimal("1000000"),
            new BigDecimal("0.5"),
            new BigDecimal("50"),
            new BigDecimal("5000")
    ),
    HIGH(
            null,                          // no upper bound
            new BigDecimal("0.25"),
            new BigDecimal("1000"),
            new BigDecimal("10000")
    );

    private final BigDecimal maxAmount;
    private final BigDecimal ratePercent;
    private final BigDecimal minFee;
    private final BigDecimal maxFee;

    ChargeTier(BigDecimal maxAmount, BigDecimal ratePercent, BigDecimal minFee, BigDecimal maxFee) {
        this.maxAmount = maxAmount;
        this.ratePercent = ratePercent;
        this.minFee = minFee;
        this.maxFee = maxFee;
    }


    public String getRateLabel() {
        return ratePercent.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
    }


    public static ChargeTier forAmount(BigDecimal amount) {
        for (ChargeTier tier : values()) {
            if (tier.maxAmount == null || amount.compareTo(tier.maxAmount) <= 0) {
                return tier;
            }
        }
        return HIGH;
    }


    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = amount
                .multiply(ratePercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (fee.compareTo(minFee) < 0) fee = minFee;
        if (fee.compareTo(maxFee) > 0) fee = maxFee;

        return fee;
    }
}
