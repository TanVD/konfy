# Konfy 

[![CircleCI](https://circleci.com/gh/TanVD/konfy.svg?style=svg)](https://circleci.com/gh/TanVD/konfy)
[![Download](https://api.bintray.com/packages/tanvd/konfy/tanvd.konfy/images/download.svg)](https://bintray.com/tanvd/konfy/tanvd.konfy/_latestVersion)


Konfy is a statically typed and easy to use configuration library for Kotlin. 

It supports plenty of formats with so-called "providers":
* Environment variables (out of the box) - support of environment variables
* TOML (konfy-toml) - support of TOML
* SSM (konfy-ssm) - support of AWS Simple System Manager parameters
* KeePass (konfy-keepass) - support of parameters storing in a kdbx encrypted files
* Kara config (konfy-kara) - support of properties config with includes (Kara framework config)

## Setup

`konfy` is released to JCenter

To set it up add library to compile dependencies:
```kotlin
repositories {
    jcenter()
}

dependencies {
    compile("tanvd.konfy", "konfy", "0.1.0")
    //Other needed providers
}
```

## What's inside

### Config provider

First of all, you will to create `ConfigProvider` object. Objects of such
type represents a read-only view to configuration file.

`ConfigProvider` can be asked directly for a config parameter via `get` call.

```kotlin
//Instantiation of EnvVarProvider with explicit conversion service
val envVar = EnvVarProvider(convert = ConversionService::convert)
//Get value from a provider with explicit default value
val value = envVar.get<Int>("value", default = 5)
```

Note: all providers includes a parameter in a constructor for a conversion
function. This function will be used to convert value from string 
representation to specific type. Conversion is needed only if specific 
configuration type does not support such a type of parameters, and it should
be deserialized from text representation.

### Config chain

Few providers can be linked to a chain. Resolution of parameter will stop
once it's found in a provider inside a chain.

In chain variables definition `provided` delegate can be used - it delegates
a field into a chain parameter. Note, that by default parameter is cached.

```kotlin
object Config: ConfigChaing(EnvVarProvider, KeepassProvider) {
    /** Field delegates call to a `key` parameter in a config. No default. */
    val key: String by provided()
    /** Field delegates call to a `other-key` parameter in a config. Default is 0. */
    val otherKey: Int by provided("other-key", 0)
}
```

## Few advices

One the most convenient usages for Konfy Chains is to split your configs
in a **secret** and not-very-secret and put one of them into KeePass db (kdbx)
and the second into simple config file - TOML. Chain will merge configs together
and provide one interface to all parameters.


