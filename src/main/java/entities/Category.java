package entities;

import java.util.List;

import javax.persistence.*;

/*
 * For any foreign key column in your database:
 * @ManyToOne
 * @JoinColumn(name = "foreign_key_column_name")
 * private ParentEntity parent;
 * 
 * For Parent table 
 * @OneToMany(mappedBy="parent_table_name",cascade = CascadeType.ALL)
 */
@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Category_Id")
	private int Category_Id;

	@Column(name = "Category_Name", nullable = false)
	private String Category_Name;

	// foreign key
	@ManyToOne
	@JoinColumn(name = "User_Id")
	private User user;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Expense> expenses;

	@Override
	public String toString() {
		return "Category_id => " + Category_Id + ", Category_Name => " + Category_Name;
	}

	public int getCategory_id() {
		return Category_Id;
	}

	public void setCategory_id(int category_id) {
		Category_Id = category_id;
	}

	public String getCategory_Name() {
		return Category_Name;
	}

	public void setCategory_Name(String category_Name) {
		Category_Name = category_Name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

}