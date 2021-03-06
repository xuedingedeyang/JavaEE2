package com.yang.jpa.helloworld.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.QueryHint;

import org.hibernate.ejb.QueryHints;
import org.hibernate.jpa.internal.EntityManagerMessageLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.yang.jpa.helloworld.Category;
import com.yang.jpa.helloworld.Customer;
import com.yang.jpa.helloworld.Department;
import com.yang.jpa.helloworld.Item;
import com.yang.jpa.helloworld.Manager;
import com.yang.jpa.helloworld.Order;






/**     测试中大坑：数据库中表名不能使用关键字，比如：order，order，order，真他妈没想到order竟然会是关键字。如果是key，foreign什么的我都忍了                                **/





class JPATest {
	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("JPA1");
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		System.out.println("init");
	}
	
	//类似于hibernate中session的get方法
	//@Test
	void testFind() {
		init();
		System.out.println(entityManager==null);
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("----------------------------");
		System.out.println(customer);
		destroy();
	}
	
	//类似于hibernate中的load方法,即在执行完getReference方法后获取的只是一个代理对象，要等待真正使用该对象时才从数据库中查询取出对象。如果在使用对象之前
	//关闭了entityManager就会出现懒加载异常
	//@Test
	public void testGetReference() {
		init();
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
		System.out.println("----------------------------");
		System.out.println(customer);
		destroy();
	}
	
	
	//类似于hibernate的save方法
	//与之不同之处在于：若对象有id，则不能执行insert操作，而会抛出异常
	@Test
	public void testPersistence() {
		init();
		Customer customer = new Customer();
		customer.setAge(14);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("Monica@qq.com");
		customer.setLastName("Monica");
		entityManager.persist(customer);
		destroy();
	}
	
	//类似于hibernate的delete方法，把对象从数据库中移除
	//但注意，该方法只能移除持久化对象，而hibernate方法能移除游离对象
	@Test
	public void testRemove() {
		init();
		Customer customer = new Customer();
		customer.setId(2);
		entityManager.remove(customer);//无法删除
		
		customer = entityManager.find(Customer.class, 2);
//		entityManager.remove(customer);//能删除
		destroy();
	}
	
	/**
	 * 总的来说，类似于hibernate Session的savaOrUpdate方法
	 * 1.若传入的是一个临时的对象
	 * 会创建一个新的对象，把临时对象的属性复制到新的对象中，然后对新的对象执行持久化操作，所以新的对象中有id，但以前的临时对象没有id
	 */
	@Test
	public void testMerge1() {
		init();
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("aa123.com");
		customer.setLastName("aa");
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println("customer id:  "+customer.getId());
		System.out.println("customer2 id:  "+customer2.getId());
		destroy();
	}
	
	
	/**
	 * 若传入的是一个游离的对象，即传入的对象有id，
	 * 1.若在EntityManager缓存中没有该对象，在数据库中也没有对应的记录，JPA会创建一个新对象，然后把当前游离对象的属性复制到新创建的对象中，然后对新对象执行insert操作
	 */
	@Test
	public void testMerge2() {
		init();
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ss123.com");
		customer.setLastName("ss");
		
		customer.setId(100);
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println("customer id:  "+customer.getId());
		System.out.println("customer2 id:  "+customer2.getId());
		destroy();
	}
	
	/**
	 * 若传入的是一个游离的对象，即传入的对象有id，
	 * 1.若在EntityManager缓存中没有 该对象，但在数据库有对应的记录，JPA会查询对应的记录，然后返回该记录对应的对象，然后把游离对象的属性复制到查询对象中。在对查询到的对象执行update 
	 */
	@Test
	public void testMerge3() {
		init();
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ee123.com");
		customer.setLastName("ee");
		
		customer.setId(4);
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println(customer == customer2);
		System.out.println("customer2 id:  "+customer2.getId());
		destroy();
	}
	
	
	/**
	 * 若传入的是一个游离的对象，即传入的对象有id，
	 * 1.若在EntityManager缓存中  有  该对象，JPA把游离对象复制到查询到的对象中。再对查询到的对象执行update 
	 */
	@Test
	public void testMerge4() {
		init();
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("1909227160@qq.com");
		customer.setLastName("yang");
		
		
		
		Customer customer2 = entityManager.find(Customer.class,4);
		entityManager.merge(customer);
		
		System.out.println(customer == customer2);
		System.out.println("customer2 id:  "+customer2.getId());
		destroy();
	}
	
	@Test
	public void testFlush() {
		init();
		Customer customer = entityManager.find(Customer.class, 1);
		
		customer.setLastName("aa");
		entityManager.flush();//如果没有执行此方法会在提交事务时执行update，调用方法强制刷新执行sql语句。同hibernate的flush方法
		destroy();
	}
	
	/**
	 * 同hibernate中session的refresh方法
	 */
	@Test
	public void testRefresh() {
		init();
		Customer customer = entityManager.find(Customer.class, 1);
		
		customer = entityManager.find(Customer.class, 1);
		
		entityManager.refresh(customer);
		destroy();
	}
	
	/**
	 * 保存多对一时，应该先保存一的一端，再保存多的一端，这样不会产生多余的sql语句
	 */
	/*@Test
	public void testManyToOne() {
		init();
		Customer customer = new Customer();
		customer.setAge(13);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("1909227160@163.com");
		customer.setLastName("Joey");
		
		Order order1 = new Order();
		order1.setOrderName("o-J-1");
		
		Order order2 = new Order();
		order2.setOrderName("o-J-2");
		
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		//执行保存操作
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
		destroy();
	}*/
	
	/**
	 * 默认情况下，使用左外连接的方式来获取 多 的一端对象和其关联的 一 的一端的对象
	 * //可以使用@ManyToOne的fetch属性来修改默认的关联属性的加载策略
	 */
