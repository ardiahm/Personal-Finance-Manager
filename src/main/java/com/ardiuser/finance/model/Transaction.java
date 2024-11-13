package main.java.com.ardiuser.finance.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private int id;
    private BigDecimal amount;
    private String description;
    private Date date;
    private String category;
    private int userId;

    public Transaction(int id, BigDecimal amount, String description, Date date, String category, int userId){
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
        this.userId = userId; 
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    @Override
    public String toString(){
        return "Transaction [ID= " + id + ", Amount= " + amount + ", Description= " + description + ", Date= " + date + ", Category= " + category + "]";
    }
}
