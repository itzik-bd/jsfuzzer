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
__JsFuzzer__ distribution zip file contains the following files:
* ___engines/___ - directory contains each engine's executable files
* ___jsfuzzer.jar___ - the java executable compiled jar
* ___JsFuzzerBulk.py___ - python script to run jsfuzzer in bulk mode

To install __JsFuzzer__:

1. Unzip these files into a directory.
2. ***Linux only***: Add execution premissions for each engine's executable file:
    * engines/Nashorn/jjs
    * engines/NodeJs/node

Running JsFuzzer
----
__JsFuzzer__ is a command-line tool: open a terminal/CMD and ```cd``` to the directory of which the files were unzipped into.
Running __JsFuzzer__ with no arguments ```java -jar jsfuzzer.zip``` prints usage information:
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
__For example__: in order to generate new program (with default configuration) and run it over all engines and compare it, execute:
```
java -jar jsfuzzer.zip --out test.js --run engines
```
(Note that ```engines```
is the directory that holds each engine executable files).

__For example__: in order to load existing JavaScript program (```p.js```) and run it over all engines with timeout of 2 sec and compare it, execute:
```
java -jar jsfuzzer.zip --load p.js --run engines --timeout 2000
```