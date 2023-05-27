from pyspark.sql import SparkSession, Column
from pyspark.sql.functions import from_json, to_json, col
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

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
            .option("startingOffsets", "earliest") \
            .load()

        # Check Dataframe when receving from Kafka
        df.printSchema()

        # Dataframe now contains only value column with strng-JSON format
        df = df.selectExpr("CAST(value AS STRING)")
        df.printSchema()

        # Dataframe now have two columns: Date and Tweets (both have string type)
        df = df.select( from_json(col('value'), schema).alias("data")).select("data.*")

        # Print to check
        df.writeStream.format("console").outputMode("append").start().awaitTermination()

        # Push the Dataframe to a Sentiment model and add a column results
            # Wait for implementation

    # In any case, if this script is stopped, the current Spark session must be destroyed too.
    finally:
        spark.stop()

TweetsConsumerAndSentimentAnalyze()