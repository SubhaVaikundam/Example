package app.controller;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Address;
import app.model.Item;
import app.model.Order;
import app.repository.OrderRepository;


@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderRepository orderRepository;
  
  @Autowired
  private MongoOperations mongoOperations;
    
	
  
  @RequestMapping(method = RequestMethod.POST, value="/addToOrder")
  public Map<String, Object> addToOrder(@RequestBody Map<String, Object> orderMap){
    
	Map<String, Object> checkResponse = new LinkedHashMap<String, Object>();
	Map<String, Object> createResponse = new LinkedHashMap<String, Object>();
	Map<String, Object> updateResponse = new LinkedHashMap<String, Object>();
	
	checkResponse.putAll(isOrderItemExists(orderMap.get("custId").toString(),orderMap.get("prodId").toString()));
	if((checkResponse.get("newOrderNeeded").toString()).equals("true")){
		System.out.println("TRUE Case");
		createResponse = createOrder(orderMap);
		return createResponse;
	}
	else{
		System.out.println("FALSE Case");
		orderMap.put("orderId",checkResponse.get("orderId").toString());
		orderMap.put("currentOrder",checkResponse.get("currentOrder"));
		updateResponse = updateOrder(orderMap);
		return updateResponse;
	}
  }
  
  public Map<String, Object> createOrder(Map<String, Object> orderMap){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	ArrayList<Item> itemsList = new ArrayList<Item>();
	int quantity = Integer.parseInt(orderMap.get("quantity").toString());
	double prodPrice = Double.valueOf(orderMap.get("totPrice").toString());
	double prodTotPrice = quantity * prodPrice;
	Item item = new Item(orderMap.get("prodId").toString(),orderMap.get("prodName").toString(),quantity,prodPrice,prodTotPrice);
	itemsList.add(item);
	double taxAmt = 0.0;
	Order order = new Order((orderMap.get("custId").toString()+timestamp.getTime()),
		orderMap.get("custId").toString(), null,null,null,null,null,null,taxAmt,prodTotPrice,prodTotPrice,"INCOMPLETE",itemsList);
    
    Map<String, Object> response = new LinkedHashMap<String, Object>();
    response.put("message", "Items added to Order");
    response.put("order", orderRepository.save(order));
    return response;
  }
  
  public Map<String, Object> updateOrder(Map<String, Object> orderMap){
    Map<String, Object> response = new LinkedHashMap<String, Object>();
	String orderId = orderMap.get("orderId").toString();
	Order currentOrder = (Order)orderMap.get("currentOrder");
	String prodId = orderMap.get("prodId").toString();
	ArrayList<Item> itemList = currentOrder.getItems();
	Item newProduct = null;
	String prodIdFound = "false";
	for(int i=0;i<itemList.size();i++)
	{
		Item product = (Item)itemList.get(i);
		if(prodId.equals(product.getItemId())){
			prodIdFound = "true";
			itemList.remove(i);
			
			int newQuantity = 0;
			if((orderMap.get("fromPage")).equals("plp")){
				newQuantity = (Integer.parseInt(orderMap.get("quantity").toString())) + 1;
				System.out.println("newQuantity:"+newQuantity);
			}
			else{
				int currentProdQuantity = product.getQuantity();
				newQuantity = currentProdQuantity + (Integer.parseInt(orderMap.get("quantity").toString()));
				System.out.println("newQuantity:"+newQuantity);
			}
			double prodPrice = Double.valueOf(product.getSalePrice());
			double newTotPrice = prodPrice*newQuantity;
			newProduct = new Item(prodId,product.getItemName(),newQuantity,prodPrice,newTotPrice);
			double currentPriceChange = (Integer.parseInt(orderMap.get("quantity").toString())) * prodPrice;
			currentOrder.setItemsSubTotal(currentOrder.getItemsSubTotal()+currentPriceChange);
		}
	}
	if(prodIdFound.equals("true")){
		itemList.add(newProduct);
		currentOrder.setItems(itemList);
		currentOrder.setOrderTotal(currentOrder.getItemsSubTotal() + currentOrder.getTaxAmt());
		response.put("message", "Item:"+prodId+" updated to the order:"+orderId);
	}
	else{
		int newProdQuant = Integer.parseInt(orderMap.get("quantity").toString());
		double newProdPrice = Double.valueOf(orderMap.get("totPrice").toString());
		double newProdTotal = newProdPrice * newProdQuant;
		newProduct = new Item(prodId,orderMap.get("prodName").toString(),Integer.parseInt(orderMap.get("quantity").toString()),newProdPrice,newProdTotal);
		itemList.add(newProduct);
		currentOrder.setItems(itemList);
		currentOrder.setItemsSubTotal(currentOrder.getItemsSubTotal()+newProdTotal);
		currentOrder.setOrderTotal(currentOrder.getItemsSubTotal() + currentOrder.getTaxAmt());
		response.put("message", "Item:"+prodId+" added to the order:"+orderId);
	}
	response.put("order", orderRepository.save(currentOrder));
	return response;
  }
  
  public Map<String, Object> isOrderItemExists(String custId,String prodId){
    Query query = new Query();
	List<String> stateList = new ArrayList<String>();
		stateList.add("INCOMPLETE");
		stateList.add("PROCESSING");
	query.addCriteria(Criteria.where("custId").is(custId).and("orderState").in(stateList));
	Order order = mongoOperations.findOne(query, Order.class);
	Map<String, Object> response = new LinkedHashMap<String, Object>();
	if(order!=null){
		response.put("newOrderNeeded","false");
		response.put("orderId",order.get_id());
		response.put("currentOrder",order);
	}
	else{
		response.put("newOrderNeeded","true");
	}
	return response;
  }
  
  @RequestMapping(method = RequestMethod.POST, value="/checkoutCurrentOrder")
	public Map<String, Object> checkoutCurrentOrder(@RequestBody Map<String, Object> userMap){
		Query query = new Query();
		List<String> stateList = new ArrayList<String>();
			stateList.add("INCOMPLETE");
			stateList.add("PROCESSING");
		query.addCriteria(Criteria.where("custId").is(userMap.get("custId").toString()).and("orderState").in(stateList));
		Order order = mongoOperations.findOne(query, Order.class);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		if(order!=null){
			if(order.getOrderState().equals("INCOMPLETE")){
				order.setOrderState("PROCESSING");
				response.put("message", "current Order");
				response.put("order",orderRepository.save(order));
			}
			else{
				response.put("message", "current order");
				response.put("order",order);
			}
		}
		else{
			response.put("message","no current order");
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getCurrentOrder/{custId}")
	public Map<String, Object> getCurrentOrder(@PathVariable("custId") String custId){
		Query query = new Query();
		List<String> stateList = new ArrayList<String>();
			stateList.add("INCOMPLETE");
			stateList.add("PROCESSING");
		query.addCriteria(Criteria.where("custId").is(custId).and("orderState").in(stateList));
		Order order = mongoOperations.findOne(query, Order.class);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		if(order!=null){
			response.put("message", "Got the current Order");
			response.put("order",order);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/processCurrentOrder")
	public Map<String, Object> processCurrentOrder(@RequestBody Map<String, Object> detailsMap){
		System.out.println("Entering processCurrentOrder");
		Query query = new Query();
		List<String> stateList = new ArrayList<String>();
			stateList.add("PROCESSING");
		query.addCriteria(Criteria.where("custId").is(detailsMap.get("custId").toString()).and("orderState").in(stateList));
		System.out.println("order from query:"+mongoOperations.findOne(query, Order.class));
		Order order = mongoOperations.findOne(query, Order.class);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		if(order!=null){
			Address shippingAddress = new Address(detailsMap.get("shipTitle").toString(),detailsMap.get("shipFirstName").toString(),
					detailsMap.get("shipLastName").toString(),detailsMap.get("shipAddr").toString(),detailsMap.get("shipCity").toString(),
					detailsMap.get("shipState").toString(),detailsMap.get("shipCountry").toString(),detailsMap.get("shipZipcode").toString());
			System.out.println("shippingAddress:"+shippingAddress);
			order.setShippingAddress(shippingAddress);
			System.out.println("checkbox:"+detailsMap.get("saveToBillingAddress").toString());
			if((detailsMap.get("saveToBillingAddress").toString()).equalsIgnoreCase("true")){
				Address billingAddress = new Address(detailsMap.get("shipTitle").toString(),detailsMap.get("shipFirstName").toString(),
						detailsMap.get("shipLastName").toString(),detailsMap.get("shipAddr").toString(),detailsMap.get("shipCity").toString(),
						detailsMap.get("shipState").toString(),detailsMap.get("shipCountry").toString(),detailsMap.get("shipZipcode").toString());
				order.setBillingAddress(billingAddress);
				System.out.println("billingAddress:"+billingAddress);
			}
			else{
				order.setBillingAddress(null);
			}
			order.setCreditcardType(detailsMap.get("ccType").toString());
			order.setCreditcardNumber(detailsMap.get("ccNumber").toString());
			if(detailsMap.containsKey("expDate")){
				System.out.println("expDate:"+detailsMap.get("expDate").toString());
				order.setExpDate(detailsMap.get("expDate").toString());
			}
			else{
				String expDate = detailsMap.get("expMonth").toString()+"-"+detailsMap.get("expYear").toString();
				order.setExpDate(expDate);
			}
			order.setNameOnCard(detailsMap.get("nameOnCard").toString());
			order.setTaxAmt(order.getTaxAmt());
			order.setOrderTotal(order.getItemsSubTotal()-order.getTaxAmt());
			if(detailsMap.containsKey("oneClick") && (detailsMap.get("oneClick").toString()).equals("true")){
				order.setOrderState("SUBMITTED");
			}
			response.put("message", "Saved the current Order");
			response.put("order",orderRepository.save(order));
			System.out.println("saved the order");
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/submitCurrentOrder")
	public Map<String, Object> submitCurrentOrder(@RequestBody Map<String, Object> detailsMap){
		Query query = new Query();
		List<String> stateList = new ArrayList<String>();
			stateList.add("PROCESSING");
		query.addCriteria(Criteria.where("custId").is(detailsMap.get("custId").toString()).and("orderState").in(stateList));
		Order order = mongoOperations.findOne(query, Order.class);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		if(order!=null){
			order.setOrderState("SUBMITTED");
			response.put("message", "Submitted the current Order");
			response.put("order",orderRepository.save(order));
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/updateOrderItem")
  public Map<String, Object> updateOrderItem(@RequestBody Map<String, Object> detailsMap){
    
	Map<String, Object> checkResponse = new LinkedHashMap<String, Object>();
	Map<String, Object> updateResponse = new LinkedHashMap<String, Object>();
	
	checkResponse.putAll(isOrderItemExists(detailsMap.get("custId").toString(),detailsMap.get("prodId").toString()));
	if((checkResponse.get("newOrderNeeded").toString()).equals("false")){
		detailsMap.put("orderId",checkResponse.get("orderId").toString());
		updateResponse = updateQuantity(detailsMap);
	}
	return updateResponse;
  }
  
  public Map<String, Object> updateQuantity(Map<String, Object> detailsMap){
    
	Map<String, Object> response = new LinkedHashMap<String, Object>();
	String orderId = detailsMap.get("orderId").toString();
	Order order = orderRepository.findOne(orderId);
	String prodId = detailsMap.get("prodId").toString();
	ArrayList<Item> itemList = order.getItems();
	Item newProduct = null;
	String prodUpdated = "false";
	for(int i=0;i<itemList.size();i++)
	{
		Item product = (Item)itemList.get(i);
		if(prodId.equals(product.getItemId())){
			
			itemList.remove(i);
			if((detailsMap.get("update").toString()).equals("add")){
				int newQuantity = product.getQuantity()+1;
				double prodPrice = product.getSalePrice();
				double newTotPrice = product.getTotPrice() + prodPrice;
				newProduct = new Item(prodId,product.getItemName(),newQuantity,prodPrice,newTotPrice);
				order.setItemsSubTotal(order.getItemsSubTotal()+prodPrice);
				prodUpdated = "true";
			}
			else if((detailsMap.get("update").toString()).equals("remove")){
				prodUpdated = "false";
				order.setItemsSubTotal(order.getItemsSubTotal()-product.getTotPrice());
			}
			else{
				int newQuantity = product.getQuantity()-1;
				if(newQuantity == 0){
					prodUpdated = "false";
					double prodPrice = product.getSalePrice();
					order.setItemsSubTotal(order.getItemsSubTotal()-prodPrice);
				}
				else{
					double prodPrice = product.getSalePrice();
					double newTotPrice = product.getTotPrice() - prodPrice;
					newProduct = new Item(prodId,product.getItemName(),newQuantity,prodPrice,newTotPrice);
					order.setItemsSubTotal(order.getItemsSubTotal()-prodPrice);
					prodUpdated = "true";
				}
			}
		}
	}
	if(prodUpdated.equals("true")){
		itemList.add(newProduct);
		order.setItems(itemList);
		order.setOrderTotal(order.getItemsSubTotal() + order.getTaxAmt());
		response.put("message", "Item:"+prodId+" updated to the order:"+orderId);
	}
	else{
		order.setItems(itemList);
		order.setOrderTotal(order.getItemsSubTotal() + order.getTaxAmt());
		response.put("message", "Item:"+prodId+" removed from the order:"+orderId);
	}
	response.put("order", orderRepository.save(order));
	return response;
  }
}
