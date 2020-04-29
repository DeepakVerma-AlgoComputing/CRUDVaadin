package com.example.CRUDVaadin;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import javax.servlet.annotation.WebServlet;

import com.example.CRUDVaadin.DB.DbContract;
import com.example.CRUDVaadin.DB.PostgresHelper;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	PostgresHelper client = new PostgresHelper(DbContract.HOST,DbContract.DB_NAME,DbContract.USERNAME,DbContract.PASSWORD);
		
		try {
			if (client.connect()) {
				System.out.println("-------------- DB connected ----------------");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

    	TextField firstName = new TextField("First Name");
    	TextField lastName = new TextField("Last Name");
    	
    	Button saveBtn = new Button("Save");
    	
    	VerticalLayout layout = new VerticalLayout();
    	layout.setMargin(true);
    	layout.setSpacing(true);
    	layout.addComponents(firstName,lastName,saveBtn);
    	
    	setContent(layout);
    	
    	saveBtn.addClickListener(e -> {
    	
    		if(!"".equalsIgnoreCase(firstName.getValue()) && !"".equalsIgnoreCase(lastName.getValue())) {
	    		
    			//1. For inserting records in database (PostgresSQL)
				LinkedHashMap<String,Object> vals = new LinkedHashMap<String,Object>();
				vals.put("firstname", firstName.getValue());
				vals.put("lastname", lastName.getValue());
				try {
					if (client.insert("record", vals) == 1) {
						System.out.println("-------------- Record added ----------------");
					}
		    	}catch (SQLException ex) {
					ex.printStackTrace();
				}
	    		
				Notification.show("Hi " + firstName.getValue() + " " + lastName.getValue() + ". Reccord saved!");
	    	}else {
	    		Notification.show("Kindly, Enter your first & last name");
	    	}
    	});
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
