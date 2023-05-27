#! /usr/bin/env bash

# This file sets up MongoDB, Kafka and start all other processes (including consumer and producer). When writing a new Python script or bash script, it should be stared by this script.

function cleanup() 
{
    # These kill processing will be run in backround to prevent the case one will stuck endlessly and prevent other do their tasks
    kafka_2.12-3.4.0/bin/kafka-server-stop.sh kafka_2.12-3.4.0/config/kraft/serverA.properties &
    kafka_2.12-3.4.0/bin/kafka-server-stop.sh kafka_2.12-3.4.0/config/kraft/serverB.properties &
    mongod --shutdown &
}

trap cleanup EXIT

# Start MongoDB server
rm -rR ./data
mkdir -p ./data/mongodb
mongod --dbpath ./data/mongodb &

# Setup Kafka
#rm -rR kafka_2.12-3.4.0.tgz
rm -rR kafka_2.12-3.4.0
rm -rR tmp
export PATH=$PATH:./kafka_2.12-3.4.0/bin
echo $PATH
#wget https://downloads.apache.org/kafka/3.4.0/kafka_2.12-3.4.0.tgz
tar xzf kafka_2.12-3.4.0.tgz

# Create new server Kafka properties files
wget -q 'https://drive.google.com/uc?export=download&id=1nc1zOxog7Ux-GMpYJOM-LhycdIv7kdK2' -O kafka_2.12-3.4.0/config/kraft/serverA.properties 
wget -q 'https://drive.google.com/uc?export=download&id=1JoqibVHhQGPy5gcd3XY5-oFKkU_kfJrT' -O kafka_2.12-3.4.0/config/kraft/serverB.properties

# Set up Kafka cluster
export KAFKA_HEAP_OPTS="-Xmx2G -Xms1G"
kafka-storage.sh random-uuid > random_uuid_kafka_group
kafka-storage.sh format -t "$(cat random_uuid_kafka_group)" -c kafka_2.12-3.4.0/config/kraft/serverA.properties
kafka-storage.sh format -t "$(cat random_uuid_kafka_group)" -c kafka_2.12-3.4.0/config/kraft/serverB.properties
sleep 5
echo "Wait 5 second to set up Kafka server"

# Run Kafka server
kafka-server-start.sh kafka_2.12-3.4.0/config/kraft/serverA.properties &
kafka-server-start.sh kafka_2.12-3.4.0/config/kraft/serverB.properties &

# Create topic
kafka-topics.sh --create --topic Tweets --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092
kafka-topics.sh --bootstrap-server localhost:9092 --list

wait