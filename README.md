# Bomb Countdown (Java Swing)

A simple Java Swing “bomb countdown” demo app that shows a countdown, blocks closing the window until it finishes, and plays a GIF + WAV when you click the exit button.

## Features
- Countdown with system beep (updates every second)
- Prevents closing the window while time remains
- Shows a popup at the end and reveals an “Exit” button
- Plays `fullBang.wav` and displays `yuna-matsumoto-bang.gif`

## Tech
- Java
- Swing (`javax.swing.Timer`)
- Java Sound (`javax.sound.sampled`)

## How to Run
- Run `apps.bombcountdown.BombCountDown`

## Assets
Place these files where the program can load them (classpath recommended):
- `yuna-matsumoto-bang.gif`
- `fullBang.wav`

## Notes
Uses `shared.UIInit.initChineseUI()` for global UI/font defaults.
