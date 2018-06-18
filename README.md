# FirstRun
An Android library to verify programmatically at application launch if it is a fresh install or an update (or a normal run).

## Installation

In your project build.gradle add the following:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

and under your app/build.gradle dependencies:
```
android {
  dependencies {
    ...
    implementation 'com.github.marcandreappel:firstrun:1.0.0'
  }
} 
```

## Implementation

In the ``onCreate`` of your activity where you need to check for the application launch status simply put:
```
...
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate()
    
    val firstRun: FirstRunStatus = FirstRun(this).status()
    ...
  }
...
```

and check the value:

```
  ...
  when (firstRun) {
    FirstRunStatus.FIRST_RUN_INSTALL -> { ... } // Fresh installation
    FirstRunStatus.FIRST_RUN_VERSION -> { ... } // Application upgraded
    else -> { ... } // FirstRunStatus.NORMAL_RUN - Normal application run 
  }
  ...
  
  ```
