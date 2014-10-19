Search-Engine
=============

Instructions on how to run the Lucene Searcher


We have provided one lucene_searcher.bat (Windows) file which in turn calls the Lucene_Searcher executable jar file.

Example: lucene_searcher.bat <Query:ucr> <Lucene Index Directory: Lucene_index>
Here Lucene_Index is the directory where all lucene indexes are created

Here is a sample working example

lucene_searcher.bat  ucr  Lucene_Index



Instructions on how to run the Hadoop Searcher

We have provided one hadoop_searcher.bat (Windows) file which in turn calls the Hadoop_searcher executable jar file.

Example: hadoop_searcher.bat <Query:college> <Hadoop Index Directory: Hadoop_Output>
Here Hadoop_Output is the directory where all Hadoop indexes are created

Here is a sample working example

hadoop_searcher.bat college Hadoop_Output



Instructions on how to run the Hadoop indexer

We have provided one hadoop_indexer.bat (Windows) file which in turn calls the MapReduce-0.0.1-SNAPSHOT executable jar file.

Example: hadoop_indexer.bat <Hadoop Input: Hadoop_Input > <Hadoop Index Directory: Hadoop_Output>
Here Hadoop_Input is the directory where all input crawled files are kept

Here Hadoop_Output is the directory where all Hadoop indexes are created

You can also directly take the MapReduce-0.0.1-SNAPSHOT executable jar file for running on the cluster

You have to create an Input File directory in your HDFS.

Copy the contents from Hadoop_Input folder to your newly created input folder in HDFS.

Say the input directory you created is In

Sample command-
  hadoop jar   MapReduce-0.0.1-SNAPSHOT.jar  In  Out



Instructions for running the web Application

1) Install Wamp Server from the following location-
http://sourceforge.net/projects/wampserver/

2) Go to the folder wamp, inside there would be an exe named wampmanager , you can start the server with that, check at the bottom right of your screen to check wamp server has got a green light to ensure its running.

3) Copy folder named Web_Application from my submission and copy it inside the folder named www inside wamp folder.

4) Now go to the browser and type
http://localhost/ Web_Application /

Now you will be all set to use the web application



