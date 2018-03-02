# Java EE Security Soteria
Playing around with new JSR-375 (Security API) and _Reference Implementation_ Soteria.

## Installation

### Requirements
* Java 8
* Apache Maven
* Database
  * PostgreSQL 9 and above
  * MySQL or MariaDB 5 and above
* Full Java EE Server
  * [Payara Full](https://www.payara.fish/downloads)
  * [Wildfly](http://wildfly.org/downloads/)

### PostgreSQL

#### Database Schema


* Prepare user and database on PostgreSQL.

```
CREATE USER demo WITH PASSWORD 'password';
CREATE DATABASE soteriadb OWNER demo ENCODING 'UTF-8';
GRANT ALL PRIVILEGES ON DATABASE soteriadb TO demo;
```

* Execute `schema.sql`.

```
psql -U demo -d soteriadb -a -f ./src/main/resources/db/schema.sql
```

#### Application Server 

##### Payara / Glassfish

###### PostgreSQL JDBC Driver
Download [PostgreSQL jdbc driver](https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar) 
and put it into `${PAYARA_HOME}/glassfish/domains/${YOUR_DOMAIN}/lib`

```
curl -o ${PAYARA_HOME}/glassfish/domains/${PAYARA_DOMAIN}/lib/postgresql-41.1.4.jar -L https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar
```

###### JDBC Resource and Pool
Make sure working directory on `${PAYARA_HOME}/bin`.

* Start Application Server.

```
./asadmin start-domain ${PAYARA_DOMAIN}
```

* Create JDBC Pool.

```
./asadmin create-jdbc-connection-pool \
--datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
--restype javax.sql.ConnectionPoolDataSource \
--property User=demo:Password=password:DatabaseName=soteriadb:ServerName=localhost:PortNumber=5432 Soteria
```

* Create JDBC Resource.

```
./asadmin create-jdbc-resource --connectionpoolid Soteria jdbc/soteria
```

##### Wildfly

We will refer to the WildFly 10/11 modules folder structure as `${WildFly_Modules}`.
E.g. `wildfly-11.0.0.Final/modules/` 
And to the Wildfly configuration folder as `${WildFly_Config}`. 
E.g. `wildfly-11.0.0.Final/standalone/configuration/`


###### PostgreSQL JDBC Driver

Download [PostgreSQL jdbc driver](https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar) 
and put it into `${WildFly_Modules}/org/postgresql/main` after you have created this folder if it does not exist.

```
curl -o ${WildFly_Modules}/org/postgresql/main/postgresql-41.1.4.jar -L https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar
```

In the same folder `${WildFly_Modules}/org/postgresql/main` create a module.xml file.
Copy the <module> element below and paste it into your module.xml file:
```
<module xmlns="urn:jboss:module:1.0" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-41.1.4.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```

You may also use the Wildfly CLI for defining the module:
```
./jboss-cli.sh

embed-server --std-out=echo --server-config=standalone.xml

module add --name=org.postgres --resources=/tmp/postgresql-42.1.4.jar --dependencies=javax.api,javax.transaction.api

exit
```
Assuming you have downloaded the PostgreSQL driver via
```
curl -o /tmp/postgresql-41.1.4.jar -L https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar
```

###### JDBC Resource
In the folder `${WildFly_Config}` modify the `standalone.xml` file (or others like `standalone-full.xml` depending on the profile of your choice) by adding the following definitions:
```
<subsystem xmlns="urn:jboss:domain:datasources:4.0">
    <datasources>
       ...
        <datasource jndi-name="java:/jdbc/soteria" pool-name="Soteria" enabled="true" use-java-context="true">
            <connection-url>jdbc:postgresql://localhost:5432/soteriadb</connection-url>
            <driver>postgres</driver>
            <security>
                <user-name>demo</user-name>
                <password>password</password>
            </security>
        </datasource>
        ...
        <drivers>
            ...
            <driver name="postgres" module="org.postgres">
                <driver-class>org.postgresql.Driver</driver-class>
                <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
            </driver>
        </drivers>
    </datasources>
</subsystem>
```

### MySQL / MariaDB

#### Database Schema


* Prepare user and database on MySQL / MariaDB.

```
CREATE DATABASE soteriadb;
GRANT USAGE ON `soteriadb`.* TO 'demo'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON `soteriadb`.* TO 'demo'@localhost IDENTIFIED BY 'password';
```

* Execute `schema.sql`.

```
mysql -u demo soteriadb < ./src/main/resources/db/schema.sql
```

#### Application Server 

##### Payara / Glassfish

###### MySQL JDBC Driver

Download [MySQL jdbc driver](https://dev.mysql.com/downloads/connector/j/) 
e.g. `mysql-connector-java-5.1.45.tar.gz`, extract the archive to a temporary folder and copy `mysql-connector-java-5.1.45-bin.jar`
into `${PAYARA_HOME}/glassfish/domains/${YOUR_DOMAIN}/lib`


###### JDBC Resource and Pool
Make sure working directory on `${PAYARA_HOME}/bin`.

* Start Application Server.

```
./asadmin start-domain ${PAYARA_DOMAIN}
```

* Create JDBC Pool.

```
./asadmin create-jdbc-connection-pool \
--datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource \
--restype javax.sql.ConnectionPoolDataSource \
--property User=demo:Password=password:DatabaseName=soteriadb:ServerName=localhost:PortNumber=5432 Soteria
```

* Create JDBC Resource.

```
./asadmin create-jdbc-resource --connectionpoolid Soteria jdbc/soteria
```

##### Wildfly

We will refer to the WildFly 10/11 modules folder structure as `${WildFly_Modules}`.
E.g. `wildfly-11.0.0.Final/modules/` 
And to the Wildfly configuration folder as `${WildFly_Config}`. 
E.g. `wildfly-11.0.0.Final/standalone/configuration/`


###### MySQL JDBC Driver

Download [MySQL jdbc driver](https://dev.mysql.com/downloads/connector/j/) 
e.g. `mysql-connector-java-5.1.45.tar.gz`, extract the archive to a temporary folder and copy `mysql-connector-java-5.1.45-bin.jar`
into `${WildFly_Modules}/com/mysql/main` after you have created this folder if it does not exist.

In the same folder `${WildFly_Modules}/com/mysql/main` create a module.xml file.
Copy the <module> element below and paste it into your module.xml file:
```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="com.mysql">
	<resources>
		<resource-root path="mysql-connector-java-5.1.45-bin.jar" />
	</resources>
	<dependencies>
		<module name="javax.api" />
		<module name="javax.transaction.api" />
	</dependencies>
</module>
```

You may also use the Wildfly CLI for defining the module:
```
./jboss-cli.sh

embed-server --std-out=echo --server-config=standalone.xml

module add --name=org.postgres --resources=/tmp/mysql-connector-java-5.1.45/mysql-connector-java-5.1.45-bin.jar --dependencies=javax.api,javax.transaction.api

exit
```
Assuming you have downloaded and extracted the MySQL driver to `/tmp/mysql-connector-java-5.1.45`.

###### JDBC Resource
In the folder `${WildFly_Config}` modify the a standalone-full.xml file by adding the following definitions:
```
<subsystem xmlns="urn:jboss:domain:datasources:4.0">    
 <datasources>
  <datasource jndi-name="java:/jdbc/soteria" pool-name="Soteria">
      <connection-url>jdbc:mysql://localhost:3306/soteriadb</connection-url>
      <driver>mysql</driver>
      <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
      <pool>
          <min-pool-size>10</min-pool-size>
          <max-pool-size>100</max-pool-size>
          <prefill>true</prefill>
      </pool>
      <security>
          <user-name>demo</user-name>
          <password>password</password>
      </security>
      <statement>
          <prepared-statement-cache-size>32</prepared-statement-cache-size>
      </statement>
  </datasource>
   ...   
   <drivers>
      ...
      <driver name="mysql" module="com.mysql">
          <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
      </driver>
   </drivers>
 </datasources>
 ...
</subsystem>
```


### Compile and Package
Being Maven centric, compile and package can be done:

```
mvn clean compile
mvn clean package
```

To simplified it can be done:

```
mvn clean install
```

Once you have the war file, you can deploy it.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would
like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
