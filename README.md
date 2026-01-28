<h1>Expense Tracker (Core Java + Hibernate)</h1>

<p>
A console-based Expense Tracker application built using <b>Core Java</b> and
<b>Hibernate ORM</b>. This project demonstrates clean backend design,
manual transaction handling, and proper ORM usage without relying on Spring Boot.
</p>

<h2>1) Architecture</h2>

<pre>
App (CLI)
 └── Service Layer
      └── Repository (DAO) Layer
           └── Hibernate ORM
                └── MySQL Database
</pre>

<h2>Features</h2>
<h2>1) User Management</h2>
<ul>
  <li>User registration with unique username</li>
  <li>Secure login using username &amp; password</li>
  <li>Account recovery for deactivated users</li>
  <li>Change password functionality</li>
  <li>Account deactivation (soft delete)</li>
  <li>Permanent account deletion</li>
  <li>User account status management (active, deleted)</li>
</ul>

<h2>2) Category Management</h2>
<ul>
  <li>View general categories</li>
  <li>Create user-specific categories</li>
  <li>Fetch category details by category ID</li>
  <li>Delete user-owned categories only</li>
  <li>
    Enforced uniqueness:
    <ul>
      <li>One user cannot have two categories with the same name</li>
    </ul>
  </li>
  <li>Automatic cleanup of categories when user is deleted (CASCADE)</li>
</ul>

<h2>3) Expense Management</h2>
<ul>
  <li>Add a single expense</li>
  <li>Add multiple expenses in one session</li>
  <li>View all expenses for a user</li>
  <li>Fetch expense by expense ID</li>
  <li>View expenses by month and year</li>
  <li>Update expense amount and category</li>
  <li>Soft delete expenses (status-based)</li>
  <li>Restore soft-deleted expenses</li>
  <li>Permanent deletion of expenses</li>
</ul>

<h2>4) Data Integrity &amp; Safety</h2>
<ul>
  <li>User-wise data isolation (users can only access their own data)</li>
  <li>Foreign key enforcement using database constraints</li>
  <li>CASCADE deletes ensure no orphan records</li>
  <li>Soft delete preserves data history where required</li>
  <li>Input validation for numeric fields (prevents crashes)</li>
</ul>

<h2>5) Database-Level Guarantees</h2>
<ul>
  <li>Unique username enforced at database level</li>
  <li>Unique (User_Id, Category_Name) enforced at database level</li>
  <li>
    Referential integrity maintained between:
    <ul>
      <li>User → Category</li>
      <li>User → Expense</li>
      <li>Category → Expense</li>
    </ul>
  </li>
</ul>

<h2>6) Technical / Architectural Features</h2>
<ul>
  <li>Built using Java 8</li>
  <li>Hibernate ORM for database operations</li>
  <li>
    Clean separation of layers:
    <ul>
      <li>Entity layer</li>
      <li>Repository layer</li>
      <li>Service layer</li>
    </ul>
  </li>
  <li>Maven-based project (pom.xml)</li>
  <li>CLI-based interface (terminal application)</li>
  <li>Ready for migration to Spring Boot + REST APIs</li>
</ul>

# Database Schema – Expense Tracker

## Overview

The Expense Tracker application uses a **relational database** with three core tables:

* user
* category
* expense

The schema is designed to:

* Maintain **user-wise data ownership**
* Enforce **referential integrity** using foreign keys
* Support **soft deletion** using status columns
* Enforce **data uniqueness at the database level**
* Automatically clean up dependent records using **CASCADE rules**

---

## User Table

### Purpose

Stores user account details. Each user owns categories and expenses.

### Columns

| Column Name     | Data Type                | Description                     |
| --------------- | ------------------------ | ------------------------------- |
| `User_Id`       | int (PK, AI)             | Unique identifier for each user |
| `User_name`     | varchar(45)              | Username                        |
| `User_Password` | varchar(108)             | User password                   |
| `Age`           | int                      | User age                        |
| `Status`        | enum('active','deleted') | Account status                  |

### Constraints

* **UNIQUE (User_name)**

### Notes

* Each username is **globally unique**
* `Status = 'deleted'` represents a **deactivated account**
* A user can have multiple categories and expenses

---

## category Table

### Purpose

Stores expense categories. Categories are **user-specific**.

### Columns

| Column Name     | Data Type    | Description                |
| --------------- | ------------ | -------------------------- |
| `Category_id`   | int (PK, AI) | Unique category identifier |
| `Category_Name` | varchar(45)  | Name of the category       |
| `User_id`       | int (FK)     | Owner user                 |

### Constraints

* **UNIQUE (`User_id`, `Category_Name`)**

### Foreign Key Constraint

| Foreign Key | References      | On Update | On Delete |
| ----------- | --------------- | --------- | --------- |
| `User_id`   | `user(User_Id)` | CASCADE   | CASCADE   |

### Notes

* A user **cannot create two categories with the same name**
* Different users **can** have categories with the same name
* If a user is deleted, all related categories are **automatically deleted**

---

## expense Table

### Purpose

Stores individual expense records created by users.

### Columns

| Column Name   | Data Type                | Description               |
| ------------- | ------------------------ | ------------------------- |
| `Expense_Id`  | int (PK, AI)             | Unique expense identifier |
| `Category_Id` | int (FK)                 | Category of the expense   |
| `User_Id`     | int (FK)                 | Owner user                |
| `Amount`      | decimal(10,2)            | Expense amount            |
| `Date`        | timestamp                | Date and time of expense  |
| `Status`      | enum('ACTIVE','DELETED') | Expense state             |

### Foreign Key Constraints

| Foreign Key   | References              | On Update | On Delete |
| ------------- | ----------------------- | --------- | --------- |
| `User_Id`     | `user(User_Id)`         | CASCADE   | CASCADE   |
| `Category_Id` | `category(Category_id)` | CASCADE   | CASCADE   |

### Notes

* Expenses support **soft delete** using `Status`
* Deleting a user or category automatically deletes related expenses
* Expense uniqueness is not restricted; multiple expenses can exist in the same category

---

## Relationships Summary

* **One User → Many Categories**
* **One User → Many Expenses**
* **One Category → Many Expenses**

All relationships are enforced using **foreign keys with CASCADE**, ensuring no orphan records exist.

---

## Design Rationale

* **Username uniqueness** prevents account ambiguity
* **(User_Id + Category_Name) uniqueness** enforces clean category organization per user
* **CASCADE deletes** simplify database cleanup
* **Soft delete** preserves logical history without immediate data loss
* Schema maps cleanly to **Hibernate / JPA entities**
* Ready for migration to **Spring Boot + Spring Data JPA**

---

## Recommended DB Constraints (for reference)

```sql
ALTER TABLE user
ADD CONSTRAINT UNIQUE (User_name);

ALTER TABLE category
