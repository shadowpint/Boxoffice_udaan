
ls=['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
ls2=[]
for i in ls:
    for j in range(1,11):
        ls2.append(str(str(i)+str(j)))
print(','.join(str(x) for x in ls2))