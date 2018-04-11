def E():
    global ptr
    T()
    A()
def T():
    global ptr
    F()
    B()
def A():
    global ptr
    if st[ptr]=='+':
        ptr+=1
        T()
        A()
    else:
        return
def F():
    global ptr
    if st[ptr]=='(':
        ptr+=1
        E()
        if st[ptr]==')':
            ptr+=1
        else:
            ptr=0
    elif st[ptr]=='a':
        ptr+=1
def B():
    global ptr
    if st[ptr]=='*':
        ptr+=1
        F()
        B()
ptr=0
print('Grammar is:\nE-> TA\nA-> +TA/eps\nT-> FB\nB-> *FB/eps\nF-> (E)/a\n')
st=input()+'$'
print('Given string is:',st)
E()
if st[ptr]=='$':
    print('Valid')
else:
    print('Invalid')

