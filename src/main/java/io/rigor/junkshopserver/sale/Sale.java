package io.rigor.junkshopserver.sale;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.rigor.junkshopserver.sale.saleitem.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "sale")
public class Sale {
  @DynamoDBHashKey
  @DynamoDBAutoGeneratedKey
  private String id;
  @DynamoDBAttribute
  private String receiptNumber;
  @DynamoDBAttribute
  private List<SaleItem> saleItems;
  @DynamoDBAttribute
  private String totalPrice;
  @DynamoDBAttribute
  private String date;
  @DynamoDBAttribute
  private String clientId;
  @DynamoDBAttribute
  private String accountId;
}
