#!/bin/bash

echo "Setting up Android development environment..."

# Install required packages
echo "Installing required packages..."
sudo apt update
sudo apt install -y unzip wget curl

# Create Android SDK directory
mkdir -p $HOME/Android/Sdk/cmdline-tools

# Download Android command line tools
echo "Downloading Android command line tools..."
wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip

# Extract command line tools
echo "Extracting command line tools..."
unzip -q /tmp/cmdline-tools.zip -d /tmp/
mv /tmp/cmdline-tools $HOME/Android/Sdk/cmdline-tools/latest

# Set up environment variables
echo "Setting up environment variables..."
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> $HOME/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> $HOME/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> $HOME/.bashrc

# Source the updated bashrc
source $HOME/.bashrc

# Accept licenses and install SDK components
echo "Installing Android SDK components..."
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin

yes | sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" "system-images;android-34;google_apis;x86_64"

echo "Android development environment setup complete!"
echo "Please run 'source ~/.bashrc' or restart your terminal to update environment variables."