package com.yang.jpa.helloworld;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="orders")
@Entity
public class Order {
	private Integer id;
	private String orderName;
	
	private Customer customer;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="order_name")
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	//映射单项n-1关联关系
	//使用@ManyToOne来映射多对一的关联关系
	//使用@JoinColumn来映射外键
	//可以使用@ManyToOne的fetch属性来修改默认的关联属性的加载策略
	@JoinColumn(name="customer_id")
	@ManyToOne(fetch=FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	 
}
