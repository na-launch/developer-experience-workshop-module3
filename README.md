# 🚀 Module 3: Event-Driven Microservices with Camel & Quarkus

Technology Stack: 

- Quarkus
- Camel (Quarkus-Camel)
- Kafka and Minio

---

## 🎯 **Scenario**

ACME is required to send data from its payment system, which is unfortunately a monolithic system that only accepts XML. ACME takes payment pre-requests and drops them into a Kafka Queue named 'package-receiver' (as pre-rendered JSON). The topics are populated by dropping files into a folder, or by running a REST API End Point.

An internal process converts the JSON to XML and sends it to another queue 'package-deliverer' where the monolothic system picks up the message and runs an internal task (not in scope) to processes the payment. 

The monolithic system, is being upgraded to be modernized, so a new requirement has arrived to move the payment transactions back to the other systems. The architects have decided that the best way to do this is to reverse the data and convert it from XML back to JSON.

---

## 🧩 **Challenge**

We will be using Camel Extensions to vastly simplyify the process.

- camel-quarkus-xj
- camel-minio
- camel-quarkus-kafka
- camel-quarkus-seda

We have proofed out a workflow as follows:

1. Take a sample data payload
2. Drop it into a file, or, use the REST API
3. Parse the code through Camel and let it work its magic to convert
4. Drop the code parsed/rendered into another queue

We have designed the system to do JSON -> XML. We would like you to finish the process, by reversing it and converting the data from XML -> JSON.

1. Log in to MINIO and create a new bucket named 'camel-drop'

2. Create 2 new topics in Kafka:

  - `<user>-package-receiver`
  - `<user>-package-deliverer`

  Generate an XML formatted file (use the sample.xml in your resources folder) as a blueprint of the XML

3. Generate your topics @ https://kafka-ui-workshop-kafka.apps.cluster-domain

  In your Java folder, edit the routes.java file to write the 'reversing' logic. HINT: Flip the same functions that are currently in there

  - a. Create a DSL that will takea HTTP request that ingests <XML>. This DSL can be accessed via CURL or an API tool like POSTMAN
  - b. Create a DSL that picks up a sample file in <user>-package-receiver, renders XML -> JSON
  - c. Create a DSL that receives a file from your drop folder, this will require you to create a new bucket in MINIO

4. For all of the above 3 (file, rest, kafka) send the final result to your <user>-package-deliverer topic.

  DSL Helper for REST: https://camel.apache.org/manual/rest-dsl.html

5. If you are testing the /endpoint, you can use this string

  ```bash
  curl -X POST   http://workshop-module3-route-project-devspaces.apps.cluster-domain/process/json2xml   -H "Content-Type: application/json"   -H "Accept: application/xml"   -d '{"transactionId":"1","customerId":"12221","amount":100,"currency":"USD","status":"Pending","message":"Transaction from company x"}'
  <?xml version="1.0" encoding="UTF-8"?>
  <transaction>
      <transactionId>1</transactionId>
      <customerId>12221</customerId>
      <amount>100</amount>
      <currency>USD</currency>
      <status>Pending</status>
      <message>Transaction from company x</message>
      </transaction>
  ```

6. Validate your output 🌀 http://kafka-consumer-app-workshop-kafka.apps.cluster-domain

---

## ✅ Key Takeaways

- You have created a Camel Route
- Parsed one file format to another with 1 line of code
- Harnessed the power of Camel Transformations / Extensions
- Quickly leveraged a file bucket and Kafka
