# EMI Compat

Unofficial compatibility fixes between [EMI](https://modrinth.com/mod/emi) and other mods.

Each integration only applies when the mod it targets is installed, and can be turned off in the client config
(`config/emicompat-client.toml`, or the mod list's config screen). The mod is client-side only. Servers don't need it.

## Integrations

### Sophisticated Backpacks & Sophisticated Storage

When an EMI recipe tree is in crafting mode, EMI highlights container slots holding items the tree still needs, so you
can see what to grab from a chest. That highlight never showed up in
[Sophisticated Backpacks](https://modrinth.com/mod/sophisticated-backpacks) or
[Sophisticated Storage](https://modrinth.com/mod/sophisticated-storage) screens.

- **Recipe tree highlights in backpacks, chests, barrels and shulker boxes.** EMI skips slots that a recipe handler
  reports as ingredient sources, because it already counts those items as "have". Sophisticated Core's handler reports
  every storage slot that way, but only counts the storage's contents when a Crafting Upgrade is installed. Without one,
  storage items were neither counted nor highlighted. Now they're highlighted, just like in a vanilla chest.
  With a Crafting Upgrade installed, EMI counts the storage's contents toward the tree, so those slots are left alone.
- **Stray overlay squares above large storages.** Sophisticated moves storage slots that are scrolled out of view, or
  hidden by its search box, off to the side instead of disabling them. EMI's slot overlays (recipe tree highlights and
  search highlighting) were drawn on them anyway, above the screen. Those slots are now skipped.

Needs Sophisticated Core 1.4.16 or newer. The fix lives in Sophisticated Core, so it covers Backpacks, Storage, or both.

## Compatibility

| Minecraft | Loader   | EMI    | Optional integrations      |
|-----------|----------|--------|----------------------------|
| 1.21.1    | NeoForge | 1.1.0+ | Sophisticated Core 1.4.16+ |

EMI's newest release is for 1.21.1, so that is the only target for now. Newer Minecraft versions can be added once EMI
supports them (see below).

## Building

```sh
./gradlew build                       # default target from gradle/minecraft-versions.properties
./gradlew build -Pmc_version=1.21.1   # a specific target
./gradlew runClient                   # dev client with EMI and every integrated mod
```

The jar ends up in `build/libs/emicompat-<minecraft>-<version>.jar`.

### Adding an integration

1. Add the mod to `gradle/minecraft-versions.properties` for every target, then to `build.gradle` as `compileOnly`
   (plus `localRuntime` to test it in the dev client).
2. Put its code in `mackery.emicompat.compat.<mod>` and its mixins in `mackery.emicompat.mixin.<mod>`.
3. Register the mixin package and the mod id in `EmiCompatMixinPlugin`, so the mixins are skipped when the mod is
   missing. Never reference the mod's classes from anywhere else.
4. Add a config section for it in `Config`, an optional dependency in `neoforge.mods.toml`, and an optional
   dependency in the Release workflow.

### Adding a Minecraft version

`gradle/minecraft-versions.properties` is the source of truth for every target. To add one:

1. Copy the `version.1.21.1.*` block, rename it, and update every value (NeoForge, Parchment, EMI and integrated mod
   versions, version ranges and Java version).
2. Append the version to `supported_versions`.
3. If some code only compiles for some targets, put it in `src/compat/<source_family>/java`.

CI builds every entry in `supported_versions`, and a release publishes one jar per entry.

## Credits

The logo is based on [EMI](https://github.com/emilyploszaj/emi)'s icon by Emi, used under the MIT License
(see `src/main/resources/LICENSE_emi`). This is an unofficial addon and isn't affiliated with EMI.
The 32×32 source is in `art/icon-32.png`.

## Releasing

Run the **Release** workflow from the Actions tab. It builds every supported target and publishes each jar to GitHub
Releases, CurseForge and Modrinth. The changelog comes from the matching section of `CHANGELOG.md` unless you enter one
in the workflow form.

The workflow needs these repository settings:

| Name                    | Kind     | Value                       |
|-------------------------|----------|-----------------------------|
| `CURSEFORGE_PROJECT_ID` | Variable | CurseForge project ID       |
| `MODRINTH_PROJECT_ID`   | Variable | Modrinth project ID or slug |
| `CURSEFORGE_TOKEN`      | Secret   | CurseForge API token        |
| `MODRINTH_TOKEN`        | Secret   | Modrinth personal token     |
