import os
import base64
import wave
import struct

# 1x1 transparent PNG
PNG_BASE64 = b"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="

png_paths = [
    "Assets/Sprites/Tisch/Background_Mid_640x360.png",
    "Assets/Sprites/Tisch/Background_High_640x360.png",
    "Assets/Sprites/Bar/JackBody_40x80.png",
    "Assets/Sprites/Bar/JackHead_26x26.png",
    "Assets/Sprites/Tisch/JackWarnung.png",
    "Assets/Sprites/Items/Ass_20x20.png",
    "Assets/Sprites/Items/SchmutzigesWasser_20x20.png",
    "Assets/Sprites/Items/VerdorbeneSuppe_20x20.png"
]
for i in range(1, 13):
    png_paths.append(f"Assets/Sprites/Tisch/JacksBlick/zoom_{i:02d}.png")

wav_paths = [
    "Assets/Sounds/SFX/sfx_heartbeat.wav",
    "Assets/Sounds/SFX/sfx_search_success.wav",
    "Assets/Sounds/SFX/sfx_search_fail.wav",
    "Assets/Sounds/SFX/sfx_warning.wav",
    "Assets/Sounds/SFX/sfx_eat.wav"
]

png_data = base64.b64decode(PNG_BASE64)

for path in png_paths:
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(png_data)
    print(f"Created {path}")

# Create silent WAV
sample_rate = 44100
duration = 0.1
n_samples = int(sample_rate * duration)
for path in wav_paths:
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with wave.open(path, 'w') as f:
        f.setnchannels(1)
        f.setsampwidth(2)
        f.setframerate(sample_rate)
        # Write zeros
        f.writeframesraw(b'\x00\x00' * n_samples)
    print(f"Created {path}")

