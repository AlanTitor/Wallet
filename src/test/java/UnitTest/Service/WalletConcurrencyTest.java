package UnitTest.Service;

import org.AlanTitor.Dto.WalletRequestDto;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Enum.OperationType;
import org.AlanTitor.Main;
import org.AlanTitor.Repository.WalletRepository;
import org.AlanTitor.Service.WalletCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
public class WalletConcurrencyTest {

    @Autowired
    WalletRepository repository;
    @Autowired
    WalletCommandService commandService;

    UUID id;

    @BeforeEach
    public void init(){
        repository.deleteAll();
        id = UUID.randomUUID();

        Wallet wallet = new Wallet();
        //wallet.setVersion(0);
        wallet.setAmount(new BigDecimal(1000));
        wallet.setId(id);

        repository.save(wallet);
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        int threads = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++){
            executor.submit(() -> {
                try{
                    WalletRequestDto dto = new WalletRequestDto();
                    dto.setId(id);
                    dto.setOperation(OperationType.DEPOSIT);
                    dto.setAmount(new BigDecimal(10));
                    commandService.updateWallet(dto);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        Wallet updatedWallet = repository.findById(id).orElseThrow();
        assertEquals(new BigDecimal("11000.00"), updatedWallet.getAmount());
    }
}
