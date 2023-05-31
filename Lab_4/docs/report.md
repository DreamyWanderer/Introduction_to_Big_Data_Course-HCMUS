---
title: "Lab 04: Streaming Data Processing with Spark"
author: All in
date: "2023-05-31"
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
| Get Twitter tweets           | 100%        |
| Stream tweets to Apache Kafka           | 100%        |
| Perform sentiment analysis on tweets           | 100%        |
| Visualize the results         | 100%        |

## **Self-reflection** {-}

- Advantages

  - All members in the group are supportive, are eager to learn, try to solve the problems/bugs as best as possible.
  - Groups have set up the plan, communication devices, document, working directory, etc. to work efficiently, as soon as this project started.
  - Everyone helped each other to solve the problems.
  - The group has learned and understood more about the workflow, components of Hadoop; knowed some tricky point in Hadoop; be ready for next projects.

- Disadvantage

  - Due to large amount of works in this project and other subjects, the quality of conducting plan has fallen off gradually.
  - The visualization is not really informative as we want.
  - We do not do the queries on the Structured Streaming to get aggregrated results directly.

## Get Twitter tweets

We **get the tweets** from the *ChatGPT-Tweets* database, which is supplied kindly by the teacher.

**Save the the tweets**: this step is implemented in *load_data.py*.

Here, we dump all the data to the *data.json*. Afther that, we connect to MongoDB and create a database named *Allin* conatins a collection named *ChatGPT_Tweets*. Finally, we push all the tweets to this collection as documents.

## Stream tweets to Kafka

### Send events from MongoDB to Kafka

This process is implemented in *producer.py*. This is used to simulate the real-time events happening in real life.

Here, we use a cursor to access the collection *ChatGPT_Tweets* in the MongoDB. A `producer` is set up for connecting to Kafka. The cursor just traverse from top to bottom of the collection and retrieve the document to send to Kafka through `producer`. As Kafka only accept key-value pair under binary format, we encode the tweets as byte UTF-8 type.

Moreover, to simulate the case that data does not go to Kafka all at once but in random interval time and random inveral amount of quantity,  we use two random variable `NUM_TWEETS` and `TIME_WAIT`. It means we retrieve `NUM_TWEETS` tweets (collection) from MongoDB, then push them into Kafka. After that, it will wait `TIME_WAIT` second before repeat above process.

### Kafka setup

This process is implemented in *setup.sh*. Normally we need Zookeeper to manage the Kafka cluster. But here, we use the new mode called *Kraft*. Kafka will manages the cluster themself through broker, controller and voting mechanism. The setup steps is done as [cited].

Here, we setup a local Kafka cluster includes 2 nodes (or 2 brokers). We only use 1 topic named **Tweets** to contain the tweets sent.

### Get events from Kafka

This process is implemented in *consumer_and_model.py*. This is used to retrieve the new tweets in Kafka and push it to sentiment analyze tool.

We set up a Spark Session to use Structured Streaming data type. A Structured Streaming object named `df` is used to contain the tweets get from Kafka in real-time. `df` has only two columns: *Date* for event time, and *Tweet* to contain tweets content. When get a new event from Kafka, we take out the `value` field of event and change it into a column in `df` as string type. We explode this column from JSON-string type to into *Data* and *Tweet* column.

## Perform sentiment analysis on Tweets

A sentiment analyze tool called `TextBlob` is used for this purpose. This is implemented in *consumer_and_model.py*.

We crete a udf which receives a string from *Tweet* column and transmit it to `TextBlob`. The results is appended to a new column named *Sentiment* in the `df`.

The sentiment result is a float number, which its negative and positive magtitude respected to how negative or positive a tweet is. If the value is 0, the tweet is neutral.

For each mini-batch that Spark has processed completely, it will send this batch of dataframe to function `ForEachBatchFunction()` through `foreachBatch()` souce sink to aggregate the results and visualize these results.

## Visualize the analytic results

This step is implemented in *visualize.py*.

We use **Plotly** library to draw plot and **Dask** to update new results in real time.

In the function `ForEachBatchFunction()` above, it traverses the row in the batch result dataframe to count number of positive/negative/neutral tweets in that mini-batch. After that it adds these value into the global variable counter `num_pos_tweet`, `num_neg_tweet` and `num_neu_tweet` to aggregate the results. These counter is exported to file *results_visualize.json*.

With 1 second interval time, in `visualize.py` we read the aggregated result in *results_visualize.json* and draw the pie chart show percentage of positive/negative/neutral tweets up to that time.

Insight form results:

## **References**
