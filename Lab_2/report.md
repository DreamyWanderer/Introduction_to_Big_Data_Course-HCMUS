---
title: "Lab 01: A Gentle Introduction to Hadoop"
author: ["your-team-name"]
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
# Lab 02: MapReduce

#### Problem 3: Weather Data
**Input**: data list including date, minimum temperature, maximum temperature and other information

**Output**: print "hot day" for days with the highest temperature greater than 40 degrees, and "cold days" for days with the lowest temperature less than 10 degrees.

**Idea**: extract information like date, minimum temperature, maximum temperature. Check for the day has the highest temperature > 40 degrees, the lowest temperature < 10 degrees, then save the date information.

**Code**:
***Mapper***
*map() function*
- Use the substring() command to get the date and temperature information
- Convert the temperature from string to real number, check the min and max temperature according to the problem requirements and write the result to the context variable.
- Use 2 if functions to check in case a day has minimum temperature less than 10 and maximum temperature greater than 40.

```java
Text date = new Text();
Text word = new Text();

String line = value.toString();
String d = line.substring(14, 22);
float max = Float.parseFloat(line.substring(104, 108).trim());
float min = Float.parseFloat(line.substring(112, 116).trim());
      
if (max > 40.0) {
    date.set(d);
    word.set("Hot day");
    context.write(date, word);
}
      
if (min < 10.0) {
    date.set(d);
    word.set("Cold day");
    context.write(date, word);
}
```

***Reducer***
*reduce() function*
- Record the output results, if there is a case in the same day that the lowest and highest temperatures are satisfied, then combine "cold day" and "hot day" into 1 result

```java
Text result = new Text();
String res = "";
for (Text val : values) {
    res = res + val + " ";
}
result.set(res);
context.write(key, result);
```

**Run program**
![Create folder, put input file into Lab02 directory and run program](images/changeps1.png)

![Proof of change your shell prompt's name](images/changeps1.png)




Code example:

```python
print("Hello")
```

```bash
cat ~/.bashrc
```

Screenshot example:

![Proof of change your shell prompt's name](images/changeps1.png)

\newpage

Screenshot example:

![ImgPlaceholder](images/placeholder-image-300x225.png)

Reference examples:

Some text in which I cite an author.[^fn1]

More text. Another citation.[^fn2]

What is this? Yet _another_ citation?[^fn3]


## References
<!-- References without citing, this will be display as resources -->
- Three Cloudera version of WordCount problem:
    - https://docs.cloudera.com/documentation/other/tutorial/CDH5/topics-/ht_wordcount1.html
    - https://docs.cloudera.com/documentation/other/tutorial/CDH5/topics/ht_wordcount2.html
    - https://docs.cloudera.com/documentation/other/tutorial/CDH5/topics/ht_wordcount3.html
- Book: MapReduce Design Patterns [Donald Miner, Adam Shook, 2012]
- All of StackOverflow link related.

<!-- References with citing, this will be display as footnotes -->
[^fn1]: So Chris Krycho, "Not Exactly a Millennium," chriskrycho.com, July 2015, http://v4.chriskrycho.com/2015/not-exactly-a-millennium.html
(accessed July 25, 2015)

[^fn2]: Contra Krycho, 15, who has everything _quite_ wrong.

[^fn3]: ibid