# MeetUp

## Finding everything in project

### Sourcecode and xml-files
All important files regarding sourcecode and xml-files is located under app/src/, where different code are resides in the following subfolders.

All the source code can be found under com/bulbasaur/dat256 where the code is structure into three main folders: model, viewmodel and services. All activities are located in viewmodel, and the code regarding the database-communication resides in services.

All xml-files regarding the various views are located in res. 

### Documents
All important documents such as the final reflection, team reflections and indiviual reflections can be found under the documents folder in the root. In the same folder are documents such as the social contract, definition of done, database structure, gui mockups and business model canvas. 

### The general structure of the app
The main file of database-communication is the file "Database" in services. The main model file is "Main", which is the run-time model of the app. The main activity file used to run the app is the file "MenuActivity in viewmodel. 

### Links to other document locations
Not eveything regarding the projects exists on github. The following is other locations of our documents. If identification is needed it is written below, otherwise you should already have access. 

#### Trello
https://trello.com/b/QVMxYfi6/dat256-software-engineering-pr

#### Google Drive
https://drive.google.com/drive/u/0/folders/1a8nYGr-IJ3wJh2ORFTUUllZsnjH8EUHg

#### Firebase
https://console.firebase.google.com/u/2/project/meetups-5921e/overview

If you are not already in the project when clicking the link, click in the top right corner and then on the project "MeetApp".

login: meetups.dat256@gmail.com

password: 123Bulbasaur

## Running the application
The app should be able to start without having to login or register any user, but the app is then restricted. To use the full app you have to create a user with the register flow or login an existing one. However, to do this according to the restrictions of firebase you have to use a whitelisted phonenumber and a pre set verification code. We have pre-set whitelisted numbers you can use, which are given below. You can whitelist your own ones, but its is a little more complicated. How this is done is given below. 

### Register with a new user
phone: +16505550000

verification code: 123456

### Login with an existing user
phone: +46721743513

verification code: 123456

### Add whielisted number
In firebase, click on Authentication in the left meny. Then click Sign-in method and click on phone. Within that view, click on "Phone numbers for testing" and add your own numbers and choose a verification code. NOTE! use country codes in the beginning of the phone numbers (+46 instead of 0 etc.)
