package app.model;

import org.springframework.data.annotation.Id;
import java.util.ArrayList;

public class Category {

  @Id
  private String _id;
  private String name;
  private String seo;
  private String topnav;
  private ArrayList sub_category;
  
  public Category(){}
  
  public Category(String _id,String name, String seo, String topnav, ArrayList sub_category){
    this._id = _id;
	this.name = name;
    this.seo = seo;
    this.topnav = topnav;
    this.sub_category = sub_category;
  }
  
  public String get_id() {
    return _id;
  }
  public void set_id(String id) {
    this._id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getSeo() {
    return seo;
  }
  public void setSeo(String seo) {
    this.seo = seo;
  }
  public String getTopnav() {
    return topnav;
  }
  public void setTopnav(String topnav) {
    this.topnav = topnav;
  }
  public ArrayList getSub_category() {
    return sub_category;
  }
  public void setSub_category(ArrayList sub_category) {
    this.sub_category = sub_category;
  }
    
}
