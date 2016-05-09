# akka-http-argonaut
Akka-http-argonaut is a simple library providing akka-http bindings to [argonaut.io](http://argonaut.io) for Marshalling/Unmarshalling.

## Add it to your project
In order to add *akka-htt-argonaut* to your project simply include the following dependency:

```"io.github.nivox" %% "akka-http-argonaut" % "0.2"```

The library is built against the following scala versions:

* 2.11.7
* 2.10.6

In order for sbt to resolve the dependency add the following resolver:

```"Akka-Http-Argonaut Bintray Repo" at "http://dl.bintray.com/nivox/maven"```

or

```Resolver.bintrayRepo("nivox", "maven")```

## Use it

Just import `io.github.nivox.akka.http.argonaut.ArgonautSupport._` everywhere you need to marshal/unmarshal to/from an HttpEntity.



