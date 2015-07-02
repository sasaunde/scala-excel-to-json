package core

//import com.unboundid.ldap.sdk.LDAPConnection
//import com.unboundid.ldap.sdk.LDAPConnectionOptions

trait Ldap  {

   //val LDAPConnectionOptions = new LDAPConnectionOptions()
  
    val host = "corporatedirectory.capgemini.com"
     
    val port = 389
    
//    def start() : LDAPConnection = {
//      
//      new LDAPConnection(host, port)
//    }
//  
//    def end(conn : LDAPConnection) = conn.close()
//  
//    def start(): InMemoryDirectoryServer = {
//    val config = new InMemoryDirectoryServerConfig("dc=org");
//    val listenerConfig = new InMemoryListenerConfig("test", null, 12345, null, null, null);
//    config.setListenerConfigs(listenerConfig);
//    config.setSchema(null); // do not check (attribute) schema
//    val server = new InMemoryDirectoryServer(config);
//    server.startListening();
//...
//    server.add("dn: cn=user@test.com,dc=staticsecurity,dc=geomajas,dc=org", "objectClass: person", "locale: nl_BE",
//      "sn: NormalUser", "givenName: Joe", "memberOf: cn=testgroup,dc=roles,dc=geomajas,dc=org", "userPassword: password");
//    server.add("dn: cn=admin@test.com,dc=staticsecurity,dc=geomajas,dc=org", "objectClass: person", "locale: nl_BE",
//      "sn: Administrator", "givenName: Cindy", "memberOf: cn=testgroup,dc=roles,dc=geomajas,dc=org", "userPassword: password");
// 
//    server
//  }
  
}