/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import org.richfaces.model.TreeNodeImpl;
/**
 *
 * @author Administrator
 */
public class MXTreeNode extends TreeNodeImpl { 
   protected Integer id; 
   protected String data; 
   protected String type; 
   
//   public MXTreeNode(String data) { 
   public MXTreeNode(Integer id, String data, String type) { 
      super(); 
      this.id = id;
      this.data = data; 
      this.type = type;
   }    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
   
   public String getData() { 
      return data; 
   } 
   
   public void setData(String data) { 
      this.data = data; 
   } 

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
   
   @Override 
   public String toString() { 
      return data; 
   }
}
