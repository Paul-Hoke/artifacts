from PIL import Image, ImageDraw

def create_banana(output_path, size=64):
    # Create an image with a transparent background
    image = Image.new("RGBA", (size, size), (255, 255, 255, 0))
    draw = ImageDraw.Draw(image)

    # Banana body (Yellow)
    # Using multiple ellipses to create a curved shape
    yellow = (255, 225, 53, 255)
    dark_yellow = (225, 195, 43, 255)
    brown = (101, 67, 33, 255)

    # Main curve
    draw.ellipse([10, 10, 50, 50], fill=yellow)
    draw.ellipse([15, 5, 55, 45], fill=(0, 0, 0, 0)) # Carve out the middle to make it a crescent

    # Refine shape
    draw.pieslice([8, 12, 52, 52], start=30, end=150, fill=yellow)
    
    # Tips
    draw.ellipse([45, 15, 52, 22], fill=brown) # Top tip
    draw.ellipse([10, 45, 18, 52], fill=brown) # Bottom tip

    # Add some detail lines
    draw.arc([12, 12, 48, 48], start=40, end=140, fill=dark_yellow, width=2)

    image.save(output_path)
    print(f"Banana sprite generated at: {output_path}")

if __name__ == "__main__":
    create_banana("src/main/resources/static/banana.png")
