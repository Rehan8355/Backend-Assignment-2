# JsonDataset API

A Spring Boot based API for storing and querying JSON datasets with **group by** and **sort by** operations.

---

## 🚀 Features
- Insert JSON records into specific datasets.
- Query all records.
- Group records by a given field.
- Sort records by a given field (ascending or descending).
- Combine **group by** and **sort by** in queries.

---

## ⚙️ Tech Stack
- **Java 17**
- **Spring Boot**
- **Maven**
- **H2 / PostgreSQL / MySQL** (configurable database)

---

## 📌 API Endpoints

### 1️⃣ Insert Record
**Endpoint**
```
---
### POST http://localhost:8080/api/dataset/employee_dataset/record
Request Body
```
json
Copy
Edit
{
  "record": {
    "id": 1,
    "name": "John Doe",
    "age": 30,
    "department": "Engineering"
  }
}
Response

json
Copy
Edit
{
  "message": "Record inserted successfully",
  "record": {
    "id": 1,
    "name": "John Doe",
    "age": 30,
    "department": "Engineering"
  }
}
2️⃣ Query All Records
Endpoint

http
Copy
Edit
GET http://localhost:8080/api/dataset/employee_dataset/query
Response

json
Copy
Edit
[
  {
    "id": 1,
    "name": "John Doe",
    "age": 30,
    "department": "Engineering"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "age": 25,
    "department": "Marketing"
  }
]
3️⃣ Query Group By Department
Endpoint

h
Copy
Edit
GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department
Response

json
Copy
Edit
{
  "Engineering": [
    { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
  ],
  "Marketing": [
    { "id": 2, "name": "Jane Smith", "age": 25, "department": "Marketing" }
  ]
}
4️⃣ Query Sort By Age (Ascending)
Endpoint

http
Copy
Edit
GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc
Response

json
Copy
Edit
[
  { "id": 2, "name": "Jane Smith", "age": 25, "department": "Marketing" },
  { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
]
5️⃣ Query Group By Department + Sort By Age (Descending)
Endpoint

http
Copy
Edit
GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department&sortBy=age&order=desc
Response

json
Copy
Edit
{
  "Engineering": [
    { "id": 3, "name": "Alice Brown", "age": 35, "department": "Engineering" },
    { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
  ],
  "Marketing": [
    { "id": 2, "name": "Jane Smith", "age": 25, "department": "Marketing" }
  ]
}
# Backend-Assignment-2
