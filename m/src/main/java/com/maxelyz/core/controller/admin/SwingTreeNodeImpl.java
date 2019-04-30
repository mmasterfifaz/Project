/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;
import java.util.*;
import javax.swing.tree.TreeNode;
import com.google.common.collect.Iterators;

public class SwingTreeNodeImpl implements TreeNode { 
    private List<SwingTreeNodeImpl> childNodes; 
    private Integer id;
    private String data;
     
    // Getters and setters
    public SwingTreeNodeImpl(Integer id, String data) { 
        super(); 
        this.id = id;
        this.data = data; 
    }
    
    public void addChild(SwingTreeNodeImpl child) { 
        if (child != null) {
            if (childNodes == null) {
                childNodes = new ArrayList<SwingTreeNodeImpl>(); 
            }
            childNodes.add(child); 
        }
    }
    
    @Override
    public TreeNode getChildAt(int childIndex) {
        return (childNodes == null) ? null : childNodes.get(childIndex); 
    }

    @Override
    public int getChildCount() {
        return 1;//(childNodes == null) ? null : childNodes.size(); 
    }
    
    @Override
    public TreeNode getParent() {
        return null;
    }
    
    @Override
    public int getIndex(TreeNode node) {
        return (childNodes == null) ? null : childNodes.indexOf(node);
    }
    
    @Override
    public boolean getAllowsChildren() {
        return true;
    }
    
    @Override
    public boolean isLeaf() {
        return (childNodes == null); 
    }
    
    @Override
    public Enumeration children() {
        return Iterators.asEnumeration(childNodes.iterator()); 
    }
    
    @Override
    public String toString() {
      return data;
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
}