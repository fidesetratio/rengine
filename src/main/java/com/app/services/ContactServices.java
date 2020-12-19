package com.app.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.model.Contact;
import com.app.model.ContactType;

@Service
public class ContactServices {
	  public List<Contact> getContactList() {
	        return Arrays.asList(new Contact("patar","timoitus",ContactType.GROUP,new ArrayList<Contact>()));
	        

	        
	    }
}
