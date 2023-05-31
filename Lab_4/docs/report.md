---
title: "Lab 04: A Gentle Introduction to Hadoop"
author: All in
date: "2023-02-17"
subtitle: "CSC14118 Introduction to Big Data 20KHMT1"
lang: "en"
titlepage: true
titlepage-color: "0B1887"
titlepage-text-color: "FFFFFF"
titlepage-rule-color: "FFFFFF"
titlepage-rule-height: 2
book: true
classoption: oneside
code-block-font-size: \scriptsize
---

- Author: [All in] group

- Date: 21/03/2023

- Subtitle: CSC14118 Introduction to Big Data 20KHMT1

- Language: English

## **Task progression** {-}

| No. of task | % completed |
|-------------|-------------|
| 1           | 100%        |
| 2           | 100%        |
| 3           | 100%        |
| 4.1         | 100%        |
| 4.2         | 15%         |

## **Self-reflection** {-}

- Advantages

  - All members in the group are supportive, are eager to learn, try to solve the problems/bugs as best as possible.
  - Groups have set up the plan, communication devices, document, working directory, etc. to work efficiently, as soon as this project started.
  - Everyone helped each other to solve the problems.
  - The group has learned and understood more about the workflow, components of Hadoop; knowed some tricky point in Hadoop; be ready for next projects.

- Disadvantage

  - Due to large amount of works in this project and other subjects, the quality of conducting plan has fallen off gradually.
  - We have not set up the real distributed Hadoop system. We will study this topics as soon as possible. As a result, we have not achieved the bonus scores fully.

## **Setting up Single-node Hadoop Cluster**

Each member has completed setting up a Single node cluster on their local machine, as described in supplied tutorial. Also, this process is captured in screenshots.

### **Member 1:** 20120090 - Nguyen The Hoang

> This member will present detail of the steps setting up Single-node Hadoop cluster. Other members will only show result in their machines. Some steps or result may be different between machines of each member, due to some difference of software/hardware environment and tutorials that they followed. But without loss of generality, the main steps are same.
>
> This Single-node Hadoop Cluster is installed in the Linux OS, Mint distro.

![Step 1: Make sure that a suitable version of Java installed](images\20120090\Task_1\1.png)

![Step 2: Download and extracted the Hadoop packaged file from the Apache Hadoop releases page](images/20120090/Task_1/3.png)

![Step 3: Set up the environment variable <code>JAVA_HOME</code> in the file <i>etc/environment</i> which points to the binary folder of Java, so that Hadoop can use Java when compiling MapReduce programs and execute damemons/processes.](images\20120090\Task_1\4.png)

![Step 4: Set up the environment variable <code>HADOOP_HOME</code> in the file <i>~/.bashrc</i> (or by using <code>export</code> temporarily) which points to the extracted folder of Hadoop, so that it is more convenient to call Hadoop command after that.](images\20120090\Task_1\5.png)

![Step 5: Check that Hadoop runs by typing `hadoop version`](images\20120090\Task_1\6.png)

#### **Running in Local (Standalone Mode)**

![Step 6: Keep the default configure file of Hadoop. Conduct the Standalone Operation as instructed in [^a], the above picture shows the result in the <i>output</i> folder](images\20120090\Task_1\7.png)

#### **Running in Pseudo-Distributed Mode**

**Step 7**: Change the configure files: <i>core-site.xml, hdfs-site.xml, mapred-site.xml, yarn-site.xml</i> as instructed in [^a] and [^b] to set up for Pseudo-Distributed Mode

![Step 8: Install <i>ssh</i>](images\20120090\Task_1\2.png)

![Step 9: Enable passwordless login by generating new SSH key with an empty passphrase](images\20120090\Task_1\8.png)

![Step 10: Formatting the HDFS filesystem <i>ssh</i>](images\20120090\Task_1\9.png)

![Step 11: Start NameNode daemon and DataNode daemon with <code>start-dfs.sh</code>](images\20120090\Task_1\10.png)

![Step 12: As instructed in [^a], make the HDFS directories required to execute MapReduce jobs; copy some local files into the distributed filesystem and run the MapReduce example provided](images\20120090\Task_1\11.png)

![Step 13: Copy the output files from the distributed filesystem to the local filesystem to examine them](images\20120090\Task_1\12.png)

#### **Running in Pseudo-Distributed Mode with YARN**

![Step 14: Start the ResourceManager daemon and NodeManager daemon by running <code>start-yarn.sh</code>. Optionally, start the history server by running <code>mr-jobhistory-daemon.sh start historyserver</code>](images\20120090\Task_1\13.png)

#### **Accessing the Web interfaces of running Hadoop process**

These are the results when start all main components of a Single-node Hadoop Cluster

