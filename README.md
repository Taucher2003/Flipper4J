# Flipper4J 🐬

<!--
The language blocks for Java are set to javascript.
IntelliJ reports errors for invalid Java syntax if they would be set to java.

However, there is no need for the full Java Class Syntax in the Readme.
JavaScript provides at least a bit of accurate syntax highlighting.
-->

Flipper4J is a library for managing feature flags.
It is heavily inspired by the [original Flipper implementation](https://github.com/jnunemaker/flipper) in Ruby.

## Installation

Releases are published into the Maven Central Repository.
Snapshot releases are available in the GitLab Package Registry for this project.

<details>
<summary>Snapshot Registry</summary>

### Maven

```xml
<repository>
    <id>gitlab</id>
    <url>https://gitlab.com/api/v4/projects/38232779/packages/maven</url>
</repository>
```

### Gradle

```kts
repositories {
    maven(url = "https://gitlab.com/api/v4/projects/38232779/packages/maven")
}
```

</details>

The project is separated into multiple modules. Choose matching adapter for your use-case.

### Maven

```xml
<dependency>
    <groupId>com.gitlab.taucher2003.flipper4j.adapter</groupId>
    <artifactId><!-- Your required adapter --></artifactId>
    <version><!-- Your desired version --></version>
</dependency>
```

### Gradle

```kts
implementation("com.gitlab.taucher2003.flipper4j.adapter", /* Your required adapter*/"", /* Your desired version*/"")
```

### Available Adapters

| ArtifactId | Use Case                                                               |
|------------|------------------------------------------------------------------------|
| http       | For use with `Flipper::Api`, connects to a remote HTTP Flipper Backend |
| property   | Uses System properties to determine status of feature toggles          |

The adapter pulls in the Flipper4J Core, the dependency does not need to be added manually.

## Configuration

The exact configuration depends on the chosen adapter. The adapters follow this convention:

```javascript
var flipper = new FlipperConfigurator()
        // adapter specific configuration
        .build()
        .createFlipper();
```

## Usage

After creating the `Flipper` instance, it can be used to evaluate feature toggles.

```javascript
if(flipper.isEnabled("cool_new_feature")) {
    // do cool new stuff
} else {
    // do boring old stuff
}
```

### Evaluation with Context

This library can evaluate feature toggles with context.
Classes which represent a context must implement the `FlipperIdentifier` interface.

```java
class User implements FlipperIdentifier {
    private final int id;
    // constructor etc.

    public String flipperId() {
        return "User:" + id;
    }
}
```

Instances of these classes can be passed to the `isEnabled` method.

```javascript
var user = new User();

if(flipper.isEnabled("cool_new_feature", user)) {
    // the user can use the cool new feature
} else {
    // the user can only use the boring old feature
}
```

### Available evaluation Gates

- [Boolean](#boolean)
- [Actors](#actors)
- [Percentage of Actors](#percentage-of-actors)
- [Percentage of Time](#percentage-of-time)

If one of the gates evaluates to `true`, the feature is considered enabled.

#### Boolean

The boolean gate is used to globally enable a feature.
It is used without context and will ignore the context if it is passed to the check.

#### Actors

The actors gate uses a list of actors to determine if the feature is enabled or not.
Eligible actors implement the `FlipperIdentifier` interface and can be passed as context to the `isEnabled(flag, context)` method.

If used without context/actor, the gate evaluates to `false`.

#### Percentage of Actors

This gate evaluates to `true` for the given percent of actors. The evaluation is constant for each actor.
If the gate evaluates to `true` for a given actor, it will always evaluate to `true` for this specific actor.

When no actor is used, the gate will evaluate to `true` if the percentage is set to `100`.

#### Percentage of Time

This gate evaluates to `true` for the given percent of method calls. The evaluation is not constant for
each actor and can lead to poor user experience if a feature is not constantly available or not available.

When no actor is used, the gate still evaluates tha random value and can evaluate to `true`.

### Toggle Modification

Administrative methods to change the state of toggles are exposed within the `FlipperAdmin`.
An instance of the `FlipperAdmin` can be retrieved with the `admin()` method on the `Flipper` instance.

### Adapter specifics

#### properties Adapter

The properties adapter expects feature gate properties in the format `<prefix>.<toggle_name>.<gate_name>`.
The prefix is set in the `FlipperConfigurator` with the `setPropertyPrefix()` method.

`toggle_name` corresponds to the name of the flag you are checking.

`gate_name` can be one of:

- `boolean`
- `actors`
- `percentage_of_actors`
- `percentage_of_time`

The values for the different gates are in different formats:

| Flag                   | Value format                                                                                                 |
|------------------------|--------------------------------------------------------------------------------------------------------------|
| `boolean`              | String `true` if the gate is enabled                                                                         |
| `actors`               | String containing all actors for which the gate is enabled<br/>By default, the actors are separated with `,` |
| `percentage_of_actors` | String representing a valid double value between 0 and 100                                                   |
| `percentage_of_time`   | String representing a valid double value between 0 and 100                                                   |
