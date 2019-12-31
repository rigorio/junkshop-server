package io.rigor.junkshopserver.cash;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "cash")
public class Cash {
  @DynamoDBHashKey
  @DynamoDBAutoGeneratedKey
  private String id;
  @DynamoDBAttribute
  private String capital;
  @DynamoDBAttribute
  private String sales;
  @DynamoDBAttribute
  private String purchases;
  @DynamoDBAttribute
  private String expenses;
  @DynamoDBAttribute
  private String cashOnHand;
  @DynamoDBAttribute
  private String date;
}
