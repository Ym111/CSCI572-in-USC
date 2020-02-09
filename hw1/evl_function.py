import json
import csv

doc_query = "query.txt"
doc_my_results = "v2.json"
doc_google_results = "google_results.json"
doc_csv = "csv_result2.csv"

#get data from google result
with open(doc_my_results) as f1:
    data1 = json.loads(f1.read())
#get data from my results
with open(doc_google_results) as f2:
    data2 = json.loads(f2.read())
#open csv to write
f3 = open(doc_csv,"w")
file = csv.writer(f3) 
#compare them
count = 1
avg_cnt = 0
avg_sc = 0
avg_per = 0
file.writerow(['Queries','Number of Overlapping Results','Percent Overlap','Spearman Coefficient'])
with open(doc_query) as query:
    for lines in open(doc_query):
        line = query.readline().strip("\n").strip("?").strip(" ")
        list1 = data1[line]
        list2 = data2[line]
        #check the number
        total = 10 #min(len(list1),len(list2))
        #overlap & value
        
        cnt = 0 #num of overlap 
        val =0 
        
        for each1 in list1:
            for each2 in list2:
                # delect http/https and www.
                new_each1 = each1.lstrip('http://').lstrip('https://').lstrip('www.')
                new_each2 = each2.lstrip('http://').lstrip('https://').lstrip('www.')
                #delect the last /
                if new_each1[-1] == '/':
                    new_each1 = new_each1[:-1]
                if new_each2[-1] == '/':
                    new_each2 = new_each2[:-1]
                new_each1.lower()
                new_each2.lower()

                if  new_each1== new_each2:
                    cnt +=1
                    val += (list1.index(each1) - list2.index(each2))**2
                    #print(list1.index(each1)," : ",list2.index(each2)," ", val)

        if cnt > 1:
            sc = 1 - ((6*val)/(cnt * ((cnt**2) -1) ))
            file.writerow([str("Query "+str(count)) ,cnt,cnt/total *100,sc])
        else:
            sc = 0
            file.writerow([str("Query "+str(count)),cnt,cnt/total *100,sc])
        
        count +=1
        avg_cnt +=cnt
        avg_per += cnt/total *100
        avg_sc += sc

        # for i in range(total):
        #     if list1[i] == list2[i]:
        #         file.writerow(["T",list1[i],list2[i]])
        #     else:
        #         file.writerow(["F",list1[i],list2[i]])
    
    file.writerow(["Averages",avg_cnt/100,avg_per /100,avg_sc/100])
            



        






#output the resule 