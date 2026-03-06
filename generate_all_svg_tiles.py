import os

skins = [
    "forest_2", "forest_1", "forest_goblin_wolfrider2", "mountain_12", "mountain_13", "mountain_14", 
    "forest_goblin_wolfrider1", "forest_lake1", "forest_lake2", "forest_event_lizard1", "forest_4", 
    "forest_goblin1", "mountain_11", "mountain_10", "mountain_9", "forest_goblin2", "forest_orc2", 
    "forest_ogre1", "forest_pig1", "forest_woodcuttingstation1", "mountain_8", "mountain_7", 
    "forest_road24", "forest_cyclops2", "forest_cyclops1", "forest_blueslime1", "forest_yellowslime1", 
    "forest_redslime2", "forest_greenslime1", "forest_orc1", "forest_ogre2", "forest_greenslime2", 
    "forest_redslime1", "forest_blueslime2", "forest_kingslime1", "forest_yellowslime2", "forest_road12", 
    "forest_road13", "forest_road14", "forest_road15", "forest_wolf2", "forest_ashtree2", "spawn", 
    "forest_copperore1", "forest_road11", "forest_wolf1", "forest_chicken1", "forest_cs1", 
    "forest_wcstation1", "forest_gcstation1", "forest_bank1", "forest_grand_exchange1", "forest_ashtree1", 
    "forest_owlbear1", "forest_cow1", "forest_village3", "forest_village1", "forest_village2", 
    "forest_lake3", "forest_lake4", "forest_jewelrycrafting1", "forest_alchemy1", "forest_tailor1", 
    "forest_mushmush1", "mountain_1", "mountain_2", "forest_village5", "forest_flyingserpent1", 
    "forest_mushmush2", "forest_flyingserpent2", "mountain_3", "mountain_4", "forest_miningstation1", 
    "forest_village6", "forest_birchtree2", "mountain_5", "mountain_6", "forest_birchtree1", 
    "forest_coal1", "forest_sprucetree2", "forest_skeleton1", "forest_skeleton2", "forest_skeleton3", 
    "forest_ironore2", "forest_road1", "forest_skeleton4", "forest_skeleton5", "forest_skeleton6", 
    "forest_highwayman1", "forest_skeleton7", "forest_skeleton8", "forest_skeleton9", "forest_sprucetree1", 
    "forest_road3", "forest_cursed_tree1", "forest_glowstem1", "forest_road7", "forest_taskstrader1", 
    "forest_spider1", "forest_cultis1", "forest_imp1", "forest_mapletree1", "forest_sheep1", 
    "forest_hellhound2", "forest_house1", "forest_cultistacolyte1", "forest_road4_cultistwizard", 
    "forest_road6", "forest_road10", "forest_road9", "forest_archaeologist1", "forest_bank2", 
    "forest_runevendor1", "forest_cultistacolyte2", "forest_imp2", "forest_hellhound1", 
    "forest_mapletree2", "forest_nettle1", "forest_coastline3", "forest_coastline4", "forest_coastline5", 
    "forest_road20", "water", "forest_coastline6", "forest_coastline2", "forest_coastline7", 
    "forest_coastline8", "forest_coastline9", "forest_coastline1", "forest_coastline10", 
    "forest_coastline11", "forest_coastline14", "desertisland_1", "desertisland_2", "desertisland_3", 
    "desertisland_17", "desertisland_4", "desertisland_5", "desertisland_6", "desertisland_7", 
    "desertisland_18", "desertisland_8", "desertisland_9", "desertisland_10", "desertisland_11", 
    "desertisland_19", "desertisland_12", "desertisland_13", "desertisland_14", "desertisland_15", 
    "sea2", "desertisland_16"
]

