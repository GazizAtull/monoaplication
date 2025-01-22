
# Project Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Architecture Overview](#architecture-overview)
3. [Design Patterns Used](#design-patterns-used)
   - [Builder Pattern](#builder-pattern)
   - [Command Pattern](#command-pattern)
   - [Observer Pattern](#observer-pattern)
   - [Decorator Pattern](#decorator-pattern)
   - [Factory Pattern](#factory-pattern)
   - [Adapter Pattern](#adapter-pattern)
4. [Modules and Components](#modules-and-components)
   - [Controllers](#controllers)
   - [Services](#services)
   - [Repositories](#repositories)
   - [Models](#models)
   - [Configurations](#configurations)
5. [Component Interactions](#component-interactions)


---

## Introduction

My project is a web application built using the Spring Boot framework. It includes features such as user authentication, product management, shopping cart functionality, order processing, and email notifications. The application employs various design patterns, enhancing its modularity, scalability, and maintainability.

---

## Architecture Overview

The application follows a layered architecture:

- **Controllers:** Handle HTTP requests and responses.
- **Services:** Contain business logic.
- **Repositories:** Interact with the database.
- **Models:** Represent the data structures.
- **Configurations:** Set up security and application settings.

---

## Design Patterns Used

### Builder Pattern

- **Purpose:** Simplify the construction of complex objects.
- **Implementation:** `UserProfileBuilder` class is used to create instances of `UserProfile`.

```java
UserProfile userProfile = new UserProfileBuilder()
        .setUser(newUser)
        .setEmail(email)
        .setFullName(fullName)
        .setPhone(phone)
        .setAddress(address)
        .build();
```

---

### Command Pattern

- **Purpose:** Encapsulate a request as an object, allowing for parameterization and queuing of requests.
- **Implementation:** `Command<T>` interface and its implementations, such as `SaveProductCommand` and `SaveUserCommand`.

```java
public interface Command<T> {
    T execute();
}
```

---

### Observer Pattern

- **Purpose:** Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified.
- **Implementation:** `Observer` interface and `EmailNotificationObserver` class to notify users about new products.

```java
public class SaveProductCommand implements Command<Product> {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
```

---

### Decorator Pattern

- **Purpose:** Attach additional responsibilities to an object dynamically.
- **Implementation:** `ProductComponent` interface, `ProductDecorator` abstract class, and `DiscountDecorator` class to apply discounts to products.

```java
public class DiscountDecorator extends ProductDecorator {
    private final double discountPercentage;

    @Override
    public Double getPrice() {
        return decoratedProduct.getPrice() * (1 - discountPercentage / 100);
    }
}
```

---

### Factory Pattern

- **Purpose:** Create objects without exposing the instantiation logic to the client.
- **Implementation:** `OrderItemFactory` interface and `DefaultOrderItemFactory` class to create `OrderItem` instances.

```java
public interface OrderItemFactory {
    OrderItem createOrderItem(Product product, int quantity, Order order);
}
```

---

### Adapter Pattern

- **Purpose:** Allow incompatible interfaces to work together.
- **Implementation:** `ProductAdapter` class adapts `Product` to the `ProductComponent` interface.

```java
public class ProductAdapter implements ProductComponent {
    private final Product product;

    @Override
    public Double getPrice() {
        return product.getPrice();
    }
}
```

---

## Modules and Components

### Controllers

- **AdminController:** Manages administrative functions like user and product management.
- **AuthController:** Handles user authentication and registration.
- **CartController:** Manages shopping cart operations.
- **HomeController:** Handles home page, product details, and user profile views.
- **PaymentController:** Processes payments and order creation.
- **GlobalControllerAdvice:** Provides global attributes and exception handling.

---

### Services

- **UserService:** Manages user data and profiles.
- **ProductService:** Handles product-related operations.
- **CartService:** Manages shopping cart logic.
- **OrderService:** Processes orders and handles order-related business logic.
- **EmailService:** Sends emails for notifications and order confirmations.
- **CustomUserDetailsService:** Implements user details service for authentication.

---

### Repositories

- **UserRepository:** CRUD operations for User.
- **UserProfileRepository:** CRUD operations for UserProfile.
- **ProductRepository:** CRUD operations for Product.
- **OrderRepository:** CRUD operations for Order.
- **OrderItemRepository:** CRUD operations for OrderItem.

---

### Models

- **User:** Represents a user in the system.
- **UserProfile:** Contains additional user information.
- **Product:** Represents a product available for purchase.
- **Order:** Represents an order placed by a user.
- **OrderItem:** Represents individual items within an order.
- **CartItem:** Represents an item in the shopping cart.

---

### Configurations

- **SecurityConfig:** Configures security settings, including authentication and authorization.
- **HttpToHttpsConfig:** Redirects HTTP requests to HTTPS.

---

## Component Interactions

- **Controllers** interact with **Services** to process HTTP requests.
- **Services** use **Repositories** to access and manipulate data.
- **Models** are used across **Controllers**, **Services**, and **Repositories** to represent data.
- **EmailService** is used by **OrderService** and Observer implementations to send emails.
- Design Patterns like Command, Observer, and Decorator are implemented in **Services** and **Controllers** to structure business logic.

---

## Example Flow: Placing an Order

1. User adds products to the cart via `CartController`.
2. `PaymentController` processes the payment and calls `OrderService` to create an order.
3. `OrderService` uses the Factory Pattern to create `OrderItem` instances.
4. `OrderService` saves the order using `OrderRepository`.
5. `OrderService` calls `EmailService` to send an order confirmation email.
6. `EmailService` uses `TemplateEngine` to generate email content.

---

