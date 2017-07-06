import requests
import os
import glob
from sys import argv


def send_post(content):
	response = requests.post('http://localhost:9982/BEBoP/validatemodel/put',data=content,headers={"Content-Type": "text/plain"})
	print response.text
	assert response.status_code == 200
	return response.text
	
def send_get(id):
	path = 'http://localhost:9982/BEBoP/validatemodel/'+id+"/"
	print path
	response = requests.get(path)
	assert response.status_code == 200
	return response.text


path = argv[1]

for filename in os.listdir(path):
	file = open(path+"\\"+filename,"rb")
	print file
	result = send_post(file)
	r = send_get(result).encode("utf-8")
	out_file = open(path+"\\"+filename+".xml","w")
	out_file.write(r)
	out_file.close()
	#print r


