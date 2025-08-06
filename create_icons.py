#!/usr/bin/env python3
from PIL import Image, ImageDraw
import os

def create_icon(size, path):
    """Create a simple colored circle icon"""
    # Create a square image with transparent background
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Draw a blue circle with white border
    margin = size // 8
    draw.ellipse([margin, margin, size-margin, size-margin], 
                fill=(25, 118, 210, 255), outline=(255, 255, 255, 255), width=2)
    
    # Add a simple "M" for Milersättning
    font_size = size // 3
    text = "M"
    bbox = draw.textbbox((0, 0), text)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    x = (size - text_width) // 2
    y = (size - text_height) // 2
    draw.text((x, y), text, fill=(255, 255, 255, 255))
    
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path, 'PNG')

# Create icons for different densities
icons = [
    (48, 'app/src/main/res/mipmap-mdpi/ic_launcher.png'),
    (72, 'app/src/main/res/mipmap-hdpi/ic_launcher.png'),
    (96, 'app/src/main/res/mipmap-xhdpi/ic_launcher.png'),
    (144, 'app/src/main/res/mipmap-xxhdpi/ic_launcher.png'),
    (192, 'app/src/main/res/mipmap-xxxhdpi/ic_launcher.png'),
]

round_icons = [
    (48, 'app/src/main/res/mipmap-mdpi/ic_launcher_round.png'),
    (72, 'app/src/main/res/mipmap-hdpi/ic_launcher_round.png'),
    (96, 'app/src/main/res/mipmap-xhdpi/ic_launcher_round.png'),
    (144, 'app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png'),
    (192, 'app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png'),
]

print("Creating app icons...")
for size, path in icons + round_icons:
    create_icon(size, path)
    print(f"Created {path}")

print("All icons created successfully!")