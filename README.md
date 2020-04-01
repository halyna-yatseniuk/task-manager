# task-manager
It's Spring Boot RESTful application to manage tasks by Users which are stored in MongoDB. JWT token which is implemented provides the possibility of registration and login users to the application with additional session security. Tasks can be created with the possibility to share them between users, also edited and deleted. Task sharing feature has a validation of not sharing the same ticket to a user who is already a viewer or to a task owner.

### Required to install:
```sh
* Java 8
```
```sh
* MongoDB Community server and Compass client
```
### To launch application *local* do further steps:
```sh
1. Clone repository on your local machine from git@github.com:halyna-yatseniuk/task-manager.git
```
```sh
2. Import project using Maven
```
```sh
3. Log in to Compass with localhost and port 27017
```
```sh
4. Run application
```

### Authorization steps:
* Perform registration/login API call to get Bearer token
* Copy Bearer token
![SwaggerToken](https://user-images.githubusercontent.com/62807345/78158096-7e61e180-7449-11ea-98f0-2410e0cee544.jpg)
* Open Authorize pop-up
* Paste copied Bearer token
![SwaggerAuthorization](https://user-images.githubusercontent.com/62807345/78158205-a05b6400-7449-11ea-8267-fbf356247ac9.jpg)
* Click ‘Authorize’ button

### Available API calls:

![SwaggerAPI](https://user-images.githubusercontent.com/62807345/78158314-bb2dd880-7449-11ea-94dd-3623b8a087fd.jpg)


### Share task flow:
Pay attention that share task feature needs action you want to perform to be provided:
**add** action - to share a task for a user and **remove** action - to delete access to the task from a user.

![TaskShare](https://user-images.githubusercontent.com/62807345/78158439-e87a8680-7449-11ea-8d1e-bddc0fcf94b4.jpg)




