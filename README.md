# League Backend Challenge

In main.go you will find a basic web server written in GoLang. It accepts a single request _/echo_. Extend the webservice with the ability to perform the following operations

Given an uploaded csv file
```
1,2,3
4,5,6
7,8,9
```

1. Echo (given)
    - Return the matrix as a string in matrix format.
    
    ```
    // Expected output
    1,2,3  0,0|0,1|0,2
    4,5,6  1,0|1,1|1,2
    7,8,9  2,0|2,1|2,2
    ``` 
2. Invert
    - Return the matrix as a string in matrix format where the columns and rows are inverted
    ```
    // Expected output
    1,4,7   0,0|1,0|2,0
    2,5,8   0,1|1,1|2,1
    3,6,9   0,2|1,2|2,2
    ``` 
3. Flatten
    - Return the matrix as a 1 line string, with values separated by commas.
    ```
    // Expected output
    1,2,3,4,5,6,7,8,9
    ``` 
4. Sum
    - Return the sum of the integers in the matrix
    ```
    // Expected output
    45
    ``` 
5. Multiply
    - Return the product of the integers in the matrix
    ```
    // Expected output
    362880
    ``` 

The input file to these functions is a matrix, of any dimension where the number of rows are equal to the number of columns (square). Each value is an integer, and there is no header row. matrix.csv is example valid input.  

Run web server
```
go run .
```

Send request
```
curl -F 'file=@/path/matrix.csv' "localhost:8080/echo"
```

## What we're looking for

- The solution runs
- The solution performs all cases correctly
- The code is easy to read
- The code is reasonably documented
- The code is tested
- The code is robust and handles invalid input and provides helpful error messages


### COMPLETED SOLUTION USER GUIDE

``RUN APPLICATION``
- From the submitted project directory, change to program directory as follows
```
cd compiled-jar
```
- Then run the following command
```
java -jar matrix-0.0.1-SNAPSHOT.jar 
```

- The application will be started on port 8090
- You can also import the source into IDE (e.g. Intellij recommended) and run as follows:
```
./mvnw spring-boot:run
```

### TESTING IMPLEMENTED API
- Locate Postman Collection: Postman collections of the implemented endpoints is available in
```
   postman-API-collection folder at project rood directory
```
- Please upload collection to postman and test from there
```
NOTE: sample request / response is available in screenshot folder at project root directory
```
