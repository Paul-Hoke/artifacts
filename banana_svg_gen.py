def create_banana_svg(output_path):
    svg_content = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg width="64" height="64" viewBox="0 0 64 64" xmlns="http://www.w3.org/2000/svg">
    <!-- Banana Body -->
    <path d="M 12,48 C 15,55 45,55 52,20 C 55,10 45,5 42,12 C 35,25 20,35 12,42 C 8,45 8,45 12,48 Z" 
          fill="#FFE135" stroke="#E1C32B" stroke-width="1"/>
    
    <!-- Top Tip (Stem) -->
    <path d="M 42,12 C 44,8 48,8 50,12" 
          fill="none" stroke="#654321" stroke-width="3" stroke-linecap="round"/>
    
    <!-- Bottom Tip -->
    <circle cx="12" cy="48" r="2" fill="#654321" />
    
    <!-- Detail Line -->
    <path d="M 18,44 C 25,38 35,30 45,18" 
          fill="none" stroke="#E1C32B" stroke-width="1.5" stroke-linecap="round" opacity="0.6"/>
</svg>
"""
    with open(output_path, "w") as f:
        f.write(svg_content)
    print(f"Banana SVG generated at: {output_path}")

if __name__ == "__main__":
    create_banana_svg("src/main/resources/static/banana.svg")
