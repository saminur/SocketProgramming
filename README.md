This program will be used to transfer files from client to server. Server will be listening on a port (say port# 5050) and Client will connect to Server and transfer files to Server. Here are details:
  1. Client should accept command line parameter to set the number of concurrent file transfers. For example, java Client 5 will transfer five files concurrently at any given time      as long as there are enough files.  If concurrency number is not entered, it should transfer one file at a time (aka concurrency=1), by default.
  2. The application should support integrity verification. That is, client and server will calculate checksum of files after it’s transferred and compare them to make sure            data is transferred without any error.
  3. test scenarios.
      a) Create a dataset with 100 files each 10MB size and transfer with concurrency 1, 2,4 and 8 and measure throughput
      b) Create a dataset with 10 files each 1GB size and transfer with concurrency 1, 2, 4 and 8 and measure throughput
      c) Combine above two datasets in a single dataset and transfer with concurrency 1,2,4 and 8 and measure throughput
  4. After tests are done, draw figure for each tests case in item#2 where x-axis is concurrency value and y-axis is throughput.
  
  
Server End Run:

javac -cp . com/sami/Server/*.java
java -cp . com/sami/Server/FileRecieveServer

Client End Run:

javac -cp . com/sami/Client/*.java
java -cp . com/sami/Client/ConcurrencyHandler

File creation:

javac -cp . com/sami/creatFile/*.java
100 files of 10 MB = java -cp . com/sami/creatFile/DataSetCreation
10 files of 1 GB = java -cp . com/sami/creatFile/DataSetCreationGB
