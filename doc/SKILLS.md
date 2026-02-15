# jdbc-util Skill

This skill teaches how to use the `de.schaeuffelhut.jdbc:jdbc-util` library, a lightweight, type-safe JDBC utility for Java 17+. It helps agents write clean, modern Java code to interact with SQL databases, eliminating boilerplate while staying close to the SQL.

## 1. Dependency

Ensure the following dependency is present in the `build.gradle` file.

```gradle
implementation 'de.schaeuffelhut.jdbc:jdbc-util:1.0.0'
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

## 3. Imports

For cleaner and more concise code, it is highly recommended to use static imports for the common factory methods and enums:

```java
import static de.schaeuffelhut.jdbc.GeneratedKeys.*;
import static de.schaeuffelhut.jdbc.ResultSetMappers.*;
import static de.schaeuffelhut.jdbc.ResultSetReaders.*;
import static de.schaeuffelhut.jdbc.ResultTypes.*;
import static de.schaeuffelhut.jdbc.StatementParameters.*;
import static de.schaeuffelhut.jdbc.StatementProperties.*;
```

## 4. Usage Patterns

### Pattern 1: Querying for Data (`selectInto`)

#### A) Mapping to a Scalar (Single Value)

Use `ResultSetMappers.scalar()` with a `ResultType`.

```java
StatementUtil statementUtil = new H2StatementUtil(); // Or your own implementation
// Setup DB tables and data

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

Use `ResultSetMappers.object()`. The order of `ResultTypes` must match the order of columns in the `SELECT` statement and the order of parameters in the constructor.

```java
record Employee(int id, String name) {}

StatementUtil statementUtil = new H2StatementUtil();
// Setup DB tables and data

Employee emp = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 2
    """,
    readOne(),
    object(Employee::new, Integer, String)
);
```

#### C) Mapping a List of Objects

Change `ResultSetReaders.readOne()` to `ResultSetReaders.readMany()`.

```java
record Employee(int id, String name) {};
StatementUtil statementUtil = new H2StatementUtil();
// Setup DB tables and data

List<Employee> employees = statementUtil.selectInto(
    """
    SELECT id, name FROM employees
    """,
    readMany(),
    object(Employee::new, Integer, String)
);
```

#### D) Mapping to an Optional Object (Zero or One Result)

Use `ResultSetReaders.readOptional()` when you expect at most one row. It returns an `Optional<T>`, which will be empty if no row is found, or contain the object if exactly one row is found. If more than one row is returned, it will throw an `IllegalStateException`. This differs from `readOne()`, which throws an exception if zero or more than one row is returned.

```java
record Employee(int id, String name) {};
StatementUtil statementUtil = new H2StatementUtil();
// Setup DB tables and data

// Case 1: No result
Optional<Employee> optionalEmployeeEmpty = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = ?
    """,
    readOptional(),
    object(Employee::new, Integer, String),
    Integer(0) // Assuming no employee with id 0
);
// optionalEmployeeEmpty is now empty

// Case 2: One result
Optional<Employee> optionalEmployeePresent = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = ?
    """,
    readOptional(),
    object(Employee::new, Integer, String),
    Integer(1) // Assuming one employee with id 1
);
// optionalEmployeePresent contains the Employee object
```

#### E) Complex Multiline Queries

For more complex queries involving multiple clauses and better readability, use Java Text Blocks for the SQL statement. This example demonstrates `WITH`, `SELECT`, `FROM`, `JOIN`, `WHERE`, `GROUP BY`, and `ORDER BY`.

```java
// For a comprehensive example, assume 'employees' and 'departments' tables exist.
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
    Double(50000.0) // Example: employees earning more than 50000
);
```

### Pattern 2: Executing CUD Operations (`execute` and `executeBatch`)

#### A) Single Insert/Update/Delete

Use `statementUtil.execute()` with `StatementParameters`.

```java
statementUtil.execute(
    """
    INSERT INTO employees (id, name) VALUES (?, ?)
    """,
    Integer(3),
    String("Charlie")
);
```

#### B) Batch Operations

Use `statementUtil.executeBatch()` by providing a `List` of `StatementInParameter[]` arrays.

