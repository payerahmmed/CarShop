/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exam.controller;

import com.exam.model.Product;
import com.exam.model.User;
import com.exam.util.HibernateUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author farid
 */
@ManagedBean(name = "homeCtrl")
@ViewScoped
public class HomeController {

    private User user;
    private Product product;
    Session session = null;
    Transaction tx = null;

    public HomeController() {
        user = new User();
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public String save() {
        tx = session.beginTransaction();
        try {
            if (this.uniqueCheckUsername(user.getUsername())) {
                session.save(user);
                tx.commit();
                session.flush();
                this.massage("Saved Successfully");
                return "admin/home";
            }
        } catch (Exception e) {
            tx.rollback();
            this.massage("Saved Failed, please try again!");
            e.printStackTrace();

        }
        return "register";
    }

    public String login() {
        tx = session.beginTransaction();
        try {
            Query query = session.createQuery("From User where username=:username and password=:password and status=1");
            query.setParameter("username", user.getUsername());
            query.setParameter("password", user.getPassword());

            User entity = (User) query.list().get(0);

            if (entity != null) {
                session.flush();
                this.massage("Login Success");
                return "admin/home";
            }

        } catch (Exception e) {
            this.massage("Login Failed, please try again!");
            e.printStackTrace();
        }
        return "login";
    }

    public boolean uniqueCheckUsername(String username) {
        try {
            Query query = session.createQuery("From User where username=:username");
            query.setParameter("username", username);

            int len = query.list().size();

            if (len < 1) {
                session.flush();
                return true;
            }

        } catch (Exception e) {
            this.massage("Username not unique, Please try again!");
            e.printStackTrace();
        }
        this.massage("Username not unique, Please try again!");
        return false;
    }

    public List<User> allUsers() {
        tx = session.beginTransaction();
        List<User> entityList = null;
        try {
            Query query = session.createQuery("From User");
            entityList = (List<User>) query.list();
            session.flush();
            return entityList;

        } catch (Exception e) {
            this.massage("Login Failed, please try again!");
            e.printStackTrace();
        }
        return entityList;
    }
    
    public List<Product> carList() {
        tx = session.beginTransaction();
        List<Product> carList = null;
        try {
            Query query = session.createQuery("From Product");
            carList = query.list();
            session.flush();
            return carList;

        } catch (Exception e) {
            this.massage("Login Failed, please try again!");
            e.printStackTrace();
        }
        return carList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void massage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(msg));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
