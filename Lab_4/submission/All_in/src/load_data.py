#load data
from datasets import load_dataset
data = load_dataset('deberain/ChatGPT-Tweets', split="train")
data = data.remove_columns(["Url", "User", "UserCreated", "UserVerified", "UserFollowers", "UserFriends", "Retweets", "Likes", "Location", "UserDescription"])

#convert data to json file
data.to_json("data.json")

#push data to MongoDB
import json
from pymongo import MongoClient

client = MongoClient()
db = client['Allin']
collection = db["ChatGPT_Tweets"]

with open('data.json') as f:
    chunks = [json.loads(line) for line in f]

collection.insert_many(chunks)