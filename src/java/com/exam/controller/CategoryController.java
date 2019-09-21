/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exam.controller;

import com.exam.model.Category;
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
@ManagedBean(name = "catCtrl")
@ViewScoped
public class CategoryController {

    private Category category;
    private Session session = null;
    private Transaction tx = null;

    public CategoryController() {
        category = new Category();
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public String save() {
        tx = session.beginTransaction();
        try {
            session.saveOrUpdate(category);
            tx.commit();
            session.flush();
            this.massage("Saved Successfully");
        } catch (Exception e) {
            tx.rollback();
            this.massage("Saved Failed, please try again!");
            e.printStackTrace();

        }
        return "category";
    }

    public List<Category> allCategories() {
        tx = session.beginTransaction();
        try {
            Query query = session.createQuery("From Category");
            List<Category> entityList = (List<Category>) query.list();
            return entityList;

        } catch (Exception e) {
            this.massage("Login Failed, please try again!");
            e.printStackTrace();
        }
        return null;
    }

    public void massage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(msg));
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
