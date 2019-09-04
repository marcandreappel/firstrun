# FirstRun

An Android library to verify programmatically at application launch if it is a fresh install or an update (or a normal run).

## Installation

In your project build.gradle add the following:

```gradle

allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

```

and in your app/build.gradle dependencies:

```gradle

android {
  dependencies {
    ...
    implementation 'com.github.marcandreappel:firstrun:2.0.0'
  }
}

```

## Implementation

In the ``onCreate`` of your activity where you need to check for the application launch status simply put:

```kotlin

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate()
    
    val firstRun: RunType = FirstRun(this).status()
    // ...
  }

```

and check the value:

```kotlin
  
  when (firstRun) {
    // Fresh installation
    RunType.INSTALL -> {
        // ...
    }
    // Normal application run 
    RunType.NORMAL -> {
        // ...
    }
    // Application updated 
    else -> { 
        when (firstRun) {
            RunType.MAJOR -> {
                // ...
            }
            RunType.MINOR -> {
                // ...
            }
            RunType.PATCH -> {
                // ...
            }
            // Code only update
            else -> {
                // ...
            }
        }
     }
  }
  // ...

  ```
