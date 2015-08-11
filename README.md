Demonstration of upickle exploding (in the real project, after I change the `model` project and incrementally compile,
 it will often start working until the next clean).

Could possibly be scala-js related and not really upickle, since it sometimes works.

upickle 0.3.5 on Scala 2.11.7 with JDK8 on OSX

output:

```
> sbt clean main/run
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=256M; support was removed in 8.0
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=512M; support was removed in 8.0
[info] Loading global plugins from /Users/steven/.sbt/0.13/plugins
[info] Loading project definition from /Users/steven/dev/upickle-boom/project
[info] Set current project to upickle-boom (in build file:/Users/steven/dev/upickle-boom/)
[success] Total time: 0 s, completed Aug 11, 2015 4:34:12 PM
[info] Updating {file:/Users/steven/dev/upickle-boom/}modelJVM...
[info] Resolving jline#jline;2.12.1 ...
[info] Done updating.
[info] Updating {file:/Users/steven/dev/upickle-boom/}main...
[info] Resolving jline#jline;2.12.1 ...
[info] Done updating.
[info] Compiling 1 Scala source to /Users/steven/dev/upickle-boom/model/.jvm/target/scala-2.11/classes...
[info] Compiling 1 Scala source to /Users/steven/dev/upickle-boom/main/target/scala-2.11/classes...
[info] Running boom.Test 

This seems to always work:

AHolder1(MyThing(2b4449e4-049e-4f14-8f42-8dc3bd908632,24c3e2dd-01b2-41ae-89f7-c522334a524b,name,None,Map(a -> b),00069f2b-d7d4-4548-8480-6695795ad055))

This seems to always fail:

[error] (run-main-0) upickle.Invalid$Data
upickle.Invalid$Data
	at upickle.GeneratedUtil$class.mapToArray(GeneratedUtil.scala:44)
	at upickle.default$.mapToArray(Api.scala:25)
	at upickle.Implicits$$anonfun$CaseR$1.applyOrElse(Implicits.scala:82)
	at upickle.Implicits$$anonfun$CaseR$1.applyOrElse(Implicits.scala:81)
	at scala.runtime.AbstractPartialFunction.apply(AbstractPartialFunction.scala:36)
	at upickle.Types$Reader$$anonfun$read$1.applyOrElse(Types.scala:93)
	at upickle.Types$Reader$$anonfun$read$1.applyOrElse(Types.scala:93)
	at scala.PartialFunction$OrElse.apply(PartialFunction.scala:167)
	at boom.model.PickleRegistry$.unpickleJs(Model.scala:53)
	at boom.model.Holder$$anonfun$createReader$1.applyOrElse(Model.scala:75)
	at boom.model.Holder$$anonfun$createReader$1.applyOrElse(Model.scala:74)
	at scala.runtime.AbstractPartialFunction.apply(AbstractPartialFunction.scala:36)
	at upickle.Types$Reader$$anonfun$read$1.applyOrElse(Types.scala:93)
	at upickle.Types$Reader$$anonfun$read$1.applyOrElse(Types.scala:93)
	at scala.PartialFunction$OrElse.apply(PartialFunction.scala:167)
	at upickle.Types$class.readJs(Types.scala:137)
	at upickle.default$.readJs(Api.scala:25)
	at upickle.Types$class.read(Types.scala:133)
	at upickle.default$.read(Api.scala:25)
	at boom.Test$.delayedEndpoint$boom$Test$1(Test.scala:24)
	at boom.Test$delayedInit$body.apply(Test.scala:11)
	at scala.Function0$class.apply$mcV$sp(Function0.scala:34)
	at scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:12)
	at scala.App$$anonfun$main$1.apply(App.scala:76)
	at scala.App$$anonfun$main$1.apply(App.scala:76)
	at scala.collection.immutable.List.foreach(List.scala:381)
	at scala.collection.generic.TraversableForwarder$class.foreach(TraversableForwarder.scala:35)
	at scala.App$class.main(App.scala:76)
	at boom.Test$.main(Test.scala:11)
	at boom.Test.main(Test.scala)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
[trace] Stack trace suppressed: run last main/compile:run for the full output.
java.lang.RuntimeException: Nonzero exit code: 1
	at scala.sys.package$.error(package.scala:27)
[trace] Stack trace suppressed: run last main/compile:run for the full output.
[error] (main/compile:run) Nonzero exit code: 1
[error] Total time: 9 s, completed Aug 11, 2015 4:34:21 PM
```
