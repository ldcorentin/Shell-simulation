import os

for i in range(20):
	print "New test"	
	for j in range(20):
	    command = "java Cluster 3 2 " + str(j)
	    os.system(command)
