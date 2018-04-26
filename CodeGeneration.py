import numpy as np
def addOpLine(line):
    part2=line.split('=')[1]
    global regs,vals
    used=[]
    spart2=list(part2)
    for s in spart2:
        if(s.isalpha()):
            for i in range(len(vals)):
                if vals[i]==s:
                    used.append(regs[i])
    for s in spart2:    
        if(s=='+'):
            print('ADD ',', '.join(used))
        if(s=='-'):
            print('SUB ',', '.join(used))
        if(s=='*'):
            print('MUL ',', '.join(used))
        if(s=='/'):
            print('DIV ',', '.join(used))
def genCode(code):
    global regs,vals,index
    vals=[]
    index=0
    regs=['R1','R2','R3','R4','R5','R6','R7','R8']
    scode=code.split(',')
    for line in scode:
        used=[]
        part1=line.split('=')[0]
        part2=line.split('=')[1]
        spart2=list(part2)
        for s in spart2:
            if(s.isalpha()):
                if s not in vals:
                    vals.append(s)
                    print('MOV ',regs[index],',',s)
                    used.append(regs[index])
                    index+=1
                else:
                    for i in range(len(vals)):
                        if vals[i]==s:
                            used.append(regs[i])  
                    continue
            else:
                op=s
        addOpLine(line)
        print('MOV ', part1, ',', used[0])
        for i in range(len(vals)):
            if part2[0] in vals[i]:
                vals[i]=part1
code= input('Enter the code:')
genCode(code)

