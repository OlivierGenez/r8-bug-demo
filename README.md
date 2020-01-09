# r8-bug-demo

This sample demonstrates that an application shrunk with R8 can crash at runtime with an `AbstractMethodError` if the following conditions are met:

- an interface is defined which contains a nullable parameter

```
interface MainPresenterContract {
    fun onCrashButtonClick(unusedParameter: String?)
}
```

- a parent class defines a function with the same signature as the interface method containing a nullable parameter

```
abstract class BasePresenter {

    fun onCrashButtonClick(unusedParameter: String?) {
        Log.d("BasePresenter", "Crash!")
    }
}
```

- a child class inherits from the previously defined parent class and implements the previously defined interface; since the parent class already implements a method with the correct signature, it doesn't need to override the corresponding method to implement the interface

```
class MainPresenter : BasePresenter(), MainPresenterContract
```

- at runtime, the interface method is called via a variable of the interface type:

```
class MainActivity : AppCompatActivity() {

    private val presenter: MainPresenterContract = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        // ...

        crashButton.setOnClickListener {
            // This method invocation will crash the app
            presenter.onCrashButtonClick("Crash!")
        }
    }
}

```

## How to use this sample

1. Run the sample
1. Tap the _Crash!_ button
1. Observe that the app crashes with the following error:

```
java.lang.AbstractMethodError: abstract method "void com.example.r8bugdemo.MainPresenterContract.a(java.lang.String)"
        at com.example.r8bugdemo.MainActivity$onCreate$1.onClick(:16)
        at android.view.View.performClick(View.java:6597)
        at android.view.View.performClickInternal(View.java:6574)
        at android.view.View.access$3100(View.java:778)
        at android.view.View$PerformClick.run(View.java:25885)
        at android.os.Handler.handleCallback(Handler.java:873)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:193)
        at android.app.ActivityThread.main(ActivityThread.java:6669)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
```

## Workarounds

### Workaround #1 : Explicitly override the method in the child class

```
abstract class BasePresenter {
    // Make this method open so it can be overriden
    open fun onCrashButtonClick(unusedParameter: String?) {
        Log.d("BasePresenter", "Crash!")
    }
}

class MainPresenter : BasePresenter(), MainPresenterContract {

    override fun onCrashButtonClick(unusedParameter: String?) {
        Log.d("BasePresenter", "This will work!")
    }
}
```

### Workaround #2 : Use the method parameter

```
abstract class BasePresenter {

    fun onCrashButtonClick(unusedParameter: String?) {
        val useThisParameterAnyway = unusedParameter
        Log.d("BasePresenter", "This will work!")
    }
}
```
