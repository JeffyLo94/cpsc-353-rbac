# [CPSC-353 Project 1 - RBAC Model](https://github.com/JeffyLo94/cpsc-353-rbac)
A console based application of a simple RBAC model.
  * Implemented in Java
  * Tested on Mac and Linux
  * Extra Credit not implemented

### Author:
Jeffrey Lo - jeffylo94@csu.fullerton.edu

### Usage :
###### Execution Checklist:
  * URA.txt and PRA.txt in same directory as jar (see METHOD 1)
  
  OR
  
  * filepath of directory containing URA.txt and PRA.txt (see METHOD 2)
  
###### Execution:

Method 1 Execution:
* Execute jar file from console (mac/linux: ```java -jar Rbac.jar``` )

Method 2 Execution:
* Execute jar from console with path as argument
      
      java -jar Rbac.jar /Users/jeffreylo/CSUFDevelopment/cpsc-353-rbac/inFiles/

###### Login

* If RBAC initialized with text files will display:

      RBAC initialized
      login:

* login with valid username.

      Correct input: ```Welcome exampleJohn!```
      Incorrect input: ```Error: user exampleJoe is not in the database!```

###### Commands once logged in:
* View User Roles (roles)

        cmd> roles
        Professor	Lecturer

* View User Permissions (permits)

        cmd> permits
        as Professor:
            modify Gradebook	edit Syllabus
        as Lecturer:
            view Syllabus


* Permission Command

      valid command: cmd> modify Gradebook
                     Access granted by virtue of roles: Professor
      invalid command: cmd> throw Ball
                       Access Denied: You are not authorized to perform this action

* Quit (q, quit, logout)

      cmd> q   
      program ending...
