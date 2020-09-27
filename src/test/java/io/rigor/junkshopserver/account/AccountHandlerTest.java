package io.rigor.junkshopserver.account;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountHandlerTest {

  @Autowired
  private AccountHandler accountHandler;
  @Before
  public void doBefore() {
  accountHandler = mock(AccountHandler.class);
  }

  @Test
  public void save() {
    Account anyAccount = new Account();
    when(accountHandler.save(anyAccount)).thenReturn(anyAccount);
    Account savedAccount = accountHandler.save(anyAccount);
    verify(accountHandler).save(anyAccount);
    assertEquals(anyAccount, savedAccount);
  }

  @Test
  public void check() {
  }

  @Test
  public void all() {
  }
}