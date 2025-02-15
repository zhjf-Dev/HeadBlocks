# Translations

All plugin messages (except console messages) can be translated. Here is the list below:

To add a translation, simply duplicate the `messages_en.yml` in language folder and name it as you wish but keep
the `_xx.yml` somewhere.  
Make your translations and then in the [config.yml](config.md) file, add the previous `xx` in language section.

```
Prefix: '&6&lH&e&lead&6&lB&e&llocks &7»&r'

Head:
  Name: "&6&lH&e&lead&6&lB&e&llocks"
  Lore:
    - ""
    - "&7&oAllows you to create a HeadBlock at the chosen location"
    - ""
    - "&2✔ &8- &7Right click + crouch to create"
    - "&c✘ &8- &7Left click + crouch to delete"
    - ""

Messages:
  NoPermission: "%prefix% &cYou do not have permission to run this command"
  NoPermissionBlock: "%prefix% &cYou do not have permission to interact with this head"
  ErrorCommand: "%prefix% &cError in the command. See usage in /hb help"
  PlayerOnly: "%prefix% &cThis command can only be executed by a player"
  PlayerNotFound: "%prefix% &cThe player is not connected"
  ReloadComplete: "%prefix% &aThe configuration has been successfully reloaded"
  InventoryFull: "%prefix% &cThere is not enough space in the player's inventory to give a HeadBlock"
  CreativeSneakRemoveHead: "%prefix% &cYou must be crouching to destroy a HeadBlock"
  CreativeSneakAddHead: "%prefix% &cYou must be crouching to place a HeadBlock"
  HeadPlaced: "%prefix% &2&l+ &aHeadBlock placed &7(%world%, %x%, %y%, %z%)"
  HeadRemoved: "%prefix% &2&c- &eHeadBlock removed &7(%world%, %x%, %y%, %z%)"
  RemoveAllConfirm: "%prefix% &cWarning, this command will remove &7%headCount% &chead(s). Add &7--confirm &cin the command to confirm"
  RemoveAllSuccess: "%prefix% &e%headCount% &ahead(s) successfully removed"
  AlreadyClaimHead: "%prefix% &cYou have already clicked on this head"
  ListHeadEmpty: "%prefix% &cThere are no heads yet"
  HeadGiven: "%prefix% &aYou have successfully received a HeadBlock"
  NoHeadFound: "%prefix% &cThis player has not yet found a head"
  ResetAllNoData: "%prefix% &cThere is no data to reset"
  ResetAllConfirm: "%prefix% &cWarning, this command will reset &7%playerCount% &cplayer(s) data. Add &7--confirm &cin the command to confirm"
  ResetAllSuccess: "%prefix% &aData of &e%playerCount% &aplayer(s) successfully reset"
  PlayerReset: "%prefix% &aThe %player% data has been reset"
  RemoveLocationError: "%prefix% &cError no matching HeadBlock was found"
  HeadAlreadyExistHere: "%prefix% &cA HeadBlock already exists at this location"
  Version: "%prefix% &aPlugin version : &e%version%"
  MeCommand:
    - ""
    - "&aNumber of heads collected : &e%current%&7/&e%max%"
    - "&aProgression : &7[%progress%&7]"
    - ""

Chat:
  Hover:
    Teleport: "&7&oTeleport to the HeadBlock"
    Remove: "&7&oDelete this HeadBlock"
    PreviousPage: "&7&oPrevious page"
    NextPage: "&7&oNext page"
    Own: "&7&oHead found"
    NotOwn: "&7&oHead not found yet"
  Box:
    Teleport: "&8[&a☄&8] &7|"
    Remove: "&7| &8[&c✘&8]"
    Own: "&7| &8[&2✔&8]"
    NotOwn: "&7| &8[&c✘&8]"
  StatsTitleLine: "&7------[ &eData of &6%player% &8» &6%headCount%&8/&6%maxHead% &eheads &7]-------"
  LineTitle: "&7------------------[ &6&lH&e&lead&6&lB&e&llocks&7 ]-----------------"
  LineCoordinate: "&6World &8: &e%worldName% &8[&6X&8: &e%x%&8, &6Y&8: &e%y%&8, &6Z&8: &e%z%&8]"
  PreviousPage: "&7---------[&8<<<&7]---------"
  NextPage: "&7---------[&8>>>&7]---------"
  PageFooter: " &8[ &e%pageNumber%&7/&e%totalPage% &8] "

Help:
  LineTop: "&7--------------------[ &e&lHelp &7]--------------------"
  Help: "&6/hb help &8: &7&oDisplay this message"
  Me: "&6/hb me &8: &7&oDisplays your progress"
  Remove: "&6/hb remove <headUuid> &8: &7&oRemove head by it's <headUuid>"
  RemoveAll: "&6/hb remove <headUuid> &8: &7&oRemove all spawned head"
  Give: "&6/hb give <player> &8: &7&oGives a HeadBlock to <player>"
  Reset: "&6/hb reset <player> &8: &7&oReset data of the <player>"
  ResetAll: "&6/hb resetall &8: &7&oReset data of all players"
  List: "&6/hb list <page> &8: &7&oDisplays the list of existing HeadBlocks"
  Stats: "&6/hb stats <player> <page> &8 &7&oDisplays the list of owned and missing heads of the <player>"
  Reload: "&6/hb reload &8: &7&oReload the configuration"
  Version: "&6/hb version &8: &7&oDisplays the version of the plugin"
```