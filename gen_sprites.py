import os

STATIC = r'C:\Users\hokep\git\artifacts\src\main\resources\static'
P = 8   # pixels per cell
N = 16  # grid size

# ── Primitives ────────────────────────────────────────────────────────────────
def grid(bg='g'):
    return [[bg]*N for _ in range(N)]

def px(G, x, y, c):
    if 0 <= x < N and 0 <= y < N: G[y][x] = c

def rect(G, x, y, w, h, c):
    for dy in range(h):
        for dx in range(w): px(G, x+dx, y+dy, c)

def disk(G, cx, cy, r, c):
    for dy in range(-r, r+1):
        for dx in range(-r, r+1):
            if dx*dx+dy*dy <= r*r: px(G, cx+dx, cy+dy, c)

def ring(G, cx, cy, r, c):
    for dy in range(-r, r+1):
        for dx in range(-r, r+1):
            d2 = dx*dx+dy*dy
            if (r-1)**2 < d2 <= r*r: px(G, cx+dx, cy+dy, c)

def hline(G, y, x0, x1, c):
    for x in range(x0, x1+1): px(G, x, y, c)

def vline(G, x, y0, y1, c):
    for y in range(y0, y1+1): px(G, x, y, c)

def to_svg(G, C):
    W=N*P; H=N*P
    lines = [f'<svg xmlns="http://www.w3.org/2000/svg" width="{W}" height="{H}" viewBox="0 0 {W} {H}" shape-rendering="crispEdges">']
    for y, row in enumerate(G):
        for x, ch in enumerate(row):
            lines.append(f'  <rect x="{x*P}" y="{y*P}" width="{P}" height="{P}" fill="{C.get(ch,"#f00")}"/>')
    lines.append('</svg>')
    return '\n'.join(lines)

def save(name, G, C):
    with open(os.path.join(STATIC, name+'.svg'), 'w') as f:
        f.write(to_svg(G, C))
    print(f'  {name}.svg')

# ── Shared palette ────────────────────────────────────────────────────────────
BASE = {
    'g': '#508038', '.': '#6ab848', ',': '#3a6028',         # grass
    'P': '#1a5c30', 'M': '#287848', 'H': '#45b868',         # pine
    'K': '#0c2e16', 't': '#6e4818', 's': '#4a2e0a',         # pine shadow / trunk
    'b': '#287820', 'c': '#42a832',                          # bush
    'r': '#787878', 'R': '#a0a0a0', 'q': '#505050',         # rock
    'Q': '#303030', 'S': '#d8e0e8', 'T': '#b0b8c0',         # rock shadow / snow
    'w': '#1848a0', 'W': '#2060c8', 'f': '#3080e0',         # water
    'F': '#a8c8f8',                                          # water foam
    'n': '#c8a050', 'N': '#e0c070', 'm': '#a07830',         # sand
    'p': '#a09070', 'e': '#c0b090', 'j': '#806050',         # path/road
    'a': '#808080', 'A': '#a8a8a8', 'z': '#505050',         # stone
    'u': '#8b5e3c', 'v': '#5e3a1a',                         # wood
    'x': '#a03020', 'X': '#702010',                         # roof
    'o': '#f0d060', 'O': '#c0a030',                         # gold
    'L': '#1858b0', 'E': '#2870c0', 'I': '#4898d8',         # lake
    'D': '#98c8f0',                                          # lake foam
    'B': '#c8d0a8', 'C': '#e8e0c0', 'Z': '#a09880',         # bone
    'G': '#405028', 'V': '#607840', 'U': '#80a858',         # goblin green
    'i': '#706050', 'J': '#907870',                          # wolf grey-brown
    '1': '#2050a0', '2': '#3878d8', '3': '#70b0f8',         # blue slime
    '4': '#a08020', '5': '#d0c030', '6': '#f0e060',         # yellow slime
    '7': '#208040', '8': '#30a855', '9': '#50d870',         # green slime
    '!': '#901818', '@': '#c03030', '#': '#f06060',          # red slime
    '$': '#6020a0', '%': '#9040d0', '^': '#c070f8',         # king/purple slime
    '&': '#a02018', '*': '#c83028', '(': '#f0f0e0',         # mushroom red/spot
    ')': '#c07030', '_': '#e09050', '+': '#905018',         # copper ore
    '-': '#607080', '=': '#809098', '[': '#a0b0b8',         # iron ore
    ']': '#202020', '{': '#383838', '}': '#505050',         # coal
    '<': '#4a5838', '>': '#6a7850', '?': '#8a9868',         # ash tree
    '/': '#d0d0c0', '\\': '#b0b0a0',                        # birch trunk
    ':': '#a04818', ';': '#d06828', '~': '#f09040',         # maple orange
    '`': '#1a1028', "'": '#2e1a40', '"': '#4a2a60',         # cursed dark
    '{': '#202020', '}': '#383838',
}

# Override conflicting: iron ore reuse bracket chars, coal reuse brace chars
# Reassign to avoid Python syntax issues - use numeric string keys won't work
# Let's use clean single chars for everything:
PALETTE = dict(BASE)  # will extend per-sprite

# ── Drawing helpers ────────────────────────────────────────────────────────────

def pine(G, cx, base, size=7):
    """top-3/4 view pine tree"""
    # trunk
    rect(G, cx, base-2, 2, 2, 't')
    # 4 triangle layers bottom→top
    layers = [(4,3),(3,2),(2,2),(1,1)]
    y = base-3
    for hw, h in layers:
        for row in range(h):
            for dx in range(-hw+row, hw-row+1):
                px(G, cx+dx, y-row, 'P')
            px(G, cx-hw+row, y-row, 'H')
            px(G, cx+hw-row, y-row, 'H')
        y -= h+1

