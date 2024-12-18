## Welcome to Our Online Book Store! 📚✨
***
<img src="./src/main/resources/images/online_book_store.jpg" width="500" height="250">

***
![Java](https://img.shields.io/badge/Java-17-blue?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-green?style=flat&logo=spring)
![Spring Boot](https://img.shields.io/badge/Mysql%20-blue?style=flat&logo=mysql)
![Spring Boot](https://img.shields.io/badge/JWT%20-orange?style=flat&logo=jwt)
![Spring Boot](https://img.shields.io/badge/Swagger%20-green?style=flat&logo=swagger)
![Docker](https://img.shields.io/badge/Docker-Container-blue?style=flat&logo=docker)
![AssertJ](https://img.shields.io/badge/AssertJ-Assertions-green?style=flat&logo=java)
![AssertJ](https://img.shields.io/badge/Liquibase-grey?style=flat&logo=java)
![AssertJ](https://img.shields.io/badge/MapStruct-gray?style=flat&logo=java)

***

### Table of Contents
***

1. [Introduction](#Introduction)
2. [Installation](#installation)
3. [Functionality](#functionality-and-features)
4. [API Documentation](#api-documentation)
   - [BookController](#bookcontroller)
   - [CategoryController](#categorycontroller)
   - [OrderController](#ordercontroller)
   - [ShoppingCartController](#shoppingcartcontroller)


## 😊 Introduction
***
### Hello, future user! 

This section is responsible for the backend functionality of our online book store. It handles essential tasks like user registration, book catalog management, and order processing, ensuring a seamless and secure shopping experience.

### How it Works:

 - **Register:** To get access to all the features, start by creating an account.
 - **Browse and Shop:** Explore our wide selection of books, add them to your cart, and place an order.
 - **Personal Account:** Your purchase history and all order details are stored in your personal account, making it 
   easy 
   to keep track of your literary adventures.

We’re here to make your book shopping experience as enjoyable and convenient as possible!

## ⚙️ Installation
***

 - Make sure you have the following installed on your machine Java Development Kit - Version 17 or higher and 
   Maven

 - To build and run this application locally, you'll need latest versions of Git, JDK installed on your computer.

 - Clone the repository: ``` git clone https://github.com/Stelmaschyk/book-store.git ```

#### Configure Database:

 - Open ``` src/main/resources/application.properties ``` file and update following properties with your database 
   connection details

```
spring.datasource.url=jdbc:tc:mysql:8.0.33:///database-name
spring.datasource.username=username
spring.datasource.password=password

jwt.expiration=3000000000000
jwt.secret= anyStringLongerThan30characters
```

## 🎯 Functionality
***

#### 🔐 User authentication
- Registration
- Login

#### 📖 Books management
- Create a Book
- Delete a Book
- Update Book Data
- Search by Book Title and Author
- Search for Books by Category

#### 🗂️ Categories management
- Create a Category
- Delete a Category
- Update a Category

#### 🛒 Cart and order management
- Add Books to Cart
- Remove from Cart
- Save Order
- Order Status


## API Documentation
***

Explore all available requests and their options using the interactive **Swagger UI** documentation.

#### How to Access Swagger UI:
1. Ensure your server is running locally at `http://localhost:8080`.
2. Open [Swagger UI](http://localhost:8080/api/swagger-ui/index.html#/).
3. Test and explore API endpoints directly in the interface.

Swagger documentation is automatically updated whenever new controllers are added or existing ones are modified.

1. **BookController**
   - `GET /books` — returns a paginated list of available books.
   - `GET /books/{id}` — retrieves a book by its unique ID.
   - `POST /books` — *(admin only)* adds a new book.
   - `PUT /books/{id}` — *(admin only)* updates an existing book by ID.
   - `DELETE /books/{id}` — *(admin only)* deletes a book by its ID.
   - `GET /books/search` — searches for books by author and title with pagination.

2. **CategoryController**
   - `POST /categories` — *(admin only)* creates a new category.
   - `GET /categories` — retrieves a paginated list of all categories.
   - `GET /categories/{id}` — fetches a category by its unique ID.
   - `GET /categories/{id}/books` — lists books in a specific category by category ID.
   - `PUT /categories/{id}` — *(admin only)* updates an existing category by ID.
   - `DELETE /categories/{id}` — *(admin only)* deletes a category by its ID.

3. **OrderController**
   - `POST /orders` — places a new order for the authenticated user.
   - `GET /orders` — fetches all orders for the authenticated user.
   - `PATCH /orders/{orderId}` — *(admin only)* updates the status of an order.
   - `GET /orders/{orderId}/items` — retrieves all items in a specific order.
   - `GET /orders/{orderId}/items/{orderItemId}` — fetches a specific item from an order by its ID.

4. **ShoppingCartController**
   - `POST /cart` — adds a book to the authenticated user's shopping cart.
   - `GET /cart` — retrieves the authenticated user's shopping cart.
   - `PUT /cart/items/{id}` — updates the quantity of a book in the cart.
   - `DELETE /cart/items/{id}` — removes a book from the shopping cart.
