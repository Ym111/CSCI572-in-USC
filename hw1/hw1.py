from bs4 import BeautifulSoup
from time import sleep
import requests
import time 
from random import randint
from html.parser import HTMLParser
import json


tmp_dict = dict()
USER_AGENT = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36'}

class SearchEngine:

    @staticmethod
    def handle_web(results):
        urls = set()
        count =0
        new_results=[]
        for each in results:
            if count ==10:
                break
            if each not in urls:
                urls.add(each)
                new_results.append(each)
                count +=1
            else:
                continue
        return new_results
        
    @staticmethod
    def search(query, sleep=True):
        if sleep: # Prevents loading too many pages too soon
            time.sleep(randint(10, 100))
        temp_url = '+'.join(query.split()) #for adding + between words for the query
        url = " https://www.teoma.com/web?q=" + temp_url
        url2 = url + "&page=2"
        #print(url)
        try:
            soup = BeautifulSoup(requests.get(url, headers=USER_AGENT).text,"html.parser")
            new_results = SearchEngine.scrape_search_result(soup)
        except:
            print("Page 1 error")
        time.sleep(randint(1, 5))
        try:
            soup2 = BeautifulSoup(requests.get(url2, headers=USER_AGENT).text,"html.parser")
            new_results.extend(SearchEngine.scrape_search_result(soup2))
        except:
            print("Page 2 error")
        tmp_dict[line] = SearchEngine.handle_web(new_results)
        

    @staticmethod
    def scrape_search_result(soup):
        raw_results = soup.find_all('li',attrs={'class':'algo-result'})
        results = []
        #implement a check to get only 10 results and also check that URLs must not be duplicated
        for result in raw_results:
            link = result.find('a').get('href') 
            results.append(link)
        return results


#############Driver code############
document = open("query.txt")
count =1
time_start = time.time()
## new json document 


for line in open("query.txt"):
    print(count,end='')
    line = document.readline().strip("\n").strip("?").strip(" ")
    resules= SearchEngine.search(line)
    count +=1
    try:
        with open("output2.json", 'w') as f:
            json.dump(tmp_dict,f,sort_keys=False, indent=4, separators=(',', ':'))
        print(" Done")
    except:
        print("Json output error")
    


time_end = time.time()
print("Total time:",format((time_end-time_start)/60, '0.2f'),"mins")


#TODO 
# try struction

# get page num -> get 1 & 2
# else 'warming'

#

####################################
