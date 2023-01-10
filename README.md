# Konfy 


Konfy is a statically typed and easy to use configuration library for Kotlin. 

The main idea of Konfy is **chains** and **views** - you are creating a **chain**
of configs to retrieve parameters from different storage through one interface; after 
it you can create **view** - a typeful interface to config parameters.

Konfy supports plenty of formats with corresponding providers:
* Environment variables (out of the box) - support of environment variables
* System properties (out of the box) - support of system properties
* JNDI resources (out of the box) - support of JNDI resources in different contexts
* Property files (out of the box) - support of property files
* TOML (konfy-toml) - support of TOML
* SSM (konfy-ssm) - support of AWS Simple System Manager parameters
* KeePass (konfy-keepass) - support of parameters stored in kdbx encrypted files
* Kara config (konfy-kara) - support of properties config with includes (Kara framework config)

## Setup

To set it up, add a library to compile dependencies:
```kotlin
repositories {
    maven(url = "https://packages.jetbrains.team/maven/p/konfy/maven")
}

dependencies {
    compile("tanvd.konfy", "konfy", "0.1.22")
    //Other necessary providers
}
```

## What's inside

### Providers

First, you will need to create `ConfigProvider` object.

`ConfigProvider` can be asked directly for a config parameter via `get` call.

```kotlin
//Instantiation of EnvVarProvider with implicit default conversion service
val envVar = EnvVarProvider()
//Get value from a provider with explicit default value
val value = envVar.get("value", default = 5)
```

#### Conversions

Almost all providers (excluding JNDI) include a parameter in a constructor 
for a conversion function. This function will be used to convert values from 
string representation to specific type. 

Conversion is needed only if a specific configuration format does not support 
such type of parameters, and it should be deserialized from text representation.

Konfy assumes the following formats of values:
* Boolean: true, false
* Integer: 1
* Double: 1.0
* String: "string"
* Array: [1, 2], ["1", "2"]

### Chaining

Few providers can be linked to a chain. Resolution of parameter will stop
once it's found in a provider inside a chain.

```kotlin
val chain = ConfigChain(EnvVarProvider, KeepassProvider)
```

### Views

Now you can create a view to a chain - it will provide you with a typeful interface
to a configuration.

```kotlin
object Config: ConfigView() {
    /** Delegates to a `key` parameter in a config. No default. Will be cached. */
    
    val key: String by provided()
    /** Delegates to an `other-key` parameter in a config. Default is 0. Will be cached.  */
    val otherKey: Int by provided("other-key", 0)
    
    /** Delegates to a `lastKey` parameter in a config. No default. Will not be cached. */
    val lastKey: Int by provided("other-key", cached = false)
}
```

View can cache parameters and provide defaults for them.

### GlobalKonfy

`GlobalKonfy` object is basically a global configuration interface. You may initialize
it on the start of an application with a configuration chain and then use it everywhere to
get configuration parameters.

Moreover, `ConfigView` by default will take its `ConfigProvider` from `GlobalKonfy`

## Few advice

One of the interesting usages for Konfy is to split your configs
into a **secret** and *not-very-secret* and put secret config into KeePass DB (kdbx)
and not-very-secret into simple config file - for example, TOML. Chain will merge configs 
together and View will provide one typeful interface  to all parameters.


