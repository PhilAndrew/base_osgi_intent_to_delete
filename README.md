
## Readme

### How to build?

sbt featuresFile osgiBundle

Then take the file target/scala-2.11/features.xml and place it in the deploy directory of Karaf.

In Karaf Shell type

features:list | grep _osgi

You can see the base_osgi

Then feature:install base_osgi

