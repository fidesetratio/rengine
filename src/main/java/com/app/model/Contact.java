package com.app.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class Contact {

    public Contact(String name, String email, ContactType type, List<Contact> members) {
		super();
		this.name = name;
		this.email = email;
		this.type = type;
		this.members = members;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ContactType getType() {
		return type;
	}
	public void setType(ContactType type) {
		this.type = type;
	}
	public List<Contact> getMembers() {
		return members;
	}
	public void setMembers(List<Contact> members) {
		this.members = members;
	}
	private String name;
    private String email;
    private ContactType type;
    private List<Contact> members;
}
