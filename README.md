# JsFuzzer

Pre Requirements
----
java 1.8

Platforms
----
**JsFuzzer** can compare JavaScript execution over different engines automatically.
In order to run JavaScript programs over an engine, an engine's executable has to be provided.
**JsFuzzer** has out-of-the-box support for the following engines and platforms:

|Platform|SpiderMonkey|NodeJs|Nashorn|Rhino|Dynjs|
|---|:---:|:---:|:---:|:---:|:---:|
|Windows 64-bit|V|V|V|V|V|
|Linux 64-bit||V|V|V|V|

In order to be able to run **JsFuzzer** on other platforms, an engine executable has to be provided as well as a new java class representing this engine.

Installation
----
**JsFuzzer** distribution zip file contains the following files:

* ***engines/*** - directory contains each engine's executable files
* ***jsfuzzer.jar*** - the java executable compiled jar
* ***jsfuzzerBulk.py*** - python script to run jsfuzzer in bulk mode

To install __JsFuzzer__:

1. Unzip these files into a directory.
2. ***Linux only***: Add execution premissions for each engine's executable file:
	* jsfuzzerBulk.py
    * engines/NodeJs/node

Running JsFuzzer
----
__JsFuzzer__ is a command-line tool: open a terminal/CMD and ```cd``` to the directory of which the files were unzipped into.
Running __JsFuzzer__ with no arguments ```java -jar jsfuzzer.jar``` prints usage information:

```
JsFuzzer version 1.0

usage: JsFuzzer [OPTIONS]
--help            - show this help

To generate new program:
--out <FILE>      - save output to file
--config <FILE>   - load costum configuration file
--seed <SEED>     - set the seed of the random generator
--execFlow <TYPE> - set the javascript execution print level (values: 'normal' or 'extensive')

To use an existing javascript file:
--load <FILE>     - load javascript file

To compare over supported engines:
--run <FOLDER>    - runs generated program over engines (path to engines folder)
--timeout <MS>    - limit each javascript engine total runtime (milliseconds, default=8000)
```

**For example**: in order to generate new program (with default configuration) and run it over all engines and compare it, execute:

```
java -jar jsfuzzer.jar --out test.js --run engines
```

(Note that ```engines```
is the directory that holds each engine executable files).

**For example**: in order to load existing JavaScript program (```p.js```) and run it over all engines with timeout of 2 sec and compare it, execute:

```
java -jar jsfuzzer.jar --load p.js --run engines --timeout 2000
```

Running JsFuzzer in bulk mode
----
To automate the process of finding differences, the python script ```jsfuzzerBulk.py``` runs jsfuzzer in generate and run mode infinitly, while capturing only those executions that resulted in differences.

script usage:
```
jsfuzzerBulk.py <jsfuzzerJarFile> <enginesDirectory> <outDir> [runCounter]
```
where arguments are:

* jsfuzzerJarFile - path to the jsfuzzer.jar file
* enginesDirectory - path to engines directory
* outDir - directory for results to be written in
* runCounter (optinal) - index to start from (useful when previous bulk was ran before)

**For example**, the following command will create a directory ```out``` with a sub-folder ```differents``` when such different is found:
```
jsfuzzerBulk.py jsfuzzer.jar engines out
```
