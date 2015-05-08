# Pure

Pure is a World Generator Bukkit plugin.

It was created to provide a simple way to use Minecraft Vanilla world generation on non-Vanilla
based Minecraft Server softwares like [Glowstone].

In the end, this will allow multiple cool things:
* Allow the use of the Vanilla world generator in a non-Vanilla context
* Keep the previous version World Generator for your Minecraft map while updating to the next version
* Have multiple maps with different Minecraft versions World Generators on the same server

## How

The plugin downloads the required Minecraft Server jar files from their official repository on Amazon S3.
It then remaps the jar file content so that it doesn't clash with other versions.
Finally, it loads the jar file and use its obfuscated content to provide a Bukkit World Generator.

## Status

In its current state, Pure only supports Minecraft 1.7.10.
As the code for each version will be very similar, while completely different due to Minecraft's obfuscation,
the 1.7.10 code must be advanced enough before it can be ported to other Minecraft versions.

The next implemented version will be 1.8.

This could handle versions as old as Minecraft Alpha 1.1.2.1, as proved by [VanillaGenerator].

## Credits

Pure is based on original work by [coelho] mostly on [VanillaGenerator] and a little on [VanillaNMS].
I first started by implementing a clean way to handle Minecraft Server jar files, then basically copy the
implementation of Minecraft 1.7.10 found in [VanillaGenerator]. I understood it, commented it, enhanced it and
fixed it. I added generated chests support among other things.

[Glowstone]: www.glowstone.net
[coelho]: github.com/coelho
[VanillaGenerator]: github.com/coelho/VanillaGenerator
[VanillaNMS]: github.com/coelho/VanillaGenerator
