package org.AlanTitor.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.AlanTitor.Enum.OperationType;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDto {

    @NotNull(message = "ID can not be null!")
    private UUID id;

    @NotNull(message = "Operation can not be null!")
    private OperationType operation;

    @NotNull(message = "Operation can not be null!")
    @Positive(message = "Amount can't be less than zero!")
    private BigDecimal amount;
}
