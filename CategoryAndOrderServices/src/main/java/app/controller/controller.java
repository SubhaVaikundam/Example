package app.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Category;
import app.repository.CategoryRepository;


@RestController
@RequestMapping("/category")
public class controller {

  @Autowired
  private CategoryRepository categoryRepository;
    
  @RequestMapping(method = RequestMethod.GET)
  public List<Object> getAllCategories(){
    List<Category> categories = categoryRepository.findAll();
    List<Object> response = new ArrayList<Object>();
    response.addAll(categories);
    return response;
  }
}
