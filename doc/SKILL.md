# jdbc-util Skill

This skill teaches how to use the `de.schaeuffelhut.jdbc:jdbc-util` library, a lightweight, type-safe JDBC utility for Java 17+. It helps agents write clean, modern Java code to interact with SQL databases, eliminating boilerplate while staying close to the SQL.

## 1. Dependency

Ensure the following dependency is present in the `build.gradle` file.

```gradle
implementation 'de.schaeuffelhut.jdbc:jdbc-util:1.1.1'
```

## 2. Core Concepts

The library revolves around a few key components that are combined to execute queries and map results.

| Class                 | Purpose                                                                    |
| --------------------- | -------------------------------------------------------------------------- |
| `StatementUtil`       | The entry point for executing queries (`selectInto`, `execute`, `executeBatch`). |
| `ResultSetReaders`    | Defines how many rows to read from the result set (e.g., `readOne()`, `readMany()`). |
| `ResultSetMappers`    | Defines how to map a single `ResultSet` row to a Java object (e.g., `scalar()`, `object()`). |
| `ResultTypes`         | Defines how to extract a single column value from a `ResultSet` row (e.g., `String`, `Integer`, `Object(UUID.class)`). |
| `StatementParameters` | Provides type-safe SQL query parameters (e.g., `Integer()`, `String()`). |

A typical query combines these components like so:

```java
// statementUtil.selectInto(SQL_QUERY, ResultSetReader, ResultSetMapper, StatementParameters...);
```

## 3. General Imports

For cleaner and more concise code, it is highly recommended to use static imports for the common factory methods and enums. While each example below includes its specific required imports, a common setup for a class using this library would be:

```java
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID; // Needed for UUID examples

import static de.schaeuffelhut.jdbc.GeneratedKeys.*;
import static de.schaeuffelhut.jdbc.ResultSetMappers.*;
import static de.schaeuffelhut.jdbc.ResultSetReaders.*;
import static de.schaeuffelhut.jdbc.ResultTypes.*;
import static de.schaeuffelhut.jdbc.StatementParameters.*;
import static de.schaeuffelhut.jdbc.StatementProperties.*;
import static de.schaeuffelhut.jdbc.StatementOutParameters.*; // For OUT parameters
import static de.schaeuffelhut.jdbc.ExtraStatementParameters.*; // For additional parameter types like UUID
```

## 5. Usage Patterns

### Pattern 1: Querying for Data (`selectInto`)

#### A) Mapping to a Scalar (Single Value)

```java
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.scalar;
import static de.schaeuffelhut.jdbc.ResultTypes.String;
import static de.schaeuffelhut.jdbc.StatementParameters.Integer;

// ...

String name = statementUtil.selectInto(
    """
    SELECT name FROM employees WHERE id = ?
    """,
    readOne(),
    scalar(String),
    Integer(1)
);
```

#### B) Mapping to a Record or Class (Object)

```java
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

record Employee(int id, String name) {}

Employee emp = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 2
    """,
    readOne(),
    object(Employee::new, Integer, String)
);
```

#### C) Mapping a List of Objects

```java
import java.util.List;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readMany;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

record Employee(int id, String name) {};

List<Employee> employees = statementUtil.selectInto(
    """
    SELECT id, name FROM employees
    """,
    readMany(),
    object(Employee::new, Integer, String)
);
```

#### D) Mapping to an Optional Object (Zero or One Result)

```java
import java.util.Optional;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOptional;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.ResultTypes.String;
import static de.schaeuffelhut.jdbc.StatementParameters.Integer;

// ...

record Employee(int id, String name) {};

// Case 1: No result
Optional<Employee> optionalEmployeeEmpty = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = ?
    """,
    readOptional(),
    object(Employee::new, Integer, String),
    Integer(0)
);

// Case 2: One result
Optional<Employee> optionalEmployeePresent = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = ?
    """,
    readOptional(),
    object(Employee::new, Integer, String),
    Integer(1)
);
```

#### E) Complex Multiline Queries

```java
import java.util.List;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readMany;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.String;
import static de.schaeuffelhut.jdbc.ResultTypes.Long;
import static de.schaeuffelhut.jdbc.ResultTypes.Double;
import static de.schaeuffelhut.jdbc.StatementParameters.Double;

// ...

record EmployeeSummary(String departmentName, long employeeCount, double avgSalary) {}

List<EmployeeSummary> summaries = statementUtil.selectInto(
    """
      WITH DepartmentSalaries AS (
             SELECT e.department_id,
                    d.name AS department_name,
                    e.salary
               FROM employees e
               JOIN departments d ON e.department_id = d.id
           )
    SELECT ds.department_name,
           COUNT(ds.salary) AS employee_count,
           AVG(ds.salary) AS avg_salary
      FROM DepartmentSalaries ds
     WHERE ds.salary > ?
    GROUP BY ds.department_name
    ORDER BY avg_salary DESC
    """,
    readMany(),
    object(
        EmployeeSummary::new,
        String,
        Long,
        Double
    ),
    Double(50000.0)
);
```

### Pattern 2: Executing CUD Operations (`execute` and `executeBatch`)

#### A) Single Insert/Update/Delete

