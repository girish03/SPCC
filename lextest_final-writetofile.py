import numpy as np
import re 
delim=[',',';','(',')','{','}','[',']','#','<','>',' ']
oper=['+','-','*','/','%','=','!']
defkey=["int","float","char","double","bool"]
othkey=["void","extern","unsigned","goto","static","class","struct","for","if","else","return","register","long","while","do","main"]
predirect=["include","define","getch"]
header=["stdio.h","conio.h","malloc.h","process.h","string.h","ctype.h"]
def ishd(w):
    for h in header:
        if h in w:
            wf.write(h+'->Header\n')
def iskey(w):
    for h in predirect:
        if h in w:
            wf.write(h+'->Keyword\n')
    for h in othkey:
        if h in w:
            wf.write(h+'->Keyword\n')
def stdt(w):
    for d in defkey:
        if d in w:
            wf.write(d+'->DataType Declaration\n')
            for l in line.split()[1:]:
                vr='[A-Za-z_][A-Za-z0-9_]*'
                match=re.findall(vr,l)
                for m in match:
                    varbls.append(m)
def isdt(w):
    for v in varbls:
        if v==w:
            wf.write(v+'->Variable\n')
def iscon(w):
    cr='\d+(?:\.\d+)?|"[A-z0-9 ]+"'
    match=re.findall(cr,w)
    for m in match:
        wf.write(m+'->Constant Value\n')
varbls=[]
cf=open('code.txt','r')
wf=open('output.txt','a')
flag=0
for line in cf:
    wf.write(line+'\n')
    lxx=line.split()
    for lx in lxx:
        rest=lx[0]
        for l in lx:
            for d in delim:
                if d in l:
                    iscon(rest)
                    wf.write(d+'->Delimiter\n')
                    rest=''
                    flag=1
                    continue
            for d in oper:
                if d in l:
                    iscon(rest)
                    wf.write(d+'->Operator\n')
                    rest=''
                    flag=1
                    continue
            if flag==1:
                flag=0
                continue
            else:
                isdt(rest)
                rest=rest+l
            ishd(rest)
            iskey(rest)
            stdt(rest)
            isdt(rest)
    wf.write('------  ------  ------  ------\n\n')
wf.close()

