# JdbcUtil

![License](https://img.shields.io/badge/License-MIT-green)
![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/de.schaeuffelhut.jdbc/jdbc-util.svg)](https://central.sonatype.com/artifact/com.fries/jdbc-util)

**Lightweight, type-safe JDBC utility for Java 17+**  

A lightweight Java utility library, keeping you close to SQL while eliminating JDBC boilerplate. Write queries in pure
SQL and map results directly to Java objects using modern type-safe method references.


## Features

- **Java 17+**
- **Auto-close** `Statement`, `ResultSet`
- **Simple query/execute API with lambda mapping**
- **Type-safe mapping** via method references, reflection, or tuples
- **Fluent API** with selectInto, execute, executeBatch
- **Built-in support** for scalars, tuples, maps, POJOs, records
- **Parameter binding** via StatementParameters
- **Batch execution** with `executeBatch`


## Dependency

### Gradle

```Gradle
implementation 'de.schaeuffelhut.jdbc:jdbc-util:1.0.0'
```

### Maven

```maven
<dependency>
    <groupId>de.schaeuffelhut.jdbc</groupId>
    <artifactId>jdbc-util</artifactId>
    <version>1.0.0</version>
</dependency>
```


## Quick Start

## `selectInto` – Powerful Query Mapping

### 1. Scalar Value

```java
String name = statementUtil.selectInto(
    "SELECT name FROM employees WHERE id = 1",
    ResultSetReaders.readOne(),
    ResultSetMappers.scalar(ResultTypes.String)
);
```

### 2. Tuple (List of values)

```java
Object[] tuple = statementUtil.selectInto(
    "SELECT id, name FROM employees WHERE id = 1",
    ResultSetReaders.readOne(),
    ResultSetMappers.tuple(ResultTypes.Integer, ResultTypes.String)
);
```

### 3. Map (by column label)

```java
Map<String, Object> map = statementUtil.selectInto(
    """
    SELECT id, name, COUNT(*) AS "count", SUM(id) AS "sum"
    FROM employees WHERE id = 1
    GROUP BY id, name
    """,
    ResultSetReaders.readOne(),
    ResultSetMappers.map(
        ResultTypes.Integer,   // id
        ResultTypes.String,   // name
        ResultTypes.Integer,  // count
        ResultTypes.Integer   // sum
    )
);
```

### 4. Object via Constructor (Record or Class)

```java
record Employee(int id, String name) {}

Employee emp = statementUtil.selectInto(
    "SELECT id, name FROM employees WHERE id = 2",
    ResultSetReaders.readOne(),
    ResultSetMappers.object(Employee::new, ResultTypes.Integer, ResultTypes.String)
);
```

### 5. Object via Reflection (Mutable Class)

```java
public static class EmployeeDto {
    public int id;
    public String name;
}

EmployeeDto dto = statementUtil.selectInto(
    "SELECT id AS \"id\", name AS \"name\" FROM employees WHERE id = 2",
    ResultSetReaders.readOne(),
    ResultSetMappers.objectViaReflection(
        EmployeeDto.class,
        ResultTypes.Integer,
        ResultTypes.String
    )
);
```

## `execute` – Insert / Update / Delete

```java
statementUtil.execute(
    """
    INSERT INTO employees (id, name) VALUES (?, ?)
    ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name
    """,
    StatementParameters.Integer(3),
    StatementParameters.String("Charlie")
);
```

## `executeBatch` – Bulk Operations

```java
record Employee(int id, String name){};
List<Employee> employees = List.of(new Employee(1, "Alice"), new Employee(2, "Bob"));

statementUtil.executeBatch(
        "INSERT INTO employees (id, name) VALUES (?, ?)",
        employees.stream()
                .map( e -> new StatementInParameter[]{
                        StatementParameters.String( e.id ),
                        StatementParameters.String( e.name )
                } )
                .toList()
);
```


## Core Components

| Class | Purpose |
|------|--------|
| `ResultSetReaders` | `readOne()`, `readMany()`, `readScalar()` |
| `ResultSetMappers` | `scalar()`, `tuple()`, `map()`, `object()`, `objectViaReflection()` |
| `ResultTypes` | `String`, `Integer`, `Long`, `Boolean`, `Object(UUID.class)`, `Enum(MyEnum.class)` |
| `StatementParameters` | `Object()`, `String()`, `Integer()`, `EnumByName()` |

## For AI Agents

For a detailed guide on how to use this library effectively with AI agents, including advanced usage patterns and best
practices, please refer to the [doc/SKILLS.md](doc/SKILL.md) file.

## License

[MIT](LICENSE) – Free for commercial and personal use.

---
**JdbcUtil** – **SQL First. Java Second. No Boilerplate.**

---
*Maintained with love by [@fschaeuffelhut](https://x.com/fschaeuffelhut)*
