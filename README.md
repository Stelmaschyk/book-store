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
3. [Configuration](#configuration)
4. [How to run project](#How-to-run-project)
5. [Postman Collection](#postman-collection)
6. [API Documentation](#api-documentation)
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

## Configuration
***

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

#### Configure .env

This project uses the following environment variables to configure the application. You should create a `.env` file at the root of your project and populate it with the variables listed below.


```env
# MySQL Database Configuration
MYSQLDB_USER=username                # The MySQL database username
MYSQLDB_PASSWORD=password            # The MySQL database password
MYSQLDB_DATABASE=database            # The name of the database
MYSQLDB_LOCAL_PORT=3307              # The local MySQL port (for local development)
MYSQLDB_DOCKER_PORT=3306             # The MySQL port exposed in Docker container

# JWT Configuration
JWT_SECRET="anyStringLongerThan30characters"        # Secret key used for JWT token generation
JWT_EXPIRATION=3000000000000                        # Expiration time for JWT tokens in milliseconds

# Spring Application Configuration
SPRING_LOCAL_PORT=8081       # The local port for the Spring application
SPRING_DOCKER_PORT=8080      # The port for the Spring application in the Docker container
DEBUG_PORT=5005              # The port for debugging the application
```

## 🎯 How to run project
***

1) First, ensure that Docker is installed. You can download it here: ```https://www.docker.
com/products/docker-desktop/```

2) Launch the Docker application.

3) After opening the project in your IDE, navigate to the ```Dockerfile``` and initiate the creation of images.

<img src="./src/main/resources/images/docker_instruction1.jpg">

4) Next, open the ```docker-compose.yml``` file and sequentially create containers.
Start with the database container:

<img src="./src/main/resources/images/docker_instruction2.jpg">

5) Once it is successfully created, proceed to create the application container.

<img src="./src/main/resources/images/docker_instruction3.jpg">

6) Finally, run the project.

7) You can test the application using Postman.

## Postman Collection
***
For your convenience in testing with Postman, below is the link to the Postman collection for this project. It contains a set of requests for testing.

### Get Started

To get started with this collection:

1. **Import the Collection**:
   - Click the link below to import the collection into Postman:
     [Import Collection](https://app.getpostman.com/join-team?invite_code=b2f17ebe2958ffc813a51de179ff27167344ee409d0b8ed621a94e5efd4e06fc)

2. **Environment Setup**:
   - Make sure to configure the required environments before making requests.

Once everything is set up, you can begin sending requests to the endpoints specified in this collection.



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


