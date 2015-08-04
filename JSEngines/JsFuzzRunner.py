import sys
import os
import shutil
import re

RHINO_FAIL_MESSAGE = "Encountered code generation error while compiling script: generated bytecode for method exceeds 64K limit."

runCounter = 0
jsFuzzDir = ''
outDir = ''

def run():
	# Create temp output directory (if not exists)
	tmpDir = outDir + '\\' +str(runCounter) + '\\'
	if os.path.exists(tmpDir):
		return
	else:
		os.makedirs(tmpDir)
	
	# Set Run line
	runLine = "java -classpath \""+jsFuzzDir+"\jsfuzzer\\bin;"+jsFuzzDir+"\jsfuzzer\\lib\\commons-lang3-3.4.jar;"+jsFuzzDir+"\jsfuzzer\\lib\\json-simple-1.1.1.jar\" Main.JsFuzzer "
	params = " --run --timeout 5000 --out " + tmpDir + "code.js"

	# Run program
	os.system(runLine + params + " > " + tmpDir + "out.txt")
	
	# Are there at least two different outputs
	if (isDifferent(tmpDir)):
		# move directory
		shutil.copytree(tmpDir, outDir + '\\differents\\' +str(runCounter))
		print (str(runCounter) + " Got different answers")
	else:
		print (str(runCounter) + " Is the same in all engines")
	# Delete temp files
	shutil.copyfile(tmpDir + "code.js", outDir + "\\All code files\\" + str(runCounter) + ".js")
	shutil.rmtree(tmpDir)
	
def isDifferent(tmpDir):
	out = " ".join(open(tmpDir + 'out.txt').readlines())
	count = out.count("equivalence class")
	
	timeout = timeOutOccured(tmpDir)
	rhinofailed = out.count(RHINO_FAIL_MESSAGE)
	
	return (count > (1 + rhinofailed + timeout))

def timeOutOccured(tmpDir):
	for line in open(tmpDir + 'out.txt').readlines():
		if ("Timeout: " in line):
			if not (re.search("Timeout:\s*$", line)):
				return 1
	
	return 0
	
if (__name__ == "__main__"):
	if (len(sys.argv) < 3):
		print ("usage: JsFuzzRunner.py jsFuzzerDir outDir [runCounter]")
		print ("example: JsFuzzRunner.py C:\\Users\\MyUser\\workspace\\JsFuzzer C:\\temp")
	else:
		jsFuzzDir = sys.argv[1]
		outDir = sys.argv[2]
	
		# Start counter at more than zero
		if (len(sys.argv) > 3):
			runCounter = int(sys.argv[3])
		
		# Create needed directories if needed
		if not os.path.exists(outDir):
			os.makedirs(outDir)
			
		if not os.path.exists(outDir + "\\All code files"):
			os.makedirs(outDir + "\\All code files")
		
		# Run the tester forever
		while(1):
			run()
			runCounter += 1