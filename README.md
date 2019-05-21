# csv2

After you clone the project

Run the following

1) chmod +x generateTestData.sh
2) ./generateTestData.sh

This will unzip the test data in test/resources folder

3) mvn clean package

This will build the jar in the target folder and run all the tests

4)  java -jar target/csv-1.0-SNAPSHOT.jar Stock src/test/resources/TCS_Million.csv

This will run the program using the Stock entity as generic param

5) java -jar target/csv-1.0-SNAPSHOT.jar Stock2 src/test/resources/TCS_MillionWithStockName.csv 

This will run the program using the Stock2 entity as generic param