![Web UI of NameNode through <code>http://localhost:9870/</code>](images\20120090\Task_1\14_NameNode.png)

![Web UI of a DataNode](images\20120090\Task_1\14_DataNode.png)

![Web UI of a ResourceManager through <code>http://localhost:8088/</code>](images\20120090\Task_1\14_YARNNode.png)

![Web UI of MapReduce history server <code>http://localhost:19888/</code>](images\20120090\Task_1\14_MapReduceServer.png)

### **Member 2:** 20120011 - Nguyen Hoang Huy

![Install Java and Hadoop then setup the environment path](images/20120011_NodeProof_0.jpg)

![Run start-all.cmd to start namenode, datanode, resourcemanager, nodemanager program](images/20120011_NodeProof_1.png)

![Access localhost:9000](images/20120011_NodeProof_2.png)

### **Member 3:** 20120030 - Nguyen Thien An

![Id of the first block to proof this is a personal computer.](images/20120030/Task_1/id.png)

![Install Java and Hadoop then setup the environment path](images/20120030/Task_1/1.png)

![Install ssh and pdsh](images/20120030/Task_1/2.png)

![JAVA_HOME and HADOOP_HOME](images/20120030/Task_1/3.png)

![Show hadoop version and bin/hadoop](images/20120030/Task_1/4.png)

![Make changes on core-site.xml, hdfs-site.xml, mapred-site.xml, yarn-site.xml](images/20120030/Task_1/5.png)

![ssh localhost](images/20120030/Task_1/6.png)

![Start all daemons hadoop](images/20120030/Task_1/7.png)

![Create a folder named 20120030 in /user](images/20120030/Task_1/8.png)

![Create folder 20120030 successfully!](images/20120030/Task_1/9.png)

![Start yarn and historyserver](images/20120030/Task_1/10.png)

![DataNode](images/20120030/Task_1/11_DataNode.png)

![MapReduce Server](images/20120030/Task_1/11_MapReduceServer.png)

![NameNode](images/20120030/Task_1/11_NameNode.png)

![YarnNode](images/20120030/Task_1/11_YarnNode.png)

![Stop all daemons hadoop](images/20120030/Task_1/12.png)

### **Member 4:** 20120165 - Hong Nhat Phuong

![Install Java and Hadoop then setup the environment path](images/20120165_Hadoop1.png)

![Make changes on core-site.xml, hdfs-site.xml, mapred-site.xml, yarn-site.xml](images/20120165_Hadoop2.png)

![Format namenode and start all daemons hadoop](images/20120165_Hadoop3.png)

## **Introduction to MapReduce**

1. How do the input keys-values, the intermediate keys-values, and the output keys-values relate?

    <p align="justify">
    The input keys-values are the data sets that are processed by the Map function. The input data is divided into key-value pairs that are distributed across the nodes in the Hadoop cluster. The Map function processes each key-value pair and produces an intermediate key-value pair.

    The intermediate keys-values are produced by the Map function and are used as input to the Reduce function. The intermediate key-value pairs are sorted by key and partitioned based on the key. Each partition is processed by a separate instance of the Reduce function.

    The output keys-values are the final result of the MapReduce process. The Reduce function produces the final key-value pairs which are written to the Hadoop Distributed File System (HDFS) or to an external storage system.
    </p>

2. How does MapReduce deal with node failures?

    <p align="justify">
    When the compute node at which the master is executing fails, a new copy can be started from the last checkpointed state. Only this one node can bring the entire process down; other node failures will be managed by the master.

    The master sends heartbeat to every worker periodically. If there is no response from the worker within a certain period of time, the master marks the worker as failed. Map tasks that were assigned by that worker will be reset to their original idle state and eligible for scheduling by other workers, even if they have completed. Completed map tasks are re-executed because their output is stored at that compute node, and is now unavailable to access.

    Similarly, reduce task in progress on the failed worker is also reset to idle and will have to be redone. Completed reduce tasks do not need to be re-executed since their output is stored in a global file system. When a map task is executed first by a worker and then later executed by another worker (because the first one failed), all workers executing reduce tasks are notified of the re-execution.
    </p>

3. What is the meaning and implication of locality? What does it use?

    Locality means that taking advantage of the fact that the input data is stored on the local disks of the machine that make up the clusters. As a result, the network bandwidth is conserved, and when running large MapReduce operations on a large cluster, most input is read locally, consume no network bandwidth, speed processes up significantly.

    Each file is divided into same size blocks (default 64 MB), then each block is created multiple copies (typically 3 copies). These blocks are stored on different machine, but highly optimised location (maybe 2 copies in the same rack, and 1 copy in the other rack in case of failing). MapReduce master take the location of the input files and above blocks into account and attempts to schedule a map task on a machine that contains a replica of the corresponding input data. If that is impossible, it trys to schedule map tasks near a replica of that task's input data (maybe on the same rack). Locality is also the result of Rack awareness of Hadoop system.

4. Which problem is addressed by introducing a combiner function to the MapReduce model? </h5>

    The combiner function introduced to the MapReduce model addresses the problem of **network congestion** and **data transfer**. In detail, When data is processed by the Map function, it generates a large number of key-value pairs, which are then sorted and shuffled before being sent to the Reduce function. This sorting and shuffling process can generate a significant amount of network traffic, especially if there are many duplicate keys. The combiner function is introduced after the Map phase and before the Shuffle and Sort phase. Its purpose is to aggregate the output of the Map function for each key, reducing the amount of data that needs to be transferred across the network. This can greatly reduce network congestion and improve the overall performance and scalability of the MapReduce job. Hence, the combiner function reduces network traffic, which is the main problem addressed by its introduction to the MapReduce model.

## **Running a warm-up problem: Word Count**

Each member has completed running the WordCount (v.1) program - a simple MapReduce program - on a Single node cluster on their local machine, as described in supplied tutorial. Also, this process is captured in screenshots.

### **Member 1:** 20120011 - Nguyen Hoang Huy

![Create input and output directory in hadoop fs](images/20120011_WordCount_0.jpg)

![Put file01.txt and file02.txt into input directory](images/20120011_WordCount_1.jpg)

![Run WordCount program](images/20120011_WordCount_2.jpg)

![Let's show the result](images/20120011_WordCount_3.jpg)

### **Member 2:** 20120030 - Nguyen Thien An

![File "input.txt"](images/20120030/Task_3/input.png)

![Create folder input and put input.txt into input directory](images/20120030/Task_3/1.png)

![Compile WordCount.java, create a jar and run WordCount program](images/20120030/Task_3/2.png)

![Show the output using -cat command](images/20120030/Task_3/3.png)

![Nodes of the cluster](images/20120030/Task_3/4.png)

![MapReduce Server](images/20120030/Task_3/MapReduceServer.png)

### **Member 3:** 20120090 - Nguyen The Hoang

![Step 1: Create the WordCount v1.0 file as instructed in [^c]. Compile this file to the <i>class</i> and <i>jar</i> file</code>](images\20120090\Task_3\1.png)

![Step 2: Start the Hadoop on the Pseudo-distributed Mode](images\20120090\Task_3\2.png)

![Step 3: As instructed in [^c], create two input files in the distributed filesystem, in the <i>input</i> folder](images\20120090\Task_3\3.png)

![Step 4: Run the MapReduce program](images\20120090\Task_3\Result_1.png)

![1. The submited jobs show in the Resource Manager YARN](images\20120090\Task_3\Result_3.png)
![2. The DataNode is doing its assigned task](images\20120090\Task_3\Result_4.png)
![3. The result is recored in the <i>output</i> folder, pulled to the local filesystem to print in the terminal](images\20120090\Task_3\Result_5.png)

### **Member 4:** 20120165 - Hong Nhat Phuong

![Create WordCount.java](images/20120165_WordCount1.png)

![Create folder input and put file01.txt, file02.txt into input directory](images/20120165_WordCount2.png)

![Compile WordCount.java, create a jar and run WordCount program](images/20120165_WordCount3.png)

![Show the output using -cat command](images/20120165_WordCount4.png)

## **Bonus**

### **Bad Relationship**

The **Unhealthy_relationship.java** program includes two phases:

1. The Mappter implementation (`UnhealthyRelationshipMapper` class), via the `map()` method, processes the input files one line at a time, read them as `Text` type object. It then splits the line into two words (seperated by whitespace): `word_1` and `word_2`. It then  emits two key-value pair of `< word_1, 1>` and `< word_2, -1>`. The first pair means that: `word_1` has one positive relation with other objects, so it will contribute value '1' to its final result. On the other hand, the second pair means that: `word_2` has one negative relation with other objects, so it will contribute value '-1' to its final result. In short, by considering each word's position in the input files, we can count each positive and negative relation of each word, and then we aggregate these value to know the final relation of each word.

    So the `map()` emits the output has type `<Text, IntWritable>`. This pair of key-value will be grouped by key and all the value of one key will be put into one list.

2. The Reducer implementation (`UnhealthyRelationshipReducer`), via the `reduce()` method, receive the key-value pairs `<word, [list_of_1_or_-1]>` for each distinct word. The `reduce()` method just aggregate all the value in `[list_of_1_or_-1]`. The final value `sum` will be used to determined if that word has negative/positive/equality relation. In specific:

    - `sum` > 0: That word has positive relation
    - `sum` = 0: That word has equality relation
    - `sum` < 0: That word has negative relation

    The `reduce()` method then just emit the final output for each word in the form key-value `<word, type_of_relation>`, with type `<Text, Text>`.

## **References**

[^a]: Apache Hadoop, “Hadoop: Setting up a Single Node Cluster,” Jul. 29, 2022. https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html.

[^b] T. White, “Appendix A. Installing Apache Hadoop,” in Hadoop: The definite guide, 2012, p. 680.

[^c] Apache Hadoop, “MapReduce Tutorial,” Jul. 29, 2022. https://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html.
