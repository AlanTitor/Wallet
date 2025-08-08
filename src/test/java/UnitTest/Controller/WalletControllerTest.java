package UnitTest.Controller;

import org.AlanTitor.Controller.WalletController;
import org.AlanTitor.Entity.Wallet;
import org.AlanTitor.Exception.GlobalExceptionHandler;
import org.AlanTitor.Exception.WalletNotFoundException;
import org.AlanTitor.Repository.WalletRepository;
import org.AlanTitor.Service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Autowired
    MockMvc mvc;

    @Mock
    WalletService service;

    @InjectMocks
    WalletController controller;

    UUID id;
    Wallet wallet;

    BigDecimal initialBalance;
    BigDecimal depositAmount;

    @BeforeEach
    public void init(){
        id = UUID.randomUUID();

        initialBalance = new BigDecimal("100.00");
        depositAmount = new BigDecimal("50.00");

        wallet = new Wallet();
        wallet.setId(id);
        wallet.setAmount(initialBalance);
        wallet.setVersion(0);

        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void testEndpointUpdateWallet_ShouldReturnOk() throws Exception {
        String json = String.format("{ \"id\": \"%s\", \"operation\": \"DEPOSIT\", \"amount\": 23}", id.toString());

        mvc.perform(post("/api/v1/wallet")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void testEndpointUpdateWallet_ShouldReturnBadRequest() throws Exception {
        String json = String.format("{ \"id\": \"%s\", \"operation\": , \"amount\": 23}", id.toString());

        mvc.perform(post("/api/v1/wallet")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testEndpointGetWallet_ShouldReturnOk() throws Exception {
        mvc.perform(get("/api/v1/wallets/{id}", id)).andExpect(status().isOk());
    }

    @Test
    public void testEndpointGetWallet_ShouldReturnBadRequest() throws Exception {
        mvc.perform(get("/api/v1/wallets/{id}", "2321")).andExpect(status().isBadRequest());
    }

    @Test
    public void testEndpointGetWallet_ShouldReturnNotFound() throws Exception {
        when(service.getWallet(any(UUID.class))).thenThrow(new WalletNotFoundException());
        mvc.perform(get("/api/v1/wallets/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void testEndpointCreateWallet_ShouldReturnWalletId() throws Exception {
        mvc.perform(get("/api/v1/wallet")).andExpect(status().isCreated());
    }
}