def generate_svg(skin_name, out_dir):
    size = 64
    
    # Defaults (Forest)
    bg_color = "#2D5A27"
    detail_color = "#3A7A32"
    theme = "forest"

    if "mountain" in skin_name:
        bg_color = "#5A5A5A"
        detail_color = "#404040"
        theme = "mountain"
    elif "desert" in skin_name:
        bg_color = "#EEDC82"
        detail_color = "#D4C268"
        theme = "desert"
    elif "water" in skin_name or "sea" in skin_name or "lake" in skin_name or "coastline" in skin_name:
        bg_color = "#1E90FF"
        detail_color = "#1874CD"
        theme = "water"

    svg_content = f'<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n'
    svg_content += f'<svg width="{size}" height="{size}" viewBox="0 0 {size} {size}" xmlns="http://www.w3.org/2000/svg">\n'
    svg_content += f'    <rect width="{size}" height="{size}" fill="{bg_color}" />\n'

    # Background Tessellation Patterns
    def add_wrapped_element(element_func, base_x, base_y):
        res = ""
        for dx in [-size, 0, size]:
            for dy in [-size, 0, size]:
                tx, ty = base_x + dx, base_y + dy
                if tx > -30 and tx < 94 and ty > -30 and ty < 94:
                    res += element_func(tx, ty)
        return res

    if theme == "forest":
        def tuft(x, y):
            return f'<path d="M {x-2},{y} L {x},{y-3} L {x+2},{y}" stroke="{detail_color}" stroke-width="1" fill="none" />\n'
        for px, py in [(15, 10), (45, 15), (10, 50), (55, 45), (32, 5), (32, 58), (5, 32), (59, 32)]:
            svg_content += add_wrapped_element(tuft, px, py)
    
    elif theme == "mountain":
        def rock(x, y):
            return f'<polygon points="{x-3},{y+2} {x},{y-3} {x+4},{y+1} {x+2},{y+4}" fill="{detail_color}" />\n'
        for px, py in [(20, 20), (50, 40), (10, 50), (40, 10), (32, 32)]:
            svg_content += add_wrapped_element(rock, px, py)

    elif theme == "desert":
        def dune(x, y):
            return f'<path d="M {x-5},{y} Q {x},{y-3} {x+5},{y}" stroke="{detail_color}" stroke-width="1" fill="none" />\n'
        for px, py in [(15, 20), (45, 45), (20, 50), (50, 15), (32, 32)]:
            svg_content += add_wrapped_element(dune, px, py)
            
    elif theme == "water":
        def wave(x, y):
            return f'<path d="M {x-4},{y} Q {x-2},{y-2} {x},{y} T {x+4},{y}" stroke="{detail_color}" stroke-width="1" fill="none" />\n'
        for px, py in [(10, 15), (30, 30), (50, 45), (20, 55), (45, 10)]:
            svg_content += add_wrapped_element(wave, px, py)

    # Foreground Elements (Simple generic representation)
    center_element = ""
    
    if "tree" in skin_name and not "cursed" in skin_name:
        center_element = f'<polygon points="32,15 42,40 22,40" fill="#1B3F18" /><rect x="29" y="40" width="6" height="10" fill="#4A3728" />'
    elif "cursed_tree" in skin_name or "dead_tree" in skin_name:
        center_element = f'<path d="M 32,50 L 32,20 M 32,35 L 20,25 M 32,30 L 45,20" stroke="#4A3728" stroke-width="3" fill="none" />'
    elif "slime" in skin_name:
        c = "#00FF00" if "green" in skin_name else "#FF0000" if "red" in skin_name else "#0000FF" if "blue" in skin_name else "#FFFF00"
        center_element = f'<path d="M 20,45 Q 32,20 44,45 Z" fill="{c}" opacity="0.8"/>'
    elif "goblin" in skin_name or "orc" in skin_name or "ogre" in skin_name or "cyclops" in skin_name:
        center_element = f'<circle cx="32" cy="32" r="10" fill="#8B4513" /><circle cx="28" cy="30" r="2" fill="#FFF" /><circle cx="36" cy="30" r="2" fill="#FFF" />'
    elif "skeleton" in skin_name or "bone" in skin_name:
        center_element = f'<circle cx="32" cy="25" r="8" fill="#FFF" /><rect x="28" y="35" width="8" height="12" fill="#FFF" />'
    elif "spider" in skin_name or "beetle" in skin_name:
        center_element = f'<circle cx="32" cy="32" r="8" fill="#000" /><path d="M 24,32 L 15,25 M 24,32 L 15,39 M 40,32 L 49,25 M 40,32 L 49,39" stroke="#000" stroke-width="2" />'
    elif "wolf" in skin_name or "hound" in skin_name or "pig" in skin_name or "cow" in skin_name or "sheep" in skin_name or "chicken" in skin_name or "bear" in skin_name or "serpent" in skin_name:
        center_element = f'<rect x="22" y="25" width="20" height="14" rx="4" fill="#A0522D" />'
    elif "ore" in skin_name or "coal" in skin_name or "mine" in skin_name:
        center_element = f'<polygon points="32,20 45,45 19,45" fill="#A9A9A9" /><polygon points="32,25 40,45 24,45" fill="#808080" />'
    elif "village" in skin_name or "house" in skin_name or "bank" in skin_name or "exchange" in skin_name or "station" in skin_name or "crafting" in skin_name or "alchemy" in skin_name:
        center_element = f'<rect x="20" y="30" width="24" height="20" fill="#8B4513" /><polygon points="16,30 32,15 48,30" fill="#A52A2A" />'
    elif "road" in skin_name:
        center_element = f'<rect x="24" y="0" width="16" height="64" fill="#CD853F" />'
        
    svg_content += f'    <!-- Foreground -->\n    {center_element}\n'
    
    # Label for clarity (small text with shadow)
    svg_content += f'    <text x="32" y="60" font-family="sans-serif" font-size="8" font-weight="bold" fill="#000" text-anchor="middle">{skin_name}</text>\n'
    svg_content += f'    <text x="32" y="59" font-family="sans-serif" font-size="8" font-weight="bold" fill="#FFF" text-anchor="middle">{skin_name}</text>\n'

    svg_content += f'</svg>'

    with open(os.path.join(out_dir, f"{skin_name}.svg"), "w") as f:
        f.write(svg_content)

out_dir = "src/main/resources/static2"
os.makedirs(out_dir, exist_ok=True)

for skin in skins:
    generate_svg(skin, out_dir)
print(f"Generated {len(skins)} tessellated SVGs in {out_dir}")
