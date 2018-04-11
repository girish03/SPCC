import numpy as np
grammar='G->a|(G)|G+G|G+G|G-G|G/G|G*G|G^G|G'
grammarsp=grammar.split('->')[1].split('|')
symb=['+','-','*','/','^','a','(',')','$']
prec=[
   ['>','>','<','<','<','<','<','>','>'],
   ['>','>','<','<','<','<','<','>','>'],
   ['>','>','>','>','<','<','<','>','>'],
   ['>','>','>','>','<','<','<','>','>'],
   ['>','>','>','>','<','<','<','>','>'],
   ['>','>','>','>','>','E','E','>','>'],
   ['<','<','<','<','<','<','<','=','E'],
   ['>','>','>','>','>','E','E','>','>'],
   ['<','<','<','<','<','<','<','E','A']
]
print('   ','\', \''.join(symb))
for i in range(len(prec)):
    print(symb[i],prec[i][:])
print('Grammar is:')
print(grammar.split('->')[0],'->',grammarsp)
def checkOpPrec():
    global stack,inputstr,flag
    global ptr
    temp=0
    nex=0
    for i in range(len(symb)):
        if symb[i]==inputstr[ptr]:
            nex=i
        if symb[i]==stack[-1]:
            temp=i
    if prec[temp][nex]=='>':
        handle=''
        stack.append('>')
        while stack[-1]!='<':
            handle=stack.pop()+handle
        handle=stack.pop()+handle
        print('Handle is:',handle)
        handle=handle[1:-1]
        for g in grammarsp:
            if handle==g:
                stack.append('G')
        print('New stack:',end='')
        print(''.join(stack))
        for g in grammarsp:
            if ''.join(stack[-len(g):])==g:
                for j in range(len(g)):
                    stack.pop()
                stack.append('G')
        print('Changed stack:',''.join(stack))
    if prec[temp][nex]=='<':
        stack.append('<')
    if prec[temp][nex]=='=':
        stack.append(inputstr[ptr])
    if prec[temp][nex]=='E':
        print('Error found!')
        print('String not accepted for Operator Precedence')
        flag=1
    if prec[temp][nex]=='A':
        print('String accepted for Operator Precedence')
def solve():
    global inputstr
    global stack,flag
    global ptr
    ptr=0
    flag=0
    stack=[]
    print(inputstr)
    for i in range(len(inputstr)-1):
        if flag==0:
            stack.append(inputstr[i])
            ptr+=1
            checkOpPrec()
        else:
            continue
    stack.append(inputstr[-1])
    print('Final Stack:',stack)
    if ''.join(stack[1:-1])=='G':
        print('String accepted for Operator Precedence')
    elif flag==0:
        print('String not accepted for Operator Precedence')
print('Enter the input string:')
inputstr='$'+input()+'$'
solve()