def tree_top(G, cx, cy, r, outer, inner, hl):
    """Top-down round tree canopy"""
    disk(G, cx, cy, r, outer)
    disk(G, cx, cy, r-2, inner)
    # highlight top-left quadrant
    for dy in range(-r+1, 0):
        for dx in range(-r+1, 0):
            if dx*dx+dy*dy <= (r-1)**2:
                px(G, cx+dx, cy+dy, hl)

def rock_formation(G, cx, cy, r, light, mid, dark, shadow):
    """Mountain rock cluster"""
    disk(G, cx, cy, r, shadow)
    disk(G, cx-1, cy-1, r-1, dark)
    disk(G, cx-1, cy-1, r-2, mid)
    disk(G, cx-2, cy-2, r-3, light)

def creature_blob(G, cx, cy, r, dark, mid, light):
    """Simple top-down creature silhouette"""
    disk(G, cx, cy, r, dark)
    disk(G, cx-1, cy-1, r-1, mid)
    px(G, cx-2, cy-2, light)

def building(G, x, y, w, h, wall, roof, door='v'):
    """Simple building footprint"""
    rect(G, x, y, w, h, wall)
    rect(G, x, y, w, 2, roof)  # top edge = roof
    px(G, x+w//2, y+h-1, door)  # door

def water_bg(G):
    rect(G, 0, 0, N, N, 'w')
    for y in range(0, N, 3):
        hline(G, y, 1, N-2, 'W')
    for y in range(1, N, 3):
        hline(G, y, 3, N-4, 'f')

def road_strip(G, vertical=True, width=4, col='p', light='e', dark='j'):
    cx = N//2
    if vertical:
        rect(G, cx-width//2, 0, width, N, col)
        vline(G, cx-width//2, 0, N-1, dark)
        vline(G, cx+width//2-1, 0, N-1, dark)
    else:
        cy = N//2
        rect(G, 0, cy-width//2, N, width, col)
        hline(G, cy-width//2, 0, N-1, dark)
        hline(G, cy+width//2-1, 0, N-1, dark)

def sand_bg(G):
    rect(G, 0, 0, N, N, 'n')
    # light patches
    for coords in [(2,2),(5,5),(10,3),(12,8),(4,12),(9,11),(7,7),(13,13)]:
        px(G, *coords, 'N')

def coast_strip(G, side='S', water_char='w', water_mid='W', land='g', foam='F'):
    """side: N S E W = which side is water"""
    rect(G, 0, 0, N, N, land)
    if side == 'S':
        rect(G, 0, 10, N, 6, water_char)
        hline(G, 10, 0, N-1, foam)
        hline(G, 11, 0, N-1, water_mid)
    elif side == 'N':
        rect(G, 0, 0, N, 6, water_char)
        hline(G, 5, 0, N-1, foam)
        hline(G, 4, 0, N-1, water_mid)
    elif side == 'E':
        rect(G, 10, 0, 6, N, water_char)
        vline(G, 10, 0, N-1, foam)
        vline(G, 11, 0, N-1, water_mid)
    elif side == 'W':
        rect(G, 0, 0, 6, N, water_char)
        vline(G, 5, 0, N-1, foam)
        vline(G, 4, 0, N-1, water_mid)

# ──────────────────────────────────────────────────────────────────────────────
# SPRITES
# ──────────────────────────────────────────────────────────────────────────────
sprites = []

# ── Forest plain tiles ────────────────────────────────────────────────────────

def mk_forest1():
    G = grid()
    # single large tree centered with 2 small bushes
    pine(G, 7, 12, 7)
    disk(G, 3, 11, 2, 'b'); px(G, 3, 10, 'c')
    disk(G, 12, 12, 2, 'b'); px(G, 12, 11, 'c')
    return G, PALETTE

def mk_forest2():
    G = grid()
    pine(G, 5, 12, 6)
    pine(G, 11, 11, 5)
    disk(G, 2, 12, 2, 'b')
    disk(G, 13, 13, 1, 'b'); px(G, 13, 12, 'c')
    return G, PALETTE

def mk_forest4():
    G = grid()
    pine(G, 4, 12, 5)
    pine(G, 8, 10, 4)
    pine(G, 12, 12, 5)
    disk(G, 2, 13, 1, 'b')
    disk(G, 14, 13, 1, 'b')
    return G, PALETTE

sprites += [
    ('forest_1', mk_forest1),
    ('forest_2', mk_forest2),
    ('forest_4', mk_forest4),
]

# ── Mountain tiles ────────────────────────────────────────────────────────────

def mk_mountain(seed):
    import random; random.seed(seed)
    G = grid()
    configs = [
        # (cx, cy, r)
        [(7, 7, 5)],
        [(5,7,4),(11,8,3)],
        [(7,7,4),(4,10,3),(10,10,3)],
        [(7,6,5),(4,10,2)],
        [(5,6,4),(11,7,4)],
        [(7,7,4),(7,11,2),(3,9,2)],
        [(7,7,5),(12,10,2),(3,11,2)],
        [(7,6,5),(11,10,3)],
        [(6,7,4),(11,7,4),(7,11,2)],
        [(7,7,5),(4,10,2),(12,10,2)],
        [(5,7,4),(11,7,4),(8,11,2)],
        [(7,7,5),(3,10,2),(12,9,2)],
        [(7,6,5),(5,10,3),(11,10,3)],
        [(7,7,5),(7,11,2)],
    ]
    peaks = configs[seed % len(configs)]
    # draw shadow first
    for cx,cy,r in peaks: disk(G, cx+1,cy+1,r,'Q')
    # rock body
    for cx,cy,r in peaks:
        disk(G, cx,cy,r,'q')
        disk(G, cx-1,cy-1,r-1,'r')
        disk(G, cx-1,cy-1,r-2,'R')
    # snow cap on largest
    cx,cy,r = sorted(peaks, key=lambda x:-x[2])[0]
    if r >= 4:
        disk(G, cx-1,cy-2,r-3,'S')
        px(G, cx-1,cy-2,'T')
    return G, PALETTE

for i in range(1, 15):
    n = i
    sprites.append((f'mountain_{n}', (lambda s=i-1: mk_mountain(s))))

# ── Lakes ─────────────────────────────────────────────────────────────────────

def mk_lake(shape):
    G = grid()
    configs = [
        (7,7,4), (7,7,5), (7,8,4), (7,7,3)
    ]
    cx,cy,r = configs[shape]
    disk(G, cx,cy,r,'L')
    disk(G, cx,cy,r-1,'E')
    disk(G, cx-1,cy-1,r-2,'I')
    ring(G, cx,cy,r,'D')
    return G, PALETTE

sprites += [(f'forest_lake{i}', (lambda s=i-1: mk_lake(s))) for i in range(1,5)]

# ── Spawn ─────────────────────────────────────────────────────────────────────

def mk_spawn():
    G = grid()
    # Gold star/diamond in centre
    cx,cy = 7,7
    for d in range(4):
        px(G,cx,cy-3+d,'o'); px(G,cx,cy+d,'o')
        px(G,cx-3+d,cy,'o'); px(G,cx+d,cy,'o')
    disk(G,cx,cy,2,'o')
    disk(G,cx,cy,1,'O')
    px(G,cx,cy,'o')
    return G, PALETTE

sprites.append(('spawn', mk_spawn))

# ── Water ─────────────────────────────────────────────────────────────────────

def mk_water():
    G = grid()
    water_bg(G)
    # ripple highlights
    for y,x0,x1 in [(2,2,5),(2,9,13),(6,4,8),(6,11,14),(10,1,4),(10,7,11),(14,3,7),(14,10,13)]:
        hline(G,y,x0,x1,'f')
        px(G,x0,y,'F'); px(G,x1,y,'F')
    return G, PALETTE

sprites.append(('water', mk_water))

# ── Coastlines ────────────────────────────────────────────────────────────────

coast_configs = {
    'forest_coastline1':  'S',
    'forest_coastline2':  'N',
    'forest_coastline3':  'E',
    'forest_coastline4':  'W',
    'forest_coastline5':  'SE',
    'forest_coastline6':  'SW',
    'forest_coastline7':  'NE',
    'forest_coastline8':  'NW',
    'forest_coastline9':  'S',
    'forest_coastline10': 'N',
    'forest_coastline11': 'E',
    'forest_coastline14': 'W',
}

def mk_coast(side):
    G = grid()
    rect(G,0,0,N,N,'g')
    if len(side)==1:
        coast_strip(G,side)
    else:
        # corner: both sides water
        a,b = side[0],side[1]
        # fill water for each direction
        for s in [a,b]: coast_strip(G,s)
    return G, PALETTE

for name,side in coast_configs.items():
    sprites.append((name, (lambda s=side: mk_coast(s))))

# ── Desert island ─────────────────────────────────────────────────────────────

def mk_desert(seed):
    G = grid()
    sand_bg(G)
    # add a few variations
    features = [
        [],
        [(7,7,'N')],
        [(7,7,'m'),(7,8,'m')],
        [(5,7,'N'),(10,8,'N')],
        [(7,7,'m')],
        [(3,5,'N'),(12,10,'N')],
        [(7,7,'m'),(7,6,'n')],
        [(7,8,'N'),(6,7,'N')],
        [(7,7,'m'),(9,9,'N')],
        [(7,7,'N'),(5,9,'m')],
        [(3,7,'N'),(11,8,'m')],
        [(7,6,'m'),(7,9,'N')],
        [(6,7,'N'),(9,8,'N'),(7,10,'m')],
        [(7,7,'m'),(5,5,'N'),(10,10,'N')],
        [(7,7,'N')],
        [(7,7,'m'),(8,8,'N')],
        [(7,7,'N'),(7,8,'m')],
        [(5,7,'m'),(10,8,'N')],
        [(7,7,'m'),(6,9,'N'),(9,6,'N')],
    ]
    for cx,cy,c in features[seed % len(features)]:
        disk(G,cx,cy,2,c)
    return G, PALETTE

island_names = [
    'desertisland_1','desertisland_2','desertisland_3','desertisland_17',
    'desertisland_4','desertisland_5','desertisland_6','desertisland_7',
    'desertisland_18','desertisland_8','desertisland_9','desertisland_10',
    'desertisland_11','desertisland_19','desertisland_12','desertisland_13',
    'desertisland_14','desertisland_15',
]
for i,name in enumerate(island_names):
    sprites.append((name, (lambda s=i: mk_desert(s))))

# ── Roads ─────────────────────────────────────────────────────────────────────

def mk_road_v():   # vertical
    G=grid(); road_strip(G,vertical=True); return G,PALETTE
def mk_road_h():   # horizontal
    G=grid(); road_strip(G,vertical=False); return G,PALETTE
def mk_road_cross():  # crossroads
    G=grid(); road_strip(G,True); road_strip(G,False); return G,PALETTE
def mk_road_ne():  # N-E corner
    G=grid()
    road_strip(G,True); road_strip(G,False)
    # mask out SW quadrant
    rect(G,0,N//2,N//2,N//2,'g')
    return G,PALETTE
def mk_road_nw():
    G=grid()
    road_strip(G,True); road_strip(G,False)
    rect(G,N//2,N//2,N//2,N//2,'g')
    return G,PALETTE
def mk_road_se():
    G=grid()
    road_strip(G,True); road_strip(G,False)
    rect(G,0,0,N//2,N//2,'g')
    return G,PALETTE
def mk_road_sw():
    G=grid()
    road_strip(G,True); road_strip(G,False)
    rect(G,N//2,0,N//2,N//2,'g')
    return G,PALETTE
def mk_road_t_n():  # T junction open N
    G=grid(); road_strip(G,True); road_strip(G,False)
    rect(G,0,0,N//2-2,N//2,'g'); rect(G,N//2+2,0,N//2-2,N//2,'g')
    return G,PALETTE
def mk_road_deadend_n():
    G=grid()
    cx=N//2; rect(G,cx-2,0,4,N//2+2,'p')
    vline(G,cx-2,0,N//2,'j'); vline(G,cx+1,0,N//2,'j')
    return G,PALETTE

road_map = {
    'forest_road1': mk_road_v,
    'forest_road3': mk_road_h,
    'forest_road4': mk_road_ne,
    'forest_road6': mk_road_se,
    'forest_road7': mk_road_nw,
    'forest_road9': mk_road_sw,
    'forest_road10': mk_road_cross,
    'forest_road11': mk_road_t_n,
    'forest_road12': mk_road_deadend_n,
    'forest_road13': mk_road_v,
    'forest_road14': mk_road_h,
    'forest_road15': mk_road_ne,
    'forest_road20': mk_road_cross,
    'forest_road24': mk_road_se,
    'forest_road4_cultistwizard': mk_road_ne,
}
for name,fn in road_map.items():
    sprites.append((name, fn))

# ── Trees (deciduous) ─────────────────────────────────────────────────────────

ASH   = {'g':'#508038','.':'#6ab848','<':'#4a5838','>':'#6a7850','?':'#8a9868','t':'#6e4818','s':'#4a2e0a'}
BIRCH = {'g':'#508038','.':'#6ab848','/':'#d0d0c0','\\':'#b0b0a0','H':'#a8d880','M':'#78b858','P':'#509038','t':'#d0d0c0','s':'#b0b0a0'}
MAPLE = {'g':'#508038','.':'#6ab848',':':'#a04818',';':'#d06828','~':'#f09040','t':'#6e4818','s':'#4a2e0a'}
SPRC  = {'g':'#508038','.':'#6ab848','P':'#0c3018','M':'#1a5028','H':'#2a7040','t':'#4a2e0a','s':'#321e06'}
CURS  = {'g':'#508038','`':"#1a1028","'":'#2e1a40','"':'#4a2a60','t':'#281420','s':'#180c10','.':'#6ab848'}

def mk_ash_tree(cx, cy, r):
    G = grid()
    disk(G,cx,cy,r,'<'); disk(G,cx-1,cy-1,r-1,'>'); disk(G,cx-1,cy-1,r-2,'?')
    px(G,cx,cy+r,'t')
    return G, ASH

def mk_birch_tree(cx,cy,r):
    G = grid()
    disk(G,cx,cy,r,'P'); disk(G,cx-1,cy-1,r-1,'M'); disk(G,cx-1,cy-1,r-2,'H')
    rect(G,cx,cy+r-1,1,2,'/')
    return G, BIRCH

def mk_maple_tree(cx,cy,r):
    G = grid()
    disk(G,cx,cy,r,':'); disk(G,cx-1,cy-1,r-1,';'); disk(G,cx-2,cy-2,r-2,'~')
    px(G,cx,cy+r,'t')
    return G, MAPLE

def mk_spruce(cx,base):
    G=grid()
    # narrow triangle layers
    for i,hw in enumerate([1,2,2,3,3,4]):
        y=base-i
        for dx in range(-hw,hw+1): px(G,cx+dx,y,'P')
        px(G,cx-hw,y,'H'); px(G,cx+hw,y,'H')
    rect(G,cx,base+1,1,2,'t')
    return G, SPRC

def mk_cursed_tree():
    G=grid()
    C=CURS
    # gnarled dead trunk + bare branches
    rect(G,7,8,2,5,"'")
    rect(G,7,8,2,5,"`")  # overwrite
    vline(G,7,6,12,"`")
    vline(G,8,6,12,"'")
    # bare branches
    hline(G,7,4,7,"'"); hline(G,7,10,12,'"')
    hline(G,9,5,7,'"'); hline(G,9,10,11,"'")
    hline(G,10,4,6,"'"); hline(G,8,10,13,'"')
    px(G,6,6,'`'); px(G,9,5,'`')
    return G, CURS

sprites += [
    ('forest_ashtree1',   lambda: mk_ash_tree(7,7,4)),
    ('forest_ashtree2',   lambda: mk_ash_tree(7,7,5)),
    ('forest_birchtree1', lambda: mk_birch_tree(7,7,4)),
    ('forest_birchtree2', lambda: mk_birch_tree(7,7,5)),
    ('forest_mapletree1', lambda: mk_maple_tree(7,7,4)),
    ('forest_mapletree2', lambda: mk_maple_tree(7,7,5)),
    ('forest_sprucetree1',lambda: mk_spruce(7,12)),
    ('forest_sprucetree2',lambda: mk_spruce(7,13)),
    ('forest_cursed_tree1', mk_cursed_tree),
]

# ── Ores / resources ──────────────────────────────────────────────────────────

COP = {'g':'#508038',')':'#c07030','_':'#e09050','+':'#905018','q':'#505050','r':'#787878'}
IRN = {'g':'#508038','-':'#607080','=':'#809098','[':'#a0b0b8','q':'#505050','Q':'#303030'}
COL = {'g':'#508038','c0':'#202020','c1':'#383838','c2':'#505050','q':'#505050'}

def mk_ore(outer, mid, light, rock_dark, rock_mid, pal):
    G=grid()
    disk(G,7,7,4,rock_dark); disk(G,6,6,3,rock_mid)
    disk(G,7,7,3,outer); disk(G,6,6,2,mid); px(G,6,6,light)
    return G, pal

def mk_copper(): return mk_ore('+',')',  '_', 'q','r', COP)
def mk_iron():   return mk_ore('q','-',  '=', 'Q','q', IRN)
def mk_coal():
    G=grid(); CPAL={'g':'#508038','B':'#202020','D':'#383838','E':'#505050','r':'#787878','q':'#505050'}
    disk(G,7,7,4,'q'); disk(G,6,6,3,'r'); disk(G,7,7,3,'B'); disk(G,6,6,2,'D'); px(G,6,6,'E')
    return G, CPAL

def mk_glowstem():
    GP={'g':'#508038','s1':'#3888c0','s2':'#50b8f0','s3':'#a0e0ff','s4':'#e0f8ff','m':'#287820','mc':'#42a832'}
    G=grid()
    disk(G,7,9,2,'m'); px(G,7,8,'mc')
    disk(G,7,6,2,'s1'); disk(G,7,6,1,'s2'); px(G,7,6,'s3')
    px(G,7,5,'s4'); px(G,6,6,'s3'); px(G,8,6,'s3')
    return G, GP

def mk_nettle():
    NP={'g':'#508038','n0':'#287820','n1':'#3a9030','n2':'#50a840','n3':'#70cc50'}
    G=grid()
    vline(G,7,8,13,'n0')
    for y,x,c in [(8,6,'n1'),(8,5,'n2'),(9,5,'n3'),(10,6,'n1'),(10,5,'n2'),
                  (8,9,'n1'),(8,10,'n2'),(9,10,'n3'),(10,9,'n1'),(10,10,'n2'),
                  (12,6,'n1'),(12,8,'n1'),(12,10,'n1')]:
        px(G,x,y,c)
    return G, NP

sprites += [
    ('forest_copperore1', mk_copper),
    ('forest_ironore2',   mk_iron),
    ('forest_coal1',      mk_coal),
    ('forest_glowstem1',  mk_glowstem),
    ('forest_nettle1',    mk_nettle),
]

# ── Slimes ────────────────────────────────────────────────────────────────────

def mk_slime(d,m,l,pal):
    G=grid()
    disk(G,7,8,4,d); disk(G,6,7,3,m); px(G,6,6,l)
    # eyes
    px(G,5,7,l); px(G,9,7,l)
    return G, pal

SLBP={'g':'#508038','d':'#1840a0','m':'#3068d8','l':'#70b0f8'}
SLYP={'g':'#508038','d':'#807010','m':'#c0a020','l':'#f0d040'}
SLGP={'g':'#508038','d':'#186030','m':'#28a040','l':'#50d870'}
SLRP={'g':'#508038','d':'#780808','m':'#b02020','l':'#f05050'}
SLKP={'g':'#508038','d':'#480878','m':'#8030b0','l':'#c060f0'}

sprites += [
    ('forest_blueslime1',   lambda: mk_slime('d','m','l',SLBP)),
    ('forest_blueslime2',   lambda: mk_slime('d','m','l',{**SLBP,'d':'#102870','m':'#204898','l':'#5090d8'})),
    ('forest_yellowslime1', lambda: mk_slime('d','m','l',SLYP)),
    ('forest_yellowslime2', lambda: mk_slime('d','m','l',{**SLYP,'d':'#605010','m':'#908020','l':'#c0b030'})),
    ('forest_greenslime1',  lambda: mk_slime('d','m','l',SLGP)),
    ('forest_greenslime2',  lambda: mk_slime('d','m','l',{**SLGP,'d':'#0a4020','m':'#187030','l':'#30a850'})),
    ('forest_redslime1',    lambda: mk_slime('d','m','l',SLRP)),
    ('forest_redslime2',    lambda: mk_slime('d','m','l',{**SLRP,'d':'#500808','m':'#881818','l':'#c04040'})),
    ('forest_kingslime1',   lambda: mk_slime('d','m','l',{**SLKP,'d':'#380860','m':'#6020a0','l':'#a050d8'})),
]

# ── Generic creature factory ──────────────────────────────────────────────────

def mk_creature(body_d, body_m, body_l, detail=None, bg='g', extra_pal=None):
    pal = dict(PALETTE); pal[bg]='#508038'
    if extra_pal: pal.update(extra_pal)
    G=grid(bg)
    # shadow
    disk(G,8,9,4,',' if bg=='g' else bg)
    # body
    disk(G,7,8,4,body_d); disk(G,6,7,3,body_m)
    px(G,5,6,body_l); px(G,6,6,body_l)
    if detail:
        for x,y,c in detail: px(G,x,y,c)
    return G, pal

# goblin - green
def mk_goblin(alt=False):
    c = ','; eye='o'
    G,P=mk_creature('G','V','U',
        detail=[(5,7,'o'),(9,7,'o'),(7,9,'G')],
        extra_pal={'G':'#384520','V':'#507038','U':'#70985a','o':'#f0d060',',':'#3a6028'})
    if alt: disk(G,7,8,4,'#384520')
    return G,P

sprites += [
    ('forest_goblin1', lambda: mk_goblin(False)),
    ('forest_goblin2', lambda: mk_goblin(True)),
]

# orc - darker green
def mk_orc(alt=False):
    d='#283818' if not alt else '#1c2810'
    m='#405028'; l='#608040'
    return mk_creature(d,m,l,
        detail=[(5,7,'#f0d060'),(9,7,'#f0d060'),(7,10,d)],
        extra_pal={d:d,m:m,l:l,'#f0d060':'#f0d060',',':'#3a6028'})

sprites += [
    ('forest_orc1', lambda: mk_orc(False)),
    ('forest_orc2', lambda: mk_orc(True)),
]

# ogre - large dark green
def mk_ogre(alt=False):
    d='#1c2a10'; m='#304020'; l='#506840'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028'})
    disk(G,7,8,5,d); disk(G,6,7,4,m); px(G,5,6,l)
    return G,P

sprites += [
    ('forest_ogre1', lambda: mk_ogre(False)),
    ('forest_ogre2', lambda: mk_ogre(True)),
]

# wolf - gray brown
def mk_wolf(alt=False):
    d='#504030' if not alt else '#383020'
    m='#706050'; l='#907870'
    return mk_creature(d,m,l,
        detail=[(5,6,l),(9,6,l),(7,10,d),(6,10,d),(8,10,d)],
        extra_pal={d:d,m:m,l:l,',':'#3a6028'})

sprites += [
    ('forest_wolf1', lambda: mk_wolf(False)),
    ('forest_wolf2', lambda: mk_wolf(True)),
]

# skeleton
def mk_skeleton(n):
    d='#a09878'; m='#c8c0a8'; l='#e8e0c8'
    extra = {d:d,m:m,l:l,',':'#3a6028'}
    G,P=mk_creature(d,m,l,extra_pal=extra)
    # skull details
    px(G,5,7,'#202020'); px(G,9,7,'#202020')
    return G,P

sprites += [(f'forest_skeleton{i}', lambda n=i: mk_skeleton(n)) for i in range(1,10)]

# highwayman - human shaped
def mk_highwayman():
    d='#302828'; m='#504848'; l='#706868'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','o':'#c0a030'})
    px(G,7,5,'#c0a030')  # hat
    rect(G,5,5,5,1,'#302828')
    return G,P

sprites.append(('forest_highwayman1', mk_highwayman))

# pig
def mk_pig():
    d='#d09898'; m='#e8b8b8'; l='#f8d8d8'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028'})
    px(G,6,7,d); px(G,8,7,d)  # nostrils
    return G,P

sprites.append(('forest_pig1', mk_pig))

# chicken
def mk_chicken():
    d='#c8a840'; m='#e8c850'; l='#f8e870'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','r':'#e04020'})
    px(G,7,6,'r')  # red comb
    return G,P

sprites.append(('forest_chicken1', mk_chicken))

# cow
def mk_cow():
    d='#303030'; m='#f0f0f0'; l='#ffffff'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028'})
    # black patches
    disk(G,5,7,2,d); disk(G,10,8,2,d)
    return G,P

sprites.append(('forest_cow1', mk_cow))

# sheep
def mk_sheep():
    d='#c0c0c0'; m='#e0e0e0'; l='#f8f8f8'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028'})
    # fluffy bumps
    for x,y in [(5,6),(9,6),(5,9),(9,9),(7,5)]: disk(G,x,y,1,l)
    return G,P

sprites.append(('forest_sheep1', mk_sheep))

# owlbear
def mk_owlbear():
    d='#403020'; m='#685040'; l='#906860'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','y':'#e0c020'})
    px(G,5,7,'y'); px(G,9,7,'y')  # owl eyes
    return G,P

sprites.append(('forest_owlbear1', mk_owlbear))

# cyclops
def mk_cyclops(alt=False):
    d='#386038' if not alt else '#284828'
    m='#508050'; l='#70a870'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','y':'#e8e020'})
    px(G,7,7,'y')  # single eye center
    return G,P

sprites += [
    ('forest_cyclops1', lambda: mk_cyclops(False)),
    ('forest_cyclops2', lambda: mk_cyclops(True)),
]

# mushmush - mushroom creature
def mk_mushmush(alt=False):
    CAP = '#b02018' if not alt else '#901808'
    SPOT= '#f0f0e0'; STM ='#d0c898'
    G,P=mk_creature(STM,STM,SPOT,extra_pal={CAP:CAP,SPOT:SPOT,STM:STM,',':'#3a6028'})
    disk(G,7,6,4,CAP); disk(G,6,6,3,CAP)
    # spots
    for x,y in [(6,5),(9,5),(7,7)]: px(G,x,y,SPOT)
    return G,P

sprites += [
    ('forest_mushmush1', lambda: mk_mushmush(False)),
    ('forest_mushmush2', lambda: mk_mushmush(True)),
]

# flying serpent
def mk_flying_serpent(alt=False):
    d='#185040' if not alt else '#102830'
    m='#288060'; l='#40b880'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','y':'#f0d020'})
    # wing outlines
    hline(G,6,3,5,'l'); hline(G,6,9,11,'l')
    hline(G,7,2,4,'l'); hline(G,7,10,12,'l')
    px(G,7,7,'y')  # eye
    return G,P

sprites += [
    ('forest_flyingserpent1', lambda: mk_flying_serpent(False)),
    ('forest_flyingserpent2', lambda: mk_flying_serpent(True)),
]

# spider
def mk_spider():
    d='#201818'; m='#382828'; l='#503838'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','r':'#e02020'})
    # 8 legs
    for dx,dy in [(-4,-2),(-4,0),(-4,2),(4,-2),(4,0),(4,2),(-3,-3),(3,-3)]:
        px(G,7+dx,7+dy,l)
    px(G,6,7,'r'); px(G,8,7,'r')  # red eyes
    return G,P

sprites.append(('forest_spider1', mk_spider))

# imp - small winged demon
def mk_imp(alt=False):
    d='#500820' if not alt else '#380614'
    m='#802030'; l='#b04050'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','y':'#f8d020'})
    for dx,dy in [(-4,-3),(-4,-2),(4,-3),(4,-2)]: px(G,7+dx,7+dy,l)
    px(G,6,7,'y'); px(G,8,7,'y')
    return G,P

sprites += [
    ('forest_imp1', lambda: mk_imp(False)),
    ('forest_imp2', lambda: mk_imp(True)),
]

# hellhound
def mk_hellhound(alt=False):
    d='#180808' if not alt else '#100606'
    m='#382020'; l='#603838'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','r':'#f02020'})
    px(G,5,7,'r'); px(G,9,7,'r')  # red eyes
    for dx in [-1,0,1]: px(G,7+dx,11,'r')  # fire breath
    return G,P

sprites += [
    ('forest_hellhound1', lambda: mk_hellhound(False)),
    ('forest_hellhound2', lambda: mk_hellhound(True)),
]

# cultist / acolyte (robed figure)
def mk_cultist(col1, col2, col3, name):
    G,P=mk_creature(col1,col2,col3,extra_pal={col1:col1,col2:col2,col3:col3,',':'#3a6028','r':'#a01818'})
    # robe hem
    rect(G,4,10,8,2,col1)
    px(G,7,6,'r')  # symbol on robe
    return G,P

sprites += [
    ('forest_cultis1',         lambda: mk_cultist('#281830','#402848','#604068','cultis1')),
    ('forest_cultistacolyte1', lambda: mk_cultist('#1a1020','#302040','#483060','acolyte1')),
    ('forest_cultistacolyte2', lambda: mk_cultist('#201520','#382838','#504858','acolyte2')),
]

# goblin wolfrider
def mk_wolfrider(alt=False):
    # Wolf body + goblin on top
    wd='#504030'; wm='#706050'; wl='#907870'
    gd='#384520'; gm='#507038'; gl='#70985a'
    G,P=mk_creature(wd,wm,wl,extra_pal={wd:wd,wm:wm,wl:wl,gd:gd,gm:gm,gl:gl,',':'#3a6028'})
    # goblin rider on top of wolf
    disk(G,7,6,2,gd); disk(G,7,6,1,gm); px(G,7,6,gl)
    return G,P

sprites += [
    ('forest_goblin_wolfrider1', lambda: mk_wolfrider(False)),
    ('forest_goblin_wolfrider2', lambda: mk_wolfrider(True)),
]

# lizard (event)
def mk_lizard():
    d='#1a4818'; m='#2a7028'; l='#40a040'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','y':'#f0d020'})
    # tail
    vline(G,7,10,14,d)
    px(G,6,11,m); px(G,8,11,m)
    px(G,6,7,'y'); px(G,8,7,'y')
    return G,P

sprites.append(('forest_event_lizard1', mk_lizard))

# archaeologist - human figure with tool
def mk_archaeologist():
    d='#604830'; m='#988060'; l='#c0a888'
    G,P=mk_creature(d,m,l,extra_pal={d:d,m:m,l:l,',':'#3a6028','h':'#c8a850','H':'#806030'})
    px(G,10,9,'h'); px(G,11,10,'H')  # tool/brush
    return G,P

sprites.append(('forest_archaeologist1', mk_archaeologist))

# ── Buildings / Stations ──────────────────────────────────────────────────────

def mk_building_basic(wall, roof, door, accent=None, pal_extra=None):
    BP={'g':'#508038',wall:wall,roof:roof,door:door,',':'#3a6028'}
    if pal_extra: BP.update(pal_extra)
    G=grid()
    rect(G,3,4,10,9,wall)
    rect(G,3,4,10,3,roof)
    rect(G,6,4,4,2,door)
    rect(G,6,9,4,2,door)
    if accent:
        for x,y,c in accent: px(G,x,y,c)
    return G,BP

def mk_bank(alt=False):
    W='#a0a0a0'; RF='#808080'; D='#686868'; G2='#f0d060'
    G,P=mk_building_basic(W,RF,D,pal_extra={G2:G2})
    rect(G,5,4,6,2,G2)  # gold facade
    px(G,7,7,G2); px(G,8,7,G2)  # gold sign
    return G,P

def mk_grand_exchange():
    W='#705080'; RF='#503060'; D='#382040'; G2='#f0d060'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,11,2,2,D)
    for x in range(4,12): px(G,x,5,G2)
    disk(G,7,7,2,G2)
    return G,BP

def mk_woodcutting_station():
    ST='#5e3a1a'; LG='#8b5e3c'; AX='#a0a0a0'; AXH='#6e4818'
    BP={'g':'#508038',ST:ST,LG:LG,AX:AX,AXH:AXH,',':'#3a6028'}
    G=grid()
    # stump
    disk(G,7,9,3,ST); disk(G,7,9,2,LG)
    # axe
    rect(G,9,4,2,5,AXH)
    rect(G,9,4,3,3,AX)
    return G,BP

def mk_mining_station():
    ST='#707070'; PK='#c0c0c0'; WD='#5e3a1a'; ORE='#c07030'
    BP={'g':'#508038',ST:ST,PK:PK,WD:WD,ORE:ORE,',':'#3a6028'}
    G=grid()
    rect(G,5,8,6,4,ST)  # platform
    rect(G,6,7,4,5,WD)  # support
    rect(G,6,4,2,5,PK)  # pickaxe handle
    rect(G,6,3,4,2,PK)  # pickaxe head
    px(G,8,10,ORE)
    return G,BP

def mk_crafting_station(color1, color2, color3):
    BP={'g':'#508038',color1:color1,color2:color2,color3:color3,',':'#3a6028'}
    G=grid()
    rect(G,3,5,10,8,color1)
    rect(G,4,4,8,4,color2)
    disk(G,7,8,2,color3)
    return G,BP

def mk_house():
    W='#c8b898'; RF='#803020'; D='#6e4818'; WIN='#a0c8e8'
    BP={'g':'#508038',W:W,RF:RF,D:D,WIN:WIN,',':'#3a6028'}
    G=grid()
    rect(G,3,5,10,8,W)
    rect(G,3,5,10,3,RF)
    rect(G,7,10,2,3,D)
    px(G,4,7,WIN); px(G,5,7,WIN)
    px(G,10,7,WIN); px(G,11,7,WIN)
    return G,BP

def mk_bank_building(alt=False):
    G2='#f0d060'
    W='#b0b0b8' if not alt else '#a8a8b0'
    RF='#808088'; D='#585860'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    for x in [3,4,5,9,10,11]: rect(G,x,4,1,9,'#686870')
    hline(G,7,4,11,G2)
    return G,BP

def mk_village(seed):
    import random; random.seed(seed*7)
    W='#c8b898'; RF='#803020'; D='#6e4818'; WIN='#a0c8e8'; RD='#a09070'
    BP={'g':'#508038',W:W,RF:RF,D:D,WIN:WIN,RD:RD,',':'#3a6028'}
    G=grid()
    # small cluster of buildings
    positions = [(2,3,6,6),(9,4,5,5),(3,10,4,5),(9,10,4,4),(5,9,3,3)]
    p = positions[seed % len(positions)]
    bx,by,bw,bh = p
    rect(G,bx,by,bw,bh,W)
    rect(G,bx,by,bw,2,RF)
    px(G,bx+bw//2,by+bh-1,D)
    return G,BP

def mk_tasks_trader():
    W='#7060a0'; RF='#504080'; D='#382860'; G2='#f0d060'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    # task board
    rect(G,5,6,4,4,'#c0b090')
    px(G,6,7,G2); px(G,7,7,G2); px(G,6,8,G2)
    return G,BP

def mk_rune_vendor():
    W='#406080'; RF='#284860'; D='#1a2e40'; G2='#50d8f0'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    disk(G,7,7,2,G2)
    return G,BP

def mk_jewelry():
    W='#805070'; RF='#603858'; D='#402840'; G2='#f0d060'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,'j2':'#e090e0',',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    px(G,6,7,G2); px(G,8,7,G2)
    px(G,7,6,'j2')
    return G,BP

def mk_alchemy():
    W='#307050'; RF='#205040'; D='#103828'; G2='#80f040'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,'b2':'#5090f0',',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    # potion
    disk(G,7,7,2,G2)
    px(G,6,6,'b2'); px(G,8,6,'b2')
    return G,BP

def mk_tailor():
    W='#906050'; RF='#704040'; D='#502828'; G2='#e0b0d0'
    BP={'g':'#508038',W:W,RF:RF,D:D,G2:G2,',':'#3a6028'}
    G=grid()
    rect(G,3,4,10,9,W); rect(G,3,4,10,3,RF)
    rect(G,7,10,2,3,D)
    # fabric bolt
    rect(G,5,6,4,4,G2)
    return G,BP

sprites += [
    ('forest_bank1',             lambda: mk_bank_building(False)),
    ('forest_bank2',             lambda: mk_bank_building(True)),
    ('forest_grand_exchange1',   mk_grand_exchange),
    ('forest_woodcuttingstation1', mk_woodcutting_station),
    ('forest_wcstation1',        mk_woodcutting_station),
    ('forest_miningstation1',    mk_mining_station),
    ('forest_cs1',               lambda: mk_crafting_station('#607080','#405060','#a0c0d0')),
    ('forest_gcstation1',        lambda: mk_crafting_station('#706040','#504028','#c0a060')),
    ('forest_jewelrycrafting1',  mk_jewelry),
    ('forest_alchemy1',          mk_alchemy),
    ('forest_tailor1',           mk_tailor),
    ('forest_taskstrader1',      mk_tasks_trader),
    ('forest_runevendor1',       mk_rune_vendor),
    ('forest_house1',            mk_house),
    ('forest_village1',          lambda: mk_village(0)),
    ('forest_village2',          lambda: mk_village(1)),
    ('forest_village3',          lambda: mk_village(2)),
    ('forest_village5',          lambda: mk_village(3)),
    ('forest_village6',          lambda: mk_village(4)),
]

# ── Generate all ──────────────────────────────────────────────────────────────
print(f'Generating {len(sprites)} sprites...')
for name, fn in sprites:
    try:
        result = fn()
        G, C = result
        save(name, G, C)
    except Exception as ex:
        print(f'  ERROR {name}: {ex}')

print('Done.')
