import os

for i in range(20):
	print "New test" + str(i)	
	for j in range(20):
	    command = "java EBS_Distribution 6 6 " + str(j)
	    os.system(command)
