import numpy as np
def removeDirectRecursion(l):
    global grammar
    global index
    nonrec=[]
    rec=[]
    change=[]
    added=''
    start=l.split('->')[0]
    prods=l.split('->')[1].split('|')
    for p in prods:
        if start==p[0]:
            rec.append(p)
        else:
            nonrec.append(p)
    print(start+'->',end='')
    for r in rec:
        for n in nonrec:
            print((n+add[index]+'|').strip(' '),end='')
        new=''
        new=new+add[index]+'->'
        new=new+r[1:]+add[index]
        index+=1
        change.append(new+'|eps')
    print('\n')
    for c in change:
        print(c)
def removeIndirectRecursion(i,j):
    global grammar
    global index1
    line=grammar.split(',')
    print('There is Indirect recursion in:',line[i],'and',line[j],'\nHence we replace in ',line[i],' the productions from ',line[j])
    check=[]
    start=[]
    prod=[]
    newa=''
    check.extend((line[i],line[j]))
    for lines in check:
        start.append(lines.split('->')[0])
        prod.append(lines.split('->')[1])
    proda=prod[0].split('|')
    prodb=prod[1].split('|')
    newa=newa+start[0]+'->'
    for a in proda:
        if a[0]==start[1]:
            for b in prodb:
                newa=newa+a.replace(a[0],b)+'|'
        else:
            newa=newa+a+'|'
    newa=newa[0:-1]
    print(newa)
    for i in range(len(line)):
        if line[i][0]==newa[0]:
            line[i]=newa
    grammar=','.join(line)
def checkIndirectRecursion():
    global grammar
    print('\n\nChecking Indirect Recursion')
    print('Given Grammar is')
    print(grammar.split(','))
    start=[]
    prods=[]
    lines=grammar.split(',')
    for l in lines:
        start.append(l.split('->')[0])
        prods.append(l.split('->')[1])
    for i in range(len(start)):
        lines=grammar.split(',')
        start=[]
        prods=[]
        for l in lines:
            start.append(l.split('->')[0])
            prods.append(l.split('->')[1])
        prod=prods[i].split('|')
        for p in prod:
            for j in range(len(start)):
                if j==i:
                    continue
                if p[0]==start[j]:
                    indprod=prods[j].split('|')
                    for ind in indprod:
                        if start[i]==ind[0]:
                            removeIndirectRecursion(i,j)
    print('\n\nFinal Grammar is:\n')
    print(grammar.split(','))
def checkDirectRecursion():
    global grammar
    print('\n\nChecking Direct Recursion')
    lines=grammar.split(',')
    print('Given Grammar is')
    print(grammar.split(','))
    for l in lines:
        prod=l.split('->')
        if prod[0]==prod[1][0]:
            print('There is direct left recursion in ',l)
            removeDirectRecursion(l)
        else:
            print(l)
grammar=input('Enter the grammar seperated by commas:')
add=['X','Y','Z']
change=[]
global grammar
index=0
index1=0
checkIndirectRecursion()
global grammar
checkDirectRecursion()