/*["_id":<OrderId>,"custId":<CUSTID>,"shippingAddress":<Address>,"billingAddress":<Address>,
 * "creditcardType":<CreditCardType>,"creditcardNumber":<CreditCardNumber>,"expDate":<ExpiryDate>,"nameOnCard":<NameOnCard>,
 * "items":[{"itemId":<ProdId>,"itemName":<itemName>,"quantity":<Quan>,"salePrice":<price>,"totPrice":<totPrice>}],
 * "tax":<taxAmt>,"itemsSubTotal":<IST>,"orderTotal":<OT>,"orderState":<OS>}*/

package app.model;

import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import app.model.Item;
public class Order {

  @Id
  private String _id;
  private String custId;
  private Address shippingAddress;
  private Address billingAddress;
  private String creditcardType;
  private String creditcardNumber;
  private String expDate;
  private String nameOnCard;
  
  private double taxAmt;
  private double itemsSubTotal;
  private double orderTotal;
  private String orderState;
  private ArrayList<Item> items;
  
  public Order(){}
  
  public Order(String _id,String custId, Address shippingAddress, Address billingAddress, String creditcardType,
		  String creditcardNumber, String expDate, String nameOnCard, double taxAmt, double itemsSubTotal, double orderTotal, String orderState, ArrayList<Item> items){
    this._id = _id;
	this.custId = custId;
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
	this.creditcardType = creditcardType;
	this.creditcardNumber = creditcardNumber;
	this.expDate = expDate;
	this.nameOnCard = nameOnCard;
	this.taxAmt = taxAmt;
	this.itemsSubTotal = itemsSubTotal;
	this.orderTotal = orderTotal;
	this.orderState = orderState;
    this.items = items;
  }
  
  public String get_id() {
    return _id;
  }
  public void set_id(String id) {
    this._id = id;
  }
  
  public String getCustId() {
    return custId;
  }
  public void setCustId(String custId) {
    this.custId = custId;
  }
  
  public Address getShippingAddress() {
    return shippingAddress;
  }
  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }
  
  public Address getBillingAddress() {
    return billingAddress;
  }
  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }
    
  public String getCreditcardType() {
	  return creditcardType;
  }
  public void setCreditcardType(String creditcardType) {
	  this.creditcardType = creditcardType;
  }
  
  public String getCreditcardNumber() {
	  return creditcardNumber;
  }
  public void setCreditcardNumber(String creditcardNumber) {
	  this.creditcardNumber = creditcardNumber;
  }
  
  public String getExpDate(){
	  return expDate;
  }
  public void setExpDate(String expDate){
	  this.expDate = expDate;
  }
  
  public String getNameOnCard(){
	  return nameOnCard;
  }
  public void setNameOnCard(String nameOnCard){
	  this.nameOnCard = nameOnCard;
  }
  
  public String getOrderState() {
    return orderState;
  }
  public void setOrderState(String orderState) {
    this.orderState = orderState;
  }
  
  public double getTaxAmt() {
    if(getShippingAddress()!=null){
	  if(getShippingAddress().getZipCode()!=null && !(getShippingAddress().getZipCode().isEmpty())){
		taxAmt = 2.5;
	  }
    }
	return taxAmt;
  }
  public void setTaxAmt(double taxAmt) {
    this.taxAmt = taxAmt;
  }
  
  public double getItemsSubTotal() {
    return itemsSubTotal;
  }
  public void setItemsSubTotal(double itemsSubTotal) {
    this.itemsSubTotal = itemsSubTotal;
  }
  
  public double getOrderTotal() {
    return orderTotal;
  }
  public void setOrderTotal(double orderTotal) {
    this.orderTotal = orderTotal;
  }
  
  public ArrayList<Item> getItems() {
    return items;
  }
  public void setItems(ArrayList<Item> items) {
    this.items = items;
  }
    
}
