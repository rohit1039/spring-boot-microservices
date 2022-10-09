# PRODUCT-SERVICE endpoints
#### 1.) Add Product
````
POST /product/add HTTP/1.1
Host: localhost:9090
Content-Type: application/json
Content-Length: 88

{
    "productName":"Samsung Galaxy A50",
    "price":"21000",
    "quantity":"50"
}
````

#### 2.) Get list of all products
````
GET /product/get-all HTTP/1.1
Host: localhost:9090
````

#### 3.) Get product by productID
````
GET /product/2 HTTP/1.1
Host: localhost:9090
````
#### 4.) Reduce product's quantity when order is placed successfully
````
PUT /product/reduce-quantity/3?quantity=1 HTTP/1.1
Host: localhost:9090
````