from pyspark.sql import SparkSession, Column, DataFrame
from pyspark.sql.functions import from_json, to_json, col, lit
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

from pyspark.sql.functions import udf
from pyspark.sql.types import FloatType

from textblob import TextBlob

import json

# Traverse each row in the DataFrame and update global variables
def process_row(row):
    sentiment = row["Sentiment"]
    if sentiment > 0:
        num_pos_tweet.add(1)
    elif sentiment < 0:
        num_neg_tweet.add(1)
    else:
        num_neu_tweet.add(1)

def GetSentimentValue(text):
    analysis = TextBlob(text)
    value = analysis.sentiment.polarity
    return float(value)

def SentimentValueToText(value):
    if (value > 0): return 'Positive'
    if (value < 0): return 'Negative'
    return 'Neutral'

def ForEachBatchFunction(df: DataFrame, epoch_id):

    # Keep df for mulptile uses
    df.persist()

    # Traverse the df and count number of pos/neg tweets. Add to global ones.
    df.foreach(process_row)

    # After that, call Visual function and transmit counter to visualize results

    # Show tweets on console
    df.show(20, False)

    # Visualize
    Visual()

    # Free this dataframe from storage memory
    df.unpersist()

def TweetsConsumerAndSentimentAnalyze():

    master = "local"
    appName = "Sentiment Analyze"
    scala_version = '2.12'
    kafka_version = '3.1.1'
    packages = [f'org.apache.spark:spark-sql-kafka-0-10_{scala_version}:{kafka_version}'
    ]

    spark = SparkSession.builder \
        .master(master) \
        .appName(appName) \
        .config("spark.jars.packages", ",".join(packages)) \
        .getOrCreate()
    conf = spark.sparkContext.getConf()
    print("Packages", conf.get("spark.jars.packages"))

    kafka_servers = "localhost:9092"
    topic = "Tweets"

    try:

        schema = StructType([\
                StructField("Date", StringType(), True), \
                StructField("Tweet", StringType(), True) \
            ])

        df = spark \
            .readStream \
            .format("kafka") \
            .option("kafka.bootstrap.servers", kafka_servers) \
            .option("subscribe", topic) \
            .load()

        # Check Dataframe when receving from Kafka
        df.printSchema()

        # Dataframe now contains only value column with strng-JSON format
        df = df.selectExpr("CAST(value AS STRING)")
        df.printSchema()

        # Dataframe now have two columns: Date and Tweets (both have string type)
        df = df.select( from_json(col('value'), schema).alias("data")).select("data.*")

        # Push the Dataframe to a Sentiment model and add a column results
        sentiment_udf = udf(lambda text: GetSentimentValue(text), FloatType())
        df = df.withColumn("Sentiment", sentiment_udf(df["Tweet"]))

        # Print to check
        # Choose only one of these. It can not print more than one source at the same time
        #df.writeStream.format("json").option("checkpointLocation", "checkpoint").option("path", "results").start()
        #df.writeStream.format("console").outputMode("append").start().awaitTermination()
        df.writeStream.foreachBatch(ForEachBatchFunction).start().awaitTermination()

    # In any case, if this script is stopped, the current Spark session must be destroyed too.
    finally:
        spark.stop()

def Visual():
    # Write statements to export visualization (images, etc). It will be percentage of pos/neg tweets to this time
    with open("results_visualize.json", "w") as outfile:
        json.dump({
            "num_pos_tweet": num_pos_tweet.value,
            "num_neg_tweet": num_neg_tweet.value,
            "num_neu_tweet": num_neu_tweet.value
        }, outfile)

if __name__ == "__main__":
    # Create a SparkSession
    master = "local"
    appName = "Sentiment Analyze"
    scala_version = '2.12'
    kafka_version = '3.1.1'
    packages = [f'org.apache.spark:spark-sql-kafka-0-10_{scala_version}:{kafka_version}'
    ]

    spark = SparkSession.builder \
        .master(master) \
        .appName(appName) \
        .config("spark.jars.packages", ",".join(packages)) \
        .getOrCreate()
    conf = spark.sparkContext.getConf()
    print("Packages", conf.get("spark.jars.packages"))

    # Global variable
    num_pos_tweet = spark.sparkContext.accumulator(0)
    num_neg_tweet = spark.sparkContext.accumulator(0)
    num_neu_tweet = spark.sparkContext.accumulator(0)
    Visual()

    TweetsConsumerAndSentimentAnalyze()