```java
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

## 4. Advanced Usage

### A) Retrieving a Single Auto-Generated Key

After an `INSERT`, you can retrieve the database-generated primary key using `GeneratedKeys.REPORT` and `StatementProperties.GENERATED_KEY` along with the desired `ResultType`.

```java
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

Similarly, you can retrieve a `List` of all generated keys from a batch `INSERT` operation by using `executeBatch` with `StatementProperties.GENERATED_KEYS`.

```java
var names = java.util.List.of("Peter", "Paul", "Mary");
var params = names.stream()
    .map(name -> new StatementInParameter[]{ StatementParameters.String(name) })
    .toList();

java.util.List<Integer> generatedIds = statementUtil.executeBatch(
    REPORT,
    GENERATED_KEYS(Integer),
    """
    INSERT INTO employees (name) VALUES (?)
    """,
    params
);

// generatedIds will contain [1, 2, 3]
```

### B) Mapping Results to a Tuple (`Object[]`)

For ad-hoc queries where a dedicated class or record is not needed, you can map results directly to an `Object[]`.

```java
Object[] tuple = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 1
    """,
    readOne(),
    tuple(Integer, String)
);

Integer id = (Integer) tuple[0];
String name = (String) tuple[1];
```

### C) Mapping Results to a `Map`

You can map a result row to a `Map<String, Object>`, where keys are the column labels from the SQL query. The `ResultTypes` must be provided in the same order as the columns in the `SELECT` statement.

```java
Map<String, Object> map = statementUtil.selectInto(
    """
    SELECT id, name FROM employees WHERE id = 1
    """,
    readOne(),
    map(
        Integer,   // for id column
        String     // for name column
    )
);

Integer id = (Integer) map.get("id");
String name = (String) map.get("name");
```

## 5. Composition: Mapping Multiple Columns to a Complex Object

There are two primary ways to handle nested objects (e.g., mapping `street` and `city` columns to an `Address` object within a `Person` object).

### Method 1: Implement a Custom `ResultType` (Most Flexible)

Create a class that implements `ResultType<T>` to handle the multi-column logic.

```java
// 1. Define the nested and parent objects
class Address {
    final String street;
    final String city;
    Address(String street, String city) {
        this.street = street;
        this.city = city;
    }
}
record Person(Address address, String name) {}

// 2. Create a custom ResultType for the nested object
class AddressResultType implements ResultType<Address> {
    @Override
    public Address getResult(ResultSet resultSet, ColumnIndex index) throws SQLException {
        // The order of .next() calls corresponds to column order in the SELECT
        String street = resultSet.getString(index.next());
        String city = resultSet.getString(index.next());
        // Handle case where all columns for the object are null
        if (street == null && city == null) return null;
        return new Address(street, city);
    }

    @Override
    public Class<Address> getResultType() {
        return Address.class;
    }
}

// 3. Use the custom ResultType in your mapper
StatementUtil statementUtil = new H2StatementUtil();
// Setup DB table with columns (e.g., street, city, name)

Person person = statementUtil.selectInto(
    """
    SELECT street, city, name FROM person_addresses WHERE id = 1
    """,
    readOne(),
    object(Person::new, new AddressResultType(), String)
);
```

### Method 2: Use `ResultTypes.mapper()` (Inline and Concise)

If the nested object also has a corresponding `ResultSetMapper`, you can wrap it using `ResultTypes.mapper()`. This is useful for composing mappers.

```java
// 1. Define the nested and parent records
record Address(String street, String city) {}
record Person(Address address, String name) {}

// 2. Use ResultTypes.mapper() to wrap an object mapper for Address
StatementUtil statementUtil = new H2StatementUtil();
// Setup DB table with columns (e.g., street, city, name)

Person person = statementUtil.selectInto(
    """
    SELECT street, city, name FROM person_complex WHERE id = 1
    """,
    readOne(),
    object(
        Person::new,
        // This ResultType consumes two String columns to build an Address
        mapper(
            Address.class,
            object(Address::new, String, String)
        ),
        // This ResultType consumes the next String column for the name
        String
    )
);
```
