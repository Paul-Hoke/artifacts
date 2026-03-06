def create_forest_3_tessellated_svg(output_path):
    size = 64
    bg = "#2D5A27"
    detail = "#3A7A32"
    trunk = "#4A3728"
    leaf1 = "#1B3F18"
    leaf2 = "#224D1E"

    def get_tree(x, y, s):
        return f"""
    <g transform="translate({x}, {y}) scale({s})">
        <polygon points="0,-20 15,10 -15,10" fill="{leaf1}" />
        <polygon points="0,-10 12,15 -12,15" fill="{leaf2}" />
        <rect x="-3" y="15" width="6" height="8" fill="{trunk}" />
    </g>"""

    def get_tuft(x, y):
        return f"M {x-2},{y} L {x},{y-3} L {x+2},{y} "

    # Elements to draw (x, y, scale for trees)
    trees_data = [
        (32, 32, 0.9),  # Center
        (0, 0, 0.7),    # Corner (will wrap)
        (64, 64, 0.7),  # Corner (will wrap)
        (0, 64, 0.7),   # Corner (will wrap)
        (64, 0, 0.7),   # Corner (will wrap)
        (10, 32, 0.5),  # Left edge
        (54, 32, 0.5)   # Right edge
    ]

    tufts_data = [
        (15, 10), (45, 15), (10, 50), (55, 45), (32, 5), (32, 58), (5, 32), (59, 32)
    ]

    svg_content = f"""<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg width="{size}" height="{size}" viewBox="0 0 {size} {size}" xmlns="http://www.w3.org/2000/svg">
    <rect width="{size}" height="{size}" fill="{bg}" />
    """

    # Add tufts (simple paths)
    path_d = ""
    for tx, ty in tufts_data:
        path_d += get_tuft(tx, ty)
    svg_content += f'<path d="{path_d}" stroke="{detail}" stroke-width="1" fill="none" />'

    # Add trees with wrapping logic
    for x, y, s in trees_data:
        # Drawing the tree at (x,y) and its wrapped counterparts
        # To be safe, we check if it's near any edge
        for dx in [-size, 0, size]:
            for dy in [-size, 0, size]:
                tx, ty = x + dx, y + dy
                # Only add if it actually contributes to the 0-64 area
                # (Bounding box of a tree is roughly 30x40)
                if tx > -20 and tx < 84 and ty > -20 and ty < 84:
                    svg_content += get_tree(tx, ty, s)

    svg_content += "\n</svg>"

    with open(output_path, "w") as f:
        f.write(svg_content)
    print(f"Tessellated Forest_3 SVG generated at: {output_path}")

if __name__ == "__main__":
    create_forest_3_tessellated_svg("src/main/resources/static2/forest_3.svg")
