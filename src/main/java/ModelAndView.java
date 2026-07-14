package main.java;

import java.util.HashMap;

public class ModelAndView {
    private HashMap<String,Object> attribute=new HashMap<>();
    private String view;

    public ModelAndView(HashMap<String,Object> attribute,String view){
        this.attribute=attribute;
        this.view=view;
    }

    public ModelAndView(){
    }

    public HashMap<String, Object> getAttribute(){
        return this.attribute;
    }

    public String getview(){
        return this.view;
    }
    public void setAttribute(HashMap<String,Object> attribute){
        this.attribute=attribute;
    }

    public void setview(String view){
        this.view=view;
    }
}
