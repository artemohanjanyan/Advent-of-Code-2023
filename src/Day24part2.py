from sympy import *

# 19, 13, 30 @ -2,  1, -2
# 18, 19, 22 @ -1, -1, -2
# 12, 31, 28 @ -1, -2, -1

x1 = 19
y1 = 13
z1 = 30
tx1 = -2
ty1 = 1
tz1 = -2
x2 = 18
y2 = 19
z2 = 22
tx2 = -1
ty2 = -1
tz2 = -2
x3 = 12
y3 = 31
z3 = 28
tx3 = -1
ty3 = -2
tz3 = -1

x0, y0, z0, tx0, ty0, tz0, t1, t2, t3 = symbols("x0 y0 z0 tx0 ty0 tz0 t1 t2 t3")

f1=x0+t1*tx0-x1-t1*tx1
f2=y0+t1*ty0-y1-t1*ty1
f3=z0+t1*tz0-z1-t1*tz1
f4=x0+t2*tx0-x2-t2*tx2
f5=y0+t2*ty0-y2-t2*ty2
f6=z0+t2*tz0-z2-t2*tz2
f7=x0+t3*tx0-x3-t3*tx3
f8=y0+t3*ty0-y3-t3*ty3
f9=z0+t3*tz0-z3-t3*tz3

print(solve(
    [f1, f2, f3, f4, f5, f6, f7, f8, f9],
    [x0, y0, z0, tx0, ty0, tz0, t1, t2, t3],
    dict=True
))
