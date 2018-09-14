"""
    La heuristica01 se calcula:
    representando en decimal la primera y la ultima
    columna y sumando las representaciones
"""
def heuristica01(t,n):
    h = 0.0
    p1 = len(t[0])-1
    for d in t[0]:
        h = h + d*10**p1
        p1 = p1 - 1
    p1 = len(t[2])-1
    for d in t[2]:
        h = h + d*10**p1
        p1 = p1 - 1
    return (2.0**n-1.0)*(1.0-h/54321.0)

"""
    La heuristica02 se calcula:
    Se suman los elementos de la ultima columna
"""
def heuristica02(t,n):
    h = 0.0
    for d in t[2]:
        h = h + d
    return (2.0**n-1.0)*(1-h/15.0)


"""
    La heuristica03 se calcula:
    Se suman los elementos de la ultima columna
"""
def heuristica03(t,n):
    h1 = 0.0
    for d in t[1]:
        h1 = h1 + d
    h2 = 0.0
    for d in t[2]:
        h2 = h2 + d
    h = h1+2.0*h2
    return (2.0**n-1.0)*(1.0-h/30.0)


import copy
inicial=[[5,4,3,2,1],[],[]]
print ("Estado Inicial")
print (inicial)
solucion = []
solucion.append(inicial)
espacio = []
distancia = []
distancia_actual = 0.0
padre_solucion = []
padre_solucion.append(inicial)
padre_espacio = []
final=[[],[],[5,4,3,2,1]]
ind = [[0,1],[0,2],[1,0],[1,2],[2,0],[2,1]]
actual = copy.deepcopy(inicial)
s = 0
iteraciones = 0
while (s==0): 
    print ("Estado Actual")
    print (actual)
    print ("Generando hijos")
    iteraciones = iteraciones + 1
    for i1 in ind:
        nt = copy.deepcopy(actual)
        if nt[i1[0]]:
            t = nt[i1[0]].pop()
            if not nt[i1[1]]:
                nt[i1[1]].append(t)
            else:
                t1 = nt[i1[1]].pop()
                if t1 > t:
                    nt[i1[1]].append(t1)
                    nt[i1[1]].append(t)
                else:
                    nt = []
        else:
            nt = []
        for n in solucion:
            if nt == n:
                nt = []
        for n in espacio:
            if nt == n:
                nt = []
        if nt:
            print ("Hijo ", i1)
            print (nt)
            espacio.append(nt)
            distancia.append(distancia_actual+1.0)
            padre_espacio.append(actual)
    
    print ("Calculando las heuristicas")
    mejor = 0
    hdtotal = 1000000
    index = 0
    if espacio:
        for t in espacio:
            d = distancia[index]
            h = heuristica01(t,3.0)
            hd = h + d
            print( h,d,hd,t)
            if hd < hdtotal:
                hdtotal = hd
                mejor = index
            index = index + 1
        actual = espacio.pop(mejor)
        distancia_actual = distancia.pop(mejor)
        padre_solucion.append(padre_espacio.pop(mejor))
        solucion.append(actual)
    else:
        s = 5
    if actual == final:
        s = 5
ruta = []
t=solucion.pop()
ruta.append(t)
tp = padre_solucion.pop()
while (tp!=inicial):
    ruta.insert(0,tp)
    i1 = solucion.index(tp)
    t = solucion.pop(i1)
    tp = padre_solucion.pop(i1)
ruta.insert(0,inicial)
print ("Solucion")
n1 = 0
for n in ruta:
    print (n1, n)
    n1 = n1 +1
print ("Interaciones = ")
print (iteraciones)





