```java
import static de.schaeuffelhut.jdbc.StatementParameters.Integer;
import static de.schaeuffelhut.jdbc.StatementParameters.String;

// ...

statementUtil.execute(
    """
    INSERT INTO employees (id, name) VALUES (?, ?)
    """,
    Integer(3),
    String("Charlie")
);
```

#### B) Batch Operations

```java
import java.util.List;
import de.schaeuffelhut.jdbc.StatementInParameter;
import static de.schaeuffelhut.jdbc.StatementParameters.Integer;
import static de.schaeuffelhut.jdbc.StatementParameters.String;

// ...

record Employee(int id, String name){};
List<Employee> employees = List.of(new Employee(4, "David"), new Employee(5, "Eve"));

statementUtil.executeBatch(
    """
    INSERT INTO employees (id, name) VALUES (?, ?)
    """,
    employees.stream()
        .map(e -> new StatementInParameter[]{
            Integer(e.id),
            String(e.name)
        })
        .toList()
);
```

## 6. Advanced Usage

### A) Retrieving a Single Auto-Generated Key

```java
import static de.schaeuffelhut.jdbc.GeneratedKeys.REPORT;
import static de.schaeuffelhut.jdbc.StatementProperties.GENERATED_KEY;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.StatementParameters.String;

// ...

Integer generatedId = statementUtil.execute(
    REPORT,
    GENERATED_KEY(Integer),
    """
    INSERT INTO employees (name) VALUES (?)
    """,
    String("Mallory")
);
```

### B) Retrieving Multiple Keys from a Batch Insert

```java
import java.util.List;
import de.schaeuffelhut.jdbc.StatementInParameter;
import static de.schaeuffelhut.jdbc.GeneratedKeys.REPORT;
import static de.schaeuffelhut.jdbc.StatementProperties.GENERATED_KEYS;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.StatementParameters;

// ...

var names = List.of("Peter", "Paul", "Mary");
var params = names.stream()
    .map(name -> new StatementInParameter[]{ StatementParameters.String(name) })
    .toList();

List<Integer> generatedIds = statementUtil.executeBatch(
    REPORT,
    GENERATED_KEYS(Integer),
    """
    INSERT INTO employees (name) VALUES (?)
    """,
    params
);
```

### C) Mapping Results to a Tuple (`Object[]`)

```java
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.tuple;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

Object[] tuple = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 1
    """,
    readOne(),
    tuple(Integer, String)
);
```

### D) Mapping Results to a `Map`

```java
import java.util.Map;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.map;
import static de.schaeuffelhut.jdbc.ResultTypes.Integer;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

Map<String, Object> map = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 1
    """,
    readOne(),
    map(
        Integer,
        String
    )
);
```

### E) Executing Stored Procedures (executeCall)

```java
import de.schaeuffelhut.jdbc.StatementOutParameter;
import de.schaeuffelhut.jdbc.StatementOutParameters;
import static de.schaeuffelhut.jdbc.StatementParameters.Integer;
import static de.schaeuffelhut.jdbc.StatementParameters.String;

// ...

// 1. Define or use a predefined StatementOutParameter
// (This example uses the predefined one from StatementOutParameters)

// 2. Execute the stored procedure call
Object[] results = statementUtil.executeCall(
    "{? = CALL MY_STORED_PROC(?, ?)}",
    StatementOutParameters.String,
    Integer(123),
    String("input string")
);

// 3. Retrieve the value from the results array
String outputValue = (String) results[0];
```

## 7. Composition: Mapping Multiple Columns to a Complex Object

### Method 1: Implement a Custom `ResultType` (Most Flexible)

```java
import java.sql.ResultSet;
import java.sql.SQLException;
import de.schaeuffelhut.jdbc.ResultType;
import de.schaeuffelhut.jdbc.ColumnIndex;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

class Address {
    final String street;
    final String city;
    Address(String street, String city) { this.street = street; this.city = city; }
}
record Person(Address address, String name) {}

class AddressResultType implements ResultType<Address> {
    @Override
    public Address getResult(ResultSet resultSet, ColumnIndex index) throws SQLException {
        String street = resultSet.getString(index.next());
        String city = resultSet.getString(index.next());
        if (street == null && city == null) return null;
        return new Address(street, city);
    }
    @Override
    public Class<Address> getResultType() { return Address.class; }
}

Person person = statementUtil.selectInto(
    """
    SELECT street, city, name FROM person_addresses WHERE id = 1
    """,
    readOne(),
    object(Person::new, new AddressResultType(), String)
);
```

### Method 2: Use `ResultTypes.mapper()` (Inline and Concise)

```java
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static de.schaeuffelhut.jdbc.ResultSetMappers.object;
import static de.schaeuffelhut.jdbc.ResultTypes.mapper;
import static de.schaeuffelhut.jdbc.ResultTypes.String;

// ...

record Address(String street, String city) {}
record Person(Address address, String name) {}

Person person = statementUtil.selectInto(
    """
    SELECT street, city, name FROM person_complex WHERE id = 1
    """,
    readOne(),
    object(
        Person::new,
        mapper(
            Address.class,
            object(Address::new, String, String)
        ),
        String
    )
);
```
