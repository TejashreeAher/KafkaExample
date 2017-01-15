logLevel := Level.Warn
resolvers += "XING Public Repositories" at "http://nexus.dc.xing.com/content/repositories/releases"

// com.sun.jdmk:jmxtools:1.2.1, com.sun.jmx:jmxri:1.2.1
resolvers += "XING 3rd party Repositories" at "http://nexus.dc.xing.com/content/repositories/thirdparty"

// javax.jms:jms:1.1
resolvers += "XING Datanucleus Repositories" at "http://nexus.dc.xing.com/content/repositories/datanucleus/"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")