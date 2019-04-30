package com.maxelyz.core.model.value.front.customerHandling;
import com.maxelyz.core.model.entity.Product;


public class ShoppingRetailProduct {
    private Product product;
    private Integer quality;
    private Double price;

    public ShoppingRetailProduct(Product product, Integer quality, Double price) {
        this.product = product;
        this.quality = quality;
        this.price = price;
    }

    public ShoppingRetailProduct(Product product) {
        this.product = product;
        quality = 1;
        price = product.getPrice();
    }



    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
}
