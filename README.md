# Java EE Security Soteria
Playing around with new JSR-375 (Security API) and _Reference Implementation_ Soteria.

## Installation

### Requirements
* Java 8
* Apache Maven
* PostgreSQL 9.5
* [Payara Full Webserver](https://www.payara.fish/downloads)

### Database Schema
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

### Application Server (Payara / Glassfish)

#### PostgreSQL JDBC Driver
Download [PostgreSQL jdbc driver](https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar) 
and put it into `${PAYARA_HOME}/glassfish/domains/${YOUR_DOMAIN}/lib`

```
curl -o ${PAYARA_HOME}/glassfish/domains/${PAYARA_DOMAIN}/lib/postgresql-41.1.4.jar -L https://jdbc.postgresql.org/download/postgresql-42.1.4.jre6.jar
```

#### JDBC Resource and Pool
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