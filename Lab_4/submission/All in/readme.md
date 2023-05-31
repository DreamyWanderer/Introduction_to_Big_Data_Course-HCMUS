# Project Big Data Fundamentals - All In group - Lab 4

## Collaborators (All in)

- `20120011` **Nguyễn Hoàng Huy**
- `20120030` **Nguyễn Thiên An**
- `20120090` **Nguyễn Thế Hoàng**
- `20120165` **Hồng Nhất Phương**

## Instructors

- `HCMUS` **Nguyễn Ngọc Thảo** ([@nnthao](nnthao@fit.hcmus.edu.vn))
- `HCMUS` **Đoàn Đình Toàn** ([@ddtoan](ddtoan18@clc.fitus.edu.vn))

## How to run

I suggest run in Linux OS. I really do not know what will happen if you run on Window OS.

1. Creating environment for this Lab from *environment.yml* file. [How to do this?](https://conda.io/projects/conda/en/latest/user-guide/tasks/manage-environments.html#creating-an-environment-from-an-environment-yml-file)
2. Activate created environment (This is important since there are lots of dependencies to run this Lab).
3. Run the *setup.sh* by *bash*. This will create MongoDB server and Kafka servers.
4. Run the *load_data.sh* in a **new** console/shell. This will load the Tweets data and push to MongoDB server. You can close console/shell when having completed this step.
5. Run the *producer.sh* by *python* in **new** console/shell. This will pull Tweets from databases and push to Kafka **as long as** this process is still running.
6. Run the *consumer_and_model.py* by *python* in **new** console/shell. This will pull events from Kafka, rebuild the original data in DataFrame format and push to a Sentiment analyze model, **as long as** this process is still running.
7. When you have finished testing or when you want to start all over again, stop (Ctrl + C) all running processes.

## Warning

- If you stop *setup.sh* process, the MongoDB and Kafka servers will stop and all data pushed to MongoDB databases, as well as logs/events in Kafka will also be deleted. In that case, you need to start again from step 3.
- Step 5 and 6 is independent to step 3. You do not need to execute step 3 again if you only stop *producer.py* or *consumer_and_model.py*. You do not need to run *load_data.sh* again if *setup.sh* is still running normally.

## What have not been implemented?

- A new Python script to read aggregrated result and create series of plotting images (which will be converted to GIF).

## Visualization

### Method 1

## Extra

- [Group working folder](https://drive.google.com/drive/folders/1qhtrla9PtIhLcE-j6GWKFn-5prRnedAG?usp=share_link) (Google Drive folder)
- Current working Markdown report link
  - [Invitation link](https://hackmd.io/join/Hy6-bXQ1n)
  - [Group Markdown report](https://hackmd.io/team/Allin)

---
<div style="page-break-after: always"></div>
