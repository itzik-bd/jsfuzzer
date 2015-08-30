import sys
import re

if (len(sys.argv) < 2):
	print("out")
	
fileN = sys.argv[1]
out = " ".join(open(fileN).readlines())

vars = re.findall('(v[0-9]+)', out)

unique = []
for i in vars:
	if not (i in unique):
		unique.append(i)
			
print ("var " + ", ".join(unique) + ";")
