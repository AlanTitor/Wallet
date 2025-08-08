package UnitTest.Service;

import org.AlanTitor.Dto.WalletRequestDto;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Enum.OperationType;
import org.AlanTitor.Exception.WalletNotFoundException;
import org.AlanTitor.Repository.WalletRepository;
import org.AlanTitor.Service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletService walletService;

    UUID id;
    Wallet wallet;

    BigDecimal initialBalance = new BigDecimal("100.00");
    BigDecimal depositAmount = new BigDecimal("50.00");

    @BeforeEach
    public void init(){
        id = UUID.randomUUID();

        wallet = new Wallet();
        wallet.setId(id);
        wallet.setAmount(initialBalance);
        wallet.setVersion(0);
    }

    @Test
    public void testDepositShouldIncrease(){
        WalletRequestDto request = new WalletRequestDto();
        request.setAmount(depositAmount);
        request.setId(id);
        request.setOperation(OperationType.DEPOSIT);

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        walletService.updateWallet(request);

        assertEquals(new BigDecimal("150.00"), wallet.getAmount());

        verify(walletRepository).save(wallet);
        verify(walletRepository).findById(id);
    }


    @Test
    public void testWithdrawShouldDecrease(){
        WalletRequestDto request = new WalletRequestDto();
        request.setAmount(depositAmount);
        request.setId(id);
        request.setOperation(OperationType.WITHDRAW);

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenAnswer(invocation -> invocation.getArgument(0));

        walletService.updateWallet(request);

        assertEquals(new BigDecimal("50.00"), wallet.getAmount());

        verify(walletRepository).findById(id);
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testShouldThrowException_WhenWalletNotFound(){
        when(walletRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWallet(id));
    }
}
