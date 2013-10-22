# enRoute Blog Manager — JPA Provider
This bundle implements the enRoute Blog Manager API on JPA as a 
demonstration of JPA's capabilities on OSGi. The bundle uses
the [osgi.bundles managed JPA][1] model to get an Entity Manager.

## Configuration
The provider needs an Entity Manager setup, and thus also a Data Source to use. 

_${Bundle-SymbolicName}-${Bundle-Version}_

[1]: http://jpm4j.org/#!/p/osgi/osgi.jpa.managed.aux?tab=readme.