package dbs.bankingsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Getter
@Setter
public class TransferMoneyDto {

    private String transferTo;
    private Double amount;

    public String getTransferTo() {
        return this.transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransferMoneyDto() {
    }

    public TransferMoneyDto(final String transferTo, final Double amount) {
        this.transferTo = transferTo;
        this.amount = amount;
    }

}
