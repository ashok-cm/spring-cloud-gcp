## Spring Data Cloud Spanner

[Spring Data](https://projects.spring.io/spring-data/) is an abstraction
for storing and retrieving POJOs in numerous storage technologies.
Spring Cloud GCP adds Spring Data support for [Google Cloud
Spanner](https://cloud.google.com/spanner/).

Maven coordinates for this module only, using [Spring Cloud GCP
BOM](getting-started.xml#bill-of-materials):

``` xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>spring-cloud-gcp-data-spanner</artifactId>
</dependency>
```

Gradle coordinates:

    dependencies {
        implementation("com.google.cloud:spring-cloud-gcp-data-spanner")
    }

We provide a [Spring Boot Starter for Spring Data
Spanner](../spring-cloud-gcp-starters/spring-cloud-gcp-starter-data-spanner),
with which you can leverage our recommended auto-configuration setup. To
use the starter, see the coordinates see below.

Maven:

``` xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>spring-cloud-gcp-starter-data-spanner</artifactId>
</dependency>
```

Gradle:

    dependencies {
        implementation("com.google.cloud:spring-cloud-gcp-starter-data-spanner")
    }

This setup takes care of bringing in the latest compatible version of
Cloud Java Cloud Spanner libraries as well.

### Configuration

To setup Spring Data Cloud Spanner, you have to configure the following:

  - Setup the connection details to Google Cloud Spanner.

  - Enable Spring Data Repositories (optional).

#### Cloud Spanner settings

You can use the Spring Boot Starter for Spring Data Spanner to
autoconfigure Google Cloud Spanner in your Spring application. It
contains all the necessary setup that makes it easy to authenticate with
your Google Cloud project. The following configuration options are
available:

|                                                                     |                                                                                                                                                                                                                             |          |                                                  |
| ------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------- | ------------------------------------------------ |
| Name                                                                | Description                                                                                                                                                                                                                 | Required | Default value                                    |
| `spring.cloud.gcp.spanner.enabled`                                  | Enables the Cloud Spanner client                                                                                                                                                                                            | No       | `true`                                           |
| `spring.cloud.gcp.spanner.instance-id`                              | Cloud Spanner instance to use                                                                                                                                                                                               | Yes      |                                                  |
| `spring.cloud.gcp.spanner.database`                                 | Cloud Spanner database to use                                                                                                                                                                                               | Yes      |                                                  |
| `spring.cloud.gcp.spanner.project-id`                               | GCP project ID where the Google Cloud Spanner API is hosted, if different from the one in the [Spring Cloud GCP Core Module](#spring-cloud-gcp-core)                                                                        | No       |                                                  |
| `spring.cloud.gcp.spanner.credentials.location`                     | OAuth2 credentials for authenticating with the Google Cloud Spanner API, if different from the ones in the [Spring Cloud GCP Core Module](#spring-cloud-gcp-core)                                                           | No       |                                                  |
| `spring.cloud.gcp.spanner.credentials.encoded-key`                  | Base64-encoded OAuth2 credentials for authenticating with the Google Cloud Spanner API, if different from the ones in the [Spring Cloud GCP Core Module](#spring-cloud-gcp-core)                                            | No       |                                                  |
| `spring.cloud.gcp.spanner.credentials.scopes`                       | [OAuth2 scope](https://developers.google.com/identity/protocols/googlescopes) for Spring Cloud GCP Cloud Spanner credentials                                                                                                | No       | <https://www.googleapis.com/auth/spanner.data>   |
| `spring.cloud.gcp.spanner.createInterleavedTableDdlOnDeleteCascade` | If `true`, then schema statements generated by `SpannerSchemaUtils` for tables with interleaved parent-child relationships will be "ON DELETE CASCADE". The schema for the tables will be "ON DELETE NO ACTION" if `false`. | No       | `true`                                           |
| `spring.cloud.gcp.spanner.numRpcChannels`                           | Number of gRPC channels used to connect to Cloud Spanner                                                                                                                                                                    | No       | 4 - Determined by Cloud Spanner client library   |
| `spring.cloud.gcp.spanner.prefetchChunks`                           | Number of chunks prefetched by Cloud Spanner for read and query                                                                                                                                                             | No       | 4 - Determined by Cloud Spanner client library   |
| `spring.cloud.gcp.spanner.minSessions`                              | Minimum number of sessions maintained in the session pool                                                                                                                                                                   | No       | 0 - Determined by Cloud Spanner client library   |
| `spring.cloud.gcp.spanner.maxSessions`                              | Maximum number of sessions session pool can have                                                                                                                                                                            | No       | 400 - Determined by Cloud Spanner client library |
| `spring.cloud.gcp.spanner.maxIdleSessions`                          | Maximum number of idle sessions session pool will maintain                                                                                                                                                                  | No       | 0 - Determined by Cloud Spanner client library   |
| `spring.cloud.gcp.spanner.writeSessionsFraction`                    | Fraction of sessions to be kept prepared for write transactions                                                                                                                                                             | No       | 0.2 - Determined by Cloud Spanner client library |
| `spring.cloud.gcp.spanner.keepAliveIntervalMinutes`                 | How long to keep idle sessions alive                                                                                                                                                                                        | No       | 30 - Determined by Cloud Spanner client library  |
| `spring.cloud.gcp.spanner.failIfPoolExhausted`                      | If all sessions are in use, fail the request by throwing an exception. Otherwise, by default, block until a session becomes available.                                                                                      | No       | `false`                                          |
| `spring.cloud.gcp.spanner.emulator.enabled`                         | Enables the usage of an emulator. If this is set to true, then you should set the `spring.cloud.gcp.spanner.emulator-host` to the host:port of your locally running emulator instance.                                      | No       | `false`                                          |
| `spring.cloud.gcp.spanner.emulator-host`                            | The host and port of the Spanner emulator; can be overridden to specify connecting to an already-running [Spanner emulator](https://cloud.google.com/spanner/docs/emulator#installing_and_running_the_emulator) instance.   | No       | `localhost:9010`                                 |

<div class="note">

For further customization of the client library `SpannerOptions`,
provide a bean implementing `SpannerOptionsCustomizer`, with a single
method that accepts a `SpannerOptions.Builder` and modifies it as
necessary.

</div>

#### Repository settings

Spring Data Repositories can be configured via the
`@EnableSpannerRepositories` annotation on your main `@Configuration`
class. With our Spring Boot Starter for Spring Data Cloud Spanner,
`@EnableSpannerRepositories` is automatically added. It is not required
to add it to any other class, unless there is a need to override finer
grain configuration parameters provided by
[`@EnableSpannerRepositories`](https://github.com/GoogleCloudPlatform/spring-cloud-gcp/blob/main/spring-cloud-gcp-data-spanner/src/main/java/com/google/cloud/spring/data/spanner/repository/config/EnableSpannerRepositories.java).

#### Autoconfiguration

Our Spring Boot autoconfiguration creates the following beans available
in the Spring application context:

  - an instance of `SpannerTemplate`

  - an instance of `SpannerDatabaseAdminTemplate` for generating table
    schemas from object hierarchies and creating and deleting tables and
    databases

  - an instance of all user-defined repositories extending
    `SpannerRepository`, `CrudRepository`, `PagingAndSortingRepository`,
    when repositories are enabled

  - an instance of `DatabaseClient` from the Google Cloud Java Client
    for Spanner, for convenience and lower level API access

### Object Mapping

Spring Data Cloud Spanner allows you to map domain POJOs to Cloud
Spanner tables via annotations:

``` java
@Table(name = "traders")
public class Trader {

    @PrimaryKey
    @Column(name = "trader_id")
    String traderId;

    String firstName;

    String lastName;

    @NotMapped
    Double temporaryNumber;
}
```

Spring Data Cloud Spanner will ignore any property annotated with
`@NotMapped`. These properties will not be written to or read from
Spanner.

#### Constructors

Simple constructors are supported on POJOs. The constructor arguments
can be a subset of the persistent properties. Every constructor argument
needs to have the same name and type as a persistent property on the
entity and the constructor should set the property from the given
argument. Arguments that are not directly set to properties are not
supported.

``` java
@Table(name = "traders")
public class Trader {
    @PrimaryKey
    @Column(name = "trader_id")
    String traderId;

    String firstName;

    String lastName;

    @NotMapped
    Double temporaryNumber;

    public Trader(String traderId, String firstName) {
        this.traderId = traderId;
        this.firstName = firstName;
    }
}
```

#### Table

The `@Table` annotation can provide the name of the Cloud Spanner table
that stores instances of the annotated class, one per row. This
annotation is optional, and if not given, the name of the table is
inferred from the class name with the first character uncapitalized.

##### SpEL expressions for table names

In some cases, you might want the `@Table` table name to be determined
dynamically. To do that, you can use [Spring Expression
Language](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions).

For example:

``` java
@Table(name = "trades_#{tableNameSuffix}")
public class Trade {
    // ...
}
```

The table name will be resolved only if the `tableNameSuffix` value/bean
in the Spring application context is defined. For example, if
`tableNameSuffix` has the value "123", the table name will resolve to
`trades_123`.

#### Primary Keys

For a simple table, you may only have a primary key consisting of a
single column. Even in that case, the `@PrimaryKey` annotation is
required. `@PrimaryKey` identifies the one or more ID properties
corresponding to the primary key.

Spanner has first class support for composite primary keys of multiple
columns. You have to annotate all of your POJO’s fields that the primary
key consists of with `@PrimaryKey` as below:

``` java
@Table(name = "trades")
public class Trade {
    @PrimaryKey(keyOrder = 2)
    @Column(name = "trade_id")
    private String tradeId;

    @PrimaryKey(keyOrder = 1)
    @Column(name = "trader_id")
    private String traderId;

    private String action;

    private BigDecimal price;

    private Double shares;

    private String symbol;
}
```

The `keyOrder` parameter of `@PrimaryKey` identifies the properties
corresponding to the primary key columns in order, starting with 1 and
increasing consecutively. Order is important and must reflect the order
defined in the Cloud Spanner schema. In our example the DDL to create
the table and its primary key is as follows:

``` sql
CREATE TABLE trades (
    trader_id STRING(MAX),
    trade_id STRING(MAX),
    action STRING(15),
    symbol STRING(10),
    price NUMERIC,
    shares FLOAT64
) PRIMARY KEY (trader_id, trade_id)
```

Spanner does not have automatic ID generation. For most use-cases,
sequential IDs should be used with caution to avoid creating data
hotspots in the system. Read [Spanner Primary Keys
documentation](https://cloud.google.com/spanner/docs/schema-and-data-model#primary_keys)
for a better understanding of primary keys and recommended practices.

#### Columns

All accessible properties on POJOs are automatically recognized as a
Cloud Spanner column. Column naming is generated by the
`PropertyNameFieldNamingStrategy` by default defined on the
`SpannerMappingContext` bean. The `@Column` annotation optionally
provides a different column name than that of the property and some
other settings:

  - `name` is the optional name of the column

  - `spannerTypeMaxLength` specifies for `STRING` and `BYTES` columns
    the maximum length. This setting is only used when generating DDL
    schema statements based on domain types.

  - `nullable` specifies if the column is created as `NOT NULL`. This
    setting is only used when generating DDL schema statements based on
    domain types.

  - `spannerType` is the Cloud Spanner column type you can optionally
    specify. If this is not specified then a compatible column type is
    inferred from the Java property type.

  - `spannerCommitTimestamp` is a boolean specifying if this property
    corresponds to an auto-populated commit timestamp column. Any value
    set in this property will be ignored when writing to Cloud Spanner.

#### Embedded Objects

If an object of type `B` is embedded as a property of `A`, then the
columns of `B` will be saved in the same Cloud Spanner table as those of
`A`.

If `B` has primary key columns, those columns will be included in the
primary key of `A`. `B` can also have embedded properties. Embedding
allows reuse of columns between multiple entities, and can be useful for
implementing parent-child situations, because Cloud Spanner requires
child tables to include the key columns of their parents.

For example:

``` java
class X {
  @PrimaryKey
  String grandParentId;

  long age;
}

class A {
  @PrimaryKey
  @Embedded
  X grandParent;

  @PrimaryKey(keyOrder = 2)
  String parentId;

  String value;
}

@Table(name = "items")
class B {
  @PrimaryKey
  @Embedded
  A parent;

  @PrimaryKey(keyOrder = 2)
  String id;

  @Column(name = "child_value")
  String value;
}
```

Entities of `B` can be stored in a table defined as:

``` sql
CREATE TABLE items (
    grandParentId STRING(MAX),
    parentId STRING(MAX),
    id STRING(MAX),
    value STRING(MAX),
    child_value STRING(MAX),
    age INT64
) PRIMARY KEY (grandParentId, parentId, id)
```

Note that the following restrictions apply when you use embedded
objects:

  - Embedded properties' column names must all be unique.

  - Embedded properties must not be passed through a constructor and the
    property must be mutable; otherwise you’ll get an error, such as
    `SpannerDataException: Column not found`. Be careful about this
    restriction when you use Kotlin’s data class to hold an embedded
    property.

#### Relationships

Spring Data Cloud Spanner supports parent-child relationships using the
Cloud Spanner [parent-child interleaved table
mechanism](https://cloud.google.com/spanner/docs/schema-and-data-model#creating-interleaved-tables).
Cloud Spanner interleaved tables enforce the one-to-many relationship
and provide efficient queries and operations on entities of a single
domain parent entity. These relationships can be up to 7 levels deep.
Cloud Spanner also provides automatic cascading delete or enforces the
deletion of child entities before parents.

While one-to-one and many-to-many relationships can be implemented in
Cloud Spanner and Spring Data Cloud Spanner using constructs of
interleaved parent-child tables, only the parent-child relationship is
natively supported.

For example, the following Java entities:

``` java
@Table(name = "Singers")
class Singer {
  @PrimaryKey
  long SingerId;

  String FirstName;

  String LastName;

  byte[] SingerInfo;

  @Interleaved
  List<Album> albums;
}

@Table(name = "Albums")
class Album {
  @PrimaryKey
  long SingerId;

  @PrimaryKey(keyOrder = 2)
  long AlbumId;

  String AlbumTitle;
}
```

These classes can correspond to an existing pair of interleaved tables.
The `@Interleaved` annotation may be applied to `Collection` properties
and the inner type is resolved as the child entity type. The schema
needed to create them can also be generated using the
`SpannerSchemaUtils` and run by using the
`SpannerDatabaseAdminTemplate`:

``` java
@Autowired
SpannerSchemaUtils schemaUtils;

@Autowired
SpannerDatabaseAdminTemplate databaseAdmin;
...

// Get the create statmenets for all tables in the table structure rooted at Singer
List<String> createStrings = this.schemaUtils.getCreateTableDdlStringsForInterleavedHierarchy(Singer.class);

// Create the tables and also create the database if necessary
this.databaseAdmin.executeDdlStrings(createStrings, true);
```

The `createStrings` list contains table schema statements using column
names and types compatible with the provided Java type and any resolved
child relationship types contained within based on the configured custom
converters.

``` sql
CREATE TABLE Singers (
  SingerId   INT64 NOT NULL,
  FirstName  STRING(1024),
  LastName   STRING(1024),
  SingerInfo BYTES(MAX),
) PRIMARY KEY (SingerId);

CREATE TABLE Albums (
  SingerId     INT64 NOT NULL,
  AlbumId      INT64 NOT NULL,
  AlbumTitle   STRING(MAX),
) PRIMARY KEY (SingerId, AlbumId),
  INTERLEAVE IN PARENT Singers ON DELETE CASCADE;
```

The `ON DELETE CASCADE` clause indicates that Cloud Spanner will delete
all Albums of a singer if the Singer is deleted. The alternative is `ON
DELETE NO ACTION`, where a Singer cannot be deleted until all of its
Albums have already been deleted. When using `SpannerSchemaUtils` to
generate the schema strings, the
`spring.cloud.gcp.spanner.createInterleavedTableDdlOnDeleteCascade`
boolean setting determines if these schema are generated as `ON DELETE
CASCADE` for `true` and `ON DELETE NO ACTION` for `false`.

Cloud Spanner restricts these relationships to 7 child layers. A table
may have multiple child tables.

On updating or inserting an object to Cloud Spanner, all of its
referenced children objects are also updated or inserted in the same
request, respectively. On read, all of the interleaved child rows are
also all read.

##### Lazy Fetch

`@Interleaved` properties are retrieved eagerly by default, but can be
fetched lazily for performance in both read and write:

``` java
@Interleaved(lazy = true)
List<Album> albums;
```

Lazily-fetched interleaved properties are retrieved upon the first
interaction with the property. If a property marked for lazy fetching is
never retrieved, then it is also skipped when saving the parent entity.

If used inside a transaction, subsequent operations on lazily-fetched
properties use the same transaction context as that of the original
parent entity.

##### Declarative Filtering with `@Where`

The `@Where` annotation could be applied to an entity class or to an
interleaved property. This annotation provides an SQL where clause that
will be applied at the fetching of interleaved collections or the entity
itself.

Let’s say we have an `Agreement` with a list of `Participants` which
could be assigned to it. We would like to fetch a list of currently
active participants. For security reasons, all records should remain in
the database forever, even if participants become inactive. That can be
easily achieved with the `@Where` annotation, which is demonstrated by
this example:

``` java
@Table(name = "participants")
public class Participant {
  //...
  boolean active;
  //...
}

@Table(name = "agreements")
public class Agreement {
  //...
  @Interleaved
  @Where("active = true")
  List<Participant> participants;
  Person person;
  //...
}
```

#### Supported Types

Spring Data Cloud Spanner natively supports the following types for
regular fields but also utilizes custom converters (detailed in
following sections) and dozens of pre-defined Spring Data custom
converters to handle other common Java types.

Natively supported types:

  - `com.google.cloud.ByteArray`

  - `com.google.cloud.Date`

  - `com.google.cloud.Timestamp`

  - `java.lang.Boolean`, `boolean`

  - `java.lang.Double`, `double`

  - `java.lang.Long`, `long`

  - `java.lang.Integer`, `int`

  - `java.lang.String`

  - `double[]`

  - `long[]`

  - `boolean[]`

  - `java.util.Date`

  - `java.time.Instant`

  - `java.sql.Date`

  - `java.time.LocalDate`

  - `java.time.LocalDateTime`

#### JSON fields

Spanner supports `JSON` type for columns. `JSON` columns are mapped to
custom POJOs annotated with `@Column(spannerType = TypeCode.JSON)`.
Read, write and query with custom SQL query are supported for JSON
annotated fields.

<div class="note">

The default Gson instance used to convert to and from JSON
representation can be customized by providing a bean of type `Gson`.

</div>

``` java
@Table(name = "traders")
public class Trader {

    @PrimaryKey
    @Column(name = "trader_id")
    String traderId;

    @Column(spannerType = TypeCode.JSON)
    Details details;
}

public class Details {
    String name;
    String affiliation;
    Boolean isActive;
}
```

#### Lists

Spanner supports `ARRAY` types for columns. `ARRAY` columns are mapped
to `List` fields in POJOs.

Example:

``` java
List<Double> curve;
```

The types inside the lists can be any singular property type.

#### Lists of Structs

Cloud Spanner queries can [construct STRUCT
values](https://cloud.google.com/spanner/docs/query-syntax#using-structs-with-select)
that appear as columns in the result. Cloud Spanner requires STRUCT
values appear in ARRAYs at the root level: `SELECT ARRAY(SELECT STRUCT(1
as val1, 2 as val2)) as pair FROM Users`.

Spring Data Cloud Spanner will attempt to read the column STRUCT values
into a property that is an `Iterable` of an entity type compatible with
the schema of the column STRUCT value.

For the previous array-select example, the following property can be
mapped with the constructed `ARRAY<STRUCT>` column: `List<TwoInts>
pair;` where the `TwoInts` type is defined:

``` java
class TwoInts {

  int val1;

  int val2;
}
```

#### Custom types

Custom converters can be used to extend the type support for user
defined types.

1.  Converters need to implement the
    `org.springframework.core.convert.converter.Converter` interface in
    both directions.

2.  The user defined type needs to be mapped to one of the basic types
    supported by Spanner:
    
      - `com.google.cloud.ByteArray`
    
      - `com.google.cloud.Date`
    
      - `com.google.cloud.Timestamp`
    
      - `java.lang.Boolean`, `boolean`
    
      - `java.lang.Double`, `double`
    
      - `java.lang.Long`, `long`
    
      - `java.lang.String`
    
      - `double[]`
    
      - `long[]`
    
      - `boolean[]`
    
      - `enum` types

3.  An instance of both Converters needs to be passed to a
    `ConverterAwareMappingSpannerEntityProcessor`, which then has to be
    made available as a `@Bean` for `SpannerEntityProcessor`.

For example:

We would like to have a field of type `Person` on our `Trade` POJO:

``` java
@Table(name = "trades")
public class Trade {
  //...
  Person person;
  //...
}
```

Where Person is a simple class:

``` java
public class Person {

  public String firstName;
  public String lastName;

}
```

We have to define the two converters:

``` java
  public class PersonWriteConverter implements Converter<Person, String> {

    @Override
    public String convert(Person person) {
      return person.firstName + " " + person.lastName;
    }
  }

  public class PersonReadConverter implements Converter<String, Person> {

    @Override
    public Person convert(String s) {
      Person person = new Person();
      person.firstName = s.split(" ")[0];
      person.lastName = s.split(" ")[1];
      return person;
    }
  }
```

That will be configured in our `@Configuration` file:

``` java
@Configuration
public class ConverterConfiguration {

    @Bean
    public SpannerEntityProcessor spannerEntityProcessor(SpannerMappingContext spannerMappingContext) {
        return new ConverterAwareMappingSpannerEntityProcessor(spannerMappingContext,
                Arrays.asList(new PersonWriteConverter()),
                Arrays.asList(new PersonReadConverter()));
    }
}
```

Note that `ConverterAwareMappingSpannerEntityProcessor` takes a list of
Converters for write and read operations to support multiple
user-defined types. When there are duplicate Converters for a
user-defined class in the list, it chooses the first matching item in
the lists. This means that, for a user-defined class `U`, write
operations use the first `Converter<U, …​>` from the write Converters
and read operations use the first `Converter<…​, U>` from the read
Converters.

#### Custom Converter for Struct Array Columns

If a `Converter<Struct, A>` is provided, then properties of type
`List<A>` can be used in your entity types.

### Spanner Operations & Template

`SpannerOperations` and its implementation, `SpannerTemplate`, provides
the Template pattern familiar to Spring developers. It provides:

  - Resource management

  - One-stop-shop to Spanner operations with the Spring Data POJO
    mapping and conversion features

  - Exception conversion

Using the `autoconfigure` provided by our Spring Boot Starter for
Spanner, your Spring application context will contain a fully configured
`SpannerTemplate` object that you can easily autowire in your
application:

``` java
@SpringBootApplication
public class SpannerTemplateExample {

    @Autowired
    SpannerTemplate spannerTemplate;

    public void doSomething() {
        this.spannerTemplate.delete(Trade.class, KeySet.all());
        //...
        Trade t = new Trade();
        //...
        this.spannerTemplate.insert(t);
        //...
        List<Trade> tradesByAction = spannerTemplate.findAll(Trade.class);
        //...
    }
}
```

The Template API provides convenience methods for:

  - [Reads](https://cloud.google.com/spanner/docs/reads), and by
    providing SpannerReadOptions and SpannerQueryOptions
    
      - Stale read
    
      - Read with secondary indices
    
      - Read with limits and offsets
    
      - Read with sorting

  - [Queries](https://cloud.google.com/spanner/docs/reads#execute_a_query)

  - DML operations (delete, insert, update, upsert)

  - Partial reads
    
      - You can define a set of columns to be read into your entity

  - Partial writes
    
      - Persist only a few properties from your entity

  - Read-only transactions

  - Locking read-write transactions

#### SQL Query

Cloud Spanner has SQL support for running read-only queries. All the
query related methods start with `query` on `SpannerTemplate`. By using
`SpannerTemplate`, you can run SQL queries that map to POJOs:

``` java
List<Trade> trades = this.spannerTemplate.query(Trade.class, Statement.of("SELECT * FROM trades"));
```

#### Read

Spanner exposes a [Read
API](https://cloud.google.com/spanner/docs/reads) for reading single row
or multiple rows in a table or in a secondary index.

Using `SpannerTemplate` you can run reads, as the following example
shows:

``` java
List<Trade> trades = this.spannerTemplate.readAll(Trade.class);
```

Main benefit of reads over queries is reading multiple rows of a certain
pattern of keys is much easier using the features of the
[`KeySet`](https://github.com/GoogleCloudPlatform/google-cloud-java/blob/main/google-cloud-spanner/src/main/java/com/google/cloud/spanner/KeySet.java)
class.

#### Advanced reads

##### Stale read

All reads and queries are **strong reads** by default. A **strong read**
is a read at a current time and is guaranteed to see all data that has
been committed up until the start of this read. An **exact staleness
read** is read at a timestamp in the past. Cloud Spanner allows you to
determine how current the data should be when you read data. With
`SpannerTemplate` you can specify the `Timestamp` by setting it on
`SpannerQueryOptions` or `SpannerReadOptions` to the appropriate read or
query methods:

Reads:

``` java
// a read with options:
SpannerReadOptions spannerReadOptions = new SpannerReadOptions().setTimestamp(myTimestamp);
List<Trade> trades = this.spannerTemplate.readAll(Trade.class, spannerReadOptions);
```

Queries:

``` java
// a query with options:
SpannerQueryOptions spannerQueryOptions = new SpannerQueryOptions().setTimestamp(myTimestamp);
List<Trade> trades = this.spannerTemplate.query(Trade.class, Statement.of("SELECT * FROM trades"), spannerQueryOptions);
```

You can also read with [**bounded
staleness**](https://cloud.google.com/spanner/docs/timestamp-bounds) by
setting
`.setTimestampBound(TimestampBound.ofMinReadTimestamp(myTimestamp))` on
the query and read options objects. Bounded staleness lets Cloud Spanner
choose any point in time later than or equal to the given
timestampBound, but it cannot be used inside transactions.

##### Read from a secondary index

Using a [secondary
index](https://cloud.google.com/spanner/docs/secondary-indexes) is
available for Reads via the Template API and it is also implicitly
available via SQL for Queries.

The following shows how to read rows from a table using a [secondary
index](https://cloud.google.com/spanner/docs/secondary-indexes) simply
by setting `index` on `SpannerReadOptions`:

``` java
SpannerReadOptions spannerReadOptions = new SpannerReadOptions().setIndex("TradesByTrader");
List<Trade> trades = this.spannerTemplate.readAll(Trade.class, spannerReadOptions);
```

##### Read with offsets and limits

Limits and offsets are only supported by Queries. The following will get
only the first two rows of the query:

``` java
SpannerQueryOptions spannerQueryOptions = new SpannerQueryOptions().setLimit(2).setOffset(3);
List<Trade> trades = this.spannerTemplate.query(Trade.class, Statement.of("SELECT * FROM trades"), spannerQueryOptions);
```

Note that the above is equivalent of running `SELECT * FROM trades
LIMIT 2 OFFSET 3`.

##### Sorting

Reads by keys do not support sorting. However, queries on the Template
API support sorting through standard SQL and also via Spring Data Sort
API:

``` java
List<Trade> trades = this.spannerTemplate.queryAll(Trade.class, Sort.by("action"));
```

If the provided sorted field name is that of a property of the domain
type, then the column name corresponding to that property will be used
in the query. Otherwise, the given field name is assumed to be the name
of the column in the Cloud Spanner table. Sorting on columns of Cloud
Spanner types STRING and BYTES can be done while ignoring case:

``` java
Sort.by(Order.desc("action").ignoreCase())
```

##### Partial read

Partial read is only possible when using Queries. In case the rows
returned by the query have fewer columns than the entity that it will be
mapped to, Spring Data will map the returned columns only. This setting
also applies to nested structs and their corresponding nested POJO
properties.

``` java
List<Trade> trades = this.spannerTemplate.query(Trade.class, Statement.of("SELECT action, symbol FROM trades"),
    new SpannerQueryOptions().setAllowMissingResultSetColumns(true));
```

If the setting is set to `false`, then an exception will be thrown if
there are missing columns in the query result.

##### Summary of options for Query vs Read

|                        |                   |                  |
| ---------------------- | ----------------- | ---------------- |
| Feature                | Query supports it | Read supports it |
| SQL                    | yes               | no               |
| Partial read           | yes               | no               |
| Limits                 | yes               | no               |
| Offsets                | yes               | no               |
| Secondary index        | yes               | yes              |
| Read using index range | no                | yes              |
| Sorting                | yes               | no               |

#### Write / Update

The write methods of `SpannerOperations` accept a POJO and writes all of
its properties to Spanner. The corresponding Spanner table and entity
metadata is obtained from the given object’s actual type.

If a POJO was retrieved from Spanner and its primary key properties
values were changed and then written or updated, the operation will
occur as if against a row with the new primary key values. The row with
the original primary key values will not be affected.

##### Insert

The `insert` method of `SpannerOperations` accepts a POJO and writes all
of its properties to Spanner, which means the operation will fail if a
row with the POJO’s primary key already exists in the table.

``` java
Trade t = new Trade();
this.spannerTemplate.insert(t);
```

##### Update

The `update` method of `SpannerOperations` accepts a POJO and writes all
of its properties to Spanner, which means the operation will fail if the
POJO’s primary key does not already exist in the table.

``` java
// t was retrieved from a previous operation
this.spannerTemplate.update(t);
```

##### Upsert

The `upsert` method of `SpannerOperations` accepts a POJO and writes all
of its properties to Spanner using update-or-insert.

``` java
// t was retrieved from a previous operation or it's new
this.spannerTemplate.upsert(t);
```

##### Partial Update

The update methods of `SpannerOperations` operate by default on all
properties within the given object, but also accept `String[]` and
`Optional<Set<String>>` of column names. If the `Optional` of set of
column names is empty, then all columns are written to Spanner. However,
if the Optional is occupied by an empty set, then no columns will be
written.

``` java
// t was retrieved from a previous operation or it's new
this.spannerTemplate.update(t, "symbol", "action");
```

#### DML

DML statements can be run by using
`SpannerOperations.executeDmlStatement`. Inserts, updates, and deletions
can affect any number of rows and entities.

You can run [partitioned
DML](https://cloud.google.com/spanner/docs/dml-partitioned) updates by
using the `executePartitionedDmlStatement` method. Partitioned DML
queries have performance benefits but also have restrictions and cannot
be used inside transactions.

#### Transactions

`SpannerOperations` provides methods to run `java.util.Function` objects
within a single transaction while making available the read and write
methods from `SpannerOperations`.

##### Read/Write Transaction

Read and write transactions are provided by `SpannerOperations` via the
`performReadWriteTransaction` method:

``` java
@Autowired
SpannerOperations mySpannerOperations;

public String doWorkInsideTransaction() {
  return mySpannerOperations.performReadWriteTransaction(
    transActionSpannerOperations -> {
      // Work with transActionSpannerOperations here.
      // It is also a SpannerOperations object.

      return "transaction completed";
    }
  );
}
```

The `performReadWriteTransaction` method accepts a `Function` that is
provided an instance of a `SpannerOperations` object. The final returned
value and type of the function is determined by the user. You can use
this object just as you would a regular `SpannerOperations` with a few
exceptions:

  - Its read functionality cannot perform stale reads, because all reads
    and writes happen at the single point in time of the transaction.

  - It cannot perform sub-transactions via `performReadWriteTransaction`
    or `performReadOnlyTransaction`.

As these read-write transactions are locking, it is recommended that you
use the `performReadOnlyTransaction` if your function does not perform
any writes.

##### Read-only Transaction

The `performReadOnlyTransaction` method is used to perform read-only
transactions using a `SpannerOperations`:

``` java
@Autowired
SpannerOperations mySpannerOperations;

public String doWorkInsideTransaction() {
  return mySpannerOperations.performReadOnlyTransaction(
    transActionSpannerOperations -> {
      // Work with transActionSpannerOperations here.
      // It is also a SpannerOperations object.

      return "transaction completed";
    }
  );
}
```

The `performReadOnlyTransaction` method accepts a `Function` that is
provided an instance of a `SpannerOperations` object. This method also
accepts a `ReadOptions` object, but the only attribute used is the
timestamp used to determine the snapshot in time to perform the reads in
the transaction. If the timestamp is not set in the read options the
transaction is run against the current state of the database. The final
returned value and type of the function is determined by the user. You
can use this object just as you would a regular `SpannerOperations` with
a few exceptions:

  - Its read functionality cannot perform stale reads (other than the
    staleness set on the entire transaction), because all reads happen
    at the single point in time of the transaction.

  - It cannot perform sub-transactions via `performReadWriteTransaction`
    or `performReadOnlyTransaction`

  - It cannot perform any write operations.

Because read-only transactions are non-locking and can be performed on
points in time in the past, these are recommended for functions that do
not perform write operations.

##### Declarative Transactions with @Transactional Annotation

This feature requires a bean of `SpannerTransactionManager`, which is
provided when using `spring-cloud-gcp-starter-data-spanner`.

`SpannerTemplate` and `SpannerRepository` support running methods with
the `@Transactional`
[annotation](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#transaction-declarative)
as transactions. If a method annotated with `@Transactional` calls
another method also annotated, then both methods will work within the
same transaction. `performReadOnlyTransaction` and
`performReadWriteTransaction` cannot be used in `@Transactional`
annotated methods because Cloud Spanner does not support transactions
within transactions.

#### DML Statements

`SpannerTemplate` supports
[DML](https://cloud.google.com/spanner/docs/dml-tasks:) `Statements`.
DML statements can also be run in transactions by using
`performReadWriteTransaction` or by using the `@Transactional`
annotation.

### Repositories

[Spring Data
Repositories](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories)
are a powerful abstraction that can save you a lot of boilerplate code.

For example:

``` java
public interface TraderRepository extends SpannerRepository<Trader, String> {
}
```

Spring Data generates a working implementation of the specified
interface, which can be conveniently autowired into an application.

The `Trader` type parameter to `SpannerRepository` refers to the
underlying domain type. The second type parameter, `String` in this
case, refers to the type of the key of the domain type.

For POJOs with a composite primary key, this ID type parameter can be
any descendant of `Object[]` compatible with all primary key properties,
any descendant of `Iterable`, or `com.google.cloud.spanner.Key`. If the
domain POJO type only has a single primary key column, then the primary
key property type can be used or the `Key` type.

For example in case of Trades, that belong to a Trader,
`TradeRepository` would look like this:

``` java
public interface TradeRepository extends SpannerRepository<Trade, String[]> {

}
```

``` java
public class MyApplication {

    @Autowired
    SpannerTemplate spannerTemplate;

    @Autowired
    StudentRepository studentRepository;

    public void demo() {

        this.tradeRepository.deleteAll();
        String traderId = "demo_trader";
        Trade t = new Trade();
        t.symbol = stock;
        t.action = action;
        t.traderId = traderId;
        t.price = new BigDecimal("100.0");
        t.shares = 12345.6;
        this.spannerTemplate.insert(t);

        Iterable<Trade> allTrades = this.tradeRepository.findAll();

        int count = this.tradeRepository.countByAction("BUY");

    }
}
```

#### CRUD Repository

`CrudRepository` methods work as expected, with one thing Spanner
specific: the `save` and `saveAll` methods work as update-or-insert.

#### Paging and Sorting Repository

You can also use `PagingAndSortingRepository` with Spanner Spring Data.
The sorting and pageable `findAll` methods available from this interface
operate on the current state of the Spanner database. As a result,
beware that the state of the database (and the results) might change
when moving page to page.

#### Spanner Repository

The `SpannerRepository` extends the `PagingAndSortingRepository`, but
adds the read-only and the read-write transaction functionality provided
by Spanner. These transactions work very similarly to those of
`SpannerOperations`, but is specific to the repository’s domain type and
provides repository functions instead of template functions.

For example, this is a read-only transaction:

``` java
@Autowired
SpannerRepository myRepo;

public String doWorkInsideTransaction() {
  return myRepo.performReadOnlyTransaction(
    transactionSpannerRepo -> {
      // Work with the single-transaction transactionSpannerRepo here.
      // This is a SpannerRepository object.

      return "transaction completed";
    }
  );
}
```

When creating custom repositories for your own domain types and query
methods, you can extend `SpannerRepository` to access Cloud
Spanner-specific features as well as all features from
`PagingAndSortingRepository` and `CrudRepository`.

### Query Methods

`SpannerRepository` supports Query Methods. Described in the following
sections, these are methods residing in your custom repository
interfaces of which implementations are generated based on their names
and annotations. Query Methods can read, write, and delete entities in
Cloud Spanner. Parameters to these methods can be any Cloud Spanner data
type supported directly or via custom configured converters. Parameters
can also be of type `Struct` or POJOs. If a POJO is given as a
parameter, it will be converted to a `Struct` with the same
type-conversion logic as used to create write mutations. Comparisons
using Struct parameters are limited to [what is available with Cloud
Spanner](https://cloud.google.com/spanner/docs/data-types#limited-comparisons-for-struct).

#### Query methods by convention

``` java
public interface TradeRepository extends SpannerRepository<Trade, String[]> {
    List<Trade> findByAction(String action);

    int countByAction(String action);

    // Named methods are powerful, but can get unwieldy
    List<Trade> findTop3DistinctByActionAndSymbolIgnoreCaseOrTraderIdOrderBySymbolDesc(
            String action, String symbol, String traderId);
}
```

In the example above, the [query
methods](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories.query-methods)
in `TradeRepository` are generated based on the name of the methods,
using the [Spring Data Query creation naming
convention](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html#repositories.query-methods.query-creation).

`List<Trade> findByAction(String action)` would translate to a `SELECT *
FROM trades WHERE action = ?`.

The function `List<Trade>
findTop3DistinctByActionAndSymbolIgnoreCaseOrTraderIdOrderBySymbolDesc(String
action, String symbol, String traderId);` will be translated as the
equivalent of this SQL query:

``` sql
SELECT DISTINCT * FROM trades
WHERE ACTION = ? AND LOWER(SYMBOL) = LOWER(?) AND TRADER_ID = ?
ORDER BY SYMBOL DESC
LIMIT 3
```

The following filter options are supported:

  - Equality

  - Greater than or equals

  - Greater than

  - Less than or equals

  - Less than

  - Is null

  - Is not null

  - Is true

  - Is false

  - Like a string

  - Not like a string

  - Contains a string

  - Not contains a string

  - In

  - Not in

Note that the phrase `SymbolIgnoreCase` is translated to `LOWER(SYMBOL)
= LOWER(?)` indicating a non-case-sensitive matching. The `IgnoreCase`
phrase may only be appended to fields that correspond to columns of type
STRING or BYTES. The Spring Data "AllIgnoreCase" phrase appended at the
end of the method name is not supported.

The `Like` or `NotLike` naming conventions:

``` java
List<Trade> findBySymbolLike(String symbolFragment);
```

The param `symbolFragment` can contain [wildcard
characters](https://cloud.google.com/spanner/docs/functions-and-operators#comparison-operators)
for string matching such as `_` and `%`.

The `Contains` and `NotContains` naming conventions:

``` java
List<Trade> findBySymbolContains(String symbolFragment);
```

The param `symbolFragment` is a [regular
expression](https://cloud.google.com/spanner/docs/functions-and-operators#regexp_contains)
that is checked for occurrences.

The `In` and `NotIn` keywords must be used with `Iterable` corresponding
parameters.

Delete queries are also supported. For example, query methods such as
`deleteByAction` or `removeByAction` delete entities found by
`findByAction`. The delete operation happens in a single transaction.

Delete queries can have the following return types: \* An integer type
that is the number of entities deleted \* A collection of entities that
were deleted \* `void`

#### Custom SQL/DML query methods

The example above for `List<Trade> fetchByActionNamedQuery(String
action)` does not match the [Spring Data Query creation naming
convention](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html#repositories.query-methods.query-creation),
so we have to map a parametrized Spanner SQL query to it.

The SQL query for the method can be mapped to repository methods in one
of two ways:

  - `namedQueries` properties file

  - using the `@Query` annotation

The names of the tags of the SQL correspond to the `@Param` annotated
names of the method parameters.

Interleaved properties are loaded eagerly, unless they are annotated
with `@Interleaved(lazy = true)`.

Custom SQL query methods can accept a single `Sort` or `Pageable`
parameter that is applied on top of the specified custom query. It is
the recommended way to control the sort order of the results, which is
not guaranteed by the `ORDER BY` clause in the SQL query. This is due to
the fact that the user-provided query is used as a sub-query, and Cloud
Spanner doesn’t preserve order in subquery results.

You might want to use `ORDER BY` with `LIMIT` to obtain the top records,
according to a specified order. However, to ensure the correct sort
order of the final result set, sort options have to be passed in with a
`Pageable`.

``` java
 @Query("SELECT * FROM trades")
    List<Trade> fetchTrades(Pageable pageable);

    @Query("SELECT * FROM trades ORDER BY price DESC LIMIT 1")
    Trade topTrade(Pageable pageable);
```

This can be used:

``` java
 List<Trade> customSortedTrades = tradeRepository.fetchTrades(PageRequest
                .of(2, 2, org.springframework.data.domain.Sort.by(Order.asc("id"))));
```

The results would be sorted by "id" in ascending order.

Your query method can also return non-entity types:

``` java
     @Query("SELECT COUNT(1) FROM trades WHERE action = @action")
    int countByActionQuery(String action);

    @Query("SELECT EXISTS(SELECT COUNT(1) FROM trades WHERE action = @action)")
    boolean existsByActionQuery(String action);

    @Query("SELECT action FROM trades WHERE action = @action LIMIT 1")
    String getFirstString(@Param("action") String action);

    @Query("SELECT action FROM trades WHERE action = @action")
    List<String> getFirstStringList(@Param("action") String action);
```

DML statements can also be run by query methods, but the only possible
return value is a `long` representing the number of affected rows. The
`dmlStatement` boolean setting must be set on `@Query` to indicate that
the query method is run as a DML statement.

``` java
     @Query(value = "DELETE FROM trades WHERE action = @action", dmlStatement = true)
    long deleteByActionQuery(String action);
```

##### Query methods with named queries properties

By default, the `namedQueriesLocation` attribute on
`@EnableSpannerRepositories` points to the
`META-INF/spanner-named-queries.properties` file. You can specify the
query for a method in the properties file by providing the SQL as the
value for the "interface.method" property:

``` properties
Trade.fetchByActionNamedQuery=SELECT * FROM trades WHERE trades.action = @tag0
```

``` java
public interface TradeRepository extends SpannerRepository<Trade, String[]> {
    // This method uses the query from the properties file instead of one generated based on name.
    List<Trade> fetchByActionNamedQuery(@Param("tag0") String action);
}
```

##### Query methods with annotation

Using the `@Query` annotation:

``` java
public interface TradeRepository extends SpannerRepository<Trade, String[]> {
    @Query("SELECT * FROM trades WHERE trades.action = @tag0")
    List<Trade> fetchByActionNamedQuery(@Param("tag0") String action);
}
```

Table names can be used directly. For example, "trades" in the above
example. Alternatively, table names can be resolved from the `@Table`
annotation on domain classes as well. In this case, the query should
refer to table names with fully qualified class names between `:`
characters: `:fully.qualified.ClassName:`. A full example would look
like:

``` java
@Query("SELECT * FROM :com.example.Trade: WHERE trades.action = @tag0")
List<Trade> fetchByActionNamedQuery(String action);
```

This allows table names evaluated with SpEL to be used in custom
queries.

SpEL can also be used to provide SQL parameters:

``` java
@Query("SELECT * FROM :com.example.Trade: WHERE trades.action = @tag0
  AND price > #{#priceRadius * -1} AND price < #{#priceRadius * 2}")
List<Trade> fetchByActionNamedQuery(String action, Double priceRadius);
```

When using the `IN` SQL clause, remember to use `IN
UNNEST(@iterableParam)` to specify a single `Iterable` parameter. You
can also use a fixed number of singular parameters such as `IN
(@stringParam1, @stringParam2)`.

#### Projections

Spring Data Spanner supports
[projections](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#projections).
You can define projection interfaces based on domain types and add query
methods that return them in your repository:

``` java
public interface TradeProjection {

    String getAction();

    @Value("#{target.symbol + ' ' + target.action}")
    String getSymbolAndAction();
}

public interface TradeRepository extends SpannerRepository<Trade, Key> {

    List<Trade> findByTraderId(String traderId);

    List<TradeProjection> findByAction(String action);

    @Query("SELECT action, symbol FROM trades WHERE action = @action")
    List<TradeProjection> findByQuery(String action);
}
```

Projections can be provided by name-convention-based query methods as
well as by custom SQL queries. If using custom SQL queries, you can
further restrict the columns retrieved from Spanner to just those
required by the projection to improve performance.

Properties of projection types defined using SpEL use the fixed name
`target` for the underlying domain object. As a result accessing
underlying properties take the form `target.<property-name>`.

#### Empty result handling in repository methods

Java `java.util.Optional` can be used to indicate the potential absence
of a return value.

Alternatively, query methods can return the result without a wrapper. In
that case the absence of a query result is indicated by returning
`null`. Repository methods returning collections are guaranteed never to
return `null` but rather the corresponding empty collection.

<div class="note">

You can enable nullability checks. For more details please see [Spring
Framework’s nullability
docs](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#null-safety).

</div>

#### REST Repositories

When running with Spring Boot, repositories can be exposed as REST
services by simply adding this dependency to your pom file:

``` xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

If you prefer to configure parameters (such as path), you can use
`@RepositoryRestResource` annotation:

``` java
@RepositoryRestResource(collectionResourceRel = "trades", path = "trades")
public interface TradeRepository extends SpannerRepository<Trade, Key> {
}
```

<div class="note">

For classes that have composite keys (multiple `@PrimaryKey` fields),
only the `Key` type is supported for the repository ID type.

</div>

For example, you can retrieve all `Trade` objects in the repository by
using `curl http://<server>:<port>/trades`, or any specific trade via
`curl http://<server>:<port>/trades/<trader_id>,<trade_id>`.

The separator between your primary key components, `id` and `trader_id`
in this case, is a comma by default, but can be configured to any string
not found in your key values by extending the `SpannerKeyIdConverter`
class:

``` java
@Component
class MySpecialIdConverter extends SpannerKeyIdConverter {

    @Override
    protected String getUrlIdSeparator() {
        return ":";
    }
}
```

You can also write trades using `curl -XPOST -H"Content-Type:
application/json" -d@test.json http://<server>:<port>/trades/` where the
file `test.json` holds the JSON representation of a `Trade` object.

### Database and Schema Admin

Databases and tables inside Spanner instances can be created
automatically from `SpannerPersistentEntity` objects:

``` java
@Autowired
private SpannerSchemaUtils spannerSchemaUtils;

@Autowired
private SpannerDatabaseAdminTemplate spannerDatabaseAdminTemplate;

public void createTable(SpannerPersistentEntity entity) {
    if(!spannerDatabaseAdminTemplate.tableExists(entity.tableName()){

      // The boolean parameter indicates that the database will be created if it does not exist.
      spannerDatabaseAdminTemplate.executeDdlStrings(Arrays.asList(
            spannerSchemaUtils.getCreateTableDDLString(entity.getType())), true);
    }
}
```

Schemas can be generated for entire object hierarchies with interleaved
relationships and composite keys.

### Events

Spring Data Cloud Spanner publishes events extending the Spring
Framework’s `ApplicationEvent` to the context that can be received by
`ApplicationListener` beans you register.

| Type                    | Description                                                                                    | Contents                                                                                                                            |
| ----------------------- | ---------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `AfterReadEvent`        | Published immediately after entities are read by key from Cloud Spanner by `SpannerTemplate`   | The entities loaded. The read options and key-set originally specified for the load operation.                                      |
| `AfterQueryEvent`       | Published immediately after entities are read by query from Cloud Spanner by `SpannerTemplate` | The entities loaded. The query options and query statement originally specified for the load operation.                             |
| `BeforeExecuteDmlEvent` | Published immediately before DML statements are executed by `SpannerTemplate`                  | The DML statement to execute.                                                                                                       |
| `AfterExecuteDmlEvent`  | Published immediately after DML statements are executed by `SpannerTemplate`                   | The DML statement to execute and the number of rows affected by the operation as reported by Cloud Spanner.                         |
| `BeforeSaveEvent`       | Published immediately before upsert/update/insert operations are executed by `SpannerTemplate` | The mutations to be sent to Cloud Spanner, the entities to be saved, and optionally the properties in those entities to save.       |
| `AfterSaveEvent`        | Published immediately after upsert/update/insert operations are executed by `SpannerTemplate`  | The mutations sent to Cloud Spanner, the entities to be saved, and optionally the properties in those entities to save.             |
| `BeforeDeleteEvent`     | Published immediately before delete operations are executed by `SpannerTemplate`               | The mutations to be sent to Cloud Spanner. The target entities, keys, or entity type originally specified for the delete operation. |
| `AfterDeleteEvent`      | Published immediately after delete operations are executed by `SpannerTemplate`                | The mutations sent to Cloud Spanner. The target entities, keys, or entity type originally specified for the delete operation.       |

### Auditing

Spring Data Cloud Spanner supports the `@LastModifiedDate` and
`@LastModifiedBy` auditing annotations for properties:

``` java
@Table
public class SimpleEntity {
    @PrimaryKey
    String id;

    @LastModifiedBy
    String lastUser;

    @LastModifiedDate
    DateTime lastTouched;
}
```

Upon insert, update, or save, these properties will be set automatically
by the framework before mutations are generated and saved to Cloud
Spanner.

To take advantage of these features, add the `@EnableSpannerAuditing`
annotation to your configuration class and provide a bean for an
`AuditorAware<A>` implementation where the type `A` is the desired
property type annotated by `@LastModifiedBy`:

``` java
@Configuration
@EnableSpannerAuditing
public class Config {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("YOUR_USERNAME_HERE");
    }
}
```

The `AuditorAware` interface contains a single method that supplies the
value for fields annotated by `@LastModifiedBy` and can be of any type.
One alternative is to use Spring Security’s `User` type:

``` java
class SpringSecurityAuditorAware implements AuditorAware<User> {

  public Optional<User> getCurrentAuditor() {

    return Optional.ofNullable(SecurityContextHolder.getContext())
              .map(SecurityContext::getAuthentication)
              .filter(Authentication::isAuthenticated)
              .map(Authentication::getPrincipal)
              .map(User.class::cast);
  }
}
```

You can also set a custom provider for properties annotated
`@LastModifiedDate` by providing a bean for `DateTimeProvider` and
providing the bean name to `@EnableSpannerAuditing(dateTimeProviderRef =
"customDateTimeProviderBean")`.

### Multi-Instance Usage

Your application can be configured to use multiple Cloud Spanner
instances or databases by providing a custom bean for
`DatabaseIdProvider`. The default bean uses the instance ID, database
name, and project ID options you configured in `application.properties`.

``` java
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        // return custom connection options provider
    }
```

The `DatabaseId` given by this provider is used as the target database
name and instance of each operation Spring Data Cloud Spanner executes.
By providing a custom implementation of this bean (for example,
supplying a thread-local `DatabaseId`), you can direct your application
to use multiple instances or databases.

Database administrative operations, such as creating tables using
`SpannerDatabaseAdminTemplate`, will also utilize the provided
`DatabaseId`.

If you would like to configure every aspect of each connection (such as
pool size and retry settings), you can supply a bean for
`Supplier<DatabaseClient>`.

### Spring Boot Actuator Support

#### Cloud Spanner Health Indicator

If you are using Spring Boot Actuator, you can take advantage of the
Cloud Spanner health indicator called `spanner`. The health indicator
will verify whether Cloud Spanner is up and accessible by your
application. To enable it, all you need to do is add the [Spring Boot
Actuator](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready)
to your project.

The `spanner` indicator will then roll up to the overall application
status visible at <http://localhost:8080/actuator/health> (use the
`management.endpoint.health.show-details` property to view per-indicator
details).

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

<div class="note">

If your application already has actuator and Cloud Spanner starters,
this health indicator is enabled by default. To disable the Cloud
Spanner indicator, set `management.health.spanner.enabled` to `false`.

</div>

The health indicator validates the connection to Spanner by executing a
query. A query to validate can be configured via
`spring.cloud.gcp.spanner.health.query` property.

|                                         |                                                |          |                                                     |
| --------------------------------------- | ---------------------------------------------- | -------- | --------------------------------------------------- |
| Name                                    | Description                                    | Required | Default value                                       |
| `management.health.spanner.enabled`     | Whether to enable the Spanner health indicator | No       | `true` with Spring Boot Actuator, `false` otherwise |
| `spring.cloud.gcp.spanner.health.query` | A query to validate                            | No       | `SELECT 1`                                          |

### Cloud Spanner Emulator

The [Cloud SDK](https://cloud.google.com/sdk) provides a local,
in-memory emulator for Cloud Spanner, which you can use to develop and
test your application. As the emulator stores data only in memory, it
will not persist data across runs. It is intended to help you use Cloud
Spanner for local development and testing, not for production
deployments.

In order to set up and start the emulator, you can follow [these
steps](https://cloud.google.com/spanner/docs/emulator).

This command can be used to create Cloud Spanner instances:

    $ gcloud spanner instances create <instance-name> --config=emulator-config --description="<description>" --nodes=1

Once the Spanner emulator is running, ensure that the following
properties are set in your `application.properties` of your Spring
application:

    spring.cloud.gcp.spanner.emulator.enabled=true
    spring.cloud.gcp.spanner.emulator-host=${EMULATOR_HOSTPORT}

### Sample

There are two sample applications available:

1.  [Sample application using Spanner Template
    directly](https://github.com/GoogleCloudPlatform/spring-cloud-gcp/tree/main/spring-cloud-gcp-samples/spring-cloud-gcp-data-spanner-template-sample)

2.  [Sample application using higher-level Spanner Repository
    capabilities](https://github.com/GoogleCloudPlatform/spring-cloud-gcp/tree/main/spring-cloud-gcp-samples/spring-cloud-gcp-data-spanner-repository-sample)