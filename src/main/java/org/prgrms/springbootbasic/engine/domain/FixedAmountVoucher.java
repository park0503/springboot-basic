package org.prgrms.springbootbasic.engine.domain;

import org.prgrms.springbootbasic.engine.enumtype.ErrorCode;
import org.prgrms.springbootbasic.engine.enumtype.VoucherType;
import org.prgrms.springbootbasic.exception.VoucherValueRangeException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private static final long MAX_VOUCHER_AMOUNT = 1000000;
    private final UUID voucherId;
    private Integer amount;
    private final VoucherType voucherType;
    private final LocalDateTime createdAt;
    private UUID customerId;

    public FixedAmountVoucher(UUID voucherId, Integer amount, LocalDateTime createdAt) {
        validateValue(amount);
        this.voucherId = voucherId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.voucherType = VoucherType.FIXED_AMOUNT;
    }

    public FixedAmountVoucher(UUID voucherId, UUID customerId, Integer amount, LocalDateTime createdAt) {
        validateValue(amount);
        this.voucherId = voucherId;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.voucherType = VoucherType.FIXED_AMOUNT;
    }

    @Override
    public void changeValue(Integer value) {
        validateValue(value);
        this.amount = value;
    }

    @Override
    public void changeOwner(Customer customer) {
        this.customerId = customer.getCustomerId();
    }

    @Override
    public void changeOwnerById(UUID customerId) {
        this.customerId = customerId;
    }

    @Override
    public void revokeOwner() {
        this.customerId = null;
    }

    @Override
    public String toString() {
        return "FixedAmountVoucher{" +
                "voucherId=" + voucherId +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public String toFileString() {
        return voucherId + "\nFixedAmount\n" + amount + "\n" + createdAt;
    }

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    @Override
    public VoucherType getVoucherType() {
        return this.voucherType;
    }

    @Override
    public Integer getValue() {
        return amount;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public Optional<UUID> getCustomerId() {
        return Optional.ofNullable(this.customerId);
    }

    private void validateValue(Integer amount) {
        if (amount < 0) {
            throw new VoucherValueRangeException("Fixed amount should be positive.", ErrorCode.VALUE_RANGE_OUT);
        } else if (amount == 0) {
            throw new VoucherValueRangeException("Fixed amount shouldn't be zero.", ErrorCode.VALUE_RANGE_OUT);
        } else if (amount > MAX_VOUCHER_AMOUNT) {
            throw new VoucherValueRangeException("Fixed amount should be less than 1000001.", ErrorCode.VALUE_RANGE_OUT);
        }
    }
}