//	@Test
//	public void testManyToOneFind() {
//		init();
//		Order order = entityManager.find(Order.class, 2);
//		
//		System.out.println(order.getOrderName());
//		System.out.println(order.getCustomer().getLastName());
//		destroy();
//	}
	
	//不能直接删除 一 的一端，因为有外键约束。
	@Test
	public void testManyToOneRemove() {
		init();
//		Order order = entityManager.find(Order.class, 2);
//		entityManager.remove(2);;
		Customer customer = entityManager.find(Customer.class, 2);
		entityManager.remove(customer);
		destroy();
	}
	
	/*@Test
	public void testManyToOneUpdate() {
		init(); 
		Order o = entityManager.find(Order.class, 2);
		o.getCustomer().setLastName("Chandler");
		destroy();
	}*/
	
	
	//若是双向 1-n 的关联关系，执行保存时
	//若先保存 n 的一端，再保存 1 的一端，会多出 2*n 条update语句
	//反过来则会多出 n 条
	//在进行双向1-n关联关系使，建议使用n的一方来维护关联关系，而1的一方不维护关联关系，这样会有效减少sql语句
	//注意：若在1的一端的@OneToMany中使用mappedBy属性，则@OneToMany端就不能使用@JoinColumn属性了
	
	/**
	 * 单向 1-n 关联关系执行保存时，一定会多出update语句
	 *因为 n 的一端在插入时不会同时插入外键列
	 */
	@Test
	public void testOneToManyPersist() {
		init();
		
		init();
		Customer customer = new Customer();
		customer.setAge(13);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("Ross@gmail.com");
		customer.setLastName("Ross");
		
		Order order1 = new Order();
		order1.setOrderName("o-P-1");
		
		Order order2 = new Order();
		order2.setOrderName("o-P-2");
		
		//建立关联关系
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		//执行保存操作
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
		
		destroy();
	}
	
	
	//默认对关联的多的一方使用懒加载策略
	@Test
	public void testOneToManyFind() {
		init();
		
		Customer c = entityManager.find(Customer.class, 5);
		System.out.println(c.getLastName());
		
		System.out.println(c.getOrders().size());
		
		destroy();
	}
	
	//默认情况下，若删除1的一端，则会把关联的n的一端外键置为空,然后进行删除
	//可以修改@OneToMany的cascade属性来修改默认的删除策略
	@Test
	public void testOneToManyRemove() {
		init();
		
		Customer c = entityManager.find(Customer.class, 5);
		entityManager.remove(c);
		destroy();
		
	}
	
	@Test
	public void testOneToManyUpdate() {
		init();
		
		Customer c = entityManager.find(Customer.class, 2);
		c.getOrders().iterator().next().setOrderName("O-XX-2");
		destroy();
		
	}
	
	
	//双向一对一关联关系，建议先保存不维护关联关系的一方，即没有外键的一方，这样可以减少sql语句的执行
	@Test
	public void testOneToOne() {
		init();
		Manager manager = new Manager();
		manager.setName("m-aa");
		
		Department department = new Department();
		department.setDeptName("d-aa");
		
		//关联关系
		manager.setDepartment(department);
		department.setManager(manager);
		
		//执行保存
		entityManager.persist(manager);
		entityManager.persist(department);
		destroy();
		
	}
	
	
	//默认情况下:1.若维护关联关系的一方，则会通过左外连接获取其关联的的对象
	//但可以通过@OneToOne的fetch属性来修改加载策略
	@Test
	public void testOneToOneFind() {
		init();
		Department department = entityManager.find(Department.class, 1);
		System.out.println(department.getDeptName());
		System.out.println(department.getManager().getClass().getName());
		destroy();
	}
	
	//同样的，获取不维护关联关系的一方，也会通过左外连接获取关联的对象
	//通过fetch修改加载策略也依然会如此。
	@Test
	public void testOneToOneFind2() {
		init();
		Manager m = entityManager.find(Manager.class,1);
		System.out.println(m.getName());
		System.out.println(m.getDepartment().getDeptName());
		destroy();
	}
	
	
	@Test
	public void testManyToMany() {
		init();
		Item i1 = new Item();
		i1.setName("i1");
		
		Item i2 = new Item();
		i2.setName("i2");
		
		Category c1 = new Category();
		c1.setName("c1");
		
		Category c2= new Category();
		c2.setName("c2");
		
		i1.getCategories().add(c1);
		i1.getCategories().add(c2);
		i2.getCategories().add(c2);
		i2.getCategories().add(c1);
		
		c1.getItems().add(i1);
		c1.getItems().add(i2);
		c2.getItems().add(i1);
		c2.getItems().add(i2);
		
		entityManager.persist(i1);
		entityManager.persist(i2);
		entityManager.persist(c1);
		entityManager.persist(c2);
		destroy();
	}
	
	
	//对于关联的集合对象，默认使用懒加载策略
	@Test
	public void testManyToManyFind() {
		init();
		Item item = entityManager.find(Item.class, 1);
		System.out.println(item.getName());
		System.out.println(item.getCategories().size());
		destroy();
	}
	
	
	@Test
	public void testSecondLevelCache() {
		init();
		Customer customer1= entityManager.find(Customer.class, 2);
		destroy();
		init();
		Customer customer2 = entityManager.find(Customer.class, 2);
		destroy();
	}
	
	
	
	@Test
	public void helloJPQL() {
		init();
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query = entityManager.createQuery(jpql);
		
		query.setParameter(1, 1);//占位符索引从1开始
		List<Customer>customers = query.getResultList();
		System.out.println(customers.size());
		destroy();
	}
	
	
	//默认情况下，若只查询部分属性，则返回Object[] 类型结果，或者Object[] 类型的List
	//也可以在实体类中创建对应的构造器，然后再kpql语句中利用对应的构造器返回实体类List
	@Test
	public void helloPartProperties() {
		init();
//		String jpql = "SELECT c.lastName,c.age FROM Customer c WHERE c.id > ?";
		String jpql = "SELECT new Customer(c.lastName,c.age) FROM Customer c WHERE c.id > ?";
		List result = entityManager.createQuery(jpql).setParameter(1, 1).getResultList();
		System.out.println(result);
		destroy();
	}
	
	//createNamedQuery适用于实体类前使用@NamedQuery标记的查询语句
	@Test
	public void testNamedQuery() {
		init();
		
		Query query = entityManager.createNamedQuery("testNamedQuery").setParameter(1, 2);
		
		Customer customer = (Customer) query.getSingleResult();
		System.out.println(customer);
		
		destroy();
	}
	
	
	//createNativeQuery使用与本地sql
	@Test
	public void testNativeQuery() {
		init();
		
		String sql = "select age from customer where id = ?";
		Query query = entityManager.createNativeQuery(sql).setParameter(1, 3);
		
		Object result = query.getSingleResult();
		System.out.println(result);
		
		destroy();
	}
	
	
	//使用hibernate的查询缓存。
	@Test
	public void testQueryCache() {
		init();
		
		String jpql = "FROM Customer c WHERE c.age > ?";
		
	
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		query.setParameter(1, 1);//占位符索引从1开始
		List<Customer>customers = query.getResultList();
		System.out.println(customers.size());
		
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		query.setParameter(1, 1);//占位符索引从1开始
		customers = query.getResultList();
		System.out.println(customers.size());
		destroy();
	}
	
	
	@Test
	public void testQueryOrderBy() {
		init();
		
		String jpql = "FROM Customer c WHERE c.age > ? ORDER BY c.age DESC";
		Query query = entityManager.createQuery(jpql);
		
		query.setParameter(1, 1);//占位符索引从1开始
		List<Customer>customers = query.getResultList();
		System.out.println(customers.size());
	
		
		destroy();
	}
	
	//查询Order数量大于2的那些Customer
	@Test
	public void testQueryGroupBy() {
		init();
		
		String jpql = "select  o.customer FROM  Order o GROUP  BY o.customer HAVING count(o.id) > 2 ";
		Query query = entityManager.createQuery(jpql);
		
		List<Customer>customers = query.getResultList();
		System.out.println(customers.size());
	
		
		destroy();
	}
	
	
	//jpql的关联查询同HQL的关联查询
	@Test
	public void testLeftOuterJoinFetch() {
		init();
		
		String jpql = "FROM Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id = ?";
		Customer customer = (Customer) entityManager.createQuery(jpql).setParameter(1, 6).getSingleResult();
		System.out.println(customer.getLastName());
		
		System.out.println(customer.getOrders().size());
		
//		List<Object[]>result = entityManager.createQuery(jpql).setParameter(1, 6).getResultList();
//		System.out.println(result);
	
		
		destroy();
	}
	
	
	
	@Test
	public void testSubQuery() {
		init();
		
		//查询所有Customer的lastName为Ross的Order
		String jpql = "SELECT o FROM Order o WHERE o.customer IN (SELECT c FROM Customer c WHERE c.lastName = ?)";
	
		Query query = entityManager.createQuery(jpql).setParameter(1, "Ross");
		List<Order>orders = query.getResultList();
		System.out.println(orders.size());
		destroy();
	}
	
	
	//使用jpql内建的一些函数.
	@Test
	public void testJPQLFuntion() {
		init();
		
		String jpql = "SELECT upper(c.email) FROM Customer c";//全部大写
		List<String>emails = entityManager.createQuery(jpql).getResultList();
		System.out.println(emails);
		destroy();
	}
	
	
	//可以使用jpql完成update 和 delete 操作
	@Test
	public void testExecuteUpdate() {
		init();
		
		String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, "Joey").setParameter(2, 2);
		
		query.executeUpdate();
		destroy();
	}
	
	
	@After
	public void destroy() {
		entityTransaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}

}
