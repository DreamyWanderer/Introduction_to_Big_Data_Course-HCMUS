from time import sleep
from json import dumps
from bson import json_util
from kafka import KafkaProducer
from pymongo import MongoClient
import random

def TweetsProducer():

  NUM_TWEETS = random.randint(10, 20)
  TIME_WAIT = random.randint(1, 10)

  # Get the cursor in MongoDB to retrieve tweets from top to bottom
  db_cursor = MongoClient()["Allin"]["ChatGPT_Tweets"].find()

  producer = KafkaProducer(bootstrap_servers = ['localhost:9092'], 
                           value_serializer = lambda x: json_util.dumps(x).encode('utf-8'))
  
  read_tweets = 0
  for record in db_cursor:
    read_tweets += 1
    producer.send("Tweets", record)
    print( json_util.dumps(record) )
    
    if read_tweets < NUM_TWEETS:
      continue

    # Push all waiting events to sending pipeline before waiting 
    producer.flush()
    print(f"Sent {NUM_TWEETS}. Wait {TIME_WAIT} seconds before send more events")
    sleep(TIME_WAIT)
    TIME_WAIT = random.randint(1, 10)
    NUM_TWEETS = random.randint(10, 20)
    read_tweets = 0 # Damn! Forgot this and it just sends events so slowly!!!

TweetsProducer()