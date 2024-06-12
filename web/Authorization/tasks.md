1) 



Страница UPdPageProfile 
настроить функционал изменения профиля
Seller
POST
/private/seller/profile/change
Change seller profile info


Страница Register/Login
Private routes

POST
/private/registerSeller
Register seller account


Private routes
Profile page
GET
/private/seller/profile
Profile info


GET
/private/seller/profile/loyalty
Loyalty of profile


Buyer
Buyer operations


UpdProfilePage
POST
/private/buyer/profile/change
Change profile info


ProfilePage
GET
/private/buyer/profile
Profile info



GET
/private/buyer/profile/loyalty
Loyalty of profile


GET
/private/buyer/profile/CLV
CLV of profile


Cart
POST
/private/buyer/cart/clear
Clear cart items


POST
/private/buyer/cart/change ??
Change cart containtment

Jump to definition

POST
UpdChangeCart
/private/buyer/cart/changeCart
Changes cart

Jump to definition

POST
CardAdd
/private/buyer/cart/add
Add product to cart



GET
CartPage
/private/buyer/cart/
Cart info


Authentication
Forgot password endpoints for public

Register
POST
/public/register
Register new user


Forget password + nodemailer
отправка кода на почту
POST
/public/forgot/send

Проверка кода на правильность
POST
/public/forgot/check


Login 
POST
/public/authenticate
Login and returns token


POST
/private/profile
Register new user



POST
/private/profile/changeTransparentPolicies
Agree many transparent policies

Order
Operations for buyers to order


POST
/private/buyer/order/create
Buyer creates order with their cart

GET
/private/buyer/order/{id}
Getting specifig of order


GET
/private/buyer/order/list
Getting list of orders


