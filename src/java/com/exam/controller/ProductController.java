/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exam.controller;

import com.exam.model.Category;
import com.exam.model.Product;
import com.exam.util.HibernateUtil;
import com.exam.util.Upload;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author farid
 */
@ManagedBean(name = "productCtrl")
@ViewScoped
public class ProductController {

    private UploadedFile file;
    private Product product;
        
    
    public ProductController() {
        product = new Product();
        
    }

    private int sqty=0;
    private Date dt = new Date();
    private String date = new SimpleDateFormat("dd-MM-yyyy").format(dt);
    private int aqty=0;
    
    
    public String saveData() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            String path = "D:\\CarShop\\web\\images";
            String hasFile = Upload.uploadFile(path, file);

            if (hasFile == null) {
                massage("File upload failed");
                return "product";
            }
            product.setImgName(hasFile);
            
            session.save(product);
            tx.commit();
            session.flush();
            this.massage("Saved Successfully");
            return "product";
        } catch (Exception e) {
            tx.rollback();
            this.massage("Saved Failed, please try again!");
            e.printStackTrace();

        }
        return "product";
    }

    public List<Category> allCategories() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query =  session.createQuery("From Category");
            List<Category> entityList = (List<Category>) query.list();
            session.flush();
            return entityList;

        } catch (Exception e) {
            this.massage("Not Found");
            e.printStackTrace();
        }
        return null;
    }
    
    public void upload() {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
     
    public void handleFileUpload(FileUploadEvent event) {
        this.file = event.getFile();        
    }

    public void massage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(msg));
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public List<Product> showCarList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Product");
            List<Product> list = q.list();
            tx.commit();
            session.flush();
            return list;
        } catch (Exception e) {
//            tx.rollback();
        }
        return null;
    }

    public int getSqty() {
        return sqty;
    }

    public void setSqty(int sqty) {
        this.sqty = sqty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String showCar(Product p) {
        product.setId(p.getId());
        product.setProductName(p.getProductName());
        product.setPrice(p.getPrice());
        product.setQuantity(p.getQuantity());
        return null;
    }
    
    
    public String salesCar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int aqty=product.getQuantity();
        product.setQuantity(aqty-sqty);
        try {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            tx.rollback();
        }
        return "salesCar";
    }

    public int getAqty() {
        return aqty;
    }

    public void setAqty(int aqty) {
        this.aqty = aqty;
    }

}
