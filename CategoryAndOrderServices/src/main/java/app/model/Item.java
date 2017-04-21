/*{"itemId":<ProdId>,"itemName":<itemName>,"quantity":<Quan>,"salePrice":<price>,"totPrice":<totPrice>}*/
package app.model;

import org.springframework.data.annotation.Id;

public class Item {

  @Id
  private String itemId;
  private String itemName;
  private int quantity;
  private double salePrice;
  private double totPrice;
    
  public Item(){}
  
  public Item(String itemId,String itemName,int quantity,double salePrice,double totPrice){
    this.itemId = itemId;
	this.itemName = itemName;
	this.quantity = quantity;
	this.salePrice = salePrice;
	this.totPrice = totPrice;
  }
  
  public String getItemId() {
    return itemId;
  }
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  
  public String getItemName() {
    return itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public double getSalePrice() {
    return salePrice;
  }
  public void setSalePrice(double salePrice) {
    this.salePrice = salePrice;
  }
  public double getTotPrice() {
    return totPrice;
  }
  public void setTotPrice(double totPrice) {
    this.totPrice = totPrice;
  }
      
}
