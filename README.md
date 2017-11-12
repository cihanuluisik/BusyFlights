**Technologies**

Spring boot, Spring validation, Spring MVC/REST, java 8, maven
Request objects are added constructors, validation tags etc. No big change.


**TODOs urgent**

- Logging
- Commonalizing request/reponse objects
- Negative/edge scenarios while calling remote services, crazy air/ tough jet should be improved. Only positive 
scenarios tested.
- Too long service calls should probably time out. Can be done through timed future calls - core java.
- Some comments - should/nice to have

**TODOs for better performance**

Calls at the moment are serialized, which might take long overall. They can be parallelized either 
- by submitting to a pool
- or multi async parallel calls through completion stages - java 8, or reactive jx


**TODOs for better reponsiveness**

One step further, making service asyncronous , i.e spring deferred result + http 2 async web, early returning results
can be pumped to the caller, for faster rendering of early results on UI for example

