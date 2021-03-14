# BitcoinTrend
A sample app for the charts in clean arch.


# Concept Used:
## 1. Architecture: The app use MVVM Clean arch with SOLID principle with Layer Modularization. The app has following modules.
a. Common Module: This module is responsible for providing common logic to whole app.
b. Network Module: This module deals with all the networking based code used in the app.
c. Datasource Module: This module acts as facotry for dillferent kind of data source(Remote and Local).
d. Domian Module: This module contains the business logic of the app. This has repository which all the business logic and usecase for each feature. 
e. Presentation/UI Module: This module has all the UI related logic and dummy views.


## 2. Orientation change: The orientation change has been handeled gracefully by ViewModel. Refer the screen shots in Responsive Layout section.


## 3. Mockk Unit test.


## 4. Dependency Injection: App uses KOIN dependecy injection.


## 5. Reactive Programming: Coroutine flow is used for reactive based approach.


## 6. Responsive Layout: App has responsive layout powered by Motion Layout for animation and relative transitions. Here are few screen shots of app in Table(Version 7.1.1) and two Phones.

a. Tablet: 

<img width="809" alt="Screenshot 2021-03-14 at 18 42 35" src="https://user-images.githubusercontent.com/16761273/111078950-27d5a200-84f8-11eb-87e6-d91b43e8b200.png">
<img width="806" alt="Screenshot 2021-03-14 at 18 42 46" src="https://user-images.githubusercontent.com/16761273/111078953-299f6580-84f8-11eb-84c2-d226b1e8b725.png">
<img width="1060" alt="Screenshot 2021-03-14 at 18 47 58" src="https://user-images.githubusercontent.com/16761273/111078956-2ad09280-84f8-11eb-9870-6c8aada7add9.png">


b. Pixel 3:

<img width="556" alt="Screenshot 2021-03-14 at 18 36 43" src="https://user-images.githubusercontent.com/16761273/111078989-4a67bb00-84f8-11eb-816f-dc32b8f66402.png">
<img width="555" alt="Screenshot 2021-03-14 at 18 36 51" src="https://user-images.githubusercontent.com/16761273/111078991-4c317e80-84f8-11eb-81df-51cf6cdcefd1.png">
<img width="1063" alt="Screenshot 2021-03-14 at 18 37 02" src="https://user-images.githubusercontent.com/16761273/111078994-4cca1500-84f8-11eb-9060-b696b85597cc.png">


c. Samsung A10

<img width="527" alt="Screenshot 2021-03-14 at 18 35 13" src="https://user-images.githubusercontent.com/16761273/111079006-61a6a880-84f8-11eb-9703-2ef1d3aa7081.png">
<img width="528" alt="Screenshot 2021-03-14 at 18 35 29" src="https://user-images.githubusercontent.com/16761273/111079008-63706c00-84f8-11eb-87e1-e6c4b3ff7809.png">
<img width="1060" alt="Screenshot 2021-03-14 at 18 35 44" src="https://user-images.githubusercontent.com/16761273/111079011-64090280-84f8-11eb-9912-e74daf2cdc5b.png">


## 7. UI states: App has three UI states: LOADING; SUCCESS; ERROR which has different UIs. Here are few state screenshots.

a. Success State:

<img width="527" alt="Screenshot 2021-03-14 at 18 35 13" src="https://user-images.githubusercontent.com/16761273/111079271-9109e500-84f9-11eb-8cc5-924dbc5c5476.png">

b. Error State:

<img width="507" alt="Screenshot 2021-03-14 at 01 16 48" src="https://user-images.githubusercontent.com/16761273/111079282-a2eb8800-84f9-11eb-9e54-11e9a066457b.png">

c. Loading Screen:

![screenshot-2021-03-14_19 12 34 143](https://user-images.githubusercontent.com/16761273/111079508-9156b000-84fa-11eb-9535-78b615234706.png)

## 8. Material Design: App uses google adviced material design specs and views.


##9. App Video:

a. Orientation Handeling: 

![ezgif com-gif-maker (5)](https://user-images.githubusercontent.com/16761273/111079598-f14d5680-84fa-11eb-8a7e-f4ca1ac5b1aa.gif)

b. Animation:

![ezgif com-gif-maker (6)](https://user-images.githubusercontent.com/16761273/111079669-32de0180-84fb-11eb-88df-9c417337d8c7.gif)

c. Feature:

![ezgif com-gif-maker (7)](https://user-images.githubusercontent.com/16761273/111079813-f363e500-84fb-11eb-9d93-c6ee332dfad2.gif)

