![Typing In Chat Plugin: See when somebody typing in chat](https://github.com/user-attachments/assets/ef10e782-8f37-4092-86d0-81a791a779f4)
# Typing In Chat Plugin
Enable you server to show when players actively typing in chat.
Server admins should install plugin on their server. Everyone who install [clientside mod](https://github.com/Orphey98/TypingInChat-Mod) will have special indication while they are typing in chat. <br>
Players who do not have client-side mod will only see other player's indicators and will not have their own. 
![demo1](https://github.com/user-attachments/assets/ee3bb3ba-be4f-4c08-ab99-f4925e1140a0)
## Features

- Animated and highly customizable. Looks defined by server admins.
- Permissions support to show or hide indication for specific users or groups.
- Visible for players that don't have clientside mod.
- Based on packets and text-display entities.
- Text-displays are attached to players.

## Customization Demo
Colored indicator, indentation enabled, text-shadow disabled: [GIF](https://github.com/user-attachments/assets/d6efe5a2-e311-4d05-87dc-89802a8ff5bd) <br>
Colored everything, no indentation, text-shadow enabled: [GIF](https://github.com/user-attachments/assets/619383d6-2cf2-483d-bbd0-aa488d97e00f) <br>
Default colors, player name hidden: [GIF](https://github.com/user-attachments/assets/a1d01a4f-9e06-446b-969f-ef562f6f6ddf)


## Installation

You need to put typinginchat-spigot.jar into server plugins folder. Also, you need to have [packetevents 2.6.0](https://github.com/retrooper/packetevents/releases/tag/v2.6.0) and [HologramAPI 1.4.7](https://github.com/max1mde/HologramAPI/releases/tag/1.4.7) installed.
You may change TIC indicators appearance with config.yml. 

```yaml
config.yml

#Show player display names
show-names: true
#Indentation style of typing icon
icon-indentation: true
#Hologram visibility radius
view-range: 16
#Hologram visibility through blocks
visible-through-blocks: false
#UNICODE character or text for typing animation
typing-char: "✎"

#Color customization
names-color: "#ffffff"
typing-icon-color: "#ffffff"
background-color: "#000000"
#From 0 to 255
background-transparency: 50
#Shadow outline of text font
text-shadow: false

#use /tic reload command to apply changes
```
## Permissions

**tic.display** - allows player to have tic indication. True by default, but you can remove it from players or groups. <br>
**tic.admin** - required for _/typinginchat reload_ command usage.
 
    
## Related

Link for client-side mode 
(required only for players who want to show when they typing in chat):
[TypingInChat-Mod](https://github.com/Orphey98/TypingInChat-Mod)